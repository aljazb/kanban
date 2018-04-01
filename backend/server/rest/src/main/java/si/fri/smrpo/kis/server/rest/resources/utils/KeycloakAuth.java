package si.fri.smrpo.kis.server.rest.resources.utils;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.Set;
import java.util.UUID;

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.*;

public class KeycloakAuth {

    public static AuthUser buildAuthUser(KeycloakPrincipal keycloakPrincipal){
        AccessToken accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();

        UserAccount ua = new UserAccount();

        ua.setId(UUID.fromString(accessToken.getSubject()));
        ua.setEmail(accessToken.getEmail());
        ua.setFirstName(accessToken.getName());
        ua.setLastName(accessToken.getFamilyName());

        Set<String> roles = accessToken.getRealmAccess().getRoles();

        ua.setInRoleProductOwner(roles.contains(ROLE_PRODUCT_OWNER));
        ua.setInRoleKanbanMaster(roles.contains(ROLE_KANBAN_MASTER));
        ua.setInRoleAdministrator(roles.contains(ROLE_ADMINISTRATOR));
        ua.setInRoleDeveloper(roles.contains(ROLE_DEVELOPER));

        return new AuthUser(ua, accessToken.getRealmAccess().getRoles());
    }



}
