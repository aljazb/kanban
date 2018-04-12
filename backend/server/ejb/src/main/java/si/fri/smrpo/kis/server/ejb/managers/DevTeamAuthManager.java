package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.persistence.criteria.From;
import java.util.List;

public class DevTeamAuthManager extends AuthManager<DevTeam> {

    public DevTeamAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<DevTeam> authCriteria()  {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                From membership = r.join("joinedUsers");
                return cb.and(p, cb.and(
                        cb.equal(membership.get("isDeleted"), false),
                        cb.equal(membership.join("userAccount").get("id"), getUserId())));
            } else {
                return p;
            }
        };
    }

    @Override
    public boolean authCriteriaRequiresDistinct() {
        return true;
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
                        ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }
    }
}
