package si.fri.smrpo.kis.core.rest.source;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;

import javax.annotation.security.PermitAll;
import java.io.Serializable;

@PermitAll
public abstract class CrudSource<
            E extends BaseEntity<E, I>,
            I extends Serializable
        > extends GetSource<E, I> implements CrudSourceImpl<E, I> {

    public CrudSource() { }

    public CrudSource(DatabaseImpl<I> database) {
        super(database);
    }

    public E create(E newEntity) throws Exception {
        return database.create(newEntity);
    }

    public E update(E newEntity) throws Exception {
        return database.update(newEntity);
    }

    public E patch(E newEntity) throws Exception {
        return database.patch(newEntity);
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
