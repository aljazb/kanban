package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="board")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Board extends UUIDEntity<Board> {

    @Column(name = "name")
    private String name;


    @OneToOne
    @JoinColumn(name = "dev_team_id", nullable = false)
    public DevTeam devTeam;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "board")
    private Set<BoardPart> boardParts;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "board")
    private Set<BoardLane> boardLanes;


    public DevTeam getDevTeam() {
        return devTeam;
    }

    public void setDevTeam(DevTeam owner) {
        this.devTeam = owner;
    }

    public Set<BoardPart> getBoardParts() {
        return boardParts;
    }

    public void setBoardParts(Set<BoardPart> rootBoardPart) {
        this.boardParts = rootBoardPart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BoardLane> getBoardLanes() {
        return boardLanes;
    }

    public void setBoardLanes(Set<BoardLane> boardLanes) {
        this.boardLanes = boardLanes;
    }
}
