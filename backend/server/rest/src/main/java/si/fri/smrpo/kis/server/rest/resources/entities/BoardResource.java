package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.BoardAuthManager;
import si.fri.smrpo.kis.server.ejb.service.interfaces.BoardServiceLocal;
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

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.*;

@Path("Board")
@RequestScoped
public class BoardResource extends CrudResource<Board, CrudSource<Board, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private BoardServiceLocal service;

    private BoardAuthManager manager;

    @Override
    protected void initSource() {
        manager = new BoardAuthManager(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = new CrudSource<>(databaseService, manager);
    }

    public BoardResource() {
        super(Board.class);
    }


    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @GET
    public Response getList() throws ApiException {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) throws ApiException {
        return super.get(id);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, Board entity) throws ApiException {
        try {
            UserAccount authUser = manager.getUserAccount();
            entity.setOwner(authUser);

            return buildResponse(service.create(entity), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, Board entity) throws ApiException {
        try {
            UserAccount authUser = manager.getUserAccount();

            return buildResponse(service.update(authUser, entity), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @PATCH
    @Path("{id}")
    public Response patch(Boolean xContent, UUID id, Board entity) throws ApiException {
        throw ApiException.buildNotImplemented();
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        return super.delete(xContent, id);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}/status")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        return super.toggleIsDeleted(xContent, id);
    }


}
