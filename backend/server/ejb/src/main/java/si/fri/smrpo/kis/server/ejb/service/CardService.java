package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.UUID;


@PermitAll
@Stateless
@Local(CardServiceLocal.class)
public class CardService implements CardServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    private void validate(Card card) throws TransactionException {
        if(card.getBoardPart() == null || card.getBoardPart().getId() == null) {
            throw new TransactionException("No board part specified");
        } else if(card.getProject() == null || card.getProject().getId() == null) {
            throw new TransactionException("No project specified");
        } else if(card.getName() == null){
            throw new TransactionException("No name specified");
        } else if(card.getColor() == null) {
            throw new TransactionException("No color specified");
        }

        if(card.getSilverBullet() == null) {
            card.setSilverBullet(false);
        }
    }

    private void checkAccess(Card entity, UserAccount authUser) throws LogicBaseException {
        Membership m;
        if(entity.getId() == null) {
            Project p = database.get(Project.class, entity.getProject().getId());
            p.queryMembership(database.getEntityManager(), authUser.getId());
            m = p.getMembership();
        } else {
            entity.queryMembership(database.getEntityManager(), authUser.getId());
            m = entity.getMembership();
        }

        if(m == null) {
            throw new TransactionException("User is not part of project", ExceptionType.INSUFFICIENT_RIGHTS);
        } else {
            if(entity.getSilverBullet()) {
                if(!m.isKanbanMaster()) {
                    throw new TransactionException("User is not kanban master", ExceptionType.INSUFFICIENT_RIGHTS);
                }
            } else {
                if(!m.isProductOwner()) {
                    throw new TransactionException("User is not product owner", ExceptionType.INSUFFICIENT_RIGHTS);
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

    @Override
    public Card create(Card card, UserAccount authUser) throws Exception {
        if(card.getSilverBullet() == null) card.setSilverBullet(false);

        validate(card);
        checkAccess(card, authUser);

        Card dbCard = database.create(card);
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
