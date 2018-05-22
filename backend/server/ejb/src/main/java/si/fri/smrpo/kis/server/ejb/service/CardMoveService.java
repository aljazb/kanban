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

import static si.fri.smrpo.kis.server.jpa.entities.BoardPart.isMoveWipValid;

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

    private boolean validCardMoveRole(CardMoveRules cmr, Membership m) {
        if(cmr.getRoleDeveloperAllowed()) {
            if(m.isDeveloper()) {
                return true;
            }
        }

        if(cmr.getRoleKanbanMasterAllowed()) {
            if(m.isKanbanMaster()) {
                return true;
            }
        }

        if(cmr.getRoleProductOwnerAllowed()) {
            if(m.isProductOwner()) {
                return true;
            }
        }

        return false;
    }

    private void validateAuthUserRights(Card card, BoardPart movedFrom, BoardPart movedTo, Boolean rejected) throws TransactionException {
        Board board = movedFrom.getBoard();

        Membership m = card.getMembership();
        if(m == null) {
            throw new TransactionException("User is not allowed to move card.");
        }

        for(CardMoveRules cmr : board.getCardMoveRules()) {
            if(rejected && cmr.getCanReject()) {
                if(cmr.getFrom().getId().equals(movedFrom.getId())) {
                    if(validCardMoveRole(cmr, m)) {
                        return;
                    }
                }
            } else if (!rejected && !cmr.getCanReject()) {
                if(
                        (cmr.getTo().getId().equals(movedTo.getId()) && cmr.getFrom().getId().equals(movedFrom.getId())) ||
                        (cmr.getBidirectionalMovement() && cmr.getTo().getId().equals(movedFrom.getId()) && cmr.getFrom().getId().equals(movedTo.getId()))
                ) {
                    if(validCardMoveRole(cmr, m)) {
                        return;
                    }
                }
            }
        }

        throw new TransactionException("Card move rules for this does not exist.");
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

        Boolean rejected = cardMove.getRejected();

        validateAuthUserRights(card, movedFrom, movedTo, rejected);

        if(rejected) {
            handleRejectedCard(card);
        }

        cardMove.setFrom(movedFrom);
        cardMove.setTo(movedTo);
        cardMove.setCard(card);

        CardMoveType requiredType = isMoveWipValid(movedTo, movedFrom) ? CardMoveType.VALID : CardMoveType.INVALID;

        if(cardMove.getCardMoveType() != null) {
            if (cardMove.getCardMoveType() != requiredType) {
                throw new TransactionException("CardMoveType is set incorrectly.");
            }
        } else {
            cardMove.setCardMoveType(requiredType);
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

    @Override
    public CardMove create(CardMove cardMove, UserAccount authUser) throws LogicBaseException {
        validate(cardMove, authUser);

        cardMove = database.create(cardMove);
        moveCard(cardMove);

        return cardMove;
    }


}
