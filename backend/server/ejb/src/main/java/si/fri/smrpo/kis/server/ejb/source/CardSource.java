package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.From;
import java.util.UUID;

@PermitAll
@Stateless
@Local(CardSourceLocal.class)
public class CardSource extends CrudSource<Card, UUID, UserAccount> implements CardSourceLocal {

    @EJB
    private DatabaseServiceLocal database;

    @EJB
    private CardServiceLocal cardService;


    @PostConstruct
    private void init() {
        setDatabase(database);
    }


    @Override
    public Paging<Card> getList(Class<Card> c, QueryParameters param, UserAccount authUser) throws Exception {
        CriteriaFilter<Card> filter = null;
        if(!authUser.getInRoleAdministrator()) {
            filter = (p, cb, r) -> {
                From membership = r.join("project").join("devTeam").join("joinedUsers");
                return cb.and(p, cb.or(
                        cb.and(
                                cb.equal(membership.get("isDeleted"), false),
                                cb.equal(membership.join("userAccount").get("id"), authUser.getId())
                        ),
                        cb.equal(r.join("owner").get("id"), authUser.getId())
                ));
            };
        }

        return super.getList(c, param, filter, filter != null, authUser);
    }

    @Override
    public Card get(Class<Card> c, UUID id, UserAccount authUser) throws Exception {
        Card entity = super.get(c, id, authUser);

        entity.queryMembership(database.getEntityManager(), authUser.getId());

        if (!authUser.getInRoleAdministrator() && entity.getMembership() == null) {
            throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        // Fetch BoardPart and Board to check for edit access
        entity.getBoardPart().getBoard().getHighestPriority();
        entity.getSubTasks().size();
        entity.getCardMoves().size();

        return entity;
    }

    @Override
    public Card create(Card newEntity, UserAccount authUser) throws Exception {
        return cardService.create(newEntity, authUser);
    }

    @Override
    public Card update(Card newEntity, UserAccount authUser) throws Exception {
        return cardService.update(newEntity, authUser);
    }

    @Override
    public Card patch(Card newEntity, UserAccount authUser) throws Exception {
        return cardService.patch(newEntity, authUser);
    }

    @Override
    public Card delete(Class<Card> c, UUID id, UserAccount authUser) throws Exception {
        return cardService.delete(id, authUser);
    }

}
