package luongvo.com.todolistminimal.Pages;

import luongvo.com.todolistminimal.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by Daniel Blokus on 30.09.2017.
 */

public class AddTask {

    public static final String TASK_NAME = "test";

    public AddTask() {
        onView(allOf(withId(R.id.todoInput)))
                .check(matches(notNullValue()))
                .check(matches(isDisplayed()));
    }

    public Home addTask() {
        isTaskNameInputDisplayed();
        fillTaskName();
        closeSoftKeyboard();
        isAddButtonDisplayed();
        clickAddButton();
        return new Home();
    }

    private void isTaskNameInputDisplayed() {
        onView(allOf(withId(R.id.todoEditText), isDisplayed()));
    }

    private void fillTaskName() {
        onView((withId(R.id.todoEditText)))
                .perform(replaceText(TASK_NAME));
    }

    private void isAddButtonDisplayed() {
        onView(withId(R.id.addTodoBtn))
                .check(matches(notNullValue()))
                .check(matches(isDisplayed()));
    }

    private void clickAddButton() {
        onView(withId(R.id.addTodoBtn))
                .perform(click());
    }

}
