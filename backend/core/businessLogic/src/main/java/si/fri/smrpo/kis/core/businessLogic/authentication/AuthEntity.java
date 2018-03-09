package si.fri.smrpo.kis.core.businessLogic.authentication;

import java.util.HashSet;
import java.util.Set;

public class AuthEntity {

    protected String id;
    protected String email;
    protected String name;
    protected String surname;

    protected Set<String> roles;

    public AuthEntity() {
        roles = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isInRole(String role){
        return roles.contains(role);
    }

}