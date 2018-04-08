package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.resource.uuid.GetResource;
import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.RequestAuthManager;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Request;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.*;

@Path("Request")
@RequestScoped
public class RequestResource extends GetResource<Request, GetSource<Request, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private RequestServiceLocal service;


    private RequestAuthManager manager;

    @Override
    protected void initSource() {
        manager = new RequestAuthManager(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = new GetSource<>(databaseService, manager);
    }

    public RequestResource() {
        super(Request.class);
    }


    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @GET
    @Override
    public Response getList() throws ApiException {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @GET
    @Path("/userRequests")
    public Response getUserRequests() {
        return Response.ok(service.getUserRequests(manager.getUserId())).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws ApiException {
        return super.get(id);
    }

    @RolesAllowed(ROLE_KANBAN_MASTER)
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, Request request) throws ApiException {
        try {
            Request dbRequest = service.create(request, manager.getUserId());
            return buildResponse(dbRequest, xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @PUT
    @Path("{id}")
    public Response accept(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        try {
            Request dbRequest = service.update(id, manager.getUserId(), true);
            return buildResponse(dbRequest, xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @DELETE
    @Path("{id}")
    public Response decline(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        try {
            Request dbRequest = service.update(id, manager.getUserId(), false);
            return buildResponse(dbRequest, xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }
}
