package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public interface BoardServiceLocal {

    Board create(Board board, UserAccount authUser) throws LogicBaseException;
    Board update(Board board, UserAccount authUser) throws LogicBaseException;

}
