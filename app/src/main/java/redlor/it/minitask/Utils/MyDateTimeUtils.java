package redlor.it.minitask.Utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import redlor.it.minitask.MainActivity;
import redlor.it.minitask.R;

import static redlor.it.minitask.Utils.NotificationPublisher.NOTIFICATION;
import static redlor.it.minitask.Utils.NotificationPublisher.NOTIFICATION_ID;


// This class includes every helper function that relates to time
public class MyDateTimeUtils {

    // Three simpledateformat for each specific use
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /* This return a formatted date if the parameter passed in is empty.
     It is used in case the user select the "set time" option without first "set date".
     The default date is today.
     We can have a equivalent fillTimeIfEmpty but I decided not to implement that
     Because time range is wider so it's hard to choose 1 point of time as default.*/
    public String fillDateIfEmpty(String date) {
        if (date.equals("")) {
            Calendar calendar = Calendar.getInstance();
            return dateFormatter.format(calendar.getTime());
        } else return date;
    }

    /*This returns a date string with passed-in integer year, months, dayofmonth.*/
    public String dateToString(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return dateFormatter.format(calendar.getTime());
    }

    /*This returns a time string with passed-in integer year, months, dayofmonth.*/
    public String timeToString(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hourOfDay, minute);
        return timeFormatter.format(calendar.getTime());
    }

    /* this check if a date user chooses is not in the past by comparing to current date*/
    public boolean checkInvalidDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date dateSet = calendar.getTime();
        return (today.compareTo(dateSet) > 0);
    }

    /* This check if a time user chooses is not in the past by comparing to current time */
    public boolean checkInvalidTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        return (hourOfDay < nowHour || (hourOfDay == nowHour && minute <= nowMinute));
    }

    /* this schedule notification to the time that user set*/
    public void ScheduleNotification(Notification notification,
                                     Context context, int notificationID, String dateTime) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // parse string parameter to milliseconds for later alarm set
        Date futureInMillis = null;
        try {
            futureInMillis = dateTimeFormatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis.getTime(), pendingIntent);
    }

    /* this cancel a future notification if item is deleted or editted */
    public void cancelScheduledNotification(Notification notification,
                                            Context context, int notificationID) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /* This is a helper function to build a notification object */
    public Notification getNotification(String content, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(R.string.task_to_be_done));
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setShowWhen(false);
        builder.setAutoCancel(true);
        return builder.build();
    }
}
