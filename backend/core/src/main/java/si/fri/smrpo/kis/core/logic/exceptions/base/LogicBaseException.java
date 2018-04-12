package si.fri.smrpo.kis.core.logic.exceptions.base;


public abstract class LogicBaseException extends Exception {

    protected ExceptionType type = ExceptionType.GENERAL;

    public LogicBaseException(String message) {
        super(message);
    }

    public LogicBaseException(ExceptionType type) {
        super();
        this.type = type;
    }

    public LogicBaseException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public LogicBaseException(String message, Exception innerException) {
        super(message, innerException);
    }

    public LogicBaseException(String message, Exception innerException, ExceptionType type) {
        super(message, innerException);
        this.type = type;
    }


    public ExceptionType getType() {
        return type;
    }

    public void setType(ExceptionType type) {
        this.type = type;
    }



}
