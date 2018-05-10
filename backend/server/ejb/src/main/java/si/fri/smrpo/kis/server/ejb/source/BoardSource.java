package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.BoardServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.BoardSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import java.util.UUID;

@PermitAll
@Stateless
@Local(BoardSourceLocal.class)
public class BoardSource extends CrudSource<Board, UUID, UserAccount> implements BoardSourceLocal {

    @EJB
    private DatabaseServiceLocal database;

    @EJB
    private BoardServiceLocal service;


    @PostConstruct
    private void init() {
        setDatabase(database);
    }

    @Override
    public Paging<Board> getList(Class<Board> c, QueryParameters param, UserAccount authUser) throws Exception {
        CriteriaFilter<Board> filter = null;
        if(!authUser.getInRoleAdministrator()) {
            filter = (p, cb, r) -> {
                From membership = r.join("projects", JoinType.LEFT).join("devTeam", JoinType.LEFT).join("joinedUsers", JoinType.LEFT);
                return cb.and(p, cb.or(
                        cb.equal(r.join("owner").get("id"), authUser.getId()),
                        cb.and(
                                cb.equal(membership.get("isDeleted"), false),
                                cb.equal(membership.join("userAccount", JoinType.LEFT).get("id"), authUser.getId()))));
            };
        }

        return super.getList(c, param, filter, filter != null, authUser);
    }

    @Override
    public Board get(Class<Board> c, UUID id, UserAccount authUser) throws Exception {
        Board entity = super.get(c, id, authUser);

        entity.queryMembership(database.getEntityManager(), authUser.getId());

        if (!authUser.getInRoleAdministrator()) {
            if(!entity.getOwner().getId().equals(authUser.getId()) && entity.getMembership() == null) {
                throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }

        entity.buildBoardPartsReferences();
        entity.fetchActiveProjectsWithCards();
        entity.getProjects().forEach(project -> {
            project.setBoard(entity);
            project.queryMembership(database.getEntityManager(), authUser.getId());
        });

        return entity;
    }

    @Override
    public Board create(Board newEntity, UserAccount authUser) throws Exception {
        return service.create(newEntity, authUser);
    }

    @Override
    public Board update(Board newEntity, UserAccount authUser) throws Exception {
        return service.update(newEntity, authUser);
    }

}
