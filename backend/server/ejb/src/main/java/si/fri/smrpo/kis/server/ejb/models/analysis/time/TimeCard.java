package si.fri.smrpo.kis.server.ejb.models.analysis.time;

import si.fri.smrpo.kis.server.jpa.entities.Card;

public class TimeCard {

    private Long time;
    private Card card;

    public TimeCard(Long time, Card card) {
        this.time = time;
        this.card = card;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
