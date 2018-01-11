package redlor.it.minitask;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luongvo on 19/07/2017.
 */

public class ToDoItem implements Serializable {
    public Map<String, Boolean> toDoItemsMap = new HashMap<>();
    private String content;
    private boolean done;
    private String reminderDate;
    private boolean hasReminder;
    private String mItemId;

    // Old constructor
    public ToDoItem(String content, boolean done, boolean hasReminder, String reminderDate) {
        this.content = content;
        this.done = done;
        this.hasReminder = hasReminder;
        this.reminderDate = reminderDate;

    }

    public ToDoItem() {
    }

    // New constructor with Firebase Id variable
    public ToDoItem(String content, boolean done, String reminderDate, boolean hasReminder, String itemId) {
        this.content = content;
        this.done = done;
        this.reminderDate = reminderDate;
        this.hasReminder = hasReminder;
        this.mItemId = itemId;
    }


    public boolean getHasReminder() {
        return hasReminder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    // compare object for remove from array list
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

    @Override
    public String toString() {
        return "ToDoItem{" +
                "content='" + content + '\'' +
                ", done=" + done +
                ", reminderDate='" + reminderDate + '\'' +
                ", hasReminder=" + hasReminder +
                ", mItemId='" + mItemId + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("done", done);
        result.put("hasReminder", hasReminder);
        result.put("reminderDate", reminderDate);

        return result;
    }
}
