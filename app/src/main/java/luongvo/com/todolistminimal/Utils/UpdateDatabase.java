package luongvo.com.todolistminimal.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import luongvo.com.todolistminimal.Database.TodoListContract;
import luongvo.com.todolistminimal.Database.TodoListDbHelper;

/**
 * Created by luongvo on 24/07/2017.
 */

// This class contains every helper function that relates to updating the database
public class UpdateDatabase {
    // this func marks an item done status in the database
    public void updateDoneInDatabase(final String content, final String reminder,
                                     final Boolean done, final Context context) {
        // because database doesn't have boolean type
        final int doneInt = (done) ? 1 : 0;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... params) {
                TodoListDbHelper mDbHelper = new TodoListDbHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursor;
                // if no reminder found then query sentence is a little different
                if (reminder.equals(" "))
                    cursor = db.rawQuery(
                            "UPDATE "
                            + TodoListContract.TodoListEntries.TABLE_NAME
                            + " SET "
                            + TodoListContract.TodoListEntries.COLUMN_NAME_DONE
                            + " = "
                            + doneInt
                            + " WHERE "
                            + TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + " = "
                            + " '" + content +"' "
                            + " AND "
                            + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                            + " IS NULL"
                            , null);
                else cursor = db.rawQuery(
                        "UPDATE "
                        + TodoListContract.TodoListEntries.TABLE_NAME
                        + " SET "
                        + TodoListContract.TodoListEntries.COLUMN_NAME_DONE
                        + " = "
                        + doneInt
                        + " WHERE "
                        + TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + " = "
                        + " '" + content +"' "
                        + " AND "
                        + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE + " = "
                        + " '" + reminder +"' "
                        , null);
                cursor.moveToFirst();
                cursor.close();
                return null;
            }
        }.execute();
    }

    // this function remove an item in the database. Use case : delete on clicked
    public long removeInDatabase(final String content, final String reminder, final Context context) {
        String whereClause1 = TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + " = "
                + " '" + content +"' "
                + " AND "
                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE
                + " IS NULL";
        // if reminder is found then query sentence is a bit different
        String whereClause2 = TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT + " = "
                + " '" + content +"' "
                + " AND "
                + TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE + " = "
                + " '" + reminder +"' ";
        TodoListDbHelper mDbHelper = new TodoListDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (reminder.equals(" "))
            return db.delete(TodoListContract.TodoListEntries.TABLE_NAME, whereClause1, null);
        else
            return db.delete(TodoListContract.TodoListEntries.TABLE_NAME, whereClause2, null);
    }
}
