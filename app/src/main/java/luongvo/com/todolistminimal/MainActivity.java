package luongvo.com.todolistminimal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luongvo.com.todolistminimal.Adapters.MyFragmentPagerAdapter;
import luongvo.com.todolistminimal.Adapters.TodoListAdapter;
import luongvo.com.todolistminimal.Database.TodoListContract;
import luongvo.com.todolistminimal.Database.TodoListDbHelper;

import static luongvo.com.todolistminimal.PageFragment.toDoItems;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager) ViewPager pager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.descriptImage) ImageView descriptImage;
    @BindView(R.id.actionButton) FloatingActionButton actionButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openAndQueryDb(0);
        initializeComponents();
    }

    private void initializeComponents() {
        ButterKnife.bind(this);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(pager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
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
                Intent intent = new Intent(v.getContext(), AddTodoItem.class);
                startActivity(intent);
            }
        });
    }

    private void openAndQueryDb(final int mPage) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pager.getAdapter().notifyDataSetChanged();
            }

            @Override
            protected Void doInBackground(Void... params) {
                toDoItems =new ArrayList<>();
                TodoListDbHelper mDbHelper = new TodoListDbHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Cursor cursor;
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
                        Log.d("This is ", "0");
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
                        Log.d("This is ", "1");
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
                        Log.d("This is ", "2");
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
                                TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT
                        ));
                        String reminderDate = cursor.getString(cursor.getColumnIndex(
                                TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                        ));
                        Boolean done = (doneInt == 1);
                        if (content == null && reminderDate == null) break;
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
                Log.d("shit", "Not found");
        }
    }

    private void applyNewColor (String actionBarColor, String tabStripColor, String indicatorColor) {
        ActionBar actionBar = getSupportActionBar();
        Window window = this.getWindow();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(actionBarColor)));
        window.setStatusBarColor(Color.parseColor(indicatorColor));
        tabStrip.setBackground(new ColorDrawable((Color.parseColor(tabStripColor))));
        tabStrip.setIndicatorColor(Color.parseColor(indicatorColor));
        actionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(actionBarColor)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
