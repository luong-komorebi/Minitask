package luongvo.com.todolistminimal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import luongvo.com.todolistminimal.Pages.Home;

import static luongvo.com.todolistminimal.Pages.AddTask.TASK_NAME;
import static luongvo.com.todolistminimal.Utils.CustomAssertions.shouldDisplayNewTaskInTheList;

/**
 * Created by Daniel Blokus on 30.09.2017.
 */

@RunWith(AndroidJUnit4.class)
public class TasksTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createDefaultTask_ShouldDisplayHomeView() {
        new Home().navigateToAddTask().addTask();
        shouldDisplayNewTaskInTheList(TASK_NAME);
    }
}
