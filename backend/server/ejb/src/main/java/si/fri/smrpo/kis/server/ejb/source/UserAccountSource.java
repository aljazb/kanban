package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.UserAccountServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.UserAccountSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.registry.infomodel.User;
import java.util.UUID;


@PermitAll
@Stateless
@Local(UserAccountSourceLocal.class)
public class UserAccountSource extends GetSource<UserAccount, UUID, UserAccount> implements UserAccountSourceLocal {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private UserAccountServiceLocal service;


    @PostConstruct
    private void init() {
        setDatabase(databaseService);
    }

    @Override
    public Paging<UserAccount> getList(Class<UserAccount> c, String search, QueryParameters param, UserAccount authUser) throws Exception {
        CriteriaFilter<UserAccount> filter = null;
        if(search != null) {
            filter = (p, cb, r) -> cb.and(p, cb.or(
                    cb.like(r.get("username"), search),
                    cb.like(r.get("email"), search),
                    cb.like(r.get("firstName"), search),
                    cb.like(r.get("lastName"), search)));
        }

        return super.getList(c, param, filter, filter != null, authUser);
    }

    @Override
    public UserAccount get(Class<UserAccount> c, UUID id, UserAccount authUser) throws Exception {
        UserAccount entity = super.get(c, id, authUser);

        if (!authUser.getInRoleAdministrator() && !entity.getId().equals(authUser.getId())) {
            throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        return entity;
    }

    @Override
    public UserAccount login(UserAccount authUser) throws LogicBaseException {
        return service.login(authUser);
    }

    @Override
    public UserAccount create(UserAccount entity, UserAccount authUser) throws LogicBaseException {
        return service.create(entity);
    }

    @Override
    public UserAccount update(UserAccount entity, UserAccount authUser) throws LogicBaseException {
        if(!authUser.getInRoleAdministrator() && !authUser.getId().equals(entity.getId())) {
            throw new OperationException("User does not have rights", ExceptionType.INSUFFICIENT_RIGHTS);
        }
        return service.update(entity);
    }

    @Override
    public UserAccount setEnabled(UUID id, Boolean enabled, UserAccount authUser) throws LogicBaseException {
        if(!authUser.getInRoleAdministrator() && !authUser.getId().equals(id)) {
            throw new OperationException("User does not have rights", ExceptionType.INSUFFICIENT_RIGHTS);
        }
        return service.setEnabled(id, enabled);
    }

    @Override
    public void setPassword(UUID id, String password, UserAccount authUser) throws LogicBaseException {
        service.setPassword(id, password);
    }

    @Override
    public void checkAvailability(UserAccount userAccount, UserAccount authUser) throws LogicBaseException {
        service.checkAvailability(userAccount);
    }

}
