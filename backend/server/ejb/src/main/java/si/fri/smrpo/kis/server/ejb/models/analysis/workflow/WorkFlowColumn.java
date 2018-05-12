package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

public class WorkFlowColumn {

    private BoardPart boardPart;
    private Integer count;

    public WorkFlowColumn(BoardPart boardPart) {
        this.boardPart = boardPart;
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
