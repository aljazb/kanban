package si.fri.smrpo.kis.api.data.response.base;

import si.fri.smrpo.kis.api.data.RequestException;

import javax.ws.rs.core.Response;

public abstract class ISApiBaseResponse {

    protected Response.Status status;
    protected RequestException isApiException;


    public ISApiBaseResponse(Response.Status status) {
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public boolean isStatusValid(){
        switch (status){
            case OK:
            case NO_CONTENT:
            case CREATED:
                return true;
            default:
                return false;
        }
    }

    public RequestException getIsApiException() {
        return isApiException;
    }

    public void setIsApiException(RequestException isApiException) {
        this.isApiException = isApiException;
    }
}
