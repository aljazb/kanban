package si.fri.smrpo.kis.server.ejb.managers.base;

import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import java.util.Set;
import java.util.UUID;


public class AuthManager<T extends UUIDEntity<T>> extends DatabaseManager<T, UUID> {

    public static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";
    public static final String ROLE_PRODUCT_OWNER = "PRODUCT_OWNER";
    public static final String ROLE_KANBAN_MASTER = "KANBAN_MASTER";
    public static final String ROLE_DEVELOPER = "DEVELOPER";
    public static final String ROLE_USER = "USER";


    protected AuthUser userAccount;

    public AuthManager(AuthUser userAccount) {
        this.userAccount = userAccount;

    }

    public boolean isUserInRole(String role) {
        return userAccount.getRoles().contains(role);
    }

    public UUID getUserId (){
        return userAccount.getUserAccount().getId();
    }

    public UserAccount getUserAccount() {
        return userAccount.getUserAccount();
    }

}