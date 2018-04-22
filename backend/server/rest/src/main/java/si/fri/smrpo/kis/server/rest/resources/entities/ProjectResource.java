package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.ProjectSource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.ProjectSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.Constants.*;

@Path("Project")
@RequestScoped
public class ProjectResource extends CrudResource<Project, ProjectSourceLocal> {

    @EJB
    private ProjectSourceLocal projectSource;

    @Override
    protected void initSource() {
        projectSource.setAuthUser(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        this.source = projectSource;
    }

    public ProjectResource() {
        super(Project.class);
    }


    @RolesAllowed({ROLE_USER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR, ROLE_PRODUCT_OWNER})
    @GET
    @Override
    public Response getList() throws Exception {
        return super.getList();
    }

    @RolesAllowed({ROLE_USER, ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR, ROLE_PRODUCT_OWNER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, Project entity) throws Exception {
        return super.create(xContent, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, Project entity) throws Exception {
        return super.update(xContent, id, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, Project entity) throws Exception {
        return super.patch(xContent, id, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
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
        return super.toggleIsDeleted(xContent, id);
    }

}
