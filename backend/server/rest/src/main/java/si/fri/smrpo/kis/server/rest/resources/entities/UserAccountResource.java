package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.resource.uuid.GetResource;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.UserAccountServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.UserAccountAuthManager;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.ROLE_ADMINISTRATOR;
import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.ROLE_KANBAN_MASTER;
import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.ROLE_USER;


@Path("UserAccount")
@RequestScoped
public class UserAccountResource extends GetResource<UserAccount, GetSource<UserAccount, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private UserAccountServiceLocal accountManager;

    private UserAccountAuthManager manager;

    @Override
    protected void initSource() {
        manager = new UserAccountAuthManager(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = new CrudSource<>(databaseService, manager);
    }

    public UserAccountResource() {
        super(UserAccount.class);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @GET
    public Response getList(@QueryParam("search") String search) throws ApiException {
        manager.setSearch(search);
        return super.getList();
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws ApiException {
        return super.get(id);
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("login")
    public Response loginUserInfo() throws ApiException {
        try {
            UserAccount userAccount = accountManager.login(manager.getUserAccount());
            return buildResponse(userAccount, true).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, UserAccount entity) throws ApiException {
        try {
            return buildResponse(accountManager.create(entity), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}/password")
    public Response setPassword(@PathParam("id") UUID id, String password) throws ApiException {
        try {
            accountManager.setPassword(id, password);
            return buildResponse().build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_USER})
    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws ApiException {
        entity.setId(id);
        try {
            return buildResponse(accountManager.update(entity), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed({ROLE_USER})
    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        try {
            return buildResponse(accountManager.setEnabled(id, false), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("{id}/status")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        try {
            return buildResponse(accountManager.setEnabled(id, null), xContent).build();
        } catch (LogicBaseException e) {
            throw ApiException.transform(e);
        }
    }
}
