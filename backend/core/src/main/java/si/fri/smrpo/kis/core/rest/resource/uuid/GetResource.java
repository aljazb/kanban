package si.fri.smrpo.kis.core.rest.resource.uuid;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.rest.enums.CacheControlType;

import si.fri.smrpo.kis.core.rest.resource.base.BaseResource;
import si.fri.smrpo.kis.core.rest.source.GetSource;
import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class GetResource<
            E extends BaseEntity<E, UUID>,
            S extends GetSourceImpl<E, UUID>
        > extends BaseResource<E, S, UUID> {

    protected int defaultMaxLimit = 50;

    protected CacheControlType listCacheControl = CacheControlType.NONE;
    protected int listCacheControlMaxAge = 60;

    protected CacheControlType getCacheControl = CacheControlType.NONE;
    protected int getCacheControlMaxAge = 180;


    public GetResource(Class<E> type) {
        super(type);
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) throws Exception {
        E dbEntity = source.get(type, id);

        EntityTag tag = dbEntity.getEntityTag();

        Response.ResponseBuilder rb = request.evaluatePreconditions(tag);
        if(rb == null){
            rb = buildResponse(dbEntity, true, false);
            rb.tag(tag);
        }

        rb.cacheControl(buildCacheControl(getCacheControlMaxAge, getCacheControl));

        return rb.build();
    }

    @GET
    public Response getList() throws Exception {
        QueryParameters param = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .maxLimit(defaultMaxLimit).defaultLimit(defaultMaxLimit).defaultOffset(0).build();

        Paging<E> paging = source.getList(type, param);

        Response.ResponseBuilder rb = buildResponse(paging);

        rb.cacheControl(buildCacheControl(listCacheControlMaxAge, listCacheControl));

        return rb.build();
    }


    protected CacheControl buildCacheControl(int maxAge, CacheControlType cacheControlType){
        CacheControl cc = new CacheControl();
        cc.setMaxAge(maxAge);

        switch (cacheControlType){
            case PUBLIC:
                cc.setPrivate(false);
                break;
            case PRIVATE:
                cc.setPrivate(true);
                break;
            case NO_CACHE:
                cc.setNoCache(true);
                break;
            case NONE:
                cc.setMaxAge(0);
                break;
        }
        return cc;
    }

}
