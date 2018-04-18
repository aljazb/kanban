package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="card")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "card.access", query = "SELECT c FROM Card c JOIN c.project p JOIN p.devTeam dt JOIN dt.joinedUsers m " +
                "WHERE c.id = :cardId AND m.isDeleted = false AND m.userAccount.id = :userId")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Card extends UUIDEntity<Card> {

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "work_load")
    private Integer workload;

    @Column(name = "color")
    private String color;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_part_id", nullable = false)
    private BoardPart boardPart;

    @OneToMany(mappedBy = "card")
    private Set<CardMove> cardMoves;

    @OneToMany(mappedBy = "card")
    private Set<SubTask> subTasks;


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWorkload() {
        return workload;
    }

    public void setWorkload(Integer workLoad) {
        this.workload = workLoad;
    }

    public BoardPart getBoardPart() {
        return boardPart;
    }

    public void setBoardPart(BoardPart boardPart) {
        this.boardPart = boardPart;
    }

    public Set<CardMove> getCardMoves() {
        return cardMoves;
    }

    public void setCardMoves(Set<CardMove> cardMoves) {
        this.cardMoves = cardMoves;
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
