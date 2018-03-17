package si.fri.smrpo.kis.app.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.app.server.rest.resources.utility.AuthUtility;
import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.database.DatabaseImpl;
import si.fri.smrpo.kis.core.jpa.entities.Board;
import si.fri.smrpo.kis.core.restComponents.resource.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("Board")
@RequestScoped
public class BoardResource extends CrudResource<Board> {

    @EJB
    private DatabaseServiceLocal databaseImpl;

    @Override
    protected DatabaseImpl getDatabaseService() {
        return databaseImpl;
    }

    protected AuthEntity getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public BoardResource() {
        super(Board.class);
    }

}
