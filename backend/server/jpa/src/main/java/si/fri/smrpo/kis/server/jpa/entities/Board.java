package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.core.jpa.anotations.Database;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name="board")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "board.membership",
                query = "SELECT m FROM Board b JOIN b.projects p JOIN p.devTeam dt JOIN dt.joinedUsers m " +
                        "WHERE b.id = :boardId AND m.userAccount.id = :userId AND m.isDeleted = false")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Board extends UUIDEntity<Board> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "remaining_days")
    private Integer remainingDays;

    @Column(name = "highest_priority")
    private Integer highestPriority;

    @Column(name = "start_dev")
    private Integer startDev;

    @Column(name = "end_dev")
    private Integer endDev;

    @Column(name = "acceptance_testing")
    private Integer acceptanceTesting;

    @Database(update = false)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    public UserAccount owner;

    @OneToMany(mappedBy = "board")
    private Set<BoardPart> boardParts;

    @OneToMany(mappedBy = "board")
    private Set<Project> projects;

    @OneToMany(mappedBy = "board")
    private Set<CardMoveRules> cardMoveRules;


    @Transient
    private Membership membership;

    public Membership queryMembership(EntityManager em, UUID authId) {
        membership = em.createNamedQuery("board.membership", Membership.class)
                .setMaxResults(1)
                .setParameter("boardId", getId())
                .setParameter("userId", authId)
                .getResultList()
                .stream().findFirst().orElse(null);

        return membership;
    }

    @JsonIgnore
    public Set<BoardPart> buildLeavesBoardParts() {
        Set<BoardPart> leafs = new HashSet<>();
        buildLeavesBoardParts(this.boardParts, leafs);
        return leafs;
    }

    @JsonIgnore
    private void buildLeavesBoardParts(Set<BoardPart> boardParts, Set<BoardPart> leafs) {
        for(BoardPart bp : boardParts) {
            if(bp.getLeafNumber() == null) {
                buildLeavesBoardParts(bp.getChildren(), leafs);
            } else {
                leafs.add(bp);
            }
        }
    }

    @JsonIgnore
    public void buildBoardPartsReferences() {
        Set<BoardPart> root = new HashSet<>();
        HashMap<UUID, BoardPart> map = new HashMap<>();

        Set<BoardPart> activeBoardParts = getBoardParts().stream()
                .filter(boardPart -> !boardPart.getIsDeleted())
                .collect(Collectors.toSet());

        for(BoardPart bp : activeBoardParts) {
            map.put(bp.getId(), bp);
            if(bp.getParent() == null) {
                root.add(bp);
            }
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
    public void fetchActiveProjectsWithCards() {
        Set<Project> activeProjects = getProjects().stream()
                .filter(e -> !e.getIsDeleted()).collect(Collectors.toSet());

        projects = activeProjects;

        for(Project p : activeProjects) { // Fetch project
            p.queryCards();
            for(Card c : p.getCards()) { // Fetch cards
                c.getSubTasks().size(); // Fetch sub tasks
                if(c.getAssignedTo() != null) {
                    c.getAssignedTo().getEmail(); // Fetch assigned to
                }
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

    public Integer getHighestPriority() {
        return highestPriority;
    }

    public void setHighestPriority(Integer highestPriority) {
        this.highestPriority = highestPriority;
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

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Integer getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }

    public Set<CardMoveRules> getCardMoveRules() {
        return cardMoveRules;
    }

    public void setCardMoveRules(Set<CardMoveRules> cardMoveRules) {
        this.cardMoveRules = cardMoveRules;
    }
}
