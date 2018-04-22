package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.RequestSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Request;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;

@PermitAll
@Stateless
@Local(RequestSourceLocal.class)
public class RequestSource extends GetSource<Request, UUID> implements RequestSourceLocal {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private RequestServiceLocal service;


    private UserAccount authUser;

    @PostConstruct
    private void init() {
        setDatabase(databaseService);
    }


    @Override
    public Paging<Request> getList(Class<Request> c, QueryParameters param) throws Exception {
        CriteriaFilter<Request> filter = null;
        if(!authUser.getInRoleAdministrator()) {
            filter = (p, cb, r) -> cb.and(p, cb.or(
                        cb.equal(r.join("sender").get("id"), authUser.getId()),
                        cb.equal(r.join("receiver").get("id"), authUser.getId())));
        }

        return super.getList(c, param, filter, filter != null);
    }

    @Override
    public Request get(Class<Request> c, UUID id) throws Exception {
        Request entity = super.get(c, id);

        if (!authUser.getInRoleAdministrator()) {
            if(!entity.isReceiverOrSender(authUser)) {
                throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }

        return entity;
    }

    @Override
    public List<Request> getUserRequests() {
        return service.getUserRequests(authUser.getId());
    }

    @Override
    public Request create(Request request) throws Exception {
        return service.create(request, authUser.getId());
    }

    @Override
    public Request update(UUID requestId, boolean status) throws Exception {
        return service.update(requestId, authUser.getId(), status);
    }

    @Override
    public UserAccount getAuthUser() {
        return authUser;
    }

    @Override
    public void setAuthUser(UserAccount authUser) {
        this.authUser = authUser;
    }
}
