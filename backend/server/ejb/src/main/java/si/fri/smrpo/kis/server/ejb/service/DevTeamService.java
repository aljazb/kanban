package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.DevTeamServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PermitAll
@Stateless
@Local(DevTeamServiceLocal.class)
public class DevTeamService implements DevTeamServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void checkForDuplicateEntry(DevTeam devTeam) throws OperationException {
        HashSet<UUID> membersIds = new HashSet<>();

        for(UserAccountMtmDevTeam member : devTeam.getJoinedUsers()) {
            UUID id = member.getUserAccount().getId();
            if(membersIds.contains(id)) {
                throw new OperationException(String.format("Duplicate entry for id: %s .", id.toString()));
            } else {
                membersIds.add(id);
            }
        }
    }

    private void checkStructure(DevTeam devTeam, UUID authId) throws OperationException {
        List<UserAccountMtmDevTeam> kanbanMasters = devTeam.getJoinedUsers().stream()
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

        List<UserAccountMtmDevTeam> productOwners = devTeam.getJoinedUsers().stream()
                .filter(e -> e.getMemberType() == MemberType.PRODUCT_OWNER)
                .collect(Collectors.toList());

        if(productOwners.isEmpty()) {
            throw new OperationException("No product owner specified.");
        }
    }

    private void loadDevTeamMembers(DevTeam devTeam) throws DatabaseException {
        for(UserAccountMtmDevTeam member : devTeam.getJoinedUsers()) {
            UUID id = member.getUserAccount().getId();
            UserAccount ua = database.getEntityManager().find(UserAccount.class, id);
            if(ua == null){
                throw new DatabaseException(String.format("User with id '%s' does not exist.", id));
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


    private DevTeam persistDevTeam(DevTeam devTeam) throws DatabaseException {
        devTeam = database.create(devTeam);
        for(UserAccountMtmDevTeam member : devTeam.getJoinedUsers()) {
            member.setDevTeam(devTeam);
            database.create(member);
        }

        return devTeam;
    }

    public DevTeam create(DevTeam devTeam, UUID authId) throws LogicBaseException {

        validateDevTeam(devTeam, authId);
        persistDevTeam(devTeam);

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
                    LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
        }

        DevTeam devTeam = database.get(DevTeam.class, devTeamId);

        UserAccountMtmDevTeam memberMtm = devTeam.getJoinedUsers().stream().filter(e -> e.getUserAccount().getId()
                .equals(memberId) && !e.getIsDeleted()).findFirst().orElse(null);

        if (memberMtm == null) {
            throw new OperationException("Member is not in the development team.");
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
                memberMtm = database.delete(UserAccountMtmDevTeam.class, memberMtm.getId());
                break;
            default:
                throw new OperationException("Member is not developer");
        }

        return memberMtm.getUserAccount();
    }

}
