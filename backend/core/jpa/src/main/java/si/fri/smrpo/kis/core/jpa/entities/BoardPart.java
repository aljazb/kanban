package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="board_part")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class BoardPart extends BaseEntity<BoardPart, UUID> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_wip", nullable = false)
    private Integer maxWip;



    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToOne(mappedBy = "rootBoardPart")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "board_part__id")
    private BoardPart parent;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "parent")
    private Set<BoardPart> children;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "boardPart")
    private Set<Card> cards;


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public BoardPart getParent() {
        return parent;
    }

    public void setParent(BoardPart parent) {
        this.parent = parent;
    }

    public Set<BoardPart> getChildren() {
        return children;
    }

    public void setChildren(Set<BoardPart> children) {
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
