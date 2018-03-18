package si.fri.smrpo.kis.core.logic.database.instance.version;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.core.DBCoreImpl;
import si.fri.smrpo.kis.core.logic.database.manager.version.DBMVersion;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;

import java.io.Serializable;

public interface DBVersionImpl<K extends Serializable> extends DBCoreImpl<K> {

    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException;

}
