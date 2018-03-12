package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.mtm.DevTeamMtmProject;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="project")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Project extends BaseEntity<Project> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;



    @ManyToOne
    @JoinColumn(name = "owner_user_account_id", nullable = false)
    public UserAccount owner;

    @ManyToOne
    @JoinColumn(name = "flow_table_id", nullable = false)
    private FlowTable flowTable;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "project")
    private Set<DevTeamMtmProject> joinedDevTeams;



    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public FlowTable getFlowTable() {
        return flowTable;
    }

    public void setFlowTable(FlowTable flowTable) {
        this.flowTable = flowTable;
    }

    public Set<DevTeamMtmProject> getJoinedDevTeams() {
        return joinedDevTeams;
    }

    public void setJoinedDevTeams(Set<DevTeamMtmProject> joinedDevTeams) {
        this.joinedDevTeams = joinedDevTeams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
