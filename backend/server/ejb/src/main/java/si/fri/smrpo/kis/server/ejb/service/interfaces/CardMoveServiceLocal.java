package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public interface CardMoveServiceLocal {

    CardMove create(CardMove cardMove, UserAccount authUser) throws LogicBaseException;

}
