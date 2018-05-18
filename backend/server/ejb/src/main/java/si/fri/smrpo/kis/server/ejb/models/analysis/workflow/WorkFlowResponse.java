package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;


import java.util.ArrayList;

public class WorkFlowResponse {


    private ArrayList<WorkFlowBoardPart> dates = new ArrayList<>();


    public ArrayList<WorkFlowBoardPart> getDates() {
        return dates;
    }

    public void setDates(ArrayList<WorkFlowBoardPart> dates) {
        this.dates = dates;
    }

    public void addDate(WorkFlowBoardPart date) {
        dates.add(date);
    }

}
