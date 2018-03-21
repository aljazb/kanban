package si.fri.smrpo.kis.core.rest.resource.providers.exceptions;

import si.fri.smrpo.kis.core.rest.exception.ApiException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    static final Logger LOG = Logger.getLogger(ApiExceptionMapper.class.getSimpleName());

    @Context
    protected HttpServletRequest httpServletRequest;

    @Override
    public Response toResponse(ApiException e) {

        String requestContent = "";
        try {
             requestContent = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String errorMessage = String.format("\n\nRemote address: %s\nRequest url: %s\nMethod: %s\nBody:\n %s\n",
                httpServletRequest.getRemoteAddr(),
                httpServletRequest.getRequestURI(),
                httpServletRequest.getMethod(),
                requestContent);

        LOG.log(Level.SEVERE, errorMessage, e);

        return Response.status(e.getStatus()).entity(e.buildDto()).build();
    }
}
