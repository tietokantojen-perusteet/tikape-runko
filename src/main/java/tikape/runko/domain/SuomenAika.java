package tikape.runko.domain;

import java.text.DateFormat;
import java.util.*;

public class SuomenAika {

    private final Date date;

    public SuomenAika(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        if (date == null) {
            return "-";
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("fi", "FI"));
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
        return dateFormat.format(date);
    }

}
