package si.fri.smrpo.kis.core.rest.resource.uuid;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.rest.enums.CacheControlType;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.resource.base.BaseResource;
import si.fri.smrpo.kis.core.rest.source.GetSource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class GetResource<
            T extends BaseEntity<T, UUID>,
            S extends GetSource<T, UUID>
        > extends BaseResource<T, S, UUID> {

    protected int defaultMaxLimit = 50;

    protected CacheControlType listCacheControl = CacheControlType.NONE;
    protected int listCacheControlMaxAge = 60;

    protected CacheControlType getCacheControl = CacheControlType.NONE;
    protected int getCacheControlMaxAge = 180;


    public GetResource(Class<T> type) {
        super(type);
    }


    @GET
    public Response getList() throws ApiException {
        QueryParameters param = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .maxLimit(defaultMaxLimit).defaultLimit(defaultMaxLimit).defaultOffset(0).build();

        Paging<T> paging = source.getList(type, param);

        Response.ResponseBuilder rb = buildResponse(paging);

        rb.cacheControl(buildCacheControl(listCacheControlMaxAge, listCacheControl));

        return rb.build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) throws ApiException {
        T dbEntity = source.get(type, id);

        EntityTag tag = dbEntity.getEntityTag();

        Response.ResponseBuilder rb = request.evaluatePreconditions(tag);
        if(rb == null){
            rb = buildResponse(dbEntity, true, false);
            rb.tag(tag);
        }

        rb.cacheControl(buildCacheControl(getCacheControlMaxAge, getCacheControl));

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
