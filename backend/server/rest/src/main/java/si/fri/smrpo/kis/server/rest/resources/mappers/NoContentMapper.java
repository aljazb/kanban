package si.fri.smrpo.kis.server.rest.resources.mappers;

import si.fri.smrpo.kis.server.ejb.exceptions.NoContentException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoContentMapper implements ExceptionMapper<NoContentException> {

    @Override
    public Response toResponse(NoContentException exception) {
        return Response.noContent().build();
    }
}
