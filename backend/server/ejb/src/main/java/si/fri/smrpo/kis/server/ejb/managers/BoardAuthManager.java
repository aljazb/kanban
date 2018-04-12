package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

import javax.persistence.criteria.From;
import java.util.*;

public class BoardAuthManager extends AuthManager<Board> {

    public BoardAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<Board> authCriteria() {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                From membership = r.join("projects").join("devTeam").join("joinedUsers");
                return cb.and(p, cb.or(
                        cb.and(
                                cb.equal(membership.get("isDeleted"), false),
                                cb.equal(membership.join("userAccount").get("id"), getUserId())
                        ),
                        cb.equal(r.join("owner").get("id"),  getUserId())
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
                    .createNamedQuery("board.access", Board.class)
                    .setMaxResults(1).setParameter("boardId", entity.getId())
                    .setParameter("userId", getUserId()).getResultList();

            if (boardAuth.isEmpty()) {
                throw new DatabaseException("User does not have permission.",
                        ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }

        setBoardPartsReferences(entity);
        entity.getProjects().size(); // Fetch project

        super.authGet(db, entity);
    }

    private void setBoardPartsReferences(Board board) {
        Set<BoardPart> boardParts = board.getBoardParts();
        HashMap<UUID, BoardPart> map = new HashMap<>();

        for(BoardPart bp : boardParts) {
            map.put(bp.getId(), bp);
        }

        for(BoardPart bp : boardParts) {
            if(bp.getParent() != null) {
                BoardPart parent = map.get(bp.getParent().getId());

                if(parent.getChildren() == null){
                    parent.setChildren(new HashSet<>());
                }
                parent.getChildren().add(bp);

                bp.setParent(parent);
            }
        }
    }
}
