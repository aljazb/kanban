package si.fri.smrpo.kis.core.rest.resource.base;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BaseResource {

    @Context
    protected SecurityContext sc;

    @Context
    protected Request request;

    @Context
    protected HttpHeaders headers;

    @Context
    protected UriInfo uriInfo;

    @Context
    protected HttpServletRequest httpServletRequest;


    protected Response.ResponseBuilder buildResponseEntity(Object obj) {
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NO_CONTENT);

        if(obj != null) {
            responseBuilder.entity(obj);
            responseBuilder.status(Response.Status.OK);
        }

        return responseBuilder;
    }

    protected Response buildNotImplemented(){
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
