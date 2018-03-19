package si.fri.smrpo.kis.core.logic.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=false)
public class OperationException extends LogicBaseException {

    public OperationException(String message) {
        super(message);
    }

    public OperationException(Metadata metadata) {
        super(metadata);
    }

    public OperationException(String message, Metadata metadata) {
        super(message, metadata);
    }

    public OperationException(String message, Exception innerException, Metadata metadata) {
        super(message, innerException, metadata);
    }

    public OperationException(String message, Exception innerException) {
        super(message, innerException);
    }
}
