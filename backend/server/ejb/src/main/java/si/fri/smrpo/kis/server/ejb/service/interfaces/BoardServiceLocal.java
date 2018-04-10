package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.Board;

public interface BoardServiceLocal {

    Board create(Board board) throws LogicBaseException;

    //Board update(Board board) throws LogicBaseException;

}
