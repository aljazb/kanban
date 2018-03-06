package si.fri.smrpo.kis.api.resource.expand;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import si.fri.smrpo.kis.api.ISApiCore;
import si.fri.smrpo.kis.api.data.request.base.ISApiBaseRequest;
import si.fri.smrpo.kis.api.data.response.EntityResponse;
import si.fri.smrpo.kis.api.exception.ISApiException;
import si.fri.smrpo.kis.api.resource.base.ISCrudResource;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

public class ISLoginCrudResource<T extends BaseEntity> extends ISCrudResource<T> {

    public ISLoginCrudResource(ISApiCore core, Class<T> type) {
        super(core, type);
    }

    public EntityResponse<T> login() throws ISApiException{
        return login(new ISApiBaseRequest());
    }
    public EntityResponse<T> login(ISApiBaseRequest request) throws ISApiException {
        String url = endpointUrl + "/login";
        HttpGet get = new HttpGet(url);
        setHeaders(get, request);

        HttpResponse response = core.getClient().execute(get);

        return buildVcgEntity(response);
    }
}
