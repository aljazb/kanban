package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.rest.resource.uuid.GetResource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.UserAccountService;
import si.fri.smrpo.kis.server.ejb.service.interfaces.UserAccountServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.UserAccountSource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.UserAccountSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.Constants.*;


@Path("UserAccount")
@RequestScoped
public class UserAccountResource extends GetResource<UserAccount, UserAccountSourceLocal, UserAccount> {

    @EJB
    private UserAccountSourceLocal userAccountSource;

    @Override
    protected void initSource() {
        source = userAccountSource;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public UserAccountResource() {
        super(UserAccount.class);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @GET
    public Response getList() throws Exception {
        String search = uriInfo.getQueryParameters().getFirst("search");

        QueryParameters param = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .maxLimit(defaultMaxLimit).defaultLimit(defaultMaxLimit).defaultOffset(0).build();

        Paging<UserAccount> paging = source.getList(type, search, param, getAuthUser());

        Response.ResponseBuilder rb = buildResponse(paging);

        rb.cacheControl(buildCacheControl(listCacheControlMaxAge, listCacheControl));

        return rb.build();
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_USER, ROLE_ADMINISTRATOR})
    @GET
    @Path("login")
    public Response loginUserInfo() throws Exception {
        return buildResponse(source.login(getAuthUser()), true).build();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, UserAccount entity) throws Exception {
        return buildResponse(source.create(entity, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}/password")
    public Response setPassword(@PathParam("id") UUID id, String password) throws Exception {
        source.setPassword(id, password, getAuthUser());
        return buildResponse().build();
    }

    @RolesAllowed({ROLE_USER})
    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws Exception {
        entity.setId(id);
        return buildResponse(source.update(entity, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_USER})
    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildResponse(source.setEnabled(id, false, getAuthUser()), xContent).build();
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("{id}/status")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildResponse(source.setEnabled(id, null, getAuthUser()), xContent).build();
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("available")
    public Response checkAvailability(UserAccount entity) throws Exception {
        source.checkAvailability(entity, getAuthUser());
        return buildResponse(null, false).build();
    }
}
