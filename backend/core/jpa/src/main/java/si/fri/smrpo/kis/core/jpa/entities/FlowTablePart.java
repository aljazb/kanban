package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="flow_table_part")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class FlowTablePart extends BaseEntity<FlowTablePart> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_wip", nullable = false)
    private Integer maxWip;



    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "rootFlowTablePart")
    private Set<FlowTable> flowTables;

    @ManyToOne
    @JoinColumn(name = "flow_table_part_parent_id")
    private FlowTablePart parent;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "parent")
    private Set<FlowTablePart> children;



    public Set<FlowTable> getFlowTables() {
        return flowTables;
    }

    public void setFlowTables(Set<FlowTable> flowTables) {
        this.flowTables = flowTables;
    }

    public FlowTablePart getParent() {
        return parent;
    }

    public void setParent(FlowTablePart parent) {
        this.parent = parent;
    }

    public Set<FlowTablePart> getChildren() {
        return children;
    }

    public void setChildren(Set<FlowTablePart> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxWip() {
        return maxWip;
    }

    public void setMaxWip(Integer maxWip) {
        this.maxWip = maxWip;
    }
}
