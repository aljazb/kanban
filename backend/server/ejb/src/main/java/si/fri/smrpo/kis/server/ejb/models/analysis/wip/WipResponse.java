package si.fri.smrpo.kis.server.ejb.models.analysis.wip;

import java.util.ArrayList;

public class WipResponse {

    private ArrayList<WipDate> dates = new ArrayList<>();

    public WipResponse() {
    }

    public void addDate(WipDate date) {
        dates.add(date);
    }

    public ArrayList<WipDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<WipDate> dates) {
        this.dates = dates;
    }
}
