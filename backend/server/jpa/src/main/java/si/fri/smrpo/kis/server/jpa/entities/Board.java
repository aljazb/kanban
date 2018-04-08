package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="board")
@Cacheable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Board extends UUIDEntity<Board> {

    @Column(name = "name", nullable = false)
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
    @JoinColumn(name = "owner_id", nullable = false)
    public UserAccount owner;

    @OneToMany(mappedBy = "board")
    private Set<BoardPart> boardParts;


    @OneToMany(mappedBy = "board")
    private Set<Project> projects;


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

    public Integer getAcceptanceTesting() {
        return acceptanceTesting;
    }

    public void setAcceptanceTesting(Integer acceptanceTesting) {
        this.acceptanceTesting = acceptanceTesting;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
