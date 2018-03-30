package si.fri.smrpo.kis.server.jpa.entities;

import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="card")
@Cacheable
public class Card extends UUIDEntity<Card> {

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "work_load")
    private Integer workload;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_part_id", nullable = false)
    private BoardPart boardPart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_lane_id", nullable = false)
    private BoardLane boardLane;

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

    public BoardLane getBoardLane() {
        return boardLane;
    }

    public void setBoardLane(BoardLane boardLane) {
        this.boardLane = boardLane;
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
}
