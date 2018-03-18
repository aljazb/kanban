package si.fri.smrpo.kis.server.rest.utility;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public class AuthUtility {

    public static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";
    public static final String ROLE_PRODUCT_OWNER = "PRODUCT_OWNER";
    public static final String ROLE_KANBAN_MASTER = "KANBAN_MASTER";
    public static final String ROLE_DEVELOPER = "DEVELOPER";
    public static final String ROLE_USER = "USER";


    public static UserAccount getAuthorizedEntity(KeycloakPrincipal principal) {
        KeycloakPrincipal<KeycloakSecurityContext> kcPrincipal = principal;
        AccessToken token = kcPrincipal.getKeycloakSecurityContext().getToken();

        UserAccount authEntity = new UserAccount();
        authEntity.setId(UUID.fromString(token.getSubject()));
        authEntity.setFirstName(token.getGivenName());
        authEntity.setLastName(token.getFamilyName());
        authEntity.setEmail(token.getEmail());

        return authEntity;
    }

}
