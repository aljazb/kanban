package si.fri.smrpo.kis.core.rest.resource.uuid;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.rest.providers.configuration.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class CrudVersionResource<T extends BaseEntityVersion<T, UUID>> extends GetResource<T> {

    public CrudVersionResource(Class<T> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, T entity) throws BusinessLogicTransactionException {
        entity.setId(null);

        T dbEntity = getDatabaseService().createVersion(entity, databaseManager);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id, T entity) throws BusinessLogicTransactionException {
        entity.setId(id);

        T dbEntity = getDatabaseService().updateVersion(entity, databaseManager);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") UUID id, T entity) throws BusinessLogicTransactionException {
        entity.setId(id);

        T dbEntity = getDatabaseService().patchVersion(entity, databaseManager);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id) throws BusinessLogicTransactionException {

        T dbEntity = getDatabaseService().delete(type, id, databaseManager);

        return buildResponse(dbEntity, xContent).build();
    }

    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") UUID id) throws BusinessLogicTransactionException {

        T dbEntity = getDatabaseService().toggleIsDeleted(type, id, databaseManager);

        return buildResponse(dbEntity, xContent).build();
    }
}
