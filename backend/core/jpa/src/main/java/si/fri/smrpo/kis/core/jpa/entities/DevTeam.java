package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.mtm.DevTeamMtmProject;
import si.fri.smrpo.kis.core.jpa.entities.mtm.UserAccountMtmDevTeam;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="dev_team")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class DevTeam extends BaseEntity<DevTeam> {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_user_account_id", nullable = false)
    public UserAccount owner;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "userAccount")
    public Set<UserAccountMtmDevTeam> joinedUsers;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "project")
    public Set<DevTeamMtmProject> joinedProjects;


    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public Set<UserAccountMtmDevTeam> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Set<UserAccountMtmDevTeam> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public Set<DevTeamMtmProject> getJoinedProjects() {
        return joinedProjects;
    }

    public void setJoinedProjects(Set<DevTeamMtmProject> joinedProjects) {
        this.joinedProjects = joinedProjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
