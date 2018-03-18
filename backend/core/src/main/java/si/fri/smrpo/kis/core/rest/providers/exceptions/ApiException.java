package si.fri.smrpo.kis.core.rest.providers.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import si.fri.smrpo.kis.core.logic.exceptions.base.BusinessLogicBaseException;

import java.io.Serializable;

public class ApiException implements Serializable {

    public String message;
    public String exception;

    public ApiException(String message, Exception exception) {
        this.message = message;
        this.exception = exception.toString();
    }

    @JsonIgnore
    public static ApiException build(BusinessLogicBaseException exception){

        Exception e = exception.getInnerException();
        if(e == null){
            e = exception;
        }

        return new ApiException(exception.getMessage(), e);
    }

    @JsonIgnore
    public static ApiException build(String message, Exception e){
        return new ApiException(message, e);
    }
}
