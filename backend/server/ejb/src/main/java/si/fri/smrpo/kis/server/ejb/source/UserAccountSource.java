package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.UserAccountServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.RequestSourceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.UserAccountSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import java.util.UUID;

@PermitAll
@Stateless
@Local(UserAccountSourceLocal.class)
public class UserAccountSource extends GetSource<UserAccount, UUID> implements UserAccountSourceLocal {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private UserAccountServiceLocal service;


    private UserAccount authUser;
    private String search;


    @PostConstruct
    private void init() {
        setDatabase(databaseService);
    }

    @Override
    public Paging<UserAccount> getList(Class<UserAccount> c, QueryParameters param) throws Exception {
        CriteriaFilter<UserAccount> filter = null;
        if(search != null) {
            filter = (p, cb, r) -> cb.and(p, cb.or(
                    cb.like(r.get("username"), search),
                    cb.like(r.get("email"), search),
                    cb.like(r.get("firstName"), search),
                    cb.like(r.get("lastName"), search)));
        }

        return super.getList(c, param, filter, filter != null);
    }

    @Override
    public UserAccount get(Class<UserAccount> c, UUID id) throws Exception {
        UserAccount entity = super.get(c, id);

        if (!authUser.getInRoleAdministrator() && !entity.getId().equals(authUser.getId())) {
            throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        return entity;
    }

    @Override
    public UserAccount login() throws LogicBaseException {
        return service.login(authUser);
    }

    @Override
    public UserAccount create(UserAccount entity) throws LogicBaseException {
        return service.create(entity);
    }

    @Override
    public UserAccount update(UserAccount entity) throws LogicBaseException {
        if(!authUser.getId().equals(entity.getId())){
            throw new OperationException("User does not have rights", ExceptionType.INSUFFICIENT_RIGHTS);
        }
        return service.update(entity);
    }

    @Override
    public UserAccount setEnabled(UUID id, Boolean enabled) throws LogicBaseException {
        if(!authUser.getInRoleAdministrator() && !authUser.getId().equals(id)) {
            throw new OperationException("User does not have rights", ExceptionType.INSUFFICIENT_RIGHTS);
        }
        return service.setEnabled(id, enabled);
    }

    @Override
    public void setPassword(UUID id, String password) throws LogicBaseException {
        service.setPassword(id, password);
    }

    @Override
    public void checkAvailability(UserAccount userAccount) throws LogicBaseException {
        service.checkAvailability(userAccount);
    }

    @Override
    public UserAccount getAuthUser() {
        return authUser;
    }

    @Override
    public void setAuthUser(UserAccount authUser) {
        this.authUser = authUser;
    }


    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
