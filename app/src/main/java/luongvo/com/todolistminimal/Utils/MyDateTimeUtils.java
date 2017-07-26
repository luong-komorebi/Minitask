package luongvo.com.todolistminimal.Utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import luongvo.com.todolistminimal.R;

import static luongvo.com.todolistminimal.Utils.NotificationPublisher.NOTIFICATION;
import static luongvo.com.todolistminimal.Utils.NotificationPublisher.NOTIFICATION_ID;

/**
 * Created by luongvo on 22/07/2017.
 */

public class MyDateTimeUtils {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

//    public long dateDiff(String dateTime) {
//        Calendar calendar = Calendar.getInstance();
//        Date today = calendar.getTime();
//        Date d1 = dateTimeFormatter.parse(dateTime);
//        return d1.getTime() - today.getTime();
//    }

    public void ScheduleNotification(Notification notification,
                                     Context context, int notificationID, String dateTime) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Date futureInMillis = null;
        try {
            futureInMillis = dateTimeFormatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis.getTime() - System.currentTimeMillis(), pendingIntent);
    }

    public Notification getNotification(String content, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.task_to_be_done));
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_warning_black_24dp);
        return builder.build();
    }
}
