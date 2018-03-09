package si.fri.smrpo.kis.app.server.rest.resources.entities;

import com.github.tfaga.lynx.beans.QueryFilter;
import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.enums.FilterOperation;
import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.app.server.rest.resources.utility.AuthUtility;
import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.database.AuthorizationManager;
import si.fri.smrpo.kis.core.businessLogic.database.Database;
import si.fri.smrpo.kis.core.businessLogic.database.DatabaseImpl;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.restComponents.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.restComponents.resource.CrudResource;
import si.fri.smrpo.kis.core.restComponents.resource.CrudVersionResource;
import si.fri.smrpo.kis.core.restComponents.utility.QueryParamatersUtility;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.UUID;

import static si.fri.smrpo.kis.app.server.rest.resources.utility.AuthUtility.*;


@Path("UserAccount")
@RequestScoped
public class UserAccountResource extends CrudResource<UserAccount> {

    @EJB
    private DatabaseServiceLocal databaseImpl;

    @Override
    protected DatabaseImpl getDatabaseService() {
        return databaseImpl;
    }

    protected AuthEntity getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public UserAccountResource() {
        super(UserAccount.class);
    }


    @RolesAllowed({ROLE_ADMINISTRATOR, ROLE_DEVELOPER})
    @GET
    @Path("login")
    public Response loginUserInfo() throws BusinessLogicTransactionException {


        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @RolesAllowed({ROLE_ADMINISTRATOR})
    @GET
    @Override
    public Response getList() throws BusinessLogicTransactionException {
        return super.getList();
    }

    //@RolesAllowed({ROLE_ADMINISTRATOR, ROLE_CUSTOMER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws BusinessLogicTransactionException {
        return super.get(id);
    }

    //@RolesAllowed(ROLE_CUSTOMER)
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, UserAccount entity) throws BusinessLogicTransactionException {
        throw BusinessLogicTransactionException.buildNotImplemented();
    }

    //@RolesAllowed(ROLE_CUSTOMER)
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws BusinessLogicTransactionException {
        return super.update(xContent, id, entity);
    }

    //@RolesAllowed(ROLE_CUSTOMER)
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws BusinessLogicTransactionException {
        return super.patch(xContent, id, entity);
    }

    //@RolesAllowed(ROLE_CUSTOMER)
    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws BusinessLogicTransactionException {
        return super.delete(xContent, id);
    }

    //@RolesAllowed(ROLE_CUSTOMER)
    @PUT
    @Path("{id}/toggleIsDeleted")
    @Override
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws BusinessLogicTransactionException {
        return super.toggleIsDeleted(xContent, id);
    }


    @Override
    protected AuthorizationManager<UserAccount> initAuthorizationManager() {
        return new AuthorizationManager<UserAccount>(getAuthorizedEntity()) {

            @Override
            public void setAuthorityFilter(QueryParameters queryParameters, Database database) throws BusinessLogicTransactionException {
                QueryFilter filter = new QueryFilter("authenticationId", FilterOperation.EQ, authEntity.getId());
                QueryParamatersUtility.addParam(queryParameters.getFilters(), filter);
            }

            @Override
            public void checkAuthority(UserAccount entity, Database database) throws BusinessLogicTransactionException {
                if(!entity.getId().equals(authEntity.getId())){
                    throw new BusinessLogicTransactionException(Response.Status.FORBIDDEN, "UserAccount does not have permission.");
                }
            }

        };
    }
}
