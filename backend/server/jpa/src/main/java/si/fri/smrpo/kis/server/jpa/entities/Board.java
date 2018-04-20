package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name="board")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "board.access.view", query = "SELECT b FROM Board b JOIN b.projects p JOIN p.devTeam dt JOIN dt.joinedUsers m " +
                "WHERE b.id = :boardId AND (b.owner.id = :userId OR m.userAccount.id = :userId)"),
        @NamedQuery(name = "board.access.edit", query = "SELECT b FROM Board b JOIN b.projects p JOIN p.devTeam dt JOIN dt.joinedUsers m " +
                "WHERE b.id = :boardId AND (b.owner.id = :userId OR (m.userAccount.id = :userId AND " +
                "(m.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_KANBAN_MASTER OR " +
                "m.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.KANBAN_MASTER)))")
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

    @Override
    protected boolean baseSkip(Field field) {
        if(super.baseSkip(field)) {
            return true;
        } else {
            switch (field.getName()) {
                case "owner": return true;
                default: return false;
            }
        }
    }

    @JsonIgnore
    public void buildBoardPartsReferences() {
        Set<BoardPart> root = new HashSet<>();
        HashMap<UUID, BoardPart> map = new HashMap<>();

        Set<BoardPart> activeBoardParts = boardParts.stream()
                .filter(boardPart -> !boardPart.getIsDeleted())
                .collect(Collectors.toSet());

        for(BoardPart bp : activeBoardParts) {
            map.put(bp.getId(), bp);
            if(bp.getParent() == null) root.add(bp);
        }

        for(BoardPart bp : activeBoardParts) {
            if(bp.getParent() != null) {
                BoardPart parent = map.get(bp.getParent().getId());

                if(parent.getChildren() == null){
                    parent.setChildren(new HashSet<>());
                }
                parent.getChildren().add(bp);

                bp.setParent(parent);
            }
        }

        boardParts = root;
    }

    @JsonIgnore
    public void fetchProjectsWithCards() {
        for(Project p : getProjects()) { // Fetch project
            for(Card c : p.getCards()) { // Fetch cards
                c.getSubTasks().size(); // Fetch sub tasks
            }
        }
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
