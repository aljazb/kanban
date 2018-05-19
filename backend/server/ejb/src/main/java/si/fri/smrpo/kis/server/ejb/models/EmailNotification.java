package si.fri.smrpo.kis.server.ejb.models;

import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmailNotification {

    private static final SimpleDateFormat basicSdf = new SimpleDateFormat("dd.MM.yyyy");

    private UserAccount kanbanMaster;
    private List<Card> cards = new ArrayList<>();

    public EmailNotification(UserAccount kanbanMaster) {
        this.kanbanMaster = kanbanMaster;
    }

    public UserAccount getKanbanMaster() {
        return kanbanMaster;
    }

    public void setKanbanMaster(UserAccount kanbanMaster) {
        this.kanbanMaster = kanbanMaster;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n The following cards are near due date: \n\n");

        for(Card c : cards) {
            sb.append(String.format("Card: %s \t Due date: %s \t Project: %s \n", c.getName(), basicSdf.format(c.getDueDate()), c.getProject().getName()));
        }


        return sb.toString();
    }
}
