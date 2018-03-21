package si.fri.smrpo.kis.core.logic.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class TransactionException extends LogicBaseException {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Metadata metadata) {
        super(metadata);
    }

    public TransactionException(String message, Metadata metadata) {
        super(message, metadata);
    }

    public TransactionException(String message, Exception innerException, Metadata metadata) {
        super(message, innerException, metadata);
    }

    public TransactionException(String message, Exception innerException) {
        super(message, innerException);
    }
}