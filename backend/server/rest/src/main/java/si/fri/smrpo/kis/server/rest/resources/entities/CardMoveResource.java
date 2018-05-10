package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.core.rest.resource.uuid.GetResource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardMoveSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.Constants.*;
import static si.fri.smrpo.kis.server.ejb.Constants.ROLE_KANBAN_MASTER;

@Path("CardMove")
@RequestScoped
public class CardMoveResource extends GetResource<CardMove, CardMoveSourceLocal, UserAccount> {

    @EJB
    private CardMoveSourceLocal cardSource;

    @Override
    protected void initSource() {
        this.source = cardSource;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public CardMoveResource() {
        super(CardMove.class);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @GET
    @Override
    public Response getList() throws Exception {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, CardMove entity) throws Exception {
        return buildResponse(source.create(entity, getAuthUser()), xContent, true, Response.Status.CREATED).build();
    }

}
