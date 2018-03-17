package si.fri.smrpo.kis.core.restComponents.resource;

import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.restComponents.providers.configuration.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;


public abstract class CrudResource<T extends BaseEntity<T, UUID>> extends GetResource<T> {


    public CrudResource(Class<T> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, T entity) throws BusinessLogicTransactionException {
        entity.setId(null);

        T dbEntity = getDatabaseService().create(entity, databaseManager);

        return buildResponse(dbEntity, xContent, false, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") UUID id, T newObject) throws BusinessLogicTransactionException {
        newObject.setId(id);

        T dbEntity = getDatabaseService().update(newObject, databaseManager);

        return buildResponse(dbEntity, xContent).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") UUID id, T entity) throws BusinessLogicTransactionException {
        entity.setId(id);

        T dbEntity = getDatabaseService().patch(entity, databaseManager);

        return buildResponse(dbEntity, xContent).build();
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
