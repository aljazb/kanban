package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.rest.source.CrudSource;
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
public class CardMoveSource extends CrudSource<CardMove, UUID> implements CardMoveSourceLocal {

    @EJB
    private CardMoveServiceLocal service;

    private UserAccount authUser;

    @Override
    public CardMove create(CardMove cardMove) throws Exception {
        return service.create(cardMove, authUser);
    }

    @Override
    public void setAuthUser(UserAccount authUser) {
        this.authUser = authUser;
    }

    @Override
    public UserAccount getAuthUser() {
        return authUser;
    }
}
