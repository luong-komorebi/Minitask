package luongvo.com.todolistminimal;

import java.io.Serializable;

/**
 * Created by luongvo on 19/07/2017.
 */

public class ToDoItem implements Serializable {
    private String content;
    private Boolean done;
    private String reminderDate;
    private Boolean hasReminder;
    private String mItemId;

    // Old constructor
  /*  public ToDoItem(String content, Boolean done, String reminderDate, Boolean hasReminder) {
        this.content = content;
        this.done = done;
        this.reminderDate = reminderDate;
        this.hasReminder = hasReminder;
    }*/

    // New cionstrucotr with Firebase Id variable
    public ToDoItem(String content, Boolean done, String reminderDate, Boolean hasReminder, String itemId) {
        this.content = content;
        this.done = done;
        this.reminderDate = reminderDate;
        this.hasReminder = hasReminder;
        this.mItemId = itemId;
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

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    // this serves logging.
//    @Override
//    public String toString() {
//        return "ToDoItem{" +
//                "content='" + content + '\'' +
//                ", done=" + done +
//                ", reminderDate='" + reminderDate + '\'' +
//                ", hasReminder=" + hasReminder +
//                '}';
//    }

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
}
