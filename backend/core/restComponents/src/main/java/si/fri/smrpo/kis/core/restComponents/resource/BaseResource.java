package si.fri.smrpo.kis.core.restComponents.resource;

import si.fri.smrpo.kis.core.businessLogic.database.instance.DatabaseImpl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.Serializable;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class BaseResource<K extends Serializable> {

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

    protected abstract DatabaseImpl<K> getDatabaseService();

}
