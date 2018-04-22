package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
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
public class UserAccountResource extends GetResource<UserAccount, UserAccountSourceLocal> {

    @EJB
    private UserAccountSourceLocal userAccountSource;

    @Override
    protected void initSource() {
        userAccountSource.setAuthUser(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = userAccountSource;
    }

    public UserAccountResource() {
        super(UserAccount.class);
    }

    @RolesAllowed({ROLE_KANBAN_MASTER, ROLE_ADMINISTRATOR})
    @GET
    public Response getList() throws Exception {
        String search = uriInfo.getQueryParameters().getFirst("search");
        source.setSearch(search);
        return super.getList();
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
        return buildResponse(source.login(), true).build();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, UserAccount entity) throws Exception {
        return buildResponse(source.create(entity), xContent).build();
    }

    @RolesAllowed({ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}/password")
    public Response setPassword(@PathParam("id") UUID id, String password) throws Exception {
        source.setPassword(id, password);
        return buildResponse().build();
    }

    @RolesAllowed({ROLE_USER})
    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, UserAccount entity) throws Exception {
        entity.setId(id);
        return buildResponse(source.update(entity), xContent).build();
    }

    @RolesAllowed({ROLE_USER})
    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildResponse(source.setEnabled(id, false), xContent).build();
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("{id}/status")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        return buildResponse(source.setEnabled(id, null), xContent).build();
    }

    @RolesAllowed(ROLE_ADMINISTRATOR)
    @PUT
    @Path("available")
    public Response checkAvailability(UserAccount entity) throws Exception {
        source.checkAvailability(entity);
        return buildResponse(null, false).build();
    }
}
