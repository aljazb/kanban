package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.Constants;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Set;

@Entity
@Table(name="user_account")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class UserAccount extends UUIDEntity<UserAccount> {


    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String email;

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String firstName;

    @Column(length = Constants.DEF_STRING_LEN)
    private String lastName;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "userAccount")
    private Set<UserAccountMtmDevTeam> joinedDevTeams;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "owner")
    private Set<Project> projects;

    @JsonIgnore
    protected boolean baseSkip(Field field){
        boolean skip = super.baseSkip(field);
        if(skip){
            return skip;
        } else {
            switch (field.getName()) {
                case "email":
                    return true;
                default:
                    return false;
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String surname) {
        this.lastName = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserAccountMtmDevTeam> getJoinedDevTems() {
        return joinedDevTeams;
    }

    public void setJoinedDevTems(Set<UserAccountMtmDevTeam> joinedDevTems) {
        this.joinedDevTeams = joinedDevTems;
    }

    public Set<UserAccountMtmDevTeam> getJoinedDevTeams() {
        return joinedDevTeams;
    }

    public void setJoinedDevTeams(Set<UserAccountMtmDevTeam> joinedDevTeams) {
        this.joinedDevTeams = joinedDevTeams;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}