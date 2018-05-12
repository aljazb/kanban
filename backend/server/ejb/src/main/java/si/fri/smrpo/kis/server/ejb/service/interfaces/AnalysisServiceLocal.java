package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowResponse;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public interface AnalysisServiceLocal  {

    WorkFlowResponse processWorkFlowResponse(WorkFlowQuery query, UserAccount authUser) throws LogicBaseException;

}
