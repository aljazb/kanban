package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="board_part")
@Cacheable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class BoardPart extends UUIDEntity<BoardPart> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_wip")
    private Integer maxWip;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    private Boolean leaf;

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

    @OneToMany(mappedBy = "from")
    private Set<CardMove> cardMovesFrom;

    @OneToMany(mappedBy = "to")
    private Set<CardMove> cardMovesTo;

    @JsonIgnore
    public ArrayList<BoardPart> buildChildrenArray() {
        ArrayList<BoardPart> boardParts = new ArrayList<>(getChildren());
        boardParts.sort(Comparator.comparingInt(BoardPart::getOrderIndex));
        return boardParts;
    }

    @JsonIgnore
    public HashMap<UUID, BoardPart> buildChildrenMap() {
        return buildMap(getChildren());
    }

    @JsonIgnore
    public static HashMap<UUID, BoardPart> buildMap(Set<BoardPart> boardParts) {
        HashMap<UUID, BoardPart> map = new HashMap<>();
        if(boardParts != null){
            for(BoardPart bp : boardParts) {
                map.put(bp.getId(), bp);
            }
        }
        return map;
    }

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
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Set<CardMove> getCardMovesFrom() {
        return cardMovesFrom;
    }

    public void setCardMovesFrom(Set<CardMove> cardMovesFrom) {
        this.cardMovesFrom = cardMovesFrom;
    }

    public Set<CardMove> getCardMovesTo() {
        return cardMovesTo;
    }

    public void setCardMovesTo(Set<CardMove> cardMovesTo) {
        this.cardMovesTo = cardMovesTo;
    }
}
