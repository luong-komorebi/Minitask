package luongvo.com.todolistminimal.Pages;

import luongvo.com.todolistminimal.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by Daniel Blokus on 30.09.2017.
 */

public class Home {

    public Home(){
        onView(allOf(withId(R.id.action_bar)))
                .check(matches(notNullValue())).check(matches(isDisplayed()));
    }

    public AddTask navigateToAddTask() {
        onView(allOf(withId(R.id.actionButton))).perform(click());
        return new AddTask();
    }
}
