package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="board_lane")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class BoardLane extends BaseEntity<BoardLane, UUID> {


    @Column(name = "name", nullable = false)
    private String name;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToOne(mappedBy = "boardLane")
    private Project project;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "boardLane")
    private Set<Card> cards;


    public Project getProject() {
        return project;
    }

    public void setProject(Project board) {
        this.project = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }


}
