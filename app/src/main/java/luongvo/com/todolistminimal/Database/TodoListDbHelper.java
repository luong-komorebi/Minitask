package luongvo.com.todolistminimal.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luongvo on 20/07/2017.
 */


// This class is for database use. Reference https://developer.android.com/training/basics/data-storage/databases.html
public class TodoListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyTodoList.db";
    // Our table has 4 attributes
    // Id, content, done status and reminderdate
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoListContract.TodoListEntries.TABLE_NAME + " (" +
                    TodoListContract.TodoListEntries._ID + " INTEGER PRIMARY KEY," +
                    TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + " TEXT," +
                    TodoListContract.TodoListEntries.COLUMN_NAME_DONE + " TEXT," +
                    TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE + " TEXT," +
                    // Declared the Firebase Id column. Redlor
                    TodoListContract.TodoListEntries.COLUMN_NAME_ID + " TEXT NOT NULL);";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoListContract.TodoListEntries.TABLE_NAME;

    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
