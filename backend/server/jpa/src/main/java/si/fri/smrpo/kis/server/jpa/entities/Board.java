package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name="board")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "board.access", query = "SELECT b FROM Board b JOIN b.projects p JOIN p.devTeam dt JOIN dt.joinedUsers m " +
                "WHERE b.id = :boardId AND (b.owner.id = :userId OR m.userAccount.id = :userId)")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Board extends UUIDEntity<Board> {

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "highest_priority")
    private UUID highestPriority;

    @Column(name = "start_dev")
    private UUID startDev;

    @Column(name = "end_dev")
    private UUID endDev;

    @Column(name = "acceptance_testing")
    private UUID acceptanceTesting;


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

    public UUID getHighestPriority() {
        return highestPriority;
    }

    public void setHighestPriority(UUID highestPriority) {
        this.highestPriority = highestPriority;
    }

    public UUID getStartDev() {
        return startDev;
    }

    public void setStartDev(UUID startDev) {
        this.startDev = startDev;
    }

    public UUID getEndDev() {
        return endDev;
    }

    public void setEndDev(UUID endDev) {
        this.endDev = endDev;
    }

    public UUID getAcceptanceTesting() {
        return acceptanceTesting;
    }

    public void setAcceptanceTesting(UUID acceptanceTesting) {
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
