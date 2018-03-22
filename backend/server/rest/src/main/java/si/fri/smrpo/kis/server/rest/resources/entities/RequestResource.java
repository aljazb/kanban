package si.fri.smrpo.kis.server.rest.resources.entities;

import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Request;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("Request")
@RequestScoped
public class RequestResource extends CrudResource<Request, CrudSource<Request, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    @Override
    protected void initSource() {
        source = new CrudSource<>(databaseService);
    }


    public RequestResource() {
        super(Request.class);
    }

}
