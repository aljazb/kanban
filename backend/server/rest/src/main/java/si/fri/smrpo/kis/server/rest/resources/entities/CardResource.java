package si.fri.smrpo.kis.server.rest.resources.entities;

import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.core.rest.resource.uuid.CrudResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("Card")
@RequestScoped
public class CardResource extends CrudResource<Card, CrudSource<Card, UUID>> {

    @EJB
    private DatabaseServiceLocal databaseService;

    @Override
    protected void initSource() {
        source = new CrudSource<>(databaseService);
    }

    public CardResource() {
        super(Card.class);
    }


}
