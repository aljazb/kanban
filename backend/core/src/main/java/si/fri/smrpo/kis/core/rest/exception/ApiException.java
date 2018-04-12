package si.fri.smrpo.kis.core.rest.exception;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ws.rs.core.Response;

public class ApiException extends Exception {

    private Response.Status status;

    public ApiException(Response.Status status) {
        this.status = status;
    }

    public ApiException(Response.Status status, String message) {
        super(message);
        this.status = status;
    }

    public ApiException(Response.Status status, Exception exception) {
        super(exception);
        this.status = status;
    }

    public ApiException(Response.Status status, String message, Exception exception) {
        super(message, exception);
        this.status = status;
    }



    public static ApiException transform(LogicBaseException e) {
        return new ApiException(getStatus(e), e.getMessage(), e);
    }

    private static Response.Status getStatus(LogicBaseException e){
        switch (e.getType()){
            case ENTITY_DOES_NOT_EXISTS:
                return Response.Status.NOT_FOUND;
            case INSUFFICIENT_RIGHTS:
                return Response.Status.FORBIDDEN;
            default:
                return Response.Status.BAD_REQUEST;
        }
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }



    public static ApiException buildNotImplemented(){
        return new ApiException(Response.Status.NOT_IMPLEMENTED);
    }



    public class ApiExceptionDTO {
        public Response.Status status;
        public String error;
        public String stack;

        public ApiExceptionDTO(ApiException e) {
            this.status = e.getStatus();
            this.error = e.getMessage();
            this.stack = e.toString();
        }
    }


    public ApiExceptionDTO buildDto(){
        return new ApiExceptionDTO(this);
    }
}
