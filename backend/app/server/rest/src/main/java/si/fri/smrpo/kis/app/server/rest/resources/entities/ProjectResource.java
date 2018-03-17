package si.fri.smrpo.kis.app.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.app.server.ejb.utility.AuthUtility;
import si.fri.smrpo.kis.core.jpa.entities.Project;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.restComponents.resource.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("Project")
@RequestScoped
public class ProjectResource extends CrudResource<Project> {

    @EJB
    private DatabaseServiceLocal databaseImpl;

    @Override
    protected DatabaseServiceLocal getDatabaseService() {
        return databaseImpl;
    }

    protected UserAccount getAuthorizedEntity() {
        return AuthUtility.getAuthorizedEntity((KeycloakPrincipal) sc.getUserPrincipal());
    }

    public ProjectResource() {
        super(Project.class);
    }

}
