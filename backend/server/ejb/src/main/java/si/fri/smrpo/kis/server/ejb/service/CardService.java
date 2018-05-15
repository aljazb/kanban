package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.UUID;

import static si.fri.smrpo.kis.server.jpa.entities.BoardPart.isMoveToAvailable;
import static si.fri.smrpo.kis.server.jpa.enums.CardMoveType.INVALID_ON_CREATE;
import static si.fri.smrpo.kis.server.jpa.enums.CardMoveType.INVALID_SILVER_BULLET_ON_CREATE;
import static si.fri.smrpo.kis.server.jpa.enums.CardMoveType.VALID;


@PermitAll
@Stateless
@Local(CardServiceLocal.class)
public class CardService implements CardServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void validate(Card card) throws LogicBaseException {
        if(card.getBoardPart() == null || card.getBoardPart().getId() == null) {
            throw new TransactionException("No board part specified");
        } else if(card.getProject() == null || card.getProject().getId() == null) {
            throw new TransactionException("No project specified");
        } else if(card.getName() == null){
            throw new TransactionException("No name specified");
        } else if(card.getColor() == null) {
            throw new TransactionException("No color specified");
        }

        BoardPart dbBp = database.find(BoardPart.class, card.getBoardPart().getId());
        if(dbBp == null) {
            throw new TransactionException("Board part does not exist");
        }

        if(card.getId() == null && card.getSilverBullet()) {
            for(Card dbCard : dbBp.getCards()) {
                if(dbCard.getSilverBullet()) {
                    throw new TransactionException("Highest priority already contains silver bullet");
                }
            }
        }

        if(card.getSilverBullet() == null) {
            card.setSilverBullet(false);
        }
    }

    private void checkAccess(Card entity, UserAccount authUser) throws LogicBaseException {
        Card card = entity;

        if(card.getId() != null) {
            card = database.get(Card.class, card.getId());
        }

        Project p = database.get(Project.class, card.getProject().getId());
        p.queryMembership(database.getEntityManager(), authUser.getId());
        Membership m = p.getMembership();

        BoardPart dbBp = database.find(BoardPart.class, card.getBoardPart().getId());
        Board dbBoard = dbBp.getBoard();

        int cardColumn = dbBp.getLeafNumber();

        if(m == null) {
            throw new TransactionException("User is not part of project", ExceptionType.INSUFFICIENT_RIGHTS);
        } else {
            if(card.getId() == null) {
                if(dbBoard.getHighestPriority() < cardColumn) {
                    throw new TransactionException("Card can only be created in columns before and in highest priority",
                            ExceptionType.INSUFFICIENT_RIGHTS);
                }
                if(card.getSilverBullet()) {
                    if(!m.isKanbanMaster()) {
                        throw new TransactionException("User must be in role kanban master to create silver bullet",
                                ExceptionType.INSUFFICIENT_RIGHTS);
                    }
                } else {
                    if(!m.isProductOwner()) {
                        throw new TransactionException("User must be in role product owner to create card",
                                ExceptionType.INSUFFICIENT_RIGHTS);
                    }
                }
            } else {
                if(dbBoard.getStartDev() <= cardColumn && cardColumn <= dbBoard.getEndDev()) {
                    if(!(m.isDeveloper() || m.isKanbanMaster())) {
                        throw new TransactionException("Card in columns between start and end dev can be edited only by kanban master and developer",
                                ExceptionType.INSUFFICIENT_RIGHTS);
                    }
                } else if(dbBoard.getAcceptanceTesting() <= cardColumn) {
                    throw new TransactionException("Card can not be edited in columns after acceptance testing",
                            ExceptionType.INSUFFICIENT_RIGHTS);
                } else if(dbBoard.getHighestPriority() >= cardColumn) {
                    if(!(m.isProductOwner() || m.isKanbanMaster())) {
                        throw new TransactionException("Card in columns before highest priority can be edited only by kanban master and product owner",
                                ExceptionType.INSUFFICIENT_RIGHTS);
                    }
                } else  {
                    if(!m.isKanbanMaster()) {
                        throw new TransactionException("Card in transitional columns can only be edited by kanban master",
                                ExceptionType.INSUFFICIENT_RIGHTS);
                    }
                }
            }
        }
    }

    private void updateCardHolders(Card c) throws Exception {
        if(c.getBoardPart() != null){
            Board b = c.getBoardPart().getBoard();
            database.update(b);
        }

        Project p = c.getProject();
        database.update(p);
    }

    private void checkWip(Card card, UserAccount authUser) throws DatabaseException {
        BoardPart boardPart = database.find(BoardPart.class, card.getBoardPart().getId());
        if(!isMoveToAvailable(boardPart, null, false)) {
            CardMove cm = new CardMove();
            cm.setCard(card);
            cm.setFrom(boardPart);
            cm.setTo(boardPart);
            cm.setCardMoveType(INVALID_ON_CREATE);
            cm.setMovedBy(authUser);

            database.create(cm);
        }
    }

    @Override
    public Card create(Card card, UserAccount authUser) throws Exception {
        if(card.getSilverBullet() == null) card.setSilverBullet(false);
        if(card.getRejected() == null) card.setRejected(false);

        validate(card);
        checkAccess(card, authUser);

        Card dbCard = database.create(card);

        checkWip(dbCard, authUser);

        updateCardHolders(dbCard);
        dbCard.getBoardPart().incWip(database);

        return dbCard;
    }

    @Override
    public Card update(Card card, UserAccount authUser) throws Exception {
        validate(card);
        checkAccess(card, authUser);

        Card c = database.update(card);
        updateCardHolders(c);

        return c;
    }

    @Override
    public Card patch(Card card, UserAccount authUser) throws Exception {
        checkAccess(card, authUser);

        Card c = database.patch(card);
        updateCardHolders(c);

        return c;
    }

    @Override
    public Card delete(UUID id, UserAccount authUser) throws Exception {
        Card c = new Card();
        c.setId(id);

        checkAccess(c, authUser);

        Card card = database.delete(Card.class, id);
        updateCardHolders(card);
        card.getBoardPart().decWip(database);

        return card;
    }

}

