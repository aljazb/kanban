package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Entity
@Table(name="dev_team")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "devTeam.membership", query = "SELECT m FROM DevTeam dt JOIN dt.joinedUsers m JOIN m.userAccount ua " +
                        "WHERE dt.id = :devTeamId AND ua.id = :userId AND m.isDeleted = false"),
        @NamedQuery(name = "devTeam.fetch.members", query = "SELECT dt FROM DevTeam dt JOIN FETCH dt.joinedUsers m JOIN FETCH m.userAccount ua " +
                "WHERE dt.id = :devTeamId AND m.isDeleted = false")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class DevTeam extends UUIDEntity<DevTeam> {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "devTeam")
    private Set<Membership> joinedUsers;

    @OneToMany(mappedBy = "devTeam")
    private Set<Project> projects;

    @Transient
    private Membership membership;

    public Membership queryMembership(EntityManager em, UUID authId) {
        membership = em.createNamedQuery("devTeam.membership", Membership.class)
                .setMaxResults(1)
                .setParameter("devTeamId", getId())
                .setParameter("userId", authId)
                .getResultList()
                .stream().findFirst().orElse(null);

        return membership;
    }

    @JsonIgnore
    public UserAccount getKanbanMaster() {
        return getJoinedUsers().stream()
                .filter(m -> !m.getIsDeleted() && m.isKanbanMaster())
                .map(Membership::getUserAccount)
                .findFirst().orElse(null);
    }

    @JsonIgnore
    public List<UserAccount> getDevelopers() {
        return getJoinedUsers().stream()
                .filter(m -> !m.getIsDeleted() && m.isDeveloper())
                .map(Membership::getUserAccount)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public UserAccount getProductOwner() {
        return getJoinedUsers().stream()
                .filter(m -> !m.getIsDeleted() && m.isProductOwner())
                .map(Membership::getUserAccount)
                .findFirst().orElse(null);
    }

    @JsonIgnore
    public Membership findMember(UUID userId) {
        return getJoinedUsers().stream()
                .filter(membership -> !membership.getIsDeleted() && membership.getUserAccount().getId().equals(userId))
                .findFirst().orElse(null);
    }



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

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
