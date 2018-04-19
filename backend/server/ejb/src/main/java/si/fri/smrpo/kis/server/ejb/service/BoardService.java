package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.BoardServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

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

            if(bp.getChildren() != null && bp.getChildren().size() > 0) {
                for(BoardPart cBp : bp.getChildren()) {
                    cBp.setParent(bp);
                }
                validate(bp.getChildren());

                if(bp.getLeaf()) {
                    throw new TransactionException("Board part is leaf and has children");
                }
            } else {
                if(!bp.getLeaf()) {
                    throw new TransactionException("Board part is not leaf and but has no children");
                }

                BoardPart p = bp.getParent();

                if(bp.getMaxWip() == 0) {
                    while (p != null){
                        if(p.getMaxWip() != 0) {
                            throw new TransactionException("Board part max wip is not valid");
                        }

                        p = bp.getParent();
                    }
                } else {
                    int maxWip = bp.getMaxWip();
                    while (p != null) {
                        if(p.getMaxWip() < maxWip) {
                            throw new TransactionException("Board part max wip is not valid");
                        }
                        maxWip = p.getMaxWip();
                        p = p.getParent();
                    }
                }

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

    private BoardPart persistPart(BoardPart part, Board board, BoardPart parent) throws LogicBaseException {

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


    private void checkEditAccess(UUID userId, UUID boardId) throws TransactionException {
        List<Board> editBoard = database.getEntityManager().createNamedQuery("board.access.edit", Board.class)
                .setParameter("userId", userId)
                .setParameter("boardId", boardId)
                .getResultList();

        if(editBoard.isEmpty()){
            throw new TransactionException("User does not have sufficient rights.", ExceptionType.INSUFFICIENT_RIGHTS);
        }
    }

    private boolean hasCardsAssignet(BoardPart boardPart) {
        if (boardPart.getLeaf()) {
            return boardPart.getCards().size() > 0;
        } else {
            for(BoardPart bp : boardPart.getChildren()) {
                if(hasCardsAssignet(bp)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void recPermDelete(BoardPart bp) throws DatabaseException {
        if(bp.getChildren() != null) {
            for(BoardPart cBp : bp.getChildren()) {
                recPermDelete(cBp);
            }
        }
        database.permDelete(BoardPart.class, bp.getId());
    }

    private void updateBoardParts(Set<BoardPart> dbBoardPart, Set<BoardPart> newBoardPart) throws LogicBaseException {
        HashMap<UUID, BoardPart> map = BoardPart.buildMap(dbBoardPart);

        for(BoardPart nBp : newBoardPart) {
            BoardPart dbBp = map.get(nBp.getId());
            if(dbBp == null) {
                dbBp = database.create(nBp);
                dbBp.setChildren(new HashSet<>());
            } else {
                if(dbBp.getLeaf() && hasCardsAssignet(dbBp)) {
                    if(!nBp.getLeaf()) {
                        throw new TransactionException("Board part with cards can not be expanded to sub parts.");
                    }
                }
                dbBp = database.update(nBp);
                map.remove(dbBp.getId());
            }
            if(nBp.getChildren() == null) nBp.setChildren(new HashSet<>());
            updateBoardParts(dbBp.getChildren(), nBp.getChildren());
        }

        for(BoardPart bp : map.values()) {
            if(hasCardsAssignet(bp)) {
                throw new TransactionException("Board part with cards can not be deleted.");
            } else {
                BoardPart p = bp.getParent();
                p.getChildren().remove(bp);

                if(p.getChildren().size() == 0){
                    p.setLeaf(true);
                    p = database.update(p);
                }

                recPermDelete(bp);
            }
        }
    }

    private Board updateBoard(Board dbBoard, Board newBoard) throws LogicBaseException {
        dbBoard.buildBoardPartsReferences();
        updateBoardParts(dbBoard.getBoardParts(), newBoard.getBoardParts());

        dbBoard = database.update(newBoard);  // Update column definitions

        dbBoard.buildBoardPartsReferences();
        dbBoard.fetchProjectsWithCards();

        return dbBoard;
    }

    @Override
    public Board update(UserAccount authUser, Board board) throws LogicBaseException {
        validate(board);
        checkEditAccess(authUser.getId(), board.getId());

        Board dbBoard = database.get(Board.class, board.getId());
        dbBoard = updateBoard(dbBoard, board);

        return dbBoard;
    }
}
