package si.fri.smrpo.kis.core.rest.providers.exceptions;

import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class EJBTransactionRolledbackExceptionMapper implements ExceptionMapper<EJBTransactionRolledbackException> {

    static final Logger LOG = Logger.getLogger(EJBTransactionRolledbackExceptionMapper.class.getSimpleName());

    @Context
    protected HttpServletRequest httpServletRequest;

    @Override
    public Response toResponse(EJBTransactionRolledbackException e) {
        LOG.log(Level.SEVERE, String.format("[%s][%s][%s}", "OptimisticLockException", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr()), e);
        return Response.status(Response.Status.BAD_REQUEST).entity(ApiException.build(e.getMessage(), e)).build();
    }
}
