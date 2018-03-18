package si.fri.smrpo.kis.server.rest.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.rest.utility.AuthUtility;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("Card")
@RequestScoped
public class CardResource extends CrudResource<Card> {

    @EJB
    private DatabaseServiceLocal databaseImpl;

    @Override
    protected DatabaseServiceLocal getDatabaseService() {
        return databaseImpl;
    }

    protected UserAccount getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public CardResource() {
        super(Card.class);
    }

}
