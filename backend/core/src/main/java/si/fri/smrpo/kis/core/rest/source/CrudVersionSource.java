package si.fri.smrpo.kis.core.rest.source;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.rest.source.interfaces.CrudVersionSourceImpl;

import java.io.Serializable;

public abstract class CrudVersionSource<
            E extends BaseEntityVersion<E, I>,
            I extends Serializable
        > extends GetSource<E, I> implements CrudVersionSourceImpl<E, I> {

    public CrudVersionSource(DatabaseImpl<I> database) {
        super(database);
    }


    public E create(E newEntityVersion) throws Exception {
        return database.createVersion(newEntityVersion);
    }

    public E update(E newEntityVersion) throws Exception {
        return database.updateVersion(newEntityVersion);
    }

    public E patch(E newEntityVersion) throws Exception {
        return database.patchVersion(newEntityVersion);
    }

    public E delete(Class<E> c, I id) throws Exception {
        return database.delete(c, id);
    }

    public E toggleIsDeleted(Class<E> c, I id) throws Exception {
        return database.toggleIsDeleted(c, id);
    }

    public E permDelete(Class<E> c, I id) throws Exception {
        return database.permDelete(c, id);
    }

}
