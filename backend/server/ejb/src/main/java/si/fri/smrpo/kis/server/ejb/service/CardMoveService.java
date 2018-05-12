package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardMoveServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import java.awt.*;

import static si.fri.smrpo.kis.server.jpa.entities.BoardPart.isMoveToAvailable;

@PermitAll
@Stateless
@Local(CardMoveServiceLocal.class)
public class CardMoveService implements CardMoveServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void handleRejectedCard(Card card ) {
        String hex = card.getColor();
        Color c = Color.decode(hex);
        c = c.darker();
        hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        card.setColor(hex);
        card.setRejected(true);
    }

    private void validateAuthUserRights(Card card, BoardPart movedFrom, BoardPart movedTo) throws TransactionException {
        Board board = movedFrom.getBoard();

        Membership m = card.getMembership();
        if(m == null) {
            throw new TransactionException("User is not allowed to move card.");
        }

        int from = movedFrom.getLeafNumber();
        int to = movedTo.getLeafNumber();

        int diff = Math.abs(from - to);
        if(diff > 1) {
            if(from >= board.getAcceptanceTesting() && to <= board.getHighestPriority()) {
                if(!m.isProductOwner()) {
                    throw new TransactionException("User is not in role product owner.");
                }

                handleRejectedCard(card);

            } else {
                throw new TransactionException("Movement for more than 1 field is not allowed.");
            }
        } else if(from >= board.getAcceptanceTesting() && to >= board.getAcceptanceTesting()) {
            if(!m.isProductOwner()) {
                throw new TransactionException("User is not in role product owner.");
            }
        } else if(m.isKanbanMaster()) {

        } else if(
                board.getStartDev() - 1 <= from && from <= board.getEndDev() + 1 &&
                board.getStartDev() - 1 <= to   &&   to <= board.getEndDev() + 1)
        {
            if(!m.isDeveloper()) {
                throw new TransactionException("User is not allowed to move card in development columns.");
            }
        } else if(from <= board.getHighestPriority() && to <= board.getHighestPriority()) {
            if(!m.isProductOwner()) {
                throw new TransactionException("User is not allowed to move card in columns highest priority and before.");
            }
        }
    }

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

        validateAuthUserRights(card, movedFrom, movedTo);

        cardMove.setFrom(movedFrom);
        cardMove.setTo(movedTo);
        cardMove.setCard(card);

        CardMoveType requiredType = isMoveToAvailable(movedTo, movedFrom, false) ? CardMoveType.VALID : CardMoveType.INVALID;

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
        database.update(c.getProject().getBoard());
    }

    public CardMove create(CardMove cardMove, UserAccount authUser) throws LogicBaseException {
        validate(cardMove, authUser);

        cardMove = database.create(cardMove);
        moveCard(cardMove);

        return cardMove;
    }


}
