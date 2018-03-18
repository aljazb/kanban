package si.fri.smrpo.kis.core.rest.providers.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicOperationException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class BusinessLogicOperationExceptionMapper implements ExceptionMapper<BusinessLogicOperationException> {

    static final Logger LOG = Logger.getLogger(BusinessLogicOperationException.class.getSimpleName());

    @Context
    protected HttpServletRequest httpServletRequest;

    @Override
    public Response toResponse(BusinessLogicOperationException e) {
        LOG.log(Level.SEVERE, String.format("[%s][%s][%s}", "ServerOperationException", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr()), e);
        return Response.status(e.getStatus()).entity(ApiException.build(e)).build();
    }
}