package si.fri.smrpo.kis.server.ejb.managers;

import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.ejb.managers.base.UserAuthManager;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class DevTeamAuthManager extends UserAuthManager<DevTeam> {

    public DevTeamAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<DevTeam> authCriteria(DatabaseCore dbCore, Class<DevTeam> c, CriteriaFilter<DevTeam> criteriaFilter) throws DatabaseException {
        return (p, cb, r) -> {
            if(criteriaFilter != null) p = criteriaFilter.createPredicate(p, cb, r);

            if(!isUserInRole(ROLE_ADMINISTRATOR)){

                Path path = r.join("joinedUsers").join("userAccount").get("id");
                Predicate authP = cb.equal(path, getUserId());
                p = authP; //cb.and(p, authP);
            }

            return p;
        };
    }

    @Override
    public void authGet(DatabaseCore db, DevTeam entity) throws DatabaseException {


        super.authGet(db, entity);
    }
}
