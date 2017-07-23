package luongvo.com.todolistminimal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.Adapters.MyFragmentPagerAdapter;
import luongvo.com.todolistminimal.Adapters.TodoListAdapter;
import luongvo.com.todolistminimal.Database.TodoListContract;
import luongvo.com.todolistminimal.Database.TodoListDbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

    private int mPage;
    @BindView(R.id.todoList) ListView todoList;
    TodoListAdapter fragmentPagerAdapter;
    public static ArrayList<ToDoItem> toDoItems;
    TodoListDbHelper mDbHelper;

    private static final String ARG_PAGE = "ARG_PAGE";

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        openAndQueryDb();
    }

    private void openAndQueryDb() {
        toDoItems = new ArrayList<>();
        mDbHelper = new TodoListDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "Select "
                + TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + ", "
                + TodoListContract.TodoListEntries.COLUMN_NAME_DONE + ", "
                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                + " FROM "
                + TodoListContract.TodoListEntries.TABLE_NAME
                , null);
        if (cursor.moveToFirst()) {
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentPagerAdapter = new TodoListAdapter(getActivity().getApplicationContext(), R.layout.todo_item, toDoItems);
        todoList.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentPagerAdapter.notifyDataSetChanged();
    }
}
