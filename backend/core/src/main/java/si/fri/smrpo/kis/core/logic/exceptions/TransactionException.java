package si.fri.smrpo.kis.core.logic.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class TransactionException extends LogicBaseException {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(ExceptionType type) {
        super(type);
    }

    public TransactionException(String message, ExceptionType type) {
        super(message, type);
    }

    public TransactionException(String message, Exception innerException, ExceptionType type) {
        super(message, innerException, type);
    }

    public TransactionException(String message, Exception innerException) {
        super(message, innerException);
    }

}