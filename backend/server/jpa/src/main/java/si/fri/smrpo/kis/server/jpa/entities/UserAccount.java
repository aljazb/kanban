package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.Constants;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="user_account")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "user-account.get-all", query = "SELECT ua FROM UserAccount ua")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class UserAccount extends UUIDEntity<UserAccount> {

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String username;

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String email;

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String firstName;

    @Column(length = Constants.DEF_STRING_LEN)
    private String lastName;


    private Boolean inRoleKanbanMaster;
    private Boolean inRoleAdministrator;
    private Boolean inRoleDeveloper;
    private Boolean inRoleProductOwner;


    @OneToMany(mappedBy = "userAccount")
    private Set<Membership> joinedDevTeams;

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

    public Set<Membership> getJoinedDevTeams() {
        return joinedDevTeams;
    }

    public void setJoinedDevTeams(Set<Membership> joinedDevTeams) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getInRoleKanbanMaster() {
        return inRoleKanbanMaster;
    }

    public void setInRoleKanbanMaster(Boolean inRoleKanbanMaster) {
        this.inRoleKanbanMaster = inRoleKanbanMaster;
    }

    public Boolean getInRoleAdministrator() {
        return inRoleAdministrator;
    }

    public void setInRoleAdministrator(Boolean inRoleAdministrator) {
        this.inRoleAdministrator = inRoleAdministrator;
    }

    public Boolean getInRoleDeveloper() {
        return inRoleDeveloper;
    }

    public void setInRoleDeveloper(Boolean inRoleDeveloper) {
        this.inRoleDeveloper = inRoleDeveloper;
    }

    public Boolean getInRoleProductOwner() {
        return inRoleProductOwner;
    }

    public void setInRoleProductOwner(Boolean inRoleProductOwner) {
        this.inRoleProductOwner = inRoleProductOwner;
    }
}
