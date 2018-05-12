package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

public class WorkFlowColumn {

    @JsonIgnore
    private BoardPart boardPart;

    @JsonProperty("value")
    private Integer count;

    public WorkFlowColumn(BoardPart boardPart) {
        this.boardPart = boardPart;
        this.count = 0;
    }

    @JsonProperty("name")
    public String getName() {
        return String.format("%s [%d]",boardPart.getName(), boardPart.getLeafNumber()) ;
    }

    public void incCount() {
        count++;
    }

    public BoardPart getBoardPart() {
        return boardPart;
    }

    public void setBoardPart(BoardPart boardPart) {
        this.boardPart = boardPart;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
