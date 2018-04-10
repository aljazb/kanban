package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name="project")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "project.access",
                query = "SELECT p.id FROM Project p JOIN p.devTeam dt JOIN dt.joinedUsers m JOIN m.userAccount ua " +
                        "WHERE p.id = :projectId AND (p.owner.id = :userId OR dt = m.devTeam AND m.userAccount = ua AND ua.id = :userId)")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_team_id")
    private DevTeam devTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_account_id")
    private UserAccount owner;

    @OneToMany(mappedBy = "project")
    private Set<Card> cards;


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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
