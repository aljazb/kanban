package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="board_part")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class BoardPart extends UUIDEntity<BoardPart> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_wip", nullable = false)
    private Integer maxWip;

    private Boolean isLeaf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_board_part_id")
    private BoardPart parent;

    @OneToMany(mappedBy = "parent")
    private Set<BoardPart> children;

    @OneToMany(mappedBy = "boardPart")
    private Set<Card> cards;


    public BoardPart getParent() {
        return parent;
    }

    public void setParent(BoardPart parent) {
        this.parent = parent;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
