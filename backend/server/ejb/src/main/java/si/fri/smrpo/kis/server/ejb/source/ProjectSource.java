package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.DevTeamSourceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.ProjectSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.From;
import java.util.UUID;

@PermitAll
@Stateless
@Local(ProjectSourceLocal.class)
public class ProjectSource extends CrudSource<Project, UUID> implements ProjectSourceLocal {

    @EJB
    private DatabaseServiceLocal databaseService;

    private UserAccount authUser;


    @PostConstruct
    private void init() {
        setDatabase(databaseService);
    }


    @Override
    public Paging<Project> getList(Class<Project> c, QueryParameters param) throws Exception {
        CriteriaFilter<Project> filter = null;
        if(!authUser.getInRoleAdministrator()) {
            filter = (p, cb, r) -> {
                From membership = r.join("devTeam").join("joinedUsers");
                return cb.and(p, cb.or(
                        cb.equal(r.join("owner").get("id"), authUser.getId()),
                        cb.and(
                                cb.equal(membership.get("isDeleted"), false),
                                cb.equal(membership.join("userAccount").get("id"), authUser.getId())
                        )));
            };
        }

        return super.getList(c, param, filter, filter != null);
    }

    @Override
    public Project get(Class<Project> c, UUID id) throws Exception {
        Project entity = super.get(c, id);

        entity.queryMembership(database.getEntityManager(), authUser.getId());

        if (!authUser.getInRoleAdministrator()) {
            if (!entity.getOwner().getId().equals(authUser.getId()) && entity.getMembership() == null) {
                throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }

        entity.queryBoard();
        entity.getCards().size(); // Fetch cards

        return entity;
    }

    @Override
    public Project create(Project newEntity) throws Exception {
        newEntity.setOwner(authUser);
        return super.create(newEntity);
    }

    public UserAccount getAuthUser() {
        return authUser;
    }

    public void setAuthUser(UserAccount authUser) {
        this.authUser = authUser;
    }
}
