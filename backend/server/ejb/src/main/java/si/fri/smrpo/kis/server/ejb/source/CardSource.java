package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.CardSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.From;
import java.util.UUID;

@PermitAll
@Stateless
@Local(CardSourceLocal.class)
public class CardSource extends CrudSource<Card, UUID> implements CardSourceLocal {

    private UserAccount authUser;

    @EJB
    private DatabaseServiceLocal database;


    @PostConstruct
    private void init() {
        setDatabase(database);
    }


    @Override
    public Paging<Card> getList(Class<Card> c, QueryParameters param) throws Exception {
        CriteriaFilter<Card> filter = null;
        if(!authUser.getInRoleAdministrator()) {
            filter = (p, cb, r) -> {
                From membership = r.join("project").join("devTeam").join("joinedUsers");
                return cb.and(p, cb.or(
                        cb.and(
                                cb.equal(membership.get("isDeleted"), false),
                                cb.equal(membership.join("userAccount").get("id"), authUser.getId())
                        ),
                        cb.equal(r.join("owner").get("id"), authUser.getId())
                ));
            };
        }

        return super.getList(c, param, filter, filter != null);
    }

    @Override
    public Card get(Class<Card> c, UUID id) throws Exception {
        Card entity = super.get(c, id);

        entity.queryMembership(database.getEntityManager(), authUser.getId());

        if (!authUser.getInRoleAdministrator() && entity.getMembership() == null) {
            throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        entity.getSubTasks().size();

        return entity;
    }


    public UserAccount getAuthUser() {
        return authUser;
    }

    public void setAuthUser(UserAccount authUser) {
        this.authUser = authUser;
    }
}
