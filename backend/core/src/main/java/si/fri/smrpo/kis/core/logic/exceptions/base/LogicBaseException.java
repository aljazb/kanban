package si.fri.smrpo.kis.core.logic.exceptions.base;


public abstract class LogicBaseException extends Exception {

    protected Metadata metadata = Metadata.GENERAL;

    public LogicBaseException(String message) {
        super(message);
    }

    public LogicBaseException(Metadata metadata) {
        super();
        this.metadata = metadata;
    }

    public LogicBaseException(String message, Metadata metadata) {
        super(message);
        this.metadata = metadata;
    }

    public LogicBaseException(String message, Exception innerException) {
        super(message, innerException);
    }

    public LogicBaseException(String message, Exception innerException, Metadata metadata) {
        super(message, innerException);
        this.metadata = metadata;
    }


    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public enum Metadata {
        GENERAL,
        ENTITY_DOES_NOT_EXISTS,
        INSUFFICIENT_RIGHTS
    }

}
