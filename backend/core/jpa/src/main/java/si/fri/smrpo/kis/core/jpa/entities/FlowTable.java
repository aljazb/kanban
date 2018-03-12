package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="flow_table")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class FlowTable extends BaseEntity<FlowTable> {

    @Column(name = "name")
    private String name;


    @ManyToOne
    @JoinColumn(name = "owner_user_account_id", nullable = false)
    public UserAccount owner;

    @ManyToOne
    @JoinColumn(name = "root_flow_table_part_id", nullable = false)
    private FlowTablePart rootFlowTablePart;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "flowTable")
    public Set<Project> projects;



    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public FlowTablePart getRootFlowTablePart() {
        return rootFlowTablePart;
    }

    public void setRootFlowTablePart(FlowTablePart rootFlowTablePart) {
        this.rootFlowTablePart = rootFlowTablePart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
