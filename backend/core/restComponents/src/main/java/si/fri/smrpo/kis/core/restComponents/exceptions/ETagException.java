package si.fri.smrpo.kis.core.restComponents.exceptions;

import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;

import javax.ws.rs.core.Response;

public class ETagException extends BusinessLogicTransactionException {

    private Response.ResponseBuilder responseBuilder;

    public ETagException(Response.Status status, String message, Response.ResponseBuilder responseBuilder) {
        super(status, message);
        this.responseBuilder = responseBuilder;
    }

    public Response.ResponseBuilder getResponseBuilder() {
        return responseBuilder;
    }
}
