package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.models.HistoryEvent;
import si.fri.smrpo.kis.server.ejb.service.interfaces.DevTeamServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.Membership;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.registry.infomodel.User;
import java.util.*;
import java.util.stream.Collectors;

@PermitAll
@Stateless
@Local(DevTeamServiceLocal.class)
public class DevTeamService implements DevTeamServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void checkForDuplicateEntry(DevTeam devTeam) throws OperationException {
        HashSet<UUID> membersIds = new HashSet<>();

        for(Membership member : devTeam.getJoinedUsers()) {
            UUID id = member.getUserAccount().getId();
            if(membersIds.contains(id)) {
                throw new OperationException(String.format("Duplicate entry for id: %s .", id.toString()));
            } else {
                membersIds.add(id);
            }
        }
    }

    private void checkStructure(DevTeam devTeam, UserAccount authUser) throws OperationException {
        List<Membership> kanbanMasters = devTeam.getJoinedUsers().stream()
                .filter(e -> e.getMemberType() == MemberType.KANBAN_MASTER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER)
                .collect(Collectors.toList());

        if(kanbanMasters.isEmpty()){
            throw new OperationException("No kanban master specified.");
        } else if(kanbanMasters.size() > 1) {
            throw new OperationException("Only one kanban master can be specified.");
        }

        UserAccount kanbanMaster = kanbanMasters.get(0).getUserAccount();

        if(!kanbanMaster.getId().equals(authUser.getId())){
            throw new OperationException("User creating dev team must be kanban master.");
        }

        List<Membership> productOwners = devTeam.getJoinedUsers().stream()
                .filter(e -> e.getMemberType() == MemberType.PRODUCT_OWNER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_PRODUCT_OWNER)
                .collect(Collectors.toList());

        if(productOwners.isEmpty()) {
            throw new OperationException("No product owner specified.");
        }

        List<Membership> developers = devTeam.getJoinedUsers().stream()
                .filter(e -> e.getMemberType() == MemberType.DEVELOPER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_PRODUCT_OWNER)
                .collect(Collectors.toList());

        if(developers.isEmpty()) {
            throw new OperationException("No developer specified.");
        }
    }

    private void loadDevTeamMembers(DevTeam devTeam) throws DatabaseException {
        for(Membership member : devTeam.getJoinedUsers()) {
            UUID id = member.getUserAccount().getId();
            UserAccount ua = database.find(UserAccount.class, id);
            if(ua == null){
                throw new DatabaseException(String.format("User with id '%s' does not exist.", id), ExceptionType.ENTITY_DOES_NOT_EXISTS);
            } else {
                member.setUserAccount(ua);
            }
        }
    }

    private void validateDevTeam(DevTeam devTeam, UserAccount authUser) throws LogicBaseException {
        checkForDuplicateEntry(devTeam);
        checkStructure(devTeam, authUser);
        loadDevTeamMembers(devTeam);
    }

    private void updateDevTeamMembers(DevTeam devTeam) throws LogicBaseException {
        DevTeam dbDevTeam = this.database.get(DevTeam.class, devTeam.getId());

        if (dbDevTeam == null) {
            throw new OperationException("Dev team does not exist.", ExceptionType.ENTITY_DOES_NOT_EXISTS);
        }

        Set<Membership> existingUsers = dbDevTeam.getJoinedUsers().stream()
                .filter(e -> !e.getIsDeleted()).distinct().collect(Collectors.toSet());

        for (Membership updatedUserMtm : devTeam.getJoinedUsers()) {
            Membership existing = existingUsers.stream()
                    .filter(userMtm -> userMtm.getUserAccount().getId().equals(updatedUserMtm.getUserAccount().getId()))
                    .findFirst().orElse(null);

            if (existing != null) {
                existingUsers.remove(existing);
                if(existing.getMemberType() != updatedUserMtm.getMemberType()){
                    existing.setMemberType(updatedUserMtm.getMemberType());
                    database.update(existing);
                }
            } else {
                updatedUserMtm.setDevTeam(dbDevTeam);
                database.create(updatedUserMtm);
            }
        }

        for (Membership existing : existingUsers) {
            database.delete(Membership.class, existing.getId());
        }
    }

    private DevTeam persistDevTeamMembers(DevTeam dbDevTeam, Set<Membership> joinedMembers) throws DatabaseException {
        for(Membership member : joinedMembers) {
            member.setDevTeam(dbDevTeam);
            database.create(member);
        }

        return dbDevTeam;
    }

    public DevTeam create(DevTeam devTeam, UserAccount userAccount) throws LogicBaseException {

        validateDevTeam(devTeam, userAccount);
        DevTeam dbDevTeam = database.create(devTeam);
        persistDevTeamMembers(dbDevTeam, devTeam.getJoinedUsers());

        return dbDevTeam;
    }

    @Override
    public DevTeam update(DevTeam devTeam, UserAccount authUser) throws LogicBaseException {

        validateDevTeam(devTeam, authUser);
        updateDevTeamMembers(devTeam);
        DevTeam dbDevTeam = database.update(devTeam);

        return dbDevTeam;
    }


    public UserAccount kickMember(UUID devTeamId, UUID memberId, UserAccount authUser) throws LogicBaseException {
        DevTeam devTeam = database.get(DevTeam.class, devTeamId);

        if (!authUser.getId().equals(devTeam.getKanbanMaster().getId())) {
            throw new OperationException("User is not KanbanMaster of this group", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        Membership memberMtm = devTeam.getJoinedUsers().stream().filter(e -> e.getUserAccount().getId()
                .equals(memberId) && !e.getIsDeleted()).findFirst().orElse(null);

        if (memberMtm == null) {
            throw new OperationException("Member is not in the development team.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        switch (memberMtm.getMemberType()) {
            case DEVELOPER_AND_KANBAN_MASTER:
                memberMtm.setMemberType(MemberType.KANBAN_MASTER);
                memberMtm = database.update(memberMtm);
                break;
            case DEVELOPER_AND_PRODUCT_OWNER:
                memberMtm.setMemberType(MemberType.PRODUCT_OWNER);
                memberMtm = database.update(memberMtm);
                break;
            case DEVELOPER:
                memberMtm = database.delete(Membership.class, memberMtm.getId());
                break;
            default:
                throw new OperationException("Member is not developer", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        return memberMtm.getUserAccount();
    }

    @Override
    public List<HistoryEvent> getDevTeamEvents(UUID devTeamId, UserAccount authUser) throws LogicBaseException {
        DevTeam dt = this.database.get(DevTeam.class, devTeamId);

        if(!authUser.getInRoleAdministrator()) {
            dt.queryMembership(database.getEntityManager(), authUser.getId());
            if(dt.getMembership() == null) {
                throw new OperationException("User is not member of this dev team");
            }
        }

        List<HistoryEvent> events = new ArrayList<>();

        for (Membership m : dt.getJoinedUsers()) {
            HistoryEvent createdEvent = new HistoryEvent();
            createdEvent.setDate(m.getCreatedOn());
            createdEvent.setEvent(String.format("User %s joined development team.", m.getUserAccount().getEmail()));
            events.add(createdEvent);

            if (m.getIsDeleted()) {
                HistoryEvent deletedEvent = new HistoryEvent();
                deletedEvent.setDate(m.getEditedOn());
                deletedEvent.setEvent(String.format("User %s left development team.", m.getUserAccount().getEmail()));
                events.add(deletedEvent);
            }
        }

        events.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        return events;
    }

}
