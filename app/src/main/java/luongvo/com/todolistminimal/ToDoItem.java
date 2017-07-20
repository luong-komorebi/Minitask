package luongvo.com.todolistminimal;

import java.util.Date;

/**
 * Created by luongvo on 19/07/2017.
 */

public class ToDoItem {
    public String content;
    public Boolean done;
    public String reminderDate;

    public ToDoItem(String content, Boolean done, String reminderDate) {
        this.content = content;
        this.done = done;
        this.reminderDate = reminderDate;
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
}
