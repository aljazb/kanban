package si.fri.smrpo.kis.core.businessLogic.dto;

import java.util.List;

public class Paging<T> {

    private List<T> items;

    private long count;


    public Paging(List<T> items, long count) {
        this.items = items;
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
