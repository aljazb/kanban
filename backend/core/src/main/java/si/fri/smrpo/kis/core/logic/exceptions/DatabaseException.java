package si.fri.smrpo.kis.core.logic.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class DatabaseException extends LogicBaseException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Metadata metadata) {
        super(metadata);
    }

    public DatabaseException(String message, Metadata metadata) {
        super(message, metadata);
    }

    public DatabaseException(String message, Exception innerException) {
        super(message, innerException);
    }

    public DatabaseException(String message, Exception innerException, Metadata metadata) {
        super(message, innerException, metadata);
    }

}
