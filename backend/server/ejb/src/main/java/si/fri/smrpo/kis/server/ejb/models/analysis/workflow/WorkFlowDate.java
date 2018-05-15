package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

import java.text.SimpleDateFormat;
import java.util.*;

import static si.fri.smrpo.kis.server.ejb.models.analysis.Utility.trimDate;

public class WorkFlowDate {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @JsonIgnore
    private Date date;

    @JsonProperty("series")
    private ArrayList<WorkFlowColumn> columns;

    @JsonIgnore
    private HashMap<UUID, WorkFlowColumn> map;

    public WorkFlowDate(Date date, List<BoardPart> leafs) {
        this.date = trimDate(date);

        map = new HashMap<>();
        columns = new ArrayList<>();
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
        Date trim = trimDate(date);
        boolean isEqual = this.date.equals(trim);

        return isEqual;
    }


    public void inc(BoardPart bp) {
        WorkFlowColumn wfc = map.get(bp.getId());
        if(wfc != null) {
            wfc.incCount();
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
}
