package si.fri.smrpo.kis.server.ejb.managers;
;
import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.List;

public class ProjectAuthManager extends AuthManager<Project> {

    public ProjectAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<Project> authCriteria() {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                return cb.and(p, cb.or(
                        cb.equal(r.join("devTeam").join("joinedUsers")
                                .join("userAccount").get("id"), getUserId()),
                        cb.equal(r.join("owner").get("id"),  getUserId())
                ));
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
    public void authGet(DatabaseCore db, Project entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)) {
            List<Project> devTeamQueryList = db.getEntityManager()
                    .createNamedQuery("project.isMember", Project.class)
                    .setMaxResults(1).setParameter("projectId", entity.getId())
                    .setParameter("userId", getUserId()).getResultList();

            if (devTeamQueryList.isEmpty()) {
                throw new DatabaseException("User does not have permission.",
                        LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
            }
        }
    }

    @Override
    public void authSet(DatabaseCore db, Project entity) throws DatabaseException {
        UserAccount owner = db.getEntityManager().getReference(UserAccount.class, getUserId());
        entity.setOwner(owner);
    }
}