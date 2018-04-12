package si.fri.smrpo.kis.core.logic.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class DatabaseException extends LogicBaseException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(ExceptionType type) {
        super(type);
    }

    public DatabaseException(String message, ExceptionType type) {
        super(message, type);
    }

    public DatabaseException(String message, Exception innerException) {
        super(message, innerException);
    }

    public DatabaseException(String message, Exception innerException, ExceptionType type) {
        super(message, innerException, type);
    }

}
