package si.fri.smrpo.kis.core.restComponents.resource;

import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;
import si.fri.smrpo.kis.core.restComponents.providers.configuration.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public abstract class CrudVersionResource<T extends BaseEntityVersion> extends GetResource<T> {

    public CrudVersionResource(Class<T> type) {
        super(type);
    }

    @POST
    public Response create(@HeaderParam("X-Content") Boolean xContent, T entity) throws BusinessLogicTransactionException {
        entity.setId(null);

        T dbEntity = getDatabaseService().createVersion(entity, authorizationManager, validationManager);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") Integer id, T entity) throws BusinessLogicTransactionException {
        entity.setId(id);

        T dbEntity = getDatabaseService().updateVersion(id, entity, authorizationManager, validationManager);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @PATCH
    @Path("{id}")
    public Response patch(@HeaderParam("X-Content") Boolean xContent,
                          @PathParam("id") Integer id, T entity) throws BusinessLogicTransactionException {
        entity.setId(id);

        T dbEntity = getDatabaseService().patchVersion(id, entity, authorizationManager, validationManager);

        return buildResponse(dbEntity, xContent, true, Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@HeaderParam("X-Content") Boolean xContent,
                           @PathParam("id") Integer id) throws BusinessLogicTransactionException {

        T dbEntity = getDatabaseService().delete(type, id, authorizationManager, validationManager);

        return buildResponse(dbEntity, xContent).build();
    }

    @PUT
    @Path("{id}/toggleIsDeleted")
    public Response toggleIsDeleted(@HeaderParam("X-Content") Boolean xContent,
                                    @PathParam("id") Integer id) throws BusinessLogicTransactionException {

        T dbEntity = getDatabaseService().toggleIsDeleted(type, id, authorizationManager, validationManager);

        return buildResponse(dbEntity, xContent).build();
    }
}
