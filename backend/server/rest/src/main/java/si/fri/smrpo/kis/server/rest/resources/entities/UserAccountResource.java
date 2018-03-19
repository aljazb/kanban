package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.source.CrudSourceImpl;
import si.fri.smrpo.kis.server.ejb.managers.UserAccountManagerLocal;
import si.fri.smrpo.kis.server.ejb.source.CrudSourceServiceLocal;
import si.fri.smrpo.kis.server.rest.utility.AuthUtility;
import si.fri.smrpo.kis.server.rest.managers.UserAccountDatabaseManager;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.rest.resource.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.UUID;

import static si.fri.smrpo.kis.server.rest.utility.AuthUtility.ROLE_ADMINISTRATOR;
import static si.fri.smrpo.kis.server.rest.utility.AuthUtility.ROLE_USER;


@Path("UserAccount")
@RequestScoped
public class UserAccountResource extends CrudResource<UserAccount> {

    @EJB
    private CrudSourceServiceLocal crudSourceImpl;

    @Override
    protected CrudSourceImpl<UUID> getDatabaseService() {
        return crudSourceImpl;
    }

    protected UserAccount getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    @Override
    protected DatabaseManager<UserAccount> setInitManager() {
        return new UserAccountDatabaseManager(getAuthorizedEntity());
    }


    @EJB
    private UserAccountManagerLocal accountManager;

    public UserAccountResource() {
        super(UserAccount.class);
    }



    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("login")
    public Response loginUserInfo() throws DatabaseException {
        UserAccount userAccount = accountManager.login(getAuthorizedEntity());
        return buildResponse(userAccount, true).build();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @GET
    @Override
    public Response getList() throws ApiException {
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
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, UserAccount entity) throws ApiException {
        throw ApiException.buildNotImplemented();
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws ApiException {
        return super.update(xContent, id, entity);
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws ApiException {
        return super.patch(xContent, id, entity);
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        return super.delete(xContent, id);
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("{id}/toggleIsDeleted")
    @Override
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws ApiException {
        return super.toggleIsDeleted(xContent, id);
    }


}
