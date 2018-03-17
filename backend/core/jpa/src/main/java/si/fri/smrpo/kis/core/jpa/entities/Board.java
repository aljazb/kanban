package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name="board")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Board extends BaseEntity<Board, UUID> {

    @Column(name = "name")
    private String name;


    @OneToOne
    @JoinColumn(name = "dev_team_id", nullable = false)
    public DevTeam devTeam;

    @OneToOne
    @JoinColumn(name = "root_board_part_id", nullable = false)
    private BoardPart rootBoardPart;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "board")
    public Set<Project> projects;


    public DevTeam getDevTeam() {
        return devTeam;
    }

    public void setDevTeam(DevTeam owner) {
        this.devTeam = owner;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public BoardPart getRootBoardPart() {
        return rootBoardPart;
    }

    public void setRootBoardPart(BoardPart rootBoardPart) {
        this.rootBoardPart = rootBoardPart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
