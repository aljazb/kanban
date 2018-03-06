package si.fri.smrpo.kis.app.server.ejb;

import si.fri.smrpo.kis.app.server.ejb.base.BaseService;
import si.fri.smrpo.kis.app.server.ejb.interfaces.CustomerServiceLocal;
import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.businessLogic.managers.CustomerManager;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;

@RolesAllowed(AuthEntity.ROLE_CUSTOMER)
@Stateless
@Local(CustomerServiceLocal.class)
public class CustomerService extends BaseService implements CustomerServiceLocal {

    protected CustomerManager customerManager;

    @PostConstruct
    protected void init() {
        customerManager = new CustomerManager(databaseService.getDatabase());
    }

    @Override
    public UserAccount get(AuthEntity authEntity) throws BusinessLogicTransactionException {
        return customerManager.get(authEntity);
    }
}
