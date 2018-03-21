package si.fri.smrpo.kis.core.rest.resource.base;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.DatabaseImpl;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.rest.source.CrudSourceImpl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.Serializable;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class BaseResource<T extends BaseEntity<T, K>, K extends Serializable> {

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
    protected DatabaseManager<T> databaseManager = null;

    protected abstract CrudSourceImpl<K> getDatabaseService();
    protected DatabaseManager<T> setInitManager() {
        return null;
    }


    @PostConstruct
    private void init(){
        databaseManager = setInitManager();
    }


    public BaseResource(Class<T> type) {
        this.type = type;
    }

}
