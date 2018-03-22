package si.fri.smrpo.kis.server.ejb.managers;

import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class DevTeamAuthManager extends AuthManager<DevTeam> {

    public DevTeamAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<DevTeam> authCriteria(DatabaseCore dbCore, Class<DevTeam> c) throws DatabaseException {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                return cb.and(p, cb.equal(
                        r.join("joinedUsers").join("userAccount").get("id"),
                        getUserId()));
            } else {
                return p;
            }
        };
    }

    @Override
    public void authGet(DatabaseCore db, DevTeam entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)) {
            List<UserAccount> devTeamQueryList = db.getEntityManager()
                    .createNamedQuery("devTeam.isMember", UserAccount.class)
                    .setMaxResults(1).setParameter("devTeamId", entity.getId())
                    .setParameter("userId", getUserId()).getResultList();

            if (devTeamQueryList.isEmpty()) {
                throw new DatabaseException("User does not have permission.",
                        LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
            }
        }
    }
}
