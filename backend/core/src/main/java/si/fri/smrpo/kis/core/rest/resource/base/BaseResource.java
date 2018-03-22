package si.fri.smrpo.kis.core.rest.resource.base;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.rest.source.base.BaseSource;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.Serializable;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class BaseResource<
        T extends BaseEntity<T, K>,
        S extends BaseSource<T, K>,
        K extends Serializable> {

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


    protected Class<T> type;
    protected S source;
    abstract protected void initSource();

    @PostConstruct
    private void init(){
        initSource();
    }


    public BaseResource(Class<T> type) {
        this.type = type;
    }

}
