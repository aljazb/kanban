package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkFlowDate {

    private Date date;
    private ArrayList<WorkFlowColumn> columns;

    public WorkFlowDate(Date date, List<BoardPart> leafs) {
        this.date = date;

        columns = new ArrayList<>();
        for (BoardPart bp : leafs) {
            columns.add(new WorkFlowColumn(bp));
        }
    }

    public void inc(BoardPart bp) {
        columns.get(bp.getLeafNumber()).incCount();
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
