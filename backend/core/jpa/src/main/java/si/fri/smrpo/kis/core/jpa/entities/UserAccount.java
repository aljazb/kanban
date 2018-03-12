package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;
import si.fri.smrpo.kis.core.jpa.entities.mtm.DevTeamMtmProject;
import si.fri.smrpo.kis.core.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.core.jpa.utility.Constants;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Set;

@Entity
@Table(name="user_account")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class UserAccount extends BaseEntity<UserAccount> {


    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String email;

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String name;

    @Column(length = Constants.DEF_STRING_LEN)
    private String surname;



    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "userAccount")
    private Set<UserAccountMtmDevTeam> joinedDevTeams;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "owner")
    private Set<DevTeam> ownedDevTeams;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "owner")
    private Set<FlowTable> ownedFlowTables;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "owner")
    private Set<Project> ownedProject;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "owner")
    private Set<Card> ownedCard;



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

    public Set<UserAccountMtmDevTeam> getJoinedDevTems() {
        return joinedDevTeams;
    }

    public void setJoinedDevTems(Set<UserAccountMtmDevTeam> joinedDevTems) {
        this.joinedDevTeams = joinedDevTems;
    }

    public Set<DevTeam> getOwnedDevTems() {
        return ownedDevTeams;
    }

    public void setOwnedDevTems(Set<DevTeam> ownedDevTems) {
        this.ownedDevTeams = ownedDevTems;
    }

    public Set<FlowTable> getOwnedFlowTables() {
        return ownedFlowTables;
    }

    public void setOwnedFlowTables(Set<FlowTable> ownedFlowTables) {
        this.ownedFlowTables = ownedFlowTables;
    }

    public Set<Project> getOwnedProject() {
        return ownedProject;
    }

    public void setOwnedProject(Set<Project> ownedProject) {
        this.ownedProject = ownedProject;
    }

    public Set<UserAccountMtmDevTeam> getJoinedDevTeams() {
        return joinedDevTeams;
    }

    public void setJoinedDevTeams(Set<UserAccountMtmDevTeam> joinedDevTeams) {
        this.joinedDevTeams = joinedDevTeams;
    }

    public Set<DevTeam> getOwnedDevTeams() {
        return ownedDevTeams;
    }

    public void setOwnedDevTeams(Set<DevTeam> ownedDevTeams) {
        this.ownedDevTeams = ownedDevTeams;
    }

    public Set<Card> getOwnedCard() {
        return ownedCard;
    }

    public void setOwnedCard(Set<Card> ownedCard) {
        this.ownedCard = ownedCard;
    }
}
