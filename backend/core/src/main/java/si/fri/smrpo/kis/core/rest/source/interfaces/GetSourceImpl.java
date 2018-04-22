package si.fri.smrpo.kis.core.rest.source.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;

import java.io.Serializable;

public interface GetSourceImpl<E extends BaseEntity<E, I>, I extends Serializable> extends BaseSourceImpl<I> {

    Paging<E> getList(Class<E> c, QueryParameters param) throws Exception;
    Paging<E> getList(Class<E> c, QueryParameters param, CriteriaFilter<E> customFilter, boolean distinct) throws Exception;
    E get(Class<E> c, I id) throws Exception;

}
