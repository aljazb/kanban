package si.fri.smrpo.kis.server.ejb.models.analysis.time;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.fri.smrpo.kis.server.jpa.entities.Card;

public class TimeCard {

    @JsonIgnore
    private Long time;

    @JsonIgnore
    private Card card;

    public TimeCard(Long time, Card card) {
        this.time = time;
        this.card = card;
    }

    @JsonIgnore
    public Long getTime() {
        return time;
    }

    @JsonProperty("value")
    public Long getValue() {
        return time;
    }

    @JsonProperty("name")
    public String getName() {
        return card.getName();
    }
}
