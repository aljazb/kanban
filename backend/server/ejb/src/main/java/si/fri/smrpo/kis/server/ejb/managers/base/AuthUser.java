package si.fri.smrpo.kis.server.ejb.managers.base;

import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.Set;

public class AuthUser {

    private UserAccount userAccount;
    private Set<String> roles;

    public AuthUser(UserAccount userAccount, Set<String> roles) {
        this.userAccount = userAccount;
        this.roles = roles;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
