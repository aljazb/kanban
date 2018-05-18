package si.fri.smrpo.kis.server.rest.resources.entities;

import org.keycloak.KeycloakPrincipal;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.server.ejb.source.interfaces.SubTaskSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.SubTask;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.rest.resources.utils.KeycloakAuth;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;

@Path("SubTask")
@RequestScoped
public class SubTaskResource extends CrudResource<SubTask, SubTaskSourceLocal, UserAccount> {

    @EJB
    private SubTaskSourceLocal task;

    public SubTaskResource() {
        super(SubTask.class);
    }

    @Override
    protected void initSource() {
        this.source = task;
    }

    @Override
    protected UserAccount getAuthUser() {
        return KeycloakAuth.buildAuthUser((KeycloakPrincipal) sc.getUserPrincipal());
    }

}
