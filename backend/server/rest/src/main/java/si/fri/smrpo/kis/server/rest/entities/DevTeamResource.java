package si.fri.smrpo.kis.server.rest.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.rest.utility.AuthUtility;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("DevTeam")
@RequestScoped
public class DevTeamResource extends CrudResource<DevTeam> {

    @EJB
    private DatabaseServiceLocal databaseImpl;

    @Override
    protected DatabaseServiceLocal getDatabaseService() {
        return databaseImpl;
    }

    protected UserAccount getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public DevTeamResource() {
        super(DevTeam.class);
    }

}