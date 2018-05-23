package si.fri.smrpo.kis.server.ejb.models.analysis.devRatio;

import java.util.ArrayList;
import java.util.List;

public class DeveloperRatioResponse {

    private List<DeveloperRatioSeries> data = new ArrayList<>();

    public List<DeveloperRatioSeries> getData() {
        return data;
    }

    public void setData(List<DeveloperRatioSeries> data) {
        this.data = data;
    }

    public void add(DeveloperRatioSeries item) {
        this.data.add(item);
    }
}
