package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.models.analysis.AnalysisQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowDate;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowResponse;
import si.fri.smrpo.kis.server.ejb.service.interfaces.AnalysisServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@PermitAll
@Stateless
@Local(AnalysisServiceLocal.class)
public class AnalysisService implements AnalysisServiceLocal {

    @EJB
    private DatabaseServiceLocal database;


    private CardMove getStartDev(Card card, Board board) {

        for(CardMove cm : card.getCardMoves()) {
            if(board.getStartDev().equals(cm.getTo().getLeafNumber())) {
                return cm;
            }
        }

        return null;
    }

    private CardMove getDone(Card card, Board board) {

        for(CardMove cm : card.getCardMoves()) {
            if(board.getAcceptanceTesting() + 1 == cm.getTo().getLeafNumber()) {
                return cm;
            }
        }

        return null;
    }

    private ArrayList<Card> filterCards(Set<Card> cards, Board board, AnalysisQuery query) {
        ArrayList<Card> filteredCards = new ArrayList<>();

        for(Card c : cards) {

            if(query.getRejected() != null) {
                if(query.getRejected() != c.getRejected()) {
                    continue;
                }
            }

            if(query.getSilverBullet() != null) {
                if(query.getSilverBullet() != c.getSilverBullet()) {
                    continue;
                }
            }

            if(query.getWorkloadFrom() != null) {
                if(c.getWorkload() == null || c.getWorkload() < query.getWorkloadFrom()) {
                    continue;
                }
            }

            if(query.getWorkloadTo() != null) {
                if(c.getWorkload() == null || c.getWorkload() > query.getWorkloadTo()) {
                    continue;
                }
            }

            if(query.getCreatedFrom() != null) {
                if(query.getCreatedFrom().after(c.getCreatedOn())) {
                    continue;
                }
            }

            if(query.getCreatedTo() != null) {
                if(query.getCreatedTo().before(c.getCreatedOn())) {
                    continue;
                }
            }

            CardMove devStart = getStartDev(c, board);
            if(query.getDevStartFrom() != null) {
                if(devStart == null) {
                    continue;
                } else {
                    if(query.getDevStartFrom().after(devStart.getCreatedOn())) {
                        continue;
                    }
                }
            }

            if(query.getDevStartTo() != null) {
                if(devStart == null) {
                    continue;
                } else {
                    if(query.getDevStartTo().before(devStart.getCreatedOn())) {
                        continue;
                    }
                }
            }

            CardMove devDone = getDone(c, board);
            if(query.getDevStartFrom() != null) {
                if(devDone == null) {
                    continue;
                } else {
                    if(query.getDevStartFrom().after(devDone.getCreatedOn())) {
                        continue;
                    }
                }
            }

            if(query.getDevStartTo() != null) {
                if(devDone == null) {
                    continue;
                } else {
                    if(query.getDevStartTo().after(devDone.getCreatedOn())) {
                        continue;
                    }
                }
            }


            filteredCards.add(c);
        }

        return filteredCards;
    }

    private void validate(WorkFlowQuery query, UserAccount authUser) throws LogicBaseException {
        if(query.getProject() == null || query.getProject().getId() == null) {
            throw new TransactionException("Query must have project id");
        }

        Project p = database.get(Project.class, query.getProject().getId());

        Membership m = p.queryMembership(database.getEntityManager(), authUser.getId());
        if(m == null || !m.isKanbanMaster()) {
            throw new TransactionException("User is not kanban master");
        }
    }

    private WorkFlowResponse buildWorkFlowResponse(WorkFlowQuery query) throws LogicBaseException {

        Project p = database.find(Project.class, query.getProject().getId());
        Board b = p.getBoard();

        ArrayList<Card> filteredCards = filterCards(p.getCards(), b, query);


        ArrayList<CardMove> cardMoves = new ArrayList<>();
        for(Card c : filteredCards) {
            cardMoves.addAll(c.getCardMoves());
        }
        cardMoves.sort(Comparator.comparing(CardMove::getCreatedOn));


        List<BoardPart> leaves = b.getBoardParts().stream()
                .filter(boardPart -> boardPart.getLeafNumber() != null)
                .sorted(Comparator.comparing(BoardPart::getLeafNumber))
                .collect(Collectors.toList());



        WorkFlowDate date = null;
        WorkFlowResponse response = new WorkFlowResponse();

        for(CardMove cm : cardMoves) {

            if(date == null || !date.getDate().equals(cm.getCreatedOn())) {
                date = new WorkFlowDate(cm.getCreatedOn(), leaves);
                response.addDate(date);
            }

            date.inc(cm.getTo());

        }

        return response;
    }

    public WorkFlowResponse processWorkFlowResponse(WorkFlowQuery query, UserAccount authUser) throws LogicBaseException {
        validate(query, authUser);
        return buildWorkFlowResponse(query);
    }

}
