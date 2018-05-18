package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.enums.CardMoveType;

import javax.persistence.*;

@Entity
@Table(name="card_move")
@Cacheable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class CardMove extends UUIDEntity<CardMove> {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardMoveType cardMoveType;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "moved_by_user_account_id")
    private UserAccount movedBy;

    @ManyToOne
    @JoinColumn(name = "from_board_part_id")
    private BoardPart from;

    @ManyToOne
    @JoinColumn(name = "to_board_part_id")
    private BoardPart to;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;


    public UserAccount getMovedBy() {
        return movedBy;
    }

    public void setMovedBy(UserAccount userAccount) {
        this.movedBy = userAccount;
    }

    public BoardPart getFrom() {
        return from;
    }

    public void setFrom(BoardPart fromBoardPart) {
        this.from = fromBoardPart;
    }

    public BoardPart getTo() {
        return to;
    }

    public void setTo(BoardPart toBoardPart) {
        this.to = toBoardPart;
    }

    public CardMoveType getCardMoveType() {
        return cardMoveType;
    }

    public void setCardMoveType(CardMoveType cardMoveType) {
        this.cardMoveType = cardMoveType;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
