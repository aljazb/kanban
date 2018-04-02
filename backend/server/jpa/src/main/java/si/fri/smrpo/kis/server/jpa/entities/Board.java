package si.fri.smrpo.kis.server.jpa.entities;


import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="board")
@Cacheable
public class Board extends UUIDEntity<Board> {

    @Column(name = "name")
    private String name;


    @Column(name = "highest_priority", nullable = false)
    private Integer highestPriority;

    @Column(name = "start_dev", nullable = false)
    private Integer startDev;

    @Column(name = "end_dev", nullable = false)
    private Integer endDev;

    @Column(name = "acceptance_testing", nullable = false)
    private Integer acceptanceTesting;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_team_id", nullable = false)
    public DevTeam devTeam;

    @OneToMany(mappedBy = "board")
    private Set<BoardPart> boardParts;

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

    public Integer getHighestPriority() {
        return highestPriority;
    }

    public void setHighestPriority(Integer higestPriority) {
        this.highestPriority = higestPriority;
    }

    public Integer getStartDev() {
        return startDev;
    }

    public void setStartDev(Integer startDev) {
        this.startDev = startDev;
    }

    public Integer getEndDev() {
        return endDev;
    }

    public void setEndDev(Integer endDev) {
        this.endDev = endDev;
    }

    public Integer getAcceptenceTesting() {
        return acceptanceTesting;
    }

    public void setAcceptenceTesting(Integer acceptanceTesting) {
        this.acceptanceTesting = acceptanceTesting;
    }
}
