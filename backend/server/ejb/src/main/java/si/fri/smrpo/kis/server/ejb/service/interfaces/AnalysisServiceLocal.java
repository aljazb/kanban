package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipResponse;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowResponse;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.ArrayList;
import java.util.List;

public interface AnalysisServiceLocal  {

    List<Project> getProjects(UserAccount authUser)throws LogicBaseException;

    WorkFlowResponse processWorkFlowResponse(WorkFlowQuery query, UserAccount authUser) throws LogicBaseException;

    WipResponse processWipResponse(WipQuery query, UserAccount authUser) throws LogicBaseException;

}
