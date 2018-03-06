package si.fri.smrpo.kis.app.server.ejb.base;

import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;

import javax.ejb.EJB;

public abstract class BaseService {

    @EJB
    protected DatabaseServiceLocal databaseService;

}
