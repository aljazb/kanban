package si.fri.smrpo.kis.core.businessLogic.database.manager.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.businessLogic.database.instance.core.DBCore;
import si.fri.smrpo.kis.core.businessLogic.database.manager.base.DBMBase;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

public class DBMCore <T extends BaseEntity> extends DBMBase {

    public void authQuery(DBCore dbCore, Class<T> c, QueryParameters param) throws BusinessLogicTransactionException {

    }

    public void authCriteria(DBCore dbCore, Class<T> c, CriteriaFilter<T> criteriaFilter) throws BusinessLogicTransactionException {

    }

    public void authGet(DBCore db, T entity) throws BusinessLogicTransactionException {

    }

    public void authSet(DBCore db, T entity) throws BusinessLogicTransactionException {

    }

    public void validate(DBCore db, T entity) throws BusinessLogicTransactionException {

    }

}
