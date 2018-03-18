package si.fri.smrpo.kis.core.logic.exceptions.base;

import javax.ws.rs.core.Response;

public abstract class BusinessLogicBaseException extends Exception {

    private Response.Status status;
    private String message;
    private Exception innerException;

    public BusinessLogicBaseException(Response.Status status) {
        super();
        this.status = status;
    }

    public BusinessLogicBaseException(Response.Status status, String message) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public BusinessLogicBaseException(Response.Status status, String message, Exception innerException) {
        super(message, innerException);
        this.message = message;
        this.status = status;
        this.innerException = innerException;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getInnerException() {
        return innerException;
    }

    public void setInnerException(Exception innerException) {
        this.innerException = innerException;
    }
}
