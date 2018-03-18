package si.fri.smrpo.kis.core.logic.database.manager.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.base.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.core.DBCore;
import si.fri.smrpo.kis.core.logic.database.manager.base.DBMBase;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;

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
