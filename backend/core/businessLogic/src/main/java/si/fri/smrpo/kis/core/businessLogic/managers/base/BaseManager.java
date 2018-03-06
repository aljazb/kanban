package si.fri.smrpo.kis.core.businessLogic.managers.base;

import si.fri.smrpo.kis.core.businessLogic.database.AuthorizationManager;
import si.fri.smrpo.kis.core.businessLogic.database.Database;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

public class BaseManager<T extends BaseEntity> {

    protected Database database;

    protected AuthorizationManager<T> authorizationManager;

    public BaseManager(Database database, AuthorizationManager<T> authorizationManager) {
        this.database = database;
        this.authorizationManager = authorizationManager;
    }

}
