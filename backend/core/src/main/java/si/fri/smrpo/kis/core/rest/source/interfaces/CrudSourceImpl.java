package si.fri.smrpo.kis.core.rest.source.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntity;

import java.io.Serializable;

public interface CrudSourceImpl<E extends BaseEntity<E, I>, I extends Serializable> extends GetSourceImpl<E, I> {

    E create(E newEntity) throws Exception;
    E update(E newEntity) throws Exception;
    E patch(E newEntity) throws Exception;
    E delete(Class<E> c, I id) throws Exception;
    E toggleIsDeleted(Class<E> c, I id) throws Exception;
    E permDelete(Class<E> c, I id) throws Exception;

}
