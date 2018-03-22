package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.managers.ProjectAuthManager;
import si.fri.smrpo.kis.server.ejb.managers.UserAccountAuthManager;
import si.fri.smrpo.kis.server.jpa.entities.Project;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("Project")
@RequestScoped
public class ProjectResource extends CrudResource<Project, CrudSource<Project, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    private ProjectAuthManager manager;

    @Override
    protected void initSource() {
        manager = new ProjectAuthManager(KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal()));
        source = new CrudSource<>(databaseService, manager);
    }

    public ProjectResource() {
        super(Project.class);
    }

}
