package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.BoardServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.*;

@PermitAll
@Stateless
@Local(BoardServiceLocal.class)
public class BoardService implements BoardServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    @Override
    public Board create(Board board) throws LogicBaseException {
        validate(board);
        return persist(board);
    }

    private void validate(Board board) throws LogicBaseException {
        if(board.getOwner() == null || board.getOwner().getId() == null) {
            throw new TransactionException("Owner not specified");
        }
        if(board.getName() == null) {
            throw new TransactionException("Name not specified");
        }
        if(board.getHighestPriority() == null) {
            throw new TransactionException("Highest priority index not specified");
        }
        if(board.getStartDev() == null) {
            throw new TransactionException("Start dev index not specified");
        }
        if(board.getEndDev() == null) {
            throw new TransactionException("End dev index not specified");
        }
        if(board.getAcceptanceTesting() == null) {
            throw new TransactionException("Acceptance testing index not specified");
        }
        if(board.getBoardParts() == null) {
            throw new TransactionException("No board part specified");
        }

        validate(board.getBoardParts());
    }

    private void validate(Set<BoardPart> boardParts) throws TransactionException {

        ArrayList<Integer> orderIndexes = new ArrayList<>();

        for(BoardPart bp : boardParts) {
            if(bp.getName() == null) {
                throw new TransactionException("Name not specified in board part");
            }
            if(bp.getOrderIndex() == null) {
                throw new TransactionException("Order index not specified in board part");
            }

            orderIndexes.add(bp.getOrderIndex());

            if(bp.getChildren() != null){
                validate(bp.getChildren());
            }
        }

        Collections.sort(orderIndexes);
        for(int i=0; i<orderIndexes.size(); i++){
            if(i != orderIndexes.get(i)){
                throw new TransactionException("Order indexes are not valid");
            }
        }

    }

    private Board persist(Board board) throws LogicBaseException {

        board = database.create(board);

        for(BoardPart bp : board.getBoardParts()) {
            persistPart(bp, board, null);
        }

        return board;
    }

    private BoardPart persistPart(BoardPart part,  Board board, BoardPart parent) throws LogicBaseException {

        part.setBoard(board);
        part.setParent(parent);

        if(part.getChildren() == null || part.getChildren().isEmpty()){
            part.setLeaf(true);
            part = database.create(part);
        } else {
            part.setLeaf(false);
            part = database.create(part);

            for(BoardPart bp : part.getChildren()) {
                persistPart(bp, board, part);
            }
        }

        return part;
    }
}
