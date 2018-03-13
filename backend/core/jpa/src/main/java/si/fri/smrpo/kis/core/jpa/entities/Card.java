package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="card")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Card extends BaseEntity<Card> {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "work_load")
    private Integer workLoad;



    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "flow_table_part_id", nullable = false)
    private FlowTablePart flowTablePart;


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }

    public FlowTablePart getFlowTablePart() {
        return flowTablePart;
    }

    public void setFlowTablePart(FlowTablePart flowTablePart) {
        this.flowTablePart = flowTablePart;
    }
}
