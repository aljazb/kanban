package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.server.ejb.service.interfaces.CardMoveServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardMoveSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.UUID;

@PermitAll
@Stateless
@Local(CardMoveSourceLocal.class)
public class CardMoveSource extends GetSource<CardMove, UUID, UserAccount> implements CardMoveSourceLocal {

    @EJB
    private CardMoveServiceLocal service;


    public CardMove create(CardMove cardMove, UserAccount authUser) throws Exception {
        return service.create(cardMove, authUser);
    }

}

