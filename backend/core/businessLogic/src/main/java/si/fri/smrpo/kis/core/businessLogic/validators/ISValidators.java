package si.fri.smrpo.kis.core.businessLogic.validators;

import si.fri.smrpo.kis.core.businessLogic.database.Database;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class ISValidators {

    public static <T extends BaseEntity> T isEntityValid(T value, boolean nullable, Database db) throws BusinessLogicTransactionException {
        if(value == null || value.getId() == null){
            if(!nullable){
                throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, value.getClass().getSimpleName() + " can not be null and id must be set.");
            } else {
                return null;
            }
        } else {
            try {
                T entity = db.get((Class<T>) value.getClass(), value.getId(), null);
                db.getEntityManager().detach(entity);
                return entity;
            } catch (BusinessLogicTransactionException e){
                throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, value.getClass().getSimpleName() + " does not exist.");
            }
        }
    }

    public static void isBigDecimalValid(BigDecimal value, boolean nullable, BigDecimal max, BigDecimal min, String fieldName) throws BusinessLogicTransactionException {
        if(value != null) {
            if(max != null){
                if(max.compareTo(value) < 0){
                    throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, fieldName + " can not be bigger than " + max.toString() + ".");
                }
            }
            if(min != null){
                if(min.compareTo(value) > 0){
                    throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, fieldName + " can not be smaller than " + min.toString() + ".");
                }
            }
        } else {
            if(!nullable){
                throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, fieldName + " can not be null.");
            }
        }
    }
}
