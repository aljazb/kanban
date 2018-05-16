package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

import java.text.SimpleDateFormat;
import java.util.*;

import static si.fri.smrpo.kis.server.ejb.models.analysis.Utility.roundUpDateToDay;

public class WorkFlowDate {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @JsonIgnore
    private Date date;

    @JsonProperty("series")
    private ArrayList<WorkFlowColumn> columns = new ArrayList<>();

    @JsonIgnore
    private HashMap<UUID, WorkFlowColumn> map = new HashMap<>();

    public WorkFlowDate(Date date) {
        this.date = date;
    }

    public WorkFlowDate(Date date, List<BoardPart> leafs) {
        this.date = roundUpDateToDay(date);

        for (BoardPart bp : leafs) {
            WorkFlowColumn wfc = new WorkFlowColumn(bp);

            map.put(bp.getId(), wfc);
            columns.add(wfc);
        }
    }


    @JsonProperty("name")
    public String getName() {
        return sdf.format(date);
    }

    public boolean equalDate(Date date) {
        Date trim = roundUpDateToDay(date);
        boolean isEqual = this.date.equals(trim);

        return isEqual;
    }


    public void handle(CardMove cm) {
        WorkFlowColumn wfcTo = map.get(cm.getTo().getId());
        if(wfcTo != null) {
            wfcTo.incCount();
        }

        if(cm.getCardMoveType() != CardMoveType.CREATE) {
            WorkFlowColumn wfcFrom = map.get(cm.getFrom().getId());
            if(wfcFrom != null) {
                wfcFrom.decCount();
            }
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<WorkFlowColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<WorkFlowColumn> columns) {
        this.columns = columns;
    }

    public WorkFlowDate copy(Date date) {
        WorkFlowDate wfd = new WorkFlowDate(date);

        for(WorkFlowColumn wfc : columns) {
            WorkFlowColumn wfcCopy = wfc.copy();

            wfd.columns.add(wfcCopy);
            wfd.map.put(wfcCopy.getBoardPart().getId(), wfcCopy);
        }

        return wfd;
    }
}
