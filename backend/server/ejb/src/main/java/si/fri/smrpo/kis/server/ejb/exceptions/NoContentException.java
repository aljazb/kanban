package si.fri.smrpo.kis.server.ejb.exceptions;

public class NoContentException extends Exception {

    public NoContentException(String msg) {
        super(msg);
    }

    public NoContentException(String msg, Exception e) {
        super(msg, e);
    }
}
