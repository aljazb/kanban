package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkFlowColumn {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @JsonIgnore
    private Date date;

    @JsonProperty("value")
    private Integer count;

    public WorkFlowColumn(Date date) {
        this.date = date;
        this.count = 0;
    }

    @JsonProperty("name")
    public String getName() {
        return sdf.format(date);
    }

    public void incCount() {
        count++;
    }

    public void decCount() {
        count--;
    }

    public void add(int value) {
        count += value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonIgnore
    public WorkFlowColumn copy() {
        WorkFlowColumn wfc = new WorkFlowColumn(date);
        wfc.count = count;
        return wfc;
    }
}
