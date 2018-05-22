package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.core.jpa.anotations.Database;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseCrudImpl;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
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

    @Column(name = "max_wip", nullable = false)
    private Integer maxWip;

    @Database(update = false)
    @Column(name = "current_wip", nullable = false)
    private Integer currentWip;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    private Integer leafNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Database(update = false)
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


    @OneToMany(mappedBy = "from")
    private Set<CardMoveRules> cardMovesRulesFrom;

    @OneToMany(mappedBy = "to")
    private Set<CardMoveRules> cardMovesRulesTo;

    @JsonIgnore
    public void incWip(DatabaseCrudImpl<UUID> database) throws DatabaseException {
        currentWip++;
        if(getParent() != null){
            getParent().incWip(database);
        }
        database.update(this);
    }

    @JsonIgnore
    public void decWip(DatabaseCrudImpl<UUID> database) throws DatabaseException {
        currentWip--;
        if(getParent() != null){
            getParent().decWip(database);
        }
        database.update(this);
    }

    @JsonIgnore
    public boolean hasChildren() {
        return getChildren() != null && getChildren().size() > 0;
    }

    @JsonIgnore
    public boolean isWipValid(BoardPart parent) {
        return parent.maxWip == 0 || maxWip <= parent.maxWip;
    }

    @JsonIgnore
    public static boolean isMoveWipValid(BoardPart to, BoardPart from) {
        HashSet<UUID> parentIds = new HashSet<>();

        while (from != null) {
            parentIds.add(from.getId());
            from = from.getParent();
        }

        while (to != null) {
            if(to.getMaxWip() != 0) {
                int currentWip = to.getCurrentWip();
                if(parentIds.contains(to.getId())) {
                    currentWip--;
                }

                if(to.getMaxWip() <= currentWip) {
                    return false;
                }
            }

            to = to.getParent();
        }

        return true;
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

    public Integer getLeafNumber() {
        return leafNumber;
    }

    public void setLeafNumber(Integer leafNumber) {
        this.leafNumber = leafNumber;
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

    public Integer getCurrentWip() {
        return currentWip;
    }

    public void setCurrentWip(Integer currentWip) {
        this.currentWip = currentWip;
    }

    public Set<CardMoveRules> getCardMovesRulesFrom() {
        return cardMovesRulesFrom;
    }

    public void setCardMovesRulesFrom(Set<CardMoveRules> cardMovesRulesFrom) {
        this.cardMovesRulesFrom = cardMovesRulesFrom;
    }

    public Set<CardMoveRules> getCardMovesRulesTo() {
        return cardMovesRulesTo;
    }

    public void setCardMovesRulesTo(Set<CardMoveRules> cardMovesRulesTo) {
        this.cardMovesRulesTo = cardMovesRulesTo;
    }
}
