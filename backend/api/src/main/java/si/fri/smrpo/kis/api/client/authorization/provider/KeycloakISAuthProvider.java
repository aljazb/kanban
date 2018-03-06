package si.fri.smrpo.kis.api.client.authorization.provider;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import si.fri.smrpo.kis.api.client.authorization.provider.base.ISApiAuthProvider;
import si.fri.smrpo.kis.api.client.authorization.provider.base.KeycloakISConfiguration;

public class KeycloakISAuthProvider extends ISApiAuthProvider {

    private KeycloakISConfiguration configuration;
    private Keycloak client;

    private AccessTokenResponse accessTokenResponse;

    public KeycloakISAuthProvider(KeycloakISConfiguration configuration) {
        this.configuration = configuration;

        initClient();
    }

    private void initClient(){
        ResteasyProviderFactory resteasyProviderFactory = new ResteasyProviderFactory();
        resteasyProviderFactory.register(ResteasyJackson2Provider.class);

        client = KeycloakBuilder.builder()
                .realm(configuration.realm)
                .serverUrl(configuration.hostUrl)
                .clientId(configuration.client)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .username(configuration.username)
                .password(configuration.password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }



    @Override
    public String getAuthorizationToken() {
        return client.tokenManager().getAccessTokenString();
    }
}
