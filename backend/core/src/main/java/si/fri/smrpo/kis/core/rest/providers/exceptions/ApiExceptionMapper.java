package si.fri.smrpo.kis.core.rest.providers.exceptions;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<LogicBaseException> {

    static final Logger LOG = Logger.getLogger(ApiExceptionMapper.class.getSimpleName());

    @Context
    protected HttpServletRequest httpServletRequest;

    @Override
    public Response toResponse(LogicBaseException e) {
        log(e);

        Response.Status status = getStatus(e);
        return Response.status(status).entity(build(e)).build();
    }

    private void log(Exception e) {
        String errorMessage = String.format("\n\nRemote address: %s\nRequest url: %s\nMethod: %s\n",
                httpServletRequest.getRemoteAddr(),
                httpServletRequest.getRequestURI(),
                httpServletRequest.getMethod());

        LOG.log(Level.SEVERE, errorMessage, e);
    }

    private Response.Status getStatus(Exception e) {
        /*if(LogicBaseException.class.isAssignableFrom(e.getClass())) {

        }*/

        return Response.Status.BAD_REQUEST;
    }

    private ApiException build(Exception e) {
        return new ApiException(e);
    }

    public class ApiException implements Serializable {
        public String error;
        public String stack;

        public ApiException(Exception e) {
            this.error = e.getMessage();
            this.stack = e.toString();
        }
    }
}
