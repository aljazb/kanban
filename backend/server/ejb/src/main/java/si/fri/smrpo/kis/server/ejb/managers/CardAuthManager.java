package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import javax.persistence.criteria.From;
import java.util.List;

public class CardAuthManager extends AuthManager<Card> {

    public CardAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public boolean authCriteriaRequiresDistinct() {
        return true;
    }

    @Override
    public CriteriaFilter<Card> authCriteria() {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                From membership = r.join("project").join("devTeam").join("joinedUsers");
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
    public void authGet(DatabaseCore db, Card entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)) {
            List<Card> boardAuth = db.getEntityManager()
                    .createNamedQuery("card.access", Card.class)
                    .setMaxResults(1).setParameter("cardId", entity.getId())
                    .setParameter("userId", getUserId()).getResultList();

            if (boardAuth.isEmpty()) {
                throw new DatabaseException("User does not have permission.",
                        ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }
        entity.getSubTasks().size();

        super.authGet(db, entity);
    }
}
