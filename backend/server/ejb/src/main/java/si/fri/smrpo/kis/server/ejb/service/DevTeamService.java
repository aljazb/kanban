package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.exceptions.NoContentException;
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

    public List<UserAccount> getDevelopers(UUID devTeamId) {
        return database.getEntityManager().createNamedQuery("devTeam.getDevelopers", UserAccount.class)
                .setParameter("id", devTeamId)
                .getResultList();
    }

    @Override
    public UserAccount getKanbanMaster(UUID devTeamId) throws NoContentException {
        try {
        return database.getEntityManager().createNamedQuery("devTeam.getKanbanMaster", UserAccount.class)
                .setParameter("id", devTeamId)
                .getSingleResult();
        } catch (NoResultException e) {
            throw new NoContentException("Kanban Master not found", e);
        }
    }

    @Override
    public UserAccount getProductOwner(UUID devTeamId) throws NoContentException {
        try {
            return database.getEntityManager().createNamedQuery("devTeam.getProductOwner", UserAccount.class)
                    .setParameter("id", devTeamId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoContentException("Product Owner not found", e);
        }
    }

}
