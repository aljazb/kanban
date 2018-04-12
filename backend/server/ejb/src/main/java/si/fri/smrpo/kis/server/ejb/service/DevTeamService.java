package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.DevTeamServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.Membership;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
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

    private void checkStructure(DevTeam devTeam, UUID authId) throws OperationException {
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

        if(!kanbanMaster.getId().equals(authId)){
            throw new OperationException("User creating dev team must be kanban master.");
        }

        List<Membership> productOwners = devTeam.getJoinedUsers().stream()
                .filter(e -> e.getMemberType() == MemberType.PRODUCT_OWNER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_PRODUCT_OWNER)
                .collect(Collectors.toList());

        if(productOwners.isEmpty()) {
            throw new OperationException("No product owner specified.");
        }
    }

    private void loadDevTeamMembers(DevTeam devTeam) throws DatabaseException {
        for(Membership member : devTeam.getJoinedUsers()) {
            UUID id = member.getUserAccount().getId();
            UserAccount ua = database.getEntityManager().find(UserAccount.class, id);
            if(ua == null){
                throw new DatabaseException(String.format("User with id '%s' does not exist.", id), ExceptionType.ENTITY_DOES_NOT_EXISTS);
            } else {
                member.setUserAccount(ua);
            }
        }
    }

    private void validateDevTeam(DevTeam devTeam, UUID authId) throws LogicBaseException {
        checkForDuplicateEntry(devTeam);
        checkStructure(devTeam, authId);
        loadDevTeamMembers(devTeam);
    }

    private void updateDevTeamMembers(DevTeam devTeam) throws LogicBaseException {
        DevTeam fromDb = this.database.get(DevTeam.class, devTeam.getId());

        if (fromDb == null) {
            throw new OperationException("Dev team does not exist.", ExceptionType.ENTITY_DOES_NOT_EXISTS);
        }

        Set<Membership> existingUsers = fromDb.getJoinedUsers().stream()
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
                updatedUserMtm.setDevTeam(fromDb);
                database.create(updatedUserMtm);
            }
        }

        for (Membership existing : existingUsers) {
            database.delete(Membership.class, existing.getId());
        }
    }

    private DevTeam persistDevTeamMembers(DevTeam devTeam) throws DatabaseException {
        for(Membership member : devTeam.getJoinedUsers()) {
            member.setDevTeam(devTeam);
            database.create(member);
        }

        return devTeam;
    }

    public DevTeam create(DevTeam devTeam, UUID authId) throws LogicBaseException {

        validateDevTeam(devTeam, authId);
        devTeam = database.create(devTeam);
        persistDevTeamMembers(devTeam);

        devTeam.setJoinedUsers(null); // prevents recursive cycling when marshalling
        return devTeam;
    }

    @Override
    public DevTeam update(DevTeam devTeam, UUID authId) throws LogicBaseException {

        validateDevTeam(devTeam, authId);
        updateDevTeamMembers(devTeam);
        devTeam = database.update(devTeam);

        devTeam.setJoinedUsers(null); // prevents recursive cycling when marshalling
        return devTeam;
    }


    public Paging<UserAccount> getDevelopers(UUID devTeamId) {
        List<UserAccount> uaList = database.getEntityManager().createNamedQuery("devTeam.getDevelopers", UserAccount.class)
                .setParameter("id", devTeamId)
                .getResultList();

        return new Paging<>(uaList, uaList.size());
    }

    @Override
    public UserAccount getKanbanMaster(UUID devTeamId) {
        return database.getEntityManager().createNamedQuery("devTeam.getKanbanMaster", UserAccount.class)
                .setParameter("id", devTeamId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public UserAccount getProductOwner(UUID devTeamId) {
            return database.getEntityManager().createNamedQuery("devTeam.getProductOwner", UserAccount.class)
                    .setParameter("id", devTeamId)
                    .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public UserAccount kickMember(UUID devTeamId, UUID memberId, UUID authId) throws LogicBaseException {
        if (!getKanbanMaster(devTeamId).getId().equals(authId)) {
            throw new OperationException("User is not KanbanMaster of this group",
                    ExceptionType.INSUFFICIENT_RIGHTS);
        }

        DevTeam devTeam = database.get(DevTeam.class, devTeamId);

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
    public DevTeam getWithUsers(UUID id) throws LogicBaseException {
        DevTeam dt = this.database.get(DevTeam.class, id);

        if (dt == null) {
            throw new OperationException("Dev team does not exist.", ExceptionType.ENTITY_DOES_NOT_EXISTS);
        }

        List<Membership> activeMembersList = database.getEntityManager().createNamedQuery("devTeam.get.active.members", Membership.class)
                .setParameter("devTeamId", dt.getId()).getResultList();

        dt.setJoinedUsers(new HashSet<>(activeMembersList));

        return dt;
    }

}
