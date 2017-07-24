package luongvo.com.todolistminimal;

import java.util.Date;

/**
 * Created by luongvo on 19/07/2017.
 */

public class ToDoItem {
    private String content;
    private Boolean done;
    private String reminderDate;
    private Boolean hasReminder;

    public ToDoItem(String content, Boolean done, String reminderDate, Boolean hasReminder) {
        this.content = content;
        this.done = done;
        this.reminderDate = reminderDate;
        this.hasReminder = hasReminder;
    }

    public Boolean getHasReminder() {
        return hasReminder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "content='" + content + '\'' +
                ", done=" + done +
                ", reminderDate='" + reminderDate + '\'' +
                ", hasReminder=" + hasReminder +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ToDoItem other = (ToDoItem) obj;
        if (!content.equals(other.content))
            return false;
        if (!reminderDate.equals(other.reminderDate))
            return false;
        if (hasReminder != other.hasReminder)
            return false;
        if (done != other.done)
            return false;
        return true;
    }
}
