package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.Project;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import java.util.*;

public class BoardAuthManager extends AuthManager<Board> {

    public BoardAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public boolean authCriteriaRequiresDistinct() {
        return true;
    }

    @Override
    public CriteriaFilter<Board> authCriteria() {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                From membership = r.join("projects", JoinType.LEFT).join("devTeam", JoinType.LEFT).join("joinedUsers", JoinType.LEFT);
                UUID id = UUID.fromString(getUserId().toString());
                return cb.and(p, cb.or(
                        cb.equal(r.join("owner").get("id"), id),
                        cb.and(
                                cb.equal(membership.get("isDeleted"), false),
                                cb.equal(membership.join("userAccount", JoinType.LEFT).get("id"), getUserId())
                        )
                ));
            } else {
                return p;
            }
        };
    }

    @Override
    public void authGet(DatabaseCore db, Board entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)) {
            List<Board> boardAuth = db.getEntityManager()
                    .createNamedQuery("board.access.view", Board.class)
                    .setMaxResults(1).setParameter("boardId", entity.getId())
                    .setParameter("userId", getUserId()).getResultList();

            if (boardAuth.isEmpty()) {
                throw new DatabaseException("User does not have permission.",
                        ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }

        entity.buildBoardPartsReferences();
        entity.fetchProjectsWithCards();

        super.authGet(db, entity);
    }


}
