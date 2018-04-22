package si.fri.smrpo.kis.core.logic.database;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseGetImpl;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.lynx.utils.JPAUtils;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@PermitAll
public abstract class DatabaseGet <I extends Serializable> extends DatabaseBase implements DatabaseGetImpl<I> {

    public DatabaseGet(EntityManager entityManager) {
        super(entityManager);
    }



    public <E extends BaseEntity<E, I>> E find(Class<E> c, I id) throws DatabaseException {
        try {
            return entityManager.find(c, id);
        } catch (Exception e){
            throw new DatabaseException(String.format("Error finding entity with id: %s", id.toString()) , e);
        }
    }

    public <E extends BaseEntity<E, I>> E get(Class<E> c, I id) throws DatabaseException {
        E entity = find(c, id);

        if(entity == null) {
            throw new DatabaseException(String.format("Entity %s with id %s does not exist.", c.getClass().getSimpleName(), id.toString()));
        }

        return entity;
    }

    public <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param) throws DatabaseException {
        return getList(c, param, null, false);
    }

    public <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param, CriteriaFilter<E> customFilter, boolean requiresDistinct) throws DatabaseException {
        try{
            List<E> items = JPAUtils.queryEntities(entityManager, c, param, customFilter, requiresDistinct);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, param, customFilter, requiresDistinct);

            return new Paging<>(items, count);
        } catch (Exception e){
            throw new DatabaseException("Exception during query.", e);
        }
    }

}
