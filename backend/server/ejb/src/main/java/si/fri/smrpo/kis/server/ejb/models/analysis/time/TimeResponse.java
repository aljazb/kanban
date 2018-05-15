package si.fri.smrpo.kis.server.ejb.models.analysis.time;

import java.util.ArrayList;

public class TimeResponse {

    private Long averageTime = 0L;
    private ArrayList<TimeCard> cards = new ArrayList<>();


    public Long getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Long averageTime) {
        this.averageTime = averageTime;
    }

    public ArrayList<TimeCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<TimeCard> cards) {
        this.cards = cards;
    }

    public void addTimeCard(TimeCard card) {
        this.cards.add(card);
    }

    public void calculateAverageTime() {
        if(cards.isEmpty()) {
            averageTime = 0L;
        } else {
            for(TimeCard tc : cards) {
                averageTime += tc.getTime();
            }

            averageTime /= cards.size();
        }
    }
}
