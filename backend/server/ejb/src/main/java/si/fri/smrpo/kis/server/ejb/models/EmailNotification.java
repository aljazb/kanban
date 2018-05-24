package si.fri.smrpo.kis.server.ejb.models;

import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmailNotification {

    private static final SimpleDateFormat basicSdf = new SimpleDateFormat("dd.MM.yyyy");

    private UserAccount receiver;
    private List<Card> cards = new ArrayList<>();

    public EmailNotification(UserAccount receiver) {
        this.receiver = receiver;
    }

    public UserAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(UserAccount receiver) {
        this.receiver = receiver;
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

    public String getTableContent() {
        StringBuilder sb = new StringBuilder();

        cards.sort((o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate()));

        for(Card c : cards) {
            sb.append("<tr>");

            sb.append("<td>");
            sb.append(c.getName());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(c.getCode());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(basicSdf.format(c.getDueDate()));
            sb.append("</td>");

            sb.append("<td>");
            sb.append(c.getProject().getName());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(c.getProject().getBoard().getName());
            sb.append("</td>");

            sb.append("</tr>");
        }


        return sb.toString();
    }
}
