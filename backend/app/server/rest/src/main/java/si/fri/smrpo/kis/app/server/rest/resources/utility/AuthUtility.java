package si.fri.smrpo.kis.app.server.rest.resources.utility;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;

public class AuthUtility {

    public static String ADMINISTRATOR = "ADMINISTRATOR";
    public static String PRODUCT_OWNER = "PRODUCT_OWNER";
    public static String KANBAN_MASTER = "KANBAN_MASTER";
    public static String DEVELOPER = "DEVELOPER";

    public static AuthEntity getAuthorizedEntity(KeycloakPrincipal principal) {
        KeycloakPrincipal<KeycloakSecurityContext> kcPrincipal = principal;
        AccessToken token = kcPrincipal.getKeycloakSecurityContext().getToken();

        AuthEntity authEntity = new AuthEntity();
        authEntity.setId(token.getSubject());
        authEntity.setName(token.getGivenName());
        authEntity.setSurname(token.getFamilyName());
        authEntity.setEmail(token.getEmail());
        authEntity.setRoles(token.getRealmAccess().getRoles());

        return authEntity;
    }

}
