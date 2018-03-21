package si.fri.smrpo.kis.core.logic.database.manager;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.manager.version.DatabaseManagerVersion;


public abstract class DatabaseManager<T extends BaseEntity> extends DatabaseManagerVersion<T> {

}
