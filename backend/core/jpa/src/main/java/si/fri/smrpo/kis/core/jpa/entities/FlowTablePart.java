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
    @OneToOne(mappedBy = "rootFlowTablePart")
    private FlowTable flowTable;

    @ManyToOne
    @JoinColumn(name = "flow_table_part_parent_id")
    private FlowTablePart parent;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "parent")
    private Set<FlowTablePart> children;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "flowTablePart")
    private Set<Card> cards;



    public FlowTable getFlowTable() {
        return flowTable;
    }

    public void setFlowTable(FlowTable flowTable) {
        this.flowTable = flowTable;
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

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
