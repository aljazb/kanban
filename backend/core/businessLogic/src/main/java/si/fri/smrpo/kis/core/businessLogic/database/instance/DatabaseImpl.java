package si.fri.smrpo.kis.core.businessLogic.database.instance;

import si.fri.smrpo.kis.core.businessLogic.database.instance.version.DBVersionImpl;

import java.io.Serializable;

public interface DatabaseImpl<K extends Serializable> extends DBVersionImpl<K> {

}
