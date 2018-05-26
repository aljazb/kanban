package si.fri.smrpo.kis.server.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;

import javax.persistence.*;

@Entity
@Table(name="card_move_rule")
@Cacheable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class CardMoveRules extends UUIDEntity<CardMoveRules> {

    private Boolean roleDeveloperAllowed = false;
    private Boolean roleProductOwnerAllowed = false;
    private Boolean canReject = false;
    private Boolean bidirectionalMovement = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false)
    private BoardPart from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false)
    private BoardPart to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board", nullable = false)
    private Board board;


    public BoardPart getFrom() {
        return from;
    }

    public void setFrom(BoardPart from) {
        this.from = from;
    }

    public BoardPart getTo() {
        return to;
    }

    public void setTo(BoardPart to) {
        this.to = to;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Boolean getCanReject() {
        return canReject;
    }

    public void setCanReject(Boolean canProductOwnerReject) {
        this.canReject = canProductOwnerReject;
    }

    public Boolean getRoleDeveloperAllowed() {
        return roleDeveloperAllowed;
    }

    public void setRoleDeveloperAllowed(Boolean roleDeveloperAllowed) {
        this.roleDeveloperAllowed = roleDeveloperAllowed;
    }

    public Boolean getRoleProductOwnerAllowed() {
        return roleProductOwnerAllowed;
    }

    public void setRoleProductOwnerAllowed(Boolean roleProductOwnerAllowed) {
        this.roleProductOwnerAllowed = roleProductOwnerAllowed;
    }

    public Boolean getBidirectionalMovement() {
        return bidirectionalMovement;
    }

    public void setBidirectionalMovement(Boolean bidirectionalMovement) {
        this.bidirectionalMovement = bidirectionalMovement;
    }
}
