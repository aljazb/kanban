package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface CardServiceLocal {

    Card create(Card card, UserAccount authUser) throws Exception;
    Card update(Card card, UserAccount authUser) throws Exception;
    Card patch(Card card, UserAccount authUser) throws Exception;
    Card delete(UUID id, String deleteMessage, UserAccount authUser) throws Exception;

}
