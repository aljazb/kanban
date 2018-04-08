package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import sun.rmi.runtime.Log;

import java.util.UUID;

public interface BoardServiceLocal {

    Board get(UUID id) throws LogicBaseException;

    Board create(Board board) throws LogicBaseException;

    //Board update(Board board) throws LogicBaseException;

}
