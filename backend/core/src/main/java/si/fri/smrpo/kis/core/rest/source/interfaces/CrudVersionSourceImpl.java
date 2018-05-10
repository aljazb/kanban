package si.fri.smrpo.kis.core.rest.source.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntity;

import java.io.Serializable;

public interface CrudVersionSourceImpl<E extends BaseEntity<E, I>, I extends Serializable, A extends Serializable> extends GetSourceImpl<E, I, A> {

    E create(E newEntityVersion, A authUser) throws Exception;
    E update(E newEntityVersion, A authUser) throws Exception;
    E patch(E newEntityVersion, A authUser) throws Exception;
    E delete(Class<E> c, I id, A authUser) throws Exception;
    E toggleIsDeleted(Class<E> c, I id, A authUser) throws Exception;
    E permDelete(Class<E> c, I id, A authUser) throws Exception;

}
