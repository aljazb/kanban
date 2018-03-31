package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.Constants;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Set;

@Entity
@Table(name="user_account")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "user-account.get-all", query = "SELECT ua FROM UserAccount ua")
})
public class UserAccount extends UUIDEntity<UserAccount> {


    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String email;

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String firstName;

    @Column(length = Constants.DEF_STRING_LEN)
    private String lastName;

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String roles;

    @OneToMany(mappedBy = "userAccount")
    private Set<UserAccountMtmDevTeam> joinedDevTeams;

    @OneToMany(mappedBy = "owner")
    private Set<Project> projects;

    @OneToMany(mappedBy = "sender")
    private Set<Request> sentRequests;

    @OneToMany(mappedBy = "receiver")
    private Set<Request> receivedRequests;

    @OneToMany(mappedBy = "movedBy")
    private Set<CardMove> cardMoves;

    @OneToMany(mappedBy = "assignedTo")
    private Set<SubTask> subTasks;


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

    public Set<Request> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(Set<Request> sendRequests) {
        this.sentRequests = sendRequests;
    }

    public Set<Request> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(Set<Request> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Set<CardMove> getCardMoves() {
        return cardMoves;
    }

    public void setCardMoves(Set<CardMove> cardMoves) {
        this.cardMoves = cardMoves;
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
