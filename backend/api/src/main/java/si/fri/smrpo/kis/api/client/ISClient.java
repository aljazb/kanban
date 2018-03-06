package si.fri.smrpo.kis.api.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import si.fri.smrpo.kis.api.ISApiConfiguration;
import si.fri.smrpo.kis.api.exception.ISApiException;

import java.io.IOException;

public class ISClient {

    private HttpClient client;

    private ISApiConfiguration configuration;

    public ISClient(ISApiConfiguration configuration){
        this.configuration = configuration;
        this.client = HttpClientBuilder.create().build();
    }

    public HttpResponse execute(HttpUriRequest request) throws ISApiException {
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            throw new ISApiException("Could not getInfo response from server");
        }
        return response;
    }

    public HttpClient getClient() {
        return client;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public ISApiConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ISApiConfiguration configuration) {
        this.configuration = configuration;
    }

}
