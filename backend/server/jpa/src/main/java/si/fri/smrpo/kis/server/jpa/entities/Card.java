package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.core.jpa.anotations.Database;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.enums.CardType;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="card")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "card.membership",
                query = "SELECT m FROM Card c JOIN c.project p JOIN p.devTeam dt JOIN dt.joinedUsers m " +
                        "WHERE c.id = :cardId AND m.userAccount.id = :userId AND m.isDeleted = false")
})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Card extends UUIDEntity<Card> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "work_load")
    private Integer workload;

    @Column(name = "color")
    private String color;

    @Column(name = "silver_bullet", nullable = false)
    private Boolean silverBullet;

    @Database(update = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Database(update = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_part_id", nullable = false)
    private BoardPart boardPart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private UserAccount assignedTo;

    @OneToMany(mappedBy = "card")
    private Set<CardMove> cardMoves;

    @OneToMany(mappedBy = "card")
    private Set<SubTask> subTasks;


    @Transient
    private Membership membership;

    public Membership queryMembership(EntityManager em, UUID authId) {
        membership = em.createNamedQuery("card.membership", Membership.class)
                .setMaxResults(1)
                .setParameter("cardId", getId())
                .setParameter("userId", authId)
                .getResultList()
                .stream().findFirst().orElse(null);

        return membership;
    }


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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Boolean getSilverBullet() {
        return silverBullet;
    }

    public void setSilverBullet(Boolean silverBullet) {
        this.silverBullet = silverBullet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public UserAccount getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserAccount assignedTo) {
        this.assignedTo = assignedTo;
    }
}
