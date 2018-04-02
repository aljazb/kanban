package si.fri.smrpo.kis.server.jpa.entities;


import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="project")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "project.isMember",
                query = "SELECT p.id FROM Project p JOIN p.devTeam dt JOIN dt.joinedUsers uaMTMdt JOIN uaMTMdt.userAccount ua " +
                        "WHERE p.id = :projectId AND dt = uaMTMdt.devTeam AND uaMTMdt.userAccount = ua AND ua.id = :userId")
})
public class Project extends UUIDEntity<Project> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "product_buyer")
    private String productBuyer;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardLane_id")
    private BoardLane boardLane;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_team_id")
    private DevTeam devTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_account_id")
    private UserAccount owner;


    public DevTeam getDevTeam() {
        return devTeam;
    }

    public void setDevTeam(DevTeam devTeam) {
        this.devTeam = devTeam;
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

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public String getProductBuyer() {
        return productBuyer;
    }

    public void setProductBuyer(String productBuyer) {
        this.productBuyer = productBuyer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BoardLane getBoardLane() {
        return boardLane;
    }

    public void setBoardLane(BoardLane boardLane) {
        this.boardLane = boardLane;
    }

}
