package si.fri.smrpo.kis.core.businessLogic.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback=true)
public class BusinessLogicTransactionException extends BusinessLogicBaseException {

    public BusinessLogicTransactionException(Response.Status status, String message) {
        super(status, message);
    }

    public BusinessLogicTransactionException(Response.Status status, String message, Exception exception) {
        super(status, message, exception);
    }

    public static BusinessLogicTransactionException buildNotImplemented(){
        return new BusinessLogicTransactionException(Response.Status.NOT_IMPLEMENTED, "Not implemented.");
    }
}
