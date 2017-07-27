package luongvo.com.todolistminimal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.Adapters.MyFragmentPagerAdapter;
import luongvo.com.todolistminimal.Database.TodoListContract;
import luongvo.com.todolistminimal.Database.TodoListDbHelper;
import luongvo.com.todolistminimal.Utils.UpdateDatabase;

import static luongvo.com.todolistminimal.PageFragment.toDoItems;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager) ViewPager pager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.descriptImage) ImageSwitcher descriptImage;
    @BindView(R.id.actionButton) FloatingActionButton actionButton;

    UpdateDatabase updateDatabase;

    // images for switcher
    private static final int[] IMAGES = {R.drawable.inbox, R.drawable.today, R.drawable.seven_day};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openAndQueryDb(0); // first query for inbox tab
        initializeComponents();
    }

    private void initializeComponents() {
        updateDatabase = new UpdateDatabase();
        ButterKnife.bind(this);
        // set the pager adapter. More info look in 3rd party library document
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(pager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // if user go to another tab change color, change image and query from database to match
                descriptImage.setImageResource(IMAGES[position]);
                changeColor(position);
                openAndQueryDb(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add new item is clicked
                Intent intent = new Intent(v.getContext(), AddTodoItem.class);
                startActivity(intent);
            }
        });

        descriptImage.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                // set props for image switcher
                ImageView imgview = new ImageView(getApplicationContext());
                imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imgview;
            }
        });
        // Photo flies in and out
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        descriptImage.setInAnimation(in);
        descriptImage.setOutAnimation(out);
        descriptImage.setImageResource(IMAGES[0]); // first start render
    }

    // this function open and query todoitems from database
    private void openAndQueryDb(final int mPage) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toDoItems = new ArrayList<>(); // construct a new arraylist for listview
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pager.getAdapter().notifyDataSetChanged(); //tell pager not to cache the view or problem happens
                // see commit message ea6cfaf97c1ba3a96f55a514e612dc5f78e2da65
            }

            @Override
            protected Void doInBackground(Void... params) {
                TodoListDbHelper mDbHelper = new TodoListDbHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Cursor cursor;
                // depending on the page : inbox, today, or next 7 days to query
                switch(mPage)
                {
                    case 0:
                        cursor = db.rawQuery(
                                "Select "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + ", "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_DONE + ", "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                                + " FROM "
                                + TodoListContract.TodoListEntries.TABLE_NAME
                                , null);
                        break;
                    case 1:
                        cursor = db.rawQuery(
                                "Select "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + ", "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_DONE + ", "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                                + " FROM "
                                + TodoListContract.TodoListEntries.TABLE_NAME
                                + " WHERE "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                                + " BETWEEN "
                                + " date('now') AND date('now', '+1 day') "
                                , null);
                        break;
                    case 2:
                        cursor = db.rawQuery(
                                "Select "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + ", "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_DONE + ", "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                                + " FROM "
                                + TodoListContract.TodoListEntries.TABLE_NAME
                                + " WHERE "
                                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                                + " BETWEEN "
                                + " date('now') AND date('now', '+7 day') "
                                , null);
                        break;
                    default:
                        cursor = null;
                }

                if(cursor.moveToFirst())
                {
                    do {
                        String content = cursor.getString(cursor.getColumnIndex(
                                TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT
                        ));
                        int doneInt = cursor.getInt(cursor.getColumnIndex(
                                TodoListContract.TodoListEntries.COLUMN_NAME_DONE
                        ));
                        String reminderDate = cursor.getString(cursor.getColumnIndex(
                                TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                        ));
                        Boolean done = (doneInt == 1); // because databse doesn't store boolean
                        if (content == null && reminderDate == null) break; // stop before cursor go too far
                        // no reminder date -> construct object with a space
                        if (reminderDate == null)
                            toDoItems.add(new ToDoItem(content, done, " ", false));
                        else toDoItems.add(new ToDoItem(content, done, reminderDate, true));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return null;
            }
        }.execute();
    }

    private void changeColor(int position) {
        switch (position) {
            case 0:
                applyNewColor("#303f9f", "#757de8", "#3f51b5");
                break;
            case 1:
                applyNewColor("#a00037", "#ff5c8d", "#d81b60");
                break;
            case 2:
                applyNewColor("#4b2c20", "#a98274", "#795548");
                break;
            default:
                break;
        }
    }

    // apply new fancy color based on material color tool
    private void applyNewColor (String actionBarColor, String tabStripColor, String indicatorColor) {
        ActionBar actionBar = getSupportActionBar();
        Window window = this.getWindow();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(actionBarColor)));
        window.setStatusBarColor(Color.parseColor(indicatorColor));
        tabStrip.setBackground(new ColorDrawable((Color.parseColor(tabStripColor))));
        tabStrip.setIndicatorColor(Color.parseColor(indicatorColor));
        actionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(actionBarColor)));

    }

    // initiates menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // process menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu_item:
                // start about activity
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.clean_all_done:
                // clean all done tasks then recreate activity
                updateDatabase.removeAllDoneItem(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
