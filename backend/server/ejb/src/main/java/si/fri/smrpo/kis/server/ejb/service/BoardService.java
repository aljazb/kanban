package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.BoardServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

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

        validateMarks(board);
        validate(board.getBoardParts());

        if(board.getProjects() != null) {
            for(Project project : board.getProjects()) {
                Project dbProject = database.get(Project.class, project.getId());
                if(dbProject == null) {
                    throw new TransactionException("Specified project does not exist");
                } else if (dbProject.getBoard() != null && !dbProject.getBoard().getId().equals(board.getId())) {
                    throw new TransactionException("Project is already taken by another board");
                }
            }
        } else {
            board.setProjects(new HashSet<>());
        }

    }

    private void validateProject(Board dbBoard, Board board) throws TransactionException {
        HashMap<UUID, Project> removedProject = UUIDEntity.buildMap(dbBoard.getProjects());

        for(Project p : board.getProjects()) {
            removedProject.remove(p.getId());
        }

        for(Project p : removedProject.values()) {
            if(p.getCards() != null) {
                if(p.getCards().size() > 0) {
                    throw new TransactionException("Project has already assigned cards and can't be removed");
                }
            }
        }
    }

    private void validateMarks(Board board) throws TransactionException {
        if(!(
                board.getHighestPriority() < board.getStartDev() &&
                board.getStartDev() < board.getEndDev() &&
                board.getEndDev() < board.getAcceptanceTesting()
        ))
        {
            throw new TransactionException("Invalid mark indexes");
        }
    }

    private void validate(Set<BoardPart> boardParts) throws TransactionException {
        ArrayList<BoardPart> leafBoardParts = new ArrayList<>();
        validate(boardParts, leafBoardParts);

        leafBoardParts.sort((o1, o2) -> o1.getLeafNumber() - o2.getLeafNumber());
        for(int i=0; i<leafBoardParts.size(); i++) {
            if(leafBoardParts.get(i).getLeafNumber() != i) {
                throw new TransactionException("Leaf board part index is not valid");
            }
        }
    }

    private void validate(Set<BoardPart> boardParts, ArrayList<BoardPart> leafBoardParts) throws TransactionException {

        ArrayList<Integer> orderIndexes = new ArrayList<>();

        for(BoardPart bp : boardParts) {
            if(bp.getName() == null) {
                throw new TransactionException("Name not specified in board part");
            }
            if(bp.getOrderIndex() == null) {
                throw new TransactionException("Order index not specified in board part");
            }

            orderIndexes.add(bp.getOrderIndex());

            if(bp.getCurrentWip() == null) {
                bp.setCurrentWip(0);
            }

            if(bp.hasChildren()) {
                if(bp.getLeafNumber() != null) {
                    throw new TransactionException("Board part is leaf and has children");
                }

                for(BoardPart cBp : bp.getChildren()) {
                    cBp.setParent(bp);
                }

                validate(bp.getChildren(), leafBoardParts);
            } else {
                if(bp.getLeafNumber() == null) {
                    throw new TransactionException("Board part is not leaf and has no children");
                }

                leafBoardParts.add(bp);

                BoardPart p = bp.getParent();
                while (p != null) {
                    if(!bp.isWipValid(p)) {
                        throw new TransactionException("Board part max wip is not valid");
                    }
                    bp = p;
                    p = p.getParent();
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

        Board dbBoard = database.create(board);

        for(BoardPart bp : board.getBoardParts()) {
            persistPart(bp, dbBoard, null);
        }

        for(Project p : board.getProjects()) {
            p.setBoard(dbBoard);
            database.update(p);
        }


        return dbBoard;
    }

    private void persistPart(BoardPart part, Board dbBoard, BoardPart parent) throws LogicBaseException {

        part.setCurrentWip(0);
        part.setBoard(dbBoard);
        part.setParent(parent);

        if(part.getChildren() == null || part.getChildren().isEmpty()){
            database.create(part);
        } else {
            BoardPart dbPart = database.create(part);

            for(BoardPart bp : part.getChildren()) {
                persistPart(bp, dbBoard, dbPart);
            }
        }
    }

    @Override
    public Board create(Board board, UserAccount authUser) throws LogicBaseException {
        board.setOwner(authUser);

        validate(board);
        return persist(board);
    }


    private void checkEditAccess(Board board, UserAccount userAccount) throws LogicBaseException {
        if(!board.getOwner().getId().equals(userAccount.getId())) {
            board.queryMembership(database.getEntityManager(), userAccount.getId());
            if(board.getMembership() == null || !board.getMembership().isKanbanMaster()) {
                throw new TransactionException("User does not have sufficient rights.", ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }
    }

    private boolean hasCardsAssigned(BoardPart boardPart) {
        return boardPart.getCurrentWip() > 0;
    }

    private void recDelete(BoardPart bp) throws DatabaseException {
        if(bp.getChildren() != null) {
            for(BoardPart cBp : bp.getChildren()) {
                recDelete(cBp);
            }
        }

        bp.setParent(null);
        database.update(bp);

        database.delete(BoardPart.class, bp.getId());
    }

    private void updateBoardParts(Set<BoardPart> dbBoardPart, Set<BoardPart> newBoardPart, UserAccount authUser) throws LogicBaseException {
        HashMap<UUID, BoardPart> map = UUIDEntity.buildMap(dbBoardPart);

        for(BoardPart nBp : newBoardPart) {
            BoardPart dbBp = map.get(nBp.getId());
            if(dbBp == null) {
                dbBp = database.create(nBp);
                dbBp.setChildren(new HashSet<>());
            } else {
                if(dbBp.getLeafNumber() != null && hasCardsAssigned(dbBp)) {
                    if(nBp.getLeafNumber() == null) {
                        throw new TransactionException("Board part with cards can not be expanded to sub parts.");
                    }
                }

                if (!nBp.getMaxWip().equals(dbBp.getMaxWip()) && dbBp.getCurrentWip() > nBp.getMaxWip()) {
                    createWipViolations(dbBp, dbBp, authUser);
                }

                database.update(nBp);
                map.remove(dbBp.getId());
            }
            if(nBp.getChildren() == null) nBp.setChildren(new HashSet<>());
            updateBoardParts(dbBp.getChildren(), nBp.getChildren(), authUser);
        }

        for(BoardPart bp : map.values()) {
            if(hasCardsAssigned(bp)) {
                throw new TransactionException("Board part with cards can not be deleted.");
            } else {
                BoardPart p = bp.getParent();
                if(p != null){
                    p.getChildren().remove(bp);
                    if(p.getChildren().size() == 0){
                        p = database.update(p);
                    }
                }

                recDelete(bp);
            }
        }
    }

    private void createWipViolations(BoardPart dbBoardPart, BoardPart originalBp, UserAccount authUser) throws DatabaseException {
        for (Card c : dbBoardPart.getCards()) {
            CardMove violationMove = new CardMove();
            violationMove.setCard(c);
            violationMove.setFrom(originalBp);
            violationMove.setTo(originalBp);
            violationMove.setCardMoveType(CardMoveType.INVALID);
            violationMove.setMovedBy(authUser);

            database.create(violationMove);
            database.update(c);
        }

        for (BoardPart bp : dbBoardPart.getChildren()) {
            createWipViolations(bp, originalBp, authUser);
        }
    }

    private void updateProject(Board dbBoard, Board newBoard) throws DatabaseException {
        HashMap<UUID, Project> assignedProject = UUIDEntity.buildMap(dbBoard.getProjects());

        for(Project p : newBoard.getProjects()) {
            Project dbP = assignedProject.get(p.getId());
            if(dbP == null) {
                p.setBoard(dbBoard);
                database.update(p);
            } else {
                assignedProject.remove(dbP.getId());
            }
        }

        for(Project p : assignedProject.values()) {
            p.setBoard(null);
            database.update(p);
        }
    }

    private Board updateBoard(Board dbBoard, Board newBoard, UserAccount authUser) throws LogicBaseException {
        updateProject(dbBoard, newBoard);

        dbBoard.buildBoardPartsReferences();
        updateBoardParts(dbBoard.getBoardParts(), newBoard.getBoardParts(), authUser);

        dbBoard = database.update(newBoard);



        dbBoard = database.get(Board.class, dbBoard.getId());
        dbBoard.buildBoardPartsReferences();
        dbBoard.fetchProjectsWithCards();


        return dbBoard;
    }

    @Override
    public Board update(Board board, UserAccount authUser) throws LogicBaseException {
        validate(board);

        Board dbBoard = database.get(Board.class, board.getId());

        validateProject(dbBoard, board);
        checkEditAccess(dbBoard, authUser);

        dbBoard = updateBoard(dbBoard, board, authUser);

        return dbBoard;
    }
}
