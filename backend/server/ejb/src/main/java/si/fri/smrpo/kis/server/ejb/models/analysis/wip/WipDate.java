package si.fri.smrpo.kis.server.ejb.models.analysis.wip;

import si.fri.smrpo.kis.server.jpa.entities.CardMove;

import java.util.ArrayList;
import java.util.Date;

import static si.fri.smrpo.kis.server.ejb.models.analysis.Utility.roundUpDateToDay;

public class WipDate {

    private Date date;
    private ArrayList<CardMove> cardMoves = new ArrayList<>();


    public WipDate(Date date) {
        this.date = date;
    }

    public boolean equalDate(Date date) {
        Date trim = roundUpDateToDay(date);
        boolean isEqual = this.date.equals(trim);
        return isEqual;
    }

    public void addCardMove(CardMove cardMove) {
        this.cardMoves.add(cardMove);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<CardMove> getCardMoves() {
        return cardMoves;
    }

    public void setCardMoves(ArrayList<CardMove> cardMoves) {
        this.cardMoves = cardMoves;
    }
}
