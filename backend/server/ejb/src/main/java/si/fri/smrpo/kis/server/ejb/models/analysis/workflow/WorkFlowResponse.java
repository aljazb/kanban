package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import java.util.ArrayList;

public class WorkFlowResponse {

    private ArrayList<WorkFlowDate> dates = new ArrayList<>();

    public WorkFlowResponse() {

    }

    public ArrayList<WorkFlowDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<WorkFlowDate> dates) {
        this.dates = dates;
    }

    public void addDate(WorkFlowDate date) {
        dates.add(date);
    }


}
