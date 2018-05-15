package si.fri.smrpo.kis.server.ejb.models.analysis;

import java.util.Calendar;
import java.util.Date;

public class Utility {

    public static Date trimDate(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);

        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);

        return c.getTime();
    }

}
