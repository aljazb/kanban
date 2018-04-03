package si.fri.smrpo.kis.server.ejb.service;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.service.interfaces.KeycloakAdminServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.*;

@PermitAll
@Stateless
@Local(KeycloakAdminServiceLocal.class)
public class KeycloakAdminService implements KeycloakAdminServiceLocal {

    private final static String KC_URL = System.getenv("KC_URL");
    private final static String KC_REALM = System.getenv("KC_REALM");
    private final static String KC_KIS_REALM = System.getenv("KC_KIS_REALM");

    private final static String KC_USER = System.getenv("KC_USER");
    private final static String KC_PASSWORD = System.getenv("KC_PASSWORD");
    private final static String KC_CLIENT = System.getenv("KC_CLIENT");


    private static Keycloak KEYCLOAK;
    private static RealmResource REALM_RESOURCE;
    private static HashMap<String, RoleRepresentation> REALM_ROLES;

    @PostConstruct
    private void init(){
        if(KEYCLOAK == null) {

            KEYCLOAK = KeycloakBuilder.builder()
                    .serverUrl(KC_URL).realm(KC_REALM).username(KC_USER).password(KC_PASSWORD).clientId(KC_CLIENT)
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();

            REALM_RESOURCE = KEYCLOAK.realm(KC_KIS_REALM);

            REALM_ROLES = new HashMap<>();
            for(RoleRepresentation rr : REALM_RESOURCE.roles().list()) {
                REALM_ROLES.put(rr.getName(), rr);
            }
        }
    }

    private UserRepresentation getUserRepresentation(String id) throws TransactionException {
        try {
            return REALM_RESOURCE.users().get(id).toRepresentation();
        } catch (Exception e){
            throw new TransactionException( "Could not retrieve KEYCLOAK user.", LogicBaseException.Metadata.ENTITY_DOES_NOT_EXISTS);
        }
    }

    private UserRepresentation buildUserRepresentation(UserAccount userAccount) throws TransactionException {
        return buildUserRepresentation(userAccount, new UserRepresentation());
    }

    private UserRepresentation buildUserRepresentation(UserAccount userAccount, String id) throws TransactionException {
        UserRepresentation user = getUserRepresentation(id);
        return buildUserRepresentation(userAccount, user);
    }

    private UserRepresentation buildUserRepresentation(UserAccount userAccount, UserRepresentation ur) throws TransactionException {
        ur.setUsername(userAccount.getUsername());
        ur.setEmail(userAccount.getEmail());
        ur.setFirstName(userAccount.getFirstName());
        ur.setLastName(userAccount.getLastName());
        ur.setEnabled(true);
        ur.setEmailVerified(true);
        return ur;
    }

    private Set<String> getUserRoles(UserAccount userAccount){
        HashSet<String> roles = new HashSet<>();
        roles.add(ROLE_USER);

        if(userAccount.getInRoleAdministrator()) roles.add(ROLE_ADMINISTRATOR);
        if(userAccount.getInRoleKanbanMaster()) roles.add(ROLE_KANBAN_MASTER);
        if(userAccount.getInRoleProductOwner()) roles.add(ROLE_PRODUCT_OWNER);
        if(userAccount.getInRoleDeveloper()) roles.add(ROLE_DEVELOPER);

        return roles;
    }

    private void updateKeycloakUserRoles(String id, Set<String> newUserRoles){
        Set<String> userRoles = REALM_RESOURCE.users().get(id).roles().realmLevel().listAll()
                .stream().map(RoleRepresentation::getName).collect(Collectors.toSet());

        Set<String> rolesToAdd = new HashSet<>(newUserRoles);
        rolesToAdd.removeAll(userRoles);
        List<RoleRepresentation> toAdd = rolesToAdd.stream().map(e -> REALM_ROLES.get(e)).collect(Collectors.toList());
        if(!toAdd.isEmpty()) {
            REALM_RESOURCE.users().get(id).roles().realmLevel().add(toAdd);
        }


        Set<String> rolesToRemove = new HashSet<>(userRoles);
        rolesToRemove.removeAll(newUserRoles);
        List<RoleRepresentation> toRemove = rolesToRemove.stream().map(e -> REALM_ROLES.get(e)).collect(Collectors.toList());
        if(!toRemove.isEmpty()) {
            REALM_RESOURCE.users().get(id).roles().realmLevel().remove(toRemove);
        }
    }

    private void updateKeycloakUserDetails(UserAccount userAccount) throws TransactionException {
        String id = userAccount.getId().toString();
        UserRepresentation ur = buildUserRepresentation(userAccount, id);
        REALM_RESOURCE.users().get(id).update(ur);
    }

    private String createKeycloakUser(UserAccount userAccount) throws TransactionException {
        UserRepresentation user = buildUserRepresentation(userAccount);
        Response result = REALM_RESOURCE.users().create(user);

        if(result.getStatus() == 201) {
            String locationHeader = result.getHeaderString("Location");
            return locationHeader.replaceAll(".*/(.*)$", "$1");
        } else {
            throw new TransactionException("Could not create account on Keycloak server.");
        }
    }



    public void setPassword(String id, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        REALM_RESOURCE.users().get(id).resetPassword(credential);
    }

    public void update(UserAccount userAccount) throws TransactionException {
        updateKeycloakUserDetails(userAccount);

        String id = userAccount.getId().toString();
        updateKeycloakUserRoles(id, getUserRoles(userAccount));
    }

    public String create(UserAccount userAccount) throws TransactionException {
        String id = createKeycloakUser(userAccount);
        updateKeycloakUserRoles(id, getUserRoles(userAccount));
        return id;
    }

    public void setEnabled(String id, boolean enabled) throws TransactionException {
        UserRepresentation ur = getUserRepresentation(id);
        ur.setEnabled(enabled);
        REALM_RESOURCE.users().get(id).update(ur);
    }

}
