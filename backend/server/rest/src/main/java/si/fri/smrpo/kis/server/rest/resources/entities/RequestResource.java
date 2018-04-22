package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.resource.uuid.GetResource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.RequestSource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.RequestSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Request;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.Constants.*;

@Path("Request")
@RequestScoped
public class RequestResource extends GetResource<Request, RequestSourceLocal> {

    @EJB
    private RequestSourceLocal requestSource;


    @Override
    protected void initSource() {
        requestSource.setAuthUser(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = requestSource;
    }

    public RequestResource() {
        super(Request.class);
    }


    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @GET
    @Override
    public Response getList() throws Exception {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @GET
    @Path("/userRequests")
    public Response getUserRequests() {
        return Response.ok(source.getUserRequests()).build();
    }

    @RolesAllowed(ROLE_KANBAN_MASTER)
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, Request request) throws Exception {
        return buildResponse(source.create(request), xContent).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @PUT
    @Path("{id}")
    public Response accept(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildResponse(source.update(id, true), xContent).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @DELETE
    @Path("{id}")
    public Response decline(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildResponse(source.update(id, false), xContent).build();
    }
}
