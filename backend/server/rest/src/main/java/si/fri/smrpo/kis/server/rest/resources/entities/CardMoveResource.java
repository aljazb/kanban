package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardMoveSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("CardMove")
@RequestScoped
public class CardMoveResource extends CrudResource<CardMove, CardMoveSourceLocal> {

    @EJB
    private CardMoveSourceLocal cardSource;

    @Override
    protected void initSource() {
        cardSource.setAuthUser(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        this.source = cardSource;
    }

    public CardMoveResource() {
        super(CardMove.class);
    }
}
