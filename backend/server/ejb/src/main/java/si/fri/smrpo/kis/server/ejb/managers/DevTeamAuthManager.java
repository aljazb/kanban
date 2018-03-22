package si.fri.smrpo.kis.server.ejb.managers;

import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class DevTeamAuthManager extends AuthManager<DevTeam> {

    public DevTeamAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<DevTeam> authCriteria(DatabaseCore dbCore, Class<DevTeam> c, CriteriaFilter<DevTeam> criteriaFilter) throws DatabaseException {
        return (p, cb, r) -> {
            if(criteriaFilter != null) p = criteriaFilter.createPredicate(p, cb, r);

            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                Path path = r.join("joinedUsers").join("userAccount").get("id");
                Predicate authP = cb.equal(path, getUserId());
                cb.and(p, authP);
            }
            return p;
        };
    }

    @Override
    public void authGet(DatabaseCore db, DevTeam entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)) {
            List<DevTeam> devTeamQueryList = db.getEntityManager()
                    .createNamedQuery("devTeam.isMember", DevTeam.class)
                    .setMaxResults(1).setParameter("devTeamId", entity.getId())
                    .setParameter("userId", getUserId()).getResultList();

            if (devTeamQueryList.isEmpty()) {
                throw new DatabaseException("User does not have permission.",
                        LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
            }
        }
    }
}
