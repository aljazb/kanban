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

@PermitAll
@Stateless
@Local(CardMoveServiceLocal.class)
public class CardMoveService implements CardMoveServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void validateAuthUserRights(Card card, BoardPart movedFrom, BoardPart movedTo) throws TransactionException {
        Board board = movedFrom.getBoard();

        Membership m = card.getMembership();
        if(m == null) {
            throw new TransactionException("User is not allowed to move card.");
        }

        int diff = Math.abs(movedFrom.getLeafNumber() - movedTo.getLeafNumber());
        if(diff > 1) {
            if(!(
                    m.isProductOwner() &&
                    movedFrom.getLeafNumber() >= board.getAcceptanceTesting() &&
                    movedTo.getLeafNumber() <= board.getHighestPriority()
            )) {
                throw new TransactionException("Movement for more than 1 field is not allowed.");
            }
        }

        if(movedFrom.getLeafNumber() <= board.getHighestPriority()) {
            if(
                    (movedTo.getLeafNumber() > board.getHighestPriority() && !m.isKanbanMaster()) ||
                    (!m.isProductOwner() && !m.isKanbanMaster())
            ) {
                throw new TransactionException("User is not allowed to move card in columns highest priority and before.");
            }
        } else if(board.getStartDev() <= movedFrom.getLeafNumber() && movedFrom.getLeafNumber() <= board.getEndDev()) {
            if(
                    (movedTo.getLeafNumber() < board.getStartDev() && !m.isKanbanMaster()) ||
                    (!m.isDeveloper() && !m.isKanbanMaster())
            ) {
                throw new TransactionException("User is not allowed to move card in development columns.");
            }
        } else if(movedFrom.getLeafNumber() >= board.getAcceptanceTesting()) {
            if(
                    (!m.isProductOwner())
            ) {
                throw new TransactionException("User is not allowed to move card in columns after acceptance testing.");
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
        database.update(c.getProject().getBoard());
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
