package si.fri.smrpo.kis.server.rest.resources.entities;

import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("Board")
@RequestScoped
public class BoardResource extends CrudResource<Board, CrudSource<Board, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    @Override
    protected void initSource() {
        source = new CrudSource<>(databaseService);
    }

    public BoardResource() {
        super(Board.class);
    }

}
