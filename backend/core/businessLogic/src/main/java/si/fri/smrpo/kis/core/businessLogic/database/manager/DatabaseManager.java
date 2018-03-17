package si.fri.smrpo.kis.core.businessLogic.database.manager;


import si.fri.smrpo.kis.core.businessLogic.database.manager.version.DBMVersion;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;


public abstract class DatabaseManager<T extends BaseEntity> extends DBMVersion<T> {

}
