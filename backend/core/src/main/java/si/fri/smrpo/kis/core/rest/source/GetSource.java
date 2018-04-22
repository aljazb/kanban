package si.fri.smrpo.kis.core.rest.source;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;

import javax.annotation.security.PermitAll;
import java.io.Serializable;

@PermitAll
public abstract class GetSource<
        E extends BaseEntity<E, I>,
        I extends Serializable> extends BaseSource<I> implements GetSourceImpl<E, I> {

    public GetSource() { }

    public GetSource(DatabaseImpl<I> database) {
        super(database);
    }

    public Paging<E> getList(Class<E> c, QueryParameters param) throws Exception {
        return database.getList(c, param);
    }

    public Paging<E> getList(Class<E> c, QueryParameters param, CriteriaFilter<E> customFilter, boolean distinct) throws Exception {
        return database.getList(c, param, customFilter, distinct);
    }

    public E get(Class<E> c, I id) throws Exception {
        return database.get(c, id);
    }

}
