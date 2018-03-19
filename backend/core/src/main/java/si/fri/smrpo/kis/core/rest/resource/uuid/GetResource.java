package si.fri.smrpo.kis.core.rest.resource.uuid;

import com.github.tfaga.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.rest.enums.CacheControlType;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.resource.base.BaseResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class GetResource<T extends BaseEntity<T, UUID>> extends BaseResource<T, UUID> {

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

        Paging<T> paging = getDatabaseService().getList(type, param, databaseManager);

        Response.ResponseBuilder rb = buildResponse(paging);

        if(!listCacheControl.equals(CacheControlType.NONE)){
            rb.cacheControl(buildCacheControl(listCacheControlMaxAge, listCacheControl));
        }

        return rb.build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) throws ApiException {

        T dbEntity = getDatabaseService().get(type, id, databaseManager);

        EntityTag tag = dbEntity.getEntityTag();

        Response.ResponseBuilder rb = request.evaluatePreconditions(tag);
        if(rb == null){
            rb = buildResponse(dbEntity, true, false);
            rb.tag(tag);
        }

        if(!getCacheControl.equals(CacheControlType.NONE)){
            rb.cacheControl(buildCacheControl(getCacheControlMaxAge, getCacheControl));
        }
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

        }
        return cc;
    }

    protected Response.ResponseBuilder buildResponse(T dbEntity) {
        return buildResponse(dbEntity, false, false, null);
    }

    protected Response.ResponseBuilder buildResponse(T dbEntity, Boolean isContentReturned) {
        return buildResponse(dbEntity, isContentReturned, false, null);
    }

    protected Response.ResponseBuilder buildResponse(T dbEntity, Boolean isContentReturned, boolean locationHeader) {
        return buildResponse(dbEntity, isContentReturned, locationHeader, null);
    }

    protected Response.ResponseBuilder buildResponse(T dbEntity, Boolean isContentReturned, boolean locationHeader, Response.Status status) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NO_CONTENT);

        if(isContentReturned != null && isContentReturned){
            responseBuilder.entity(dbEntity);
            responseBuilder.status(Response.Status.OK);
        }

        if(status != null){
            responseBuilder.status(status);
        }

        if(locationHeader){
            String locationValue = type.getSimpleName() + "/" + dbEntity.getId();
            responseBuilder.header("Location", locationValue);
        }

        return responseBuilder;
    }

    protected Response.ResponseBuilder buildResponse(Paging<T> paging){

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        responseBuilder.header("X-Count", paging.getCount());
        responseBuilder.entity(paging.getItems());

        return responseBuilder;
    }

}
