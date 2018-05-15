package si.fri.smrpo.kis.server.ejb.models.analysis.time;

import si.fri.smrpo.kis.server.ejb.models.analysis.AnalysisQuery;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

public class TimeQuery extends AnalysisQuery {

    private BoardPart from;
    private BoardPart to;

    public BoardPart getFrom() {
        return from;
    }

    public void setFrom(BoardPart from) {
        this.from = from;
    }

    public BoardPart getTo() {
        return to;
    }

    public void setTo(BoardPart to) {
        this.to = to;
    }
}
