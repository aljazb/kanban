package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="dev_team")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "devTeam.getKanbanMaster",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE dt.id = :id AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND " +
                        "(uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.KANBAN_MASTER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_KANBAN_MASTER)"),
        @NamedQuery(name = "devTeam.getMembers",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE dt.id = :id AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua"),
        @NamedQuery(name = "devTeam.isMember",
                query = "SELECT ua.id FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua " +
                        "WHERE dt.id = :devTeamId AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND ua.id = :userId")
})
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class DevTeam extends UUIDEntity<DevTeam> {

    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "devTeam")
    private Set<UserAccountMtmDevTeam> joinedUsers;

    @OneToMany(mappedBy = "devTeam")
    private Set<Project> projects;

    @OneToOne(mappedBy = "devTeam", fetch = FetchType.LAZY)
    private Board board;


    public Set<UserAccountMtmDevTeam> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Set<UserAccountMtmDevTeam> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> joinedProjects) {
        this.projects = joinedProjects;
    }
}
