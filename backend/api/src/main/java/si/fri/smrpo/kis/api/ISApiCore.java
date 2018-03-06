package si.fri.smrpo.kis.api;

import si.fri.smrpo.kis.api.client.ISClient;
import si.fri.smrpo.kis.api.client.authorization.provider.base.ISApiAuthProvider;

public class ISApiCore {

    private ISApiConfiguration configuration;
    private ISClient client;
    private ISApiAuthProvider apiAuthProvider;

    private boolean defaultCoreContentHeader = false;

    public ISApiCore(ISApiConfiguration configuration, ISApiAuthProvider apiAuthProvider) {
        this.configuration = configuration;
        this.client = new ISClient(configuration);
        this.apiAuthProvider = apiAuthProvider;
    }

    public ISApiConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ISApiConfiguration configuration) {
        this.configuration = configuration;
    }

    public ISClient getClient() {
        return client;
    }

    public void setClient(ISClient client) {
        this.client = client;
    }

    public ISApiAuthProvider getApiAuthProvider() {
        return apiAuthProvider;
    }

    public void setApiAuthProvider(ISApiAuthProvider apiAuthProvider) {
        this.apiAuthProvider = apiAuthProvider;
    }

    public boolean getDefaultCoreContentHeader() {
        return defaultCoreContentHeader;
    }

    public void setDefaultCoreContentHeader(boolean defaultCoreContentHeader) {
        this.defaultCoreContentHeader = defaultCoreContentHeader;
    }
}
