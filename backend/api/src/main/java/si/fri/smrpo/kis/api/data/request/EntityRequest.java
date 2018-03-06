package si.fri.smrpo.kis.api.data.request;

import si.fri.smrpo.kis.api.data.request.base.ISApiBaseRequest;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

public class EntityRequest<T extends BaseEntity> extends ISApiBaseRequest {

    private T entity;

    public EntityRequest(T entity) {
        this.entity = entity;
    }

    public EntityRequest(T entity, String eTagHeader) {
        this.entity = entity;
        this.eTagHeader = eTagHeader;
    }

    public EntityRequest(T entity, boolean xContentHeader) {
        this.entity = entity;
        this.xContentHeader = xContentHeader;
    }

    public EntityRequest(T entity, String eTagHeader, boolean xContentHeader) {
        this.entity = entity;
        this.eTagHeader = eTagHeader;
        this.xContentHeader = xContentHeader;
    }


    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
