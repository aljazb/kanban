package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.CardSource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
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

@Path("Card")
@RequestScoped
public class CardResource extends CrudResource<Card, CardSourceLocal, UserAccount> {

    @EJB
    private CardSourceLocal cardSource;

    @Override
    protected void initSource() {
        this.source = cardSource;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public CardResource() {
        super(Card.class);
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR, ROLE_USER})
    @GET
    @Override
    public Response getList() throws Exception {
        return super.getList();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR, ROLE_USER})
    @GET
    @Path("{id}")
    @Override
    public Response get(@PathParam("id") UUID id) throws Exception {
        return super.get(id);
    }

    @RolesAllowed({ROLE_PRODUCT_OWNER, ROLE_KANBAN_MASTER})
    @POST
    @Override
    public Response create(@HeaderParam("X-Content") Boolean xContent, Card entity) throws Exception {
        return buildResponse(source.create(entity, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @PUT
    @Path("{id}")
    @Override
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, Card entity) throws Exception {
        return buildResponse(source.update(entity, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @PATCH
    @Path("{id}")
    @Override
    public Response patch(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, Card entity) throws Exception {
        return buildResponse(source.patch(entity, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id) throws Exception {
        String message = this.uriInfo.getQueryParameters().getFirst("message");
        return buildResponse(source.delete(type, id, message, getAuthUser()), xContent).build();
    }

    @RolesAllowed({ROLE_DEVELOPER, ROLE_KANBAN_MASTER, ROLE_PRODUCT_OWNER, ROLE_ADMINISTRATOR})
    @Override
    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") UUID id) throws Exception {

        return buildNotImplemented();
    }
}
