package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="dev_team")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "devTeam.getKanbanMaster",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE uaMTMdt.isDeleted = FALSE AND dt.id = :id AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND " +
                        "(uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.KANBAN_MASTER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_KANBAN_MASTER)"),
        @NamedQuery(name = "devTeam.getProductOwner",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE uaMTMdt.isDeleted = FALSE AND dt.id = :id AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND " +
                        "(uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.PRODUCT_OWNER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_PRODUCT_OWNER)"),
        @NamedQuery(name = "devTeam.getMembers",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE uaMTMdt.isDeleted = FALSE AND dt.id = :id AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua"),
        @NamedQuery(name = "devTeam.isMember",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua " +
                        "WHERE uaMTMdt.isDeleted = FALSE AND dt.id = :devTeamId AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND ua.id = :userId"),
        @NamedQuery(name = "devTeam.getDevelopers",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE uaMTMdt.isDeleted = FALSE AND dt.id = :id AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND " +
                        "(uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_KANBAN_MASTER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_PRODUCT_OWNER)"),
        @NamedQuery(name = "devTeam.isDeveloper",
                query = "SELECT ua FROM DevTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua WHERE uaMTMdt.isDeleted = FALSE AND dt.id = :devTeamId AND ua.id = :userId AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND " +
                        "(uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_KANBAN_MASTER OR uaMTMdt.memberType = si.fri.smrpo.kis.server.jpa.enums.MemberType.DEVELOPER_AND_PRODUCT_OWNER)"),
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class DevTeam extends UUIDEntity<DevTeam> {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "devTeam")
    private Set<Membership> joinedUsers;

    @OneToMany(mappedBy = "devTeam")
    private Set<Project> projects;


    public Set<Membership> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Set<Membership> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> joinedProjects) {
        this.projects = joinedProjects;
    }
}
