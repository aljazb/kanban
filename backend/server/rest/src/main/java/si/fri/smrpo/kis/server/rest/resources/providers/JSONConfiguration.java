package si.fri.smrpo.kis.server.rest.resources.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.type.Type;
import si.fri.smrpo.kis.core.rest.utility.JSONObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JSONConfiguration implements ContextResolver<ObjectMapper> {



    public static final ObjectMapper jsonMapper = JSONObjectMapper.buildDefault()
                   .registerModule(buildHibernateModule());

    private static Hibernate5Module buildHibernateModule(){

        Mapping identifierMapping = new Mapping() {
            @Override
            public IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
                return null;
            }

            @Override
            public Type getIdentifierType(String className) throws MappingException {
                return null;
            }

            @Override
            public String getIdentifierPropertyName(String className) throws MappingException {
                return "userId";
            }

            @Override
            public Type getReferencedPropertyType(String className, String propertyName) throws MappingException {
                return null;
            }
        };

        Hibernate5Module hm = new Hibernate5Module(identifierMapping);

        hm.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);
        hm.configure(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);

        return hm;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return jsonMapper;
    }

}