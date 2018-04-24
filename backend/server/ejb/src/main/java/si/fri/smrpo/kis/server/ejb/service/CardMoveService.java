package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardMoveServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

@PermitAll
@Stateless
@Local(CardMoveServiceLocal.class)
public class CardMoveService implements CardMoveServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void validate(CardMove cardMove, UserAccount authUser) throws LogicBaseException {
        cardMove.setMovedBy(authUser);

        Card card = database.get(Card.class, cardMove.getCard().getId());
        if (card == null) {
            throw new TransactionException("Card not found.");
        }
        card.queryMembership(database.getEntityManager(), authUser.getId());
        if(card.getMembership() == null) {
            throw new TransactionException("User does not have permission", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        BoardPart movedFrom = card.getBoardPart();

        BoardPart movedTo = database.get(BoardPart.class, cardMove.getTo().getId());
        if (movedTo == null) {
            throw new TransactionException("BoardPart to not found.");
        }

        cardMove.setFrom(movedFrom);
        cardMove.setTo(movedTo);
        cardMove.setCard(card);

        CardMoveType requiredType = isMoveToAvailable(movedTo) ? CardMoveType.VALID : CardMoveType.INVALID;

        if (cardMove.getCardMoveType() != requiredType) {
            throw new TransactionException("CardMoveType is set incorrectly.");
        }

        movedTo.incWip(database);
        movedFrom.decWip(database);
    }

    private void moveCard(CardMove cardMove) throws DatabaseException {
        Card c = cardMove.getCard();
        c.setBoardPart(cardMove.getTo());
        database.update(c);
    }

    public CardMove create(CardMove cardMove, UserAccount authUser) throws LogicBaseException {
        validate(cardMove, authUser);
        cardMove = database.create(cardMove);
        moveCard(cardMove);

        return cardMove;
    }

    private boolean isMoveToAvailable(BoardPart bp) {
        if(bp.getMaxWip() > bp.getCurrentWip()) {
            if(bp.getParent() == null) {
                return true;
            } else {
                return isMoveToAvailable(bp.getParent());
            }
        }
        return false;
    }


}
