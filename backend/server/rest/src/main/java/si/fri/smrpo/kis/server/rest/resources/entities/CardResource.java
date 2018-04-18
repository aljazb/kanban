package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.CardAuthManager;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("Card")
@RequestScoped
public class CardResource extends CrudResource<Card, CrudSource<Card, UUID>> {

    @EJB
    private DatabaseServiceLocal database;

    private CardAuthManager manager;

    @Override
    protected void initSource() {
        manager = new CardAuthManager(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = new CrudSource<>(database, manager);
    }

    public CardResource() {
        super(Card.class);
    }

}
