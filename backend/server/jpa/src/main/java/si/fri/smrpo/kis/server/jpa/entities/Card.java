package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;

@Entity
@Table(name="card")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Card extends UUIDEntity<Card> {

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "work_load")
    private Integer workLoad;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_part_id", nullable = false)
    private BoardPart boardPart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_lane_id", nullable = false)
    private BoardLane boardLane;


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

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
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
}
