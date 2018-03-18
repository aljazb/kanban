package si.fri.smrpo.kis.server.rest.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.UserAccountManagerLocal;
import si.fri.smrpo.kis.server.rest.utility.AuthUtility;
import si.fri.smrpo.kis.server.rest.managers.UserAccountDBM;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
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
    private DatabaseServiceLocal databaseImpl;

    @Override
    protected DatabaseServiceLocal getDatabaseService() {
        return databaseImpl;
    }


    protected UserAccount getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    @Override
    protected DatabaseManager<UserAccount> setInitManager() {
        return new UserAccountDBM(getAuthorizedEntity());
    }


    @EJB
    private UserAccountManagerLocal accountManager;

    public UserAccountResource() {
        super(UserAccount.class);
    }



    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("login")
    public Response loginUserInfo() throws BusinessLogicTransactionException {
        UserAccount userAccount = accountManager.login(getAuthorizedEntity());
        return buildResponse(userAccount).build();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @GET
    @Override
    public Response getList() throws BusinessLogicTransactionException {
        return super.getList();
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws BusinessLogicTransactionException {
        return super.get(id);
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, UserAccount entity) throws BusinessLogicTransactionException {
        throw BusinessLogicTransactionException.buildNotImplemented();
    }

    @RolesAllowed(ROLE_USER)
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws BusinessLogicTransactionException {
        return super.update(xContent, id, entity);
    }

    @RolesAllowed(ROLE_USER)
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws BusinessLogicTransactionException {
        return super.patch(xContent, id, entity);
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws BusinessLogicTransactionException {

        return super.delete(xContent, id);
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("{id}/toggleIsDeleted")
    @Override
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws BusinessLogicTransactionException {
        return super.toggleIsDeleted(xContent, id);
    }


}
