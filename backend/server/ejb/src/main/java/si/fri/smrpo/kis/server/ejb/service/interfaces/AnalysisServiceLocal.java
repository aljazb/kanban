package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.models.analysis.devRatio.DeveloperRatioResponse;
import si.fri.smrpo.kis.server.ejb.models.analysis.devRatio.DeveloperRatioQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.time.TimeQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.time.TimeResponse;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.wip.WipResponse;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowResponse;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.List;

public interface AnalysisServiceLocal  {

    List<Project> getProjects(UserAccount authUser)throws LogicBaseException;

    WorkFlowResponse processWorkFlowResponse(WorkFlowQuery query, UserAccount authUser) throws LogicBaseException;

    WipResponse processWipResponse(WipQuery query, UserAccount authUser) throws LogicBaseException;

    TimeResponse processTimeResponse(TimeQuery query, UserAccount authUser) throws LogicBaseException;

    DeveloperRatioResponse processDeveloperRatio(DeveloperRatioQuery query, UserAccount authUser) throws LogicBaseException;

}
