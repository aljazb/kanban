package si.fri.smrpo.kis.api.data.request;

import si.fri.smrpo.kis.api.data.request.base.ISApiBaseRequest;

public class IdRequest extends ISApiBaseRequest {

    protected Integer id;

    public IdRequest(Integer id) {
        this.id = id;
    }

    public IdRequest(Integer id, String eTagHeader) {
        this.id = id;
        this.eTagHeader = eTagHeader;
    }

    public IdRequest(Integer id, boolean xContentHeader) {
        this.id = id;
        this.xContentHeader = xContentHeader;
    }

    public IdRequest(Integer id, String eTagHeader, boolean xContentHeader) {
        this.id = id;
        this.eTagHeader = eTagHeader;
        this.xContentHeader = xContentHeader;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
