package si.fri.smrpo.kis.core.rest.resource.uuid;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.core.rest.source.GetSource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;


public abstract class CrudResource<
            T extends BaseEntity<T, UUID>,
            S extends CrudSource<T, UUID>
        > extends GetResource<T, S> {


    public CrudResource(Class<T> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, T entity) throws ApiException {
        entity.setId(null);

        T dbEntity = source.create(entity);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id, T newObject) throws ApiException {
        newObject.setId(id);

        T dbEntity = source.update(newObject);

        return buildResponse(dbEntity, xContent).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") UUID id, T entity) throws ApiException {
        entity.setId(id);

        T dbEntity = source.patch(entity);

        return buildResponse(dbEntity, xContent).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id) throws ApiException {

        T dbEntity = source.delete(type, id);

        return buildResponse(dbEntity, xContent).build();
    }

    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") UUID id) throws ApiException {

        T dbEntity = source.toggleIsDeleted(type, id);

        return buildResponse(dbEntity, xContent).build();
    }

}
