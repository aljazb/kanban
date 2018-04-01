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
    private static RoleRepresentation RP_ADMINISTRATOR;
    private static RoleRepresentation RP_PRODUCT_OWNER;
    private static RoleRepresentation RP_KANBAN_MASTER;
    private static RoleRepresentation RP_DEVELOPER;

    @PostConstruct
    private void init(){
        if(KEYCLOAK == null){
            KEYCLOAK = KeycloakBuilder.builder()
                    .serverUrl(KC_URL).realm(KC_REALM).username(KC_USER).password(KC_PASSWORD).clientId(KC_CLIENT)
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();

            REALM_RESOURCE = KEYCLOAK.realm(KC_KIS_REALM);

            List<RoleRepresentation> rr = KEYCLOAK.realm(KC_KIS_REALM).roles().list();

            RP_ADMINISTRATOR = rr.stream().filter(e -> e.getName().equals(ROLE_ADMINISTRATOR)).findFirst().orElse(null);
            RP_PRODUCT_OWNER = rr.stream().filter(e -> e.getName().equals(ROLE_PRODUCT_OWNER)).findFirst().orElse(null);
            RP_KANBAN_MASTER = rr.stream().filter(e -> e.getName().equals(ROLE_KANBAN_MASTER)).findFirst().orElse(null);
            RP_DEVELOPER = rr.stream().filter(e -> e.getName().equals(ROLE_DEVELOPER)).findFirst().orElse(null);
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
        return ur;
    }

    public void setEnabled(String id, boolean enabled) throws TransactionException {
        UserRepresentation ur = getUserRepresentation(id);
        ur.setEnabled(enabled);

        REALM_RESOURCE.users().get(id).update(ur);
    }

    private void updateDetails(UserAccount userAccount) throws TransactionException {
        String id = userAccount.getId().toString();
        UserRepresentation ur = buildUserRepresentation(userAccount, id);
        REALM_RESOURCE.users().get(id).update(ur);
    }

    private void updateRoles(UserAccount userAccount){
        String id = userAccount.getId().toString();

        List<RoleRepresentation> toAdd = new ArrayList<>();
        List<RoleRepresentation> toRemove = new ArrayList<>();

        HashMap<String, RoleRepresentation> userRealmRoles = new HashMap<>();
        for(RoleRepresentation role : KEYCLOAK.realm(KC_KIS_REALM).users().get(id).roles().realmLevel().listAll()){
            userRealmRoles.put(role.getName(), role);
        }

        boolean isDeveloper = userAccount.getInRoleDeveloper();
        boolean hasDeveloper = userRealmRoles.containsKey(ROLE_DEVELOPER);
        if(isDeveloper && !hasDeveloper) {
                toAdd.add(RP_DEVELOPER);
        } else if(!isDeveloper && hasDeveloper) {
            toRemove.add(RP_DEVELOPER);
        }

        boolean isKanbanMaster = userAccount.getInRoleKanbanMaster();
        boolean hasKanbanMaster = userRealmRoles.containsKey(ROLE_KANBAN_MASTER);
        if(isKanbanMaster && !hasKanbanMaster) {
            toAdd.add(RP_KANBAN_MASTER);
        } else if(!isKanbanMaster && hasKanbanMaster) {
            toRemove.add(RP_KANBAN_MASTER);
        }

        boolean isProductOwner = userAccount.getInRoleProductOwner();
        boolean hasProductOwner = userRealmRoles.containsKey(ROLE_PRODUCT_OWNER);
        if(isProductOwner && !hasProductOwner) {
            toAdd.add(RP_PRODUCT_OWNER);
        } else if(!isProductOwner && hasProductOwner) {
            toRemove.add(RP_PRODUCT_OWNER);
        }

        boolean isAdministrator = userAccount.getInRoleAdministrator();
        boolean hasAdministrator = userRealmRoles.containsKey(ROLE_ADMINISTRATOR);
        if(isAdministrator && !hasAdministrator) {
            toAdd.add(RP_ADMINISTRATOR);
        } else if(!isAdministrator && hasAdministrator) {
            toRemove.add(RP_ADMINISTRATOR);
        }

        if(!toAdd.isEmpty()){
            REALM_RESOURCE.users().get(id).roles().realmLevel().add(toAdd);
        }
        if(!toRemove.isEmpty()){
            REALM_RESOURCE.users().get(id).roles().realmLevel().remove(toRemove);
        }
    }

    private void updatePassword(UserAccount userAccount){
        if(userAccount.getPassword() != null) {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userAccount.getPassword());
            credential.setTemporary(false);

            String id = userAccount.getId().toString();
            REALM_RESOURCE.users().get(id).resetPassword(credential);
        }
    }

    public void update(UserAccount userAccount) throws TransactionException {
        updateDetails(userAccount);
        updateRoles(userAccount);
        updatePassword(userAccount);
    }

    public String create(UserAccount userAccount) throws TransactionException {
        UserRepresentation user = buildUserRepresentation(userAccount);

        Response result = REALM_RESOURCE.users().create(user);

        if(result.getStatus() != 201) {
            throw new TransactionException("Could not create account on KEYCLOAK server.");
        } else {
            String locationHeader = result.getHeaderString("Location");
            String userId = locationHeader.replaceAll(".*/(.*)$", "$1");

            return userId;
        }
    }
}
