package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.resource.base.BaseResource;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowQuery;
import si.fri.smrpo.kis.server.ejb.models.analysis.workflow.WorkFlowResponse;
import si.fri.smrpo.kis.server.ejb.service.interfaces.AnalysisServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("Analysis")
@RequestScoped
public class AnalysisResource extends BaseResource {

    @EJB
    private AnalysisServiceLocal service;


    private UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    @Path("Project")
    @GET
    public Response getProjects() throws Exception {
        return Response.status(Response.Status.OK).entity(service.getProjects(getAuthUser())).build();
    }

    @Path("WorkFlow")
    @PUT
    public Response getWorkFlowAnalysis(WorkFlowQuery query) throws Exception {
        WorkFlowResponse response = service.processWorkFlowResponse(query, getAuthUser());
        return Response.status(Response.Status.OK).entity(response.getDates()).build();
    }

}
