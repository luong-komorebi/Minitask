package luongvo.com.todolistminimal;

/**
 * Created by luongvo on 19/07/2017.
 */

public class ToDoItem {
    public String content;
    public Boolean done;

    public ToDoItem(String content, Boolean done) {
        this.content = content;
        this.done = done;
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
