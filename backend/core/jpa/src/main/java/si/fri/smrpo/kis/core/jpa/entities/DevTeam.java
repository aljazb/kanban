package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
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


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "userAccount")
    private Set<UserAccountMtmDevTeam> joinedUsers;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "devTeam")
    private Set<Project> projects;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToOne(mappedBy = "devTeam")
    private FlowTable flowTable;


    public Set<UserAccountMtmDevTeam> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Set<UserAccountMtmDevTeam> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FlowTable getFlowTable() {
        return flowTable;
    }

    public void setFlowTable(FlowTable flowTable) {
        this.flowTable = flowTable;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> joinedProjects) {
        this.projects = joinedProjects;
    }
}
