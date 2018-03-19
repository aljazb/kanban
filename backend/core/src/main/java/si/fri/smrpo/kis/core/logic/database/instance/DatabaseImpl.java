package si.fri.smrpo.kis.core.logic.database.instance;

import si.fri.smrpo.kis.core.logic.database.instance.version.DatabaseVersionImpl;

import java.io.Serializable;

public interface DatabaseImpl<K extends Serializable> extends DatabaseVersionImpl<K> {

}
