package luongvo.com.todolistminimal.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import luongvo.com.todolistminimal.Database.TodoListContract;
import luongvo.com.todolistminimal.Database.TodoListDbHelper;
import luongvo.com.todolistminimal.MainActivity;
import luongvo.com.todolistminimal.R;

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

    // add item to database given every info
    public long addItemToDatabase(String content, Boolean done, String reminderDate, Context context) {
        TodoListDbHelper dbHelper = new TodoListDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // when insert into database, also construct a new object for notifydatasetchanged()
        values.put(TodoListContract.TodoListEntries.COLUMN_NAME_CONTENT, content);
        values.put(TodoListContract.TodoListEntries.COLUMN_NAME_DONE, done);
        if (reminderDate.equals(" ")) // no reminder = reminder is null
            values.putNull(TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE);
        else  //  with reminder
            values.put(TodoListContract.TodoListEntries.COLUMN_NAME_REMINDERDATE, reminderDate);
        // insert into a row in database
        return db.insert(TodoListContract.TodoListEntries.TABLE_NAME, null, values);
    }

    // this function remove all done item in database
    public void removeAllDoneItem(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.cleaning));
        // async task to delete done item
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // dismiss dialog and recreate activity after delete
                progressDialog.dismiss();
                ((Activity)context).recreate(); // get activity method from context by cast
            }

            @Override
            protected Void doInBackground(Void... params) {
                // delete all with done status = 1
                String whereClause = TodoListContract.TodoListEntries.COLUMN_NAME_DONE + " = 1";
                TodoListDbHelper mDbHelper = new TodoListDbHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.delete(TodoListContract.TodoListEntries.TABLE_NAME, whereClause, null);
                return null;
            }
        }.execute();
    }
}
