package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.CardSource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("Card")
@RequestScoped
public class CardResource extends CrudResource<Card, CardSourceLocal> {

    @EJB
    private CardSourceLocal cardSource;

    @Override
    protected void initSource() {
        cardSource.setAuthUser(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        this.source = cardSource;
    }

    public CardResource() {
        super(Card.class);
    }


    @Override
    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") UUID id) throws Exception {

        return buildNotImplemented();
    }
}
