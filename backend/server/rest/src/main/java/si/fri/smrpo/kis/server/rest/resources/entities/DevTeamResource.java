package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.server.ejb.source.interfaces.DevTeamSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.Constants.*;

@Path("DevTeam")
@RequestScoped
public class DevTeamResource extends CrudResource<DevTeam, DevTeamSourceLocal, UserAccount> {

    @EJB
    private DevTeamSourceLocal devTeamSource;

    @Override
    protected void initSource() {
        source = devTeamSource;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public DevTeamResource() {
        super(DevTeam.class);
    }


    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR, ROLE_USER})
    @GET
    @Override
    public Response getList() throws Exception {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR, ROLE_USER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, DevTeam entity) throws Exception {
        return super.create(xContent, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, DevTeam entity) throws Exception {
        return super.update(xContent, id, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, DevTeam entity) throws Exception {
        return super.patch(xContent, id, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildNotImplemented();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}/toggleIsDeleted")
    @Override
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildNotImplemented();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @GET
    @Path("{id}/events")
    public Response getEvents(@PathParam("id") UUID id) throws Exception {
        return buildResponseEntity(source.getEvents(id, getAuthUser())).build();
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @DELETE
    @Path("{devTeamId}/user/{uid}")
    public Response kickMember(@HeaderParam("X-Content") Boolean xContent, @PathParam("devTeamId") UUID devTeamId, @PathParam("uid") UUID userId) throws Exception {
        return buildResponse(source.kickMember(devTeamId, userId, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @DELETE
    @Path("{devTeamId}/po/{uid}")
    public Response demotePO(@HeaderParam("X-Content") Boolean xContent, @PathParam("devTeamId") UUID devTeamId, @PathParam("uid") UUID userId) throws Exception {
        source.demoteProductOwner(devTeamId, userId, getAuthUser());
        return Response.noContent().build();
    }

}
