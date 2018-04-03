package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="board_lane")
@Cacheable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class BoardLane extends UUIDEntity<BoardLane> {


    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "boardLane", fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
