package si.fri.smrpo.kis.core.rest.resource.base;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.rest.source.BaseSource;
import si.fri.smrpo.kis.core.rest.source.interfaces.BaseSourceImpl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.Serializable;


public abstract class BaseEntityResource<
        E extends BaseEntity<E, I>,
        S extends BaseSourceImpl<I>,
        I extends Serializable,
        A extends Serializable
    > extends BaseResource {

    protected Class<E> type;
    protected S source;
    abstract protected void initSource();

    abstract protected A getAuthUser();

    @PostConstruct
    private void init(){
        initSource();
    }

    public BaseEntityResource(Class<E> type) {
        this.type = type;
    }

    protected <B extends BaseEntity> Response.ResponseBuilder buildResponse() {
        return buildResponse(null, false, false, null);
    }

    protected <B extends BaseEntity> Response.ResponseBuilder buildResponse(B dbEntity) {
        return buildResponse(dbEntity, false, false, null);
    }

    protected <B extends BaseEntity> Response.ResponseBuilder buildResponse(B dbEntity, Boolean isContentReturned) {
        return buildResponse(dbEntity, isContentReturned, false, null);
    }

    protected <B extends BaseEntity> Response.ResponseBuilder buildResponse(B dbEntity, Boolean isContentReturned, boolean locationHeader) {
        return buildResponse(dbEntity, isContentReturned, locationHeader, null);
    }

    protected <B extends BaseEntity> Response.ResponseBuilder buildResponse(B dbEntity, Boolean isContentReturned, boolean locationHeader, Response.Status status) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NO_CONTENT);

        if(dbEntity != null) {
            if(isContentReturned != null && isContentReturned){
                responseBuilder.entity(dbEntity);
                responseBuilder.status(Response.Status.OK);
            }

            if(status != null){
                responseBuilder.status(status);
            }
        }

        if(locationHeader){
            String locationValue = type.getSimpleName() + "/" + dbEntity.getId();
            responseBuilder.header("Location", locationValue);
        }

        return responseBuilder;
    }

    protected <B extends BaseEntity> Response.ResponseBuilder buildResponse(Paging<B> paging){

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        responseBuilder.header("X-Count", paging.getCount());
        responseBuilder.entity(paging.getItems());

        return responseBuilder;
    }

}