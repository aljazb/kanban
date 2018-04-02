package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.DevTeamAuthManager;
import si.fri.smrpo.kis.server.ejb.service.interfaces.DevTeamServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.enums.RequestType;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.stream.Collectors;

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.ROLE_ADMINISTRATOR;
import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.ROLE_DEVELOPER;
import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.ROLE_KANBAN_MASTER;

@Path("DevTeam")
@RequestScoped
public class DevTeamResource extends CrudResource<DevTeam, CrudSource<DevTeam, UUID>> {

    @EJB
    private DevTeamServiceLocal devTeamService;

    @EJB
    private RequestServiceLocal requestService;

    @EJB
    private DatabaseServiceLocal databaseService;


    private DevTeamAuthManager manager;

    @Override
    protected void initSource() {
        manager = new DevTeamAuthManager(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = new CrudSource<>(databaseService, manager);
    }


    public DevTeamResource() {
        super(DevTeam.class);
    }


    @RolesAllowed({ROLE_KANBAN_MASTER})
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, DevTeam entity) throws ApiException {
        try {
            DevTeam devTeam = devTeamService.create(entity, manager.getUserId());
            return buildResponse(devTeam, xContent, true ,Response.Status.CREATED).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, DevTeam entity) throws ApiException {
        try {
            DevTeam devTeam = devTeamService.update(entity, manager.getUserId());
            return buildResponse(devTeam, xContent, true).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, DevTeam entity) throws ApiException {
        return super.patch(xContent, id, entity);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        return super.delete(xContent, id);
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}/toggleIsDeleted")
    @Override
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        return super.toggleIsDeleted(xContent, id);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER})
    @GET
    @Override
    public Response getList() throws ApiException {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws ApiException {
        try {
            DevTeam dt = devTeamService.getWithUsers(id);
            for (UserAccountMtmDevTeam mtm : dt.getJoinedUsers()) {
                mtm.setDevTeam(null);
            }
            dt.setJoinedUsers(dt.getJoinedUsers().stream().filter(e -> !e.getIsDeleted()).collect(Collectors.toSet()));
            return buildResponse(dt, true).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER})
    @GET
    @Path("{id}/developers")
    public Response getMembers(@PathParam("id") UUID id) {
        return buildResponse(devTeamService.getDevelopers(id)).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER})
    @GET
    @Path("{id}/kanbanMaster")
    public Response getKanbanMaster(@PathParam("id") UUID id) {
        return buildResponse(devTeamService.getKanbanMaster(id), true).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER})
    @GET
    @Path("{id}/productOwner")
    public Response getProductOwner(@PathParam("id") UUID id) {
        return buildResponse(devTeamService.getProductOwner(id), true).build();
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @DELETE
    @Path("{devTeamId}/user/{uid}")
    public Response kickMember(@HeaderParam("X-Content") Boolean xContent, @PathParam("devTeamId") UUID devTeamId, @PathParam("uid") UUID userId) throws ApiException {
        try {
            return buildResponse(devTeamService.kickMember(devTeamId, userId, manager.getUserId()), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_KANBAN_MASTER})
    @DELETE
    @Path("{devTeamId}/po/{uid}")
    public Response demotePO(@HeaderParam("X-Content") Boolean xContent, @PathParam("devTeamId") UUID devTeamId, @PathParam("uid") UUID userId) throws ApiException {
        try {
            requestService.demotePO(devTeamId, manager.getUserId());
            return Response.noContent().build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }
}
