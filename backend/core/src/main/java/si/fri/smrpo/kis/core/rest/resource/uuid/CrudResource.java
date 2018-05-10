package si.fri.smrpo.kis.core.rest.resource.uuid;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.UUID;


public abstract class CrudResource<
            E extends BaseEntity<E, UUID>,
            S extends CrudSourceImpl<E, UUID, A>,
            A extends Serializable
        > extends GetResource<E, S, A> {


    public CrudResource(Class<E> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, E entity) throws Exception {
        E dbEntity = source.create(entity, getAuthUser());
        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent, @PathParam("id") UUID id, E newObject) throws Exception {
        newObject.setId(id);

        E dbEntity = source.update(newObject, getAuthUser());

        return buildResponse(dbEntity, xContent).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") UUID id, E entity) throws Exception {
        entity.setId(id);

        E dbEntity = source.patch(entity, getAuthUser());

        return buildResponse(dbEntity, xContent).build();
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
