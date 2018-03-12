package si.fri.smrpo.kis.core.jpa.entities.mtm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.DevTeam;
import si.fri.smrpo.kis.core.jpa.entities.Project;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name="dev_team_mtm_project")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class DevTeamMtmProject extends BaseEntity<DevTeamMtmProject> {


    @ManyToOne
    @JoinColumn(name = "dev_team_id", nullable = false)
    private DevTeam devTeam;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    public DevTeam getDevTeam() {
        return devTeam;
    }

    public void setDevTeam(DevTeam devTeam) {
        this.devTeam = devTeam;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
