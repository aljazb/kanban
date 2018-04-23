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
        if (!authUser.getId().equals(cardMove.getMovedBy().getId())) {
            throw new OperationException("User moving the card is not the logged in user.", ExceptionType.INSUFFICIENT_RIGHTS);
        }
        cardMove.setMovedBy(authUser);

        BoardPart movedFrom = database.get(BoardPart.class, cardMove.getFrom().getId());

        if (movedFrom == null) {
            throw new TransactionException("BoardPart from not found.");
        }

        BoardPart movedTo = database.get(BoardPart.class, cardMove.getTo().getId());

        if (movedTo == null) {
            throw new TransactionException("BoardPart to not found.");
        }

        cardMove.setFrom(movedFrom);
        cardMove.setTo(movedTo);

        Card card = database.get(Card.class, cardMove.getCard().getId());

        if (card == null) {
            throw new TransactionException("Card not found.");
        }

        if (!movedFrom.getCards().contains(card)) {
            throw new TransactionException("Card not in from BoardPart.");
        }

        cardMove.setCard(card);

        CardMoveType requiredType = (movedTo.getCards().size() + 1 > movedTo.getMaxWip()) ? CardMoveType.INVALID :
                CardMoveType.VALID;

        if (cardMove.getCardMoveType() != requiredType) {
            throw new TransactionException("CardMoveType is set incorrectly.");
        }

        // TODO check if user has move permissions
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
}
