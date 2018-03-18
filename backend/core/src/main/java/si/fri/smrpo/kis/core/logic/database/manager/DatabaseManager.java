package si.fri.smrpo.kis.core.logic.database.manager;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.manager.version.DBMVersion;


public abstract class DatabaseManager<T extends BaseEntity> extends DBMVersion<T> {

}
