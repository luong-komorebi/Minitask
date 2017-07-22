package luongvo.com.todolistminimal.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by luongvo on 22/07/2017.
 */

public class MyDateTimeUtils {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    public String fillDateIfEmpty(String date) {
        if (date.equals("")) {
            Calendar calendar = Calendar.getInstance();
            return dateFormatter.format(calendar.getTime());
        }
        else return date;
    }

    public String dateToString(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return dateFormatter.format(calendar.getTime());
    }

    public String timeToString(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0,0,0,hourOfDay,minute);
        return timeFormatter.format(calendar.getTime());
    }

    public boolean checkInvalidDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date dateSet = calendar.getTime();
        return (today.compareTo(dateSet) > 0);
    }

    public boolean checkInvalidTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        return (hourOfDay < nowHour || (hourOfDay == nowHour && minute <= nowMinute));
    }
}
