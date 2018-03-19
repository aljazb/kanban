package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.source.CrudSourceImpl;
import si.fri.smrpo.kis.server.ejb.source.CrudSourceServiceLocal;
import si.fri.smrpo.kis.server.rest.utility.AuthUtility;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("Board")
@RequestScoped
public class BoardResource extends CrudResource<Board> {

    @EJB
    private CrudSourceServiceLocal crudSourceImpl;

    @Override
    protected CrudSourceImpl<UUID> getDatabaseService() {
        return crudSourceImpl;
    }

    protected UserAccount getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public BoardResource() {
        super(Board.class);
    }

}
