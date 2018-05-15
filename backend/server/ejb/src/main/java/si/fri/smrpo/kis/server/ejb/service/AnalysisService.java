package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.models.analysis.AnalysisQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.time.TimeCard;
import si.fri.smrpo.kis.server.ejb.models.analysis.time.TimeQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.time.TimeResponse;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipDate;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipResponse;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowDate;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowResponse;
import si.fri.smrpo.kis.server.ejb.service.interfaces.AnalysisServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

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


    @Override
    public List<Project> getProjects(UserAccount authUser) throws LogicBaseException {
        return database.getEntityManager()
                .createNamedQuery("projects.with.membership.kanban-master", Project.class)
                .setParameter("userId", authUser.getId())
                .getResultList();
    }

    private CardMove getStartDev(Card card, Board board) {

        for(CardMove cm : card.getCardMoves()) {
            if(board.getStartDev().equals(cm.getTo().getLeafNumber())) {
                return cm;
            }
        }

        return null;
    }

    private CardMove getFinished(Card card, Board board) {

        for(CardMove cm : card.getCardMoves()) {
            if(cm.getTo().getLeafNumber() != null && board.getAcceptanceTesting() + 1 == cm.getTo().getLeafNumber()) {
                return cm;
            }
        }

        return null;
    }

    private ArrayList<Card> filterCards(Set<Card> cards, Board board, AnalysisQuery query) {
        ArrayList<Card> filteredCards = new ArrayList<>();

        for(Card c : cards) {
            if(c.getIsDeleted()) {
                continue;
            }

            if(query.getNewFunctionality() != null) {
                if(c.getRejected() || c.getSilverBullet()) {
                    continue;
                }
            }

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
                    if(!devStart.getCreatedOn().after(query.getDevStartFrom())) {
                        continue;
                    }
                }
            }

            if(query.getDevStartTo() != null) {
                if(devStart == null) {
                    continue;
                } else {
                    if(!devStart.getCreatedOn().before(query.getDevStartTo())) {
                        continue;
                    }
                }
            }

            CardMove devFinished = getFinished(c, board);
            if(query.getFinishedFrom() != null) {
                if(devFinished == null) {
                    continue;
                } else {
                    if(!devFinished.getCreatedOn().after(query.getFinishedFrom())) {
                        continue;
                    }
                }
            }

            if(query.getFinishedTo() != null) {
                if(devFinished == null) {
                    continue;
                } else {
                    if(!devFinished.getCreatedOn().before(query.getFinishedTo())) {
                        continue;
                    }
                }
            }


            filteredCards.add(c);
        }

        return filteredCards;
    }

    private void validate(AnalysisQuery query, UserAccount authUser) throws LogicBaseException {
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
            cardMoves.addAll(
                    c.getCardMoves().stream()
                            .filter(cm -> query.isShowDateValid(cm))
                            .collect(Collectors.toSet())
            );
        }
        cardMoves.sort(Comparator.comparing(CardMove::getCreatedOn));


        Set<UUID> ids = query.getLeafBoardParts().stream().map(BoardPart::getId).collect(Collectors.toSet());
        List<BoardPart> leaves = b.getBoardParts().stream()
                .filter(boardPart -> ids.contains(boardPart.getId()))
                .sorted(Comparator.comparing(BoardPart::getLeafNumber))
                .collect(Collectors.toList());



        WorkFlowResponse response = new WorkFlowResponse();

        if(!cardMoves.isEmpty()) {
            WorkFlowDate date = null;

            for(CardMove cm : cardMoves) {

                if(date == null || !date.equalDate(cm.getCreatedOn())) {
                    date = new WorkFlowDate(cm.getCreatedOn(), leaves);
                    response.addDate(date);
                }

                date.inc(cm.getTo());
            }
        }

        return response;
    }

    @Override
    public WorkFlowResponse processWorkFlowResponse(WorkFlowQuery query, UserAccount authUser) throws LogicBaseException {
        validate(query, authUser);
        return buildWorkFlowResponse(query);
    }


    private WipResponse buildWipResponse(WipQuery query) throws DatabaseException {
        Project p = database.find(Project.class, query.getProject().getId());
        Board b = p.getBoard();

        ArrayList<Card> filteredCards = filterCards(p.getCards(), b, query);


        ArrayList<CardMove> cardMoves = new ArrayList<>();
        for(Card c : filteredCards) {
            cardMoves.addAll(
                    c.getCardMoves().stream()
                            .filter(cm -> cm.getCardMoveType() != CardMoveType.VALID && query.isShowDateValid(cm))
                            .collect(Collectors.toSet())
            );
        }
        cardMoves.sort(Comparator.comparing(CardMove::getCreatedOn));


        WipResponse response = new WipResponse();

        if(!cardMoves.isEmpty()) {
            WipDate date = null;

            for(CardMove cm : cardMoves) {

                if(date == null || !date.equalDate(cm.getCreatedOn())) {
                    date = new WipDate(cm.getCreatedOn());
                    response.addDate(date);
                }

                cm.getMovedBy().getEmail(); // Fetch
                cm.getTo().getLeafNumber(); // Fetch
                cm.getFrom().getLeafNumber(); // Fetch

                date.addCardMove(cm);
            }
        }

        return response;
    }

    @Override
    public WipResponse processWipResponse(WipQuery query, UserAccount authUser) throws LogicBaseException {
        validate(query, authUser);
        return buildWipResponse(query);
    }

    private TimeResponse buildTimeResponse(TimeQuery query) throws LogicBaseException {
        Project p = database.find(Project.class, query.getProject().getId());
        Board b = p.getBoard();

        ArrayList<Card> filteredCards = filterCards(p.getCards(), b, query);

        TimeResponse response = new TimeResponse();

        for(Card c : filteredCards) {

            CardMove from = c.getCardMoves().stream()
                    .filter(e -> e.getTo().getId().equals(query.getFrom().getId()))
                    .findFirst().orElse(null);

            CardMove to =c.getCardMoves().stream()
                    .filter(e -> e.getTo().getId().equals(query.getTo().getId()))
                    .findFirst().orElse(null);

            if(to != null && from != null) {
                Long time = to.getCreatedOn().getTime() - from.getCreatedOn().getTime();
                time /= 1000;

                response.addTimeCard(new TimeCard(time, c));
            }
        }

        response.calculateAverageTime();

        return response;
    }

    @Override
    public TimeResponse processTimeResponse(TimeQuery query, UserAccount authUser) throws LogicBaseException {
        validate(query, authUser);
        return buildTimeResponse(query);
    }

}
