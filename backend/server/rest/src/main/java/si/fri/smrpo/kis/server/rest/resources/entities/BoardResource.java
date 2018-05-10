package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.BoardServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.BoardSource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.BoardSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
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


@Path("Board")
@RequestScoped
public class BoardResource extends CrudResource<Board, BoardSourceLocal, UserAccount> {

    @EJB
    private BoardSourceLocal boardSource;

    @Override
    protected void initSource() {
        source = boardSource;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public BoardResource() {
        super(Board.class);
    }


    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR, ROLE_PRODUCT_OWNER})
    @GET
    public Response getList() throws Exception {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR, ROLE_PRODUCT_OWNER})
    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, Board entity) throws Exception {
        return super.create(xContent, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, Board entity) throws Exception {
        return super.update(xContent, id, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @PATCH
    @Path("{id}")
    public Response patch(Boolean xContent, UUID id, Board entity) throws Exception {
        return buildNotImplemented();
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return super.delete(xContent, id);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}/status")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return super.toggleIsDeleted(xContent, id);
    }


}
