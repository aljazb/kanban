package si.fri.smrpo.kis.app.server.rest.resources.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import si.fri.smrpo.kis.core.restComponents.utility.JSONObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JSONConfiguration implements ContextResolver<ObjectMapper> {

    public static final ObjectMapper jsonMapper =
           JSONObjectMapper.buildDefault().registerModule(new Hibernate5Module());

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return jsonMapper;
    }
}