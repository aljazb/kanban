package si.fri.smrpo.kis.core.logic.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=false)
public class OperationException extends LogicBaseException {

    public OperationException(String message) {
        super(message);
    }

    public OperationException(ExceptionType type) {
        super(type);
    }

    public OperationException(String message, ExceptionType type) {
        super(message, type);
    }

    public OperationException(String message, Exception innerException, ExceptionType type) {
        super(message, innerException, type);
    }

    public OperationException(String message, Exception innerException) {
        super(message, innerException);
    }
}
