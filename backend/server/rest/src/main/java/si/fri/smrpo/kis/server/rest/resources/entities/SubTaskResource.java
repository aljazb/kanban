package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.SubTaskSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.server.jpa.entities.SubTask;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.Constants.*;
import static si.fri.smrpo.kis.server.ejb.Constants.ROLE_ADMINISTRATOR;
import static si.fri.smrpo.kis.server.ejb.Constants.ROLE_KANBAN_MASTER;

@Path("SubTask")
@RequestScoped
public class SubTaskResource extends CrudResource<SubTask, SubTaskSourceLocal, UserAccount> {

    @EJB
    private SubTaskSourceLocal task;

    public SubTaskResource() {
        super(SubTask.class);
    }

    @Override
    protected void initSource() {
        this.source = task;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    @RolesAllowed({ROLE_USER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR, ROLE_PRODUCT_OWNER})
    @GET
    @Override
    public Response getList() throws Exception {
        return buildNotImplemented();
    }

    @RolesAllowed({ROLE_USER, ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR, ROLE_PRODUCT_OWNER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, SubTask entity) throws Exception {
        return super.create(xContent, entity);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, SubTask entity) throws Exception {
        return super.update(xContent, id, entity);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, SubTask entity) throws Exception {
        return buildNotImplemented();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER})
    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return super.delete(xContent, id);
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("{id}/toggleIsDeleted")
    @Override
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildNotImplemented();
    }

}
