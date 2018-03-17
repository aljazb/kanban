package si.fri.smrpo.kis.core.businessLogic.database.instance.version;

import si.fri.smrpo.kis.core.businessLogic.database.instance.core.DBCoreImpl;
import si.fri.smrpo.kis.core.businessLogic.database.manager.core.DBMCore;
import si.fri.smrpo.kis.core.businessLogic.database.manager.version.DBMVersion;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;

import java.io.Serializable;

public interface DBVersionImpl<K extends Serializable> extends DBCoreImpl<K> {

    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException;

}
