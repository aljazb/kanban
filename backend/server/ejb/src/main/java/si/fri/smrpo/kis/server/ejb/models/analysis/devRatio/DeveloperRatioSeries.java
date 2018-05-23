package si.fri.smrpo.kis.server.ejb.models.analysis.devRatio;

import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public class DeveloperRatioSeries {

    private UserAccount developer;

    private Integer combinedCards = 0;
    private Integer combinedWorkload = 0;

    public DeveloperRatioSeries(UserAccount developer) {
        this.developer = developer;
    }

    public UserAccount getDeveloper() {
        return developer;
    }

    public void setDeveloper(UserAccount developer) {
        this.developer = developer;
    }

    public Integer getCombinedCards() {
        return combinedCards;
    }

    public void setCombinedCards(Integer combinedCards) {
        this.combinedCards = combinedCards;
    }

    public Integer getCombinedWorkload() {
        return combinedWorkload;
    }

    public void setCombinedWorkload(Integer combinedWorkload) {
        this.combinedWorkload = combinedWorkload;
    }

    public void incCards() {
        this.combinedCards++;
    }

    public void incWorkload(Integer value) {
        this.combinedWorkload += value;
    }
}
