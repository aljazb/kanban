package si.fri.smrpo.kis.core.rest.resource.uuid;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.core.rest.source.CrudVersionSource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class CrudVersionResource<
            E extends BaseEntityVersion<E, UUID>,
            S extends CrudVersionSource<E, UUID>
        > extends GetResource<E, S> {

    public CrudVersionResource(Class<E> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, E entity) throws ApiException {
        entity.setId(null);

        E dbEntity = source.create(entity);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id, E entity) throws ApiException {
        entity.setId(id);

        E dbEntity = source.update(entity);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") UUID id, E entity) throws ApiException {
        entity.setId(id);

        E dbEntity = source.patch(entity);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id) throws ApiException {

        E dbEntity = source.delete(type, id);

        return buildResponse(dbEntity, xContent).build();
    }

    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") UUID id) throws ApiException {

        E dbEntity = source.toggleIsDeleted(type, id);

        return buildResponse(dbEntity, xContent).build();
    }
}
