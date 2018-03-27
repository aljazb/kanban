package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
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
import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@PermitAll
@Stateless
@Local(DevTeamServiceLocal.class)
public class DevTeamService implements DevTeamServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    public DevTeam create(DevTeam devTeam, UUID userId) throws DatabaseException {

        UserAccount ua = database.getEntityManager().getReference(UserAccount.class, userId);

        devTeam = database.create(devTeam);

        UserAccountMtmDevTeam member = new UserAccountMtmDevTeam();
        member.setMemberType(MemberType.KANBAN_MASTER);
        member.setDevTeam(devTeam);
        member.setUserAccount(ua);

        member = database.create(member);

        HashSet<UserAccountMtmDevTeam> members = new HashSet<>();
        members.add(member);

        devTeam.setJoinedUsers(members);

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
