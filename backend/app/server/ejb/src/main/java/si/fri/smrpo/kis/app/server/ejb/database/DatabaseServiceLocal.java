package si.fri.smrpo.kis.app.server.ejb.database;

import si.fri.smrpo.kis.core.businessLogic.database.Database;
import si.fri.smrpo.kis.core.businessLogic.database.DatabaseImpl;

public interface DatabaseServiceLocal extends DatabaseImpl {

    Database getDatabase();

}
