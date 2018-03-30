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
        } else if(kanbanMasters.size() > 2) {
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

        List<UserAccountMtmDevTeam> developers = devTeam.getJoinedUsers().stream()
                .filter(e -> e.getMemberType() == MemberType.DEVELOPER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER ||
                        e.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER)
                .collect(Collectors.toList());

        if(developers.isEmpty()){
            throw new OperationException("At least one developer must be specified.");
        }
    }

    private void loadDevTeamMembers(DevTeam devTeam) throws DatabaseException {
        for(UserAccountMtmDevTeam member : devTeam.getJoinedUsers()) {
            UUID id = member.getUserAccount().getId();
            UserAccount ua = database.getEntityManager().find(UserAccount.class, id);
            if(ua == null){
                throw new DatabaseException(String.format("User with id '%s' does not exist."));
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

}
