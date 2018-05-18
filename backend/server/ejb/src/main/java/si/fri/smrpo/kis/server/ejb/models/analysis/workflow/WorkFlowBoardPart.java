package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

import java.text.SimpleDateFormat;
import java.util.*;

import static si.fri.smrpo.kis.server.ejb.models.analysis.Utility.roundUpDateToDay;

public class WorkFlowBoardPart {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @JsonIgnore
    private BoardPart boardPart;

    @JsonProperty("series")
    private ArrayList<WorkFlowColumn> columns = new ArrayList<>();

    @JsonIgnore
    private HashMap<String, WorkFlowColumn> map = new HashMap<>();

    public WorkFlowBoardPart(BoardPart boardPart, Date from, Date to) {
        this.boardPart = boardPart;

        Calendar cal = Calendar.getInstance();
        cal.setTime(roundUpDateToDay(from));
        cal.add(Calendar.DAY_OF_MONTH, -1);

        Date now = cal.getTime();

        while (now.before(to)) {
            WorkFlowColumn wfc = new WorkFlowColumn(now);

            cal.add(Calendar.DAY_OF_MONTH, 1);
            now = cal.getTime();

            map.put(sdf.format(now), wfc);
            columns.add(wfc);
        }
    }


    @JsonProperty("name")
    public String getName() {
        return boardPart.getName();
    }

    public void inc(Date now) {
        WorkFlowColumn wfcTo = map.get(sdf.format(now));
        if(wfcTo != null) {
            wfcTo.incCount();
        }
    }

    public void dec(Date now) {
        WorkFlowColumn wfcTo = map.get(sdf.format(now));
        if(wfcTo != null) {
            wfcTo.decCount();
        }
    }

    public ArrayList<WorkFlowColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<WorkFlowColumn> columns) {
        this.columns = columns;
    }

}
