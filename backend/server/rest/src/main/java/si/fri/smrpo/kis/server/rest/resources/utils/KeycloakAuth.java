package si.fri.smrpo.kis.server.rest.resources.utils;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public class KeycloakAuth {

    public static AuthUser buildAuthUser(KeycloakPrincipal keycloakPrincipal){
        AccessToken accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();

        UserAccount ua = new UserAccount();

        ua.setId(UUID.fromString(accessToken.getSubject()));
        ua.setEmail(accessToken.getEmail());
        ua.setFirstName(accessToken.getName());
        ua.setLastName(accessToken.getFamilyName());
        ua.setRoles(String.join(", ", accessToken.getRealmAccess().getRoles()));

        return new AuthUser(ua, accessToken.getRealmAccess().getRoles());
    }



}
