package si.fri.smrpo.kis.core.businessLogic.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback=false)
public class BusinessLogicOperationException extends BusinessLogicBaseException {

    public BusinessLogicOperationException(Response.Status status, String message) {
        super(status, message);
    }

    public BusinessLogicOperationException(Response.Status status, String message, Exception exception) {
        super(status, message, exception);
    }
}
