package si.fri.smrpo.kis.core.rest.resource.uuid;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.source.CrudVersionSource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.UUID;

public abstract class CrudVersionResource<
            E extends BaseEntityVersion<E, UUID>,
            S extends CrudVersionSource<E, UUID, A>,
            A extends Serializable
        > extends GetResource<E, S, A> {

    public CrudVersionResource(Class<E> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, E entity) throws Exception {
        entity.setId(null);

        E dbEntity = source.create(entity, getAuthUser());

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id, E entity) throws Exception {
        entity.setId(id);

        E dbEntity = source.update(entity, getAuthUser());

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") UUID id, E entity) throws Exception {
        entity.setId(id);

        E dbEntity = source.patch(entity, getAuthUser());

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id) throws Exception {

        E dbEntity = source.delete(type, id, getAuthUser());

        return buildResponse(dbEntity, xContent).build();
    }

    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") UUID id) throws Exception {

        E dbEntity = source.toggleIsDeleted(type, id, getAuthUser());

        return buildResponse(dbEntity, xContent).build();
    }
}
