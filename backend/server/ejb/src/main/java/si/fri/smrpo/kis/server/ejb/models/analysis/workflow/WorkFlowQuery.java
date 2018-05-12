package si.fri.smrpo.kis.server.ejb.models.analysis.workflow;

import si.fri.smrpo.kis.server.ejb.models.analysis.AnalysisQuery;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;

import java.util.Set;

public class WorkFlowQuery extends AnalysisQuery {

    private Set<BoardPart> leafBoardParts;

    public Set<BoardPart> getLeafBoardParts() {
        return leafBoardParts;
    }

    public void setLeafBoardParts(Set<BoardPart> leafBoardParts) {
        this.leafBoardParts = leafBoardParts;
    }
}