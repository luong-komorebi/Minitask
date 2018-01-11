package redlor.it.minitask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import redlor.it.minitask.Utils.MyDateTimeUtils;
import redlor.it.minitask.Utils.UpdateFirebase;

/**
 * Created by Redlor on 26/11/2017.
 * This Fragment shows the details of an item.
 * In dual pane it is placed next to MainActivity
 */

public class DetailFragment extends Fragment {

    @BindView(R.id.todoInfo)
    TextView todoInfo;
    @BindView(R.id.reminderInfo)
    TextView reminderInfo;
    @BindView(R.id.editTodoBtn)
    FloatingActionButton editTodo;
    @BindView(R.id.deleteTodoBtn)
    FloatingActionButton deleteTodo;

    String content;
    String reminder;
    Boolean hasReminder = true;
    Boolean done = false;
    String mItemId;

    MyDateTimeUtils dateTimeUtils; // util to do stuffs with notification

    // Declare the class with Firebase methods
    UpdateFirebase updateFirebase;

    ToDoItem currentToDoItem;
    private long oldRowId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailFragment() {
    }

    // Prevent dialog dismiss when orientation changes.
    private static void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().beginTransaction().attach(this).commit();
        if (container == null) {
            return null;
        }
        // If the is rotated, get the existing data
        if (savedInstanceState != null) {
            content = savedInstanceState.getString("currentContent");
            reminder = savedInstanceState.getString("currentReminder");
            hasReminder = savedInstanceState.getBoolean("currenthasReminder");
            done = savedInstanceState.getBoolean("currentDone");
            mItemId = savedInstanceState.getString("currentItemId");

            // Otherwise get the data from the DetailActivity
        } else {
            Bundle bundle = new Bundle();
            bundle = getArguments();
            if (bundle != null) {
                content = bundle.getString("content");
                reminder = bundle.getString("reminder");
                hasReminder = bundle.getBoolean("hasReminder");
                done = bundle.getBoolean("done");
                mItemId = bundle.getString("itemId");
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        dateTimeUtils = new MyDateTimeUtils();
        assignComponents();

        return rootView;
    }

    private void assignComponents() {
        // update UI with the content taken from intent
        todoInfo.setText(content);

        if (hasReminder)
            reminderInfo.setText(reminder);
        else reminderInfo.setText(getString(R.string.not_found));
        // if edit button is press fire add activity with a little tweak

        if (content == null) {
            deleteTodo.setClickable(false);
            editTodo.setClickable(false);
        } else {
            // if delete is pressed then delete item in database and also remove object for notifydatsetchanged
            deleteTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_menu_delete)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.delete_single_task_message)
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(getActivity(), R.string.item_deleted, Toast.LENGTH_SHORT).show();

                                    ToDoItem toDoItem = new ToDoItem(content, done, reminder, hasReminder, mItemId);
                                    updateFirebase = new UpdateFirebase();
                                    updateFirebase.deleteItem(toDoItem);

                                    Activity a = getActivity();
                                    if (a != null) {
                                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

                                        //   getActivity().finish();

                                        // This intent is for the dual pane mode, to refresh the UI
                                        a.startActivity(new Intent(getContext(), MainActivity.class));
                                        a.overridePendingTransition(0, 0);
                                    } else {
                                        getActivity().getSupportFragmentManager().isDestroyed();
                                    }

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                }
                            }).create();
                    alertDialog.show();
                    doKeepDialog(alertDialog);
                }


                   /* Toast.makeText(getActivity(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                    ToDoItem toDoItem = new ToDoItem(content, done, reminder, hasReminder, mItemId);

                    // remove existing scheduled notification if existed
                    if (!reminder.equals(" "))
                        dateTimeUtils.cancelScheduledNotification(dateTimeUtils.getNotification(content, getContext()),
                                getContext(), (int) oldRowId);

                    // Instantiate a new UpdateFirebase class
                    updateFirebase = new UpdateFirebase();
                    // **New** Delete the item from Firebase Database
                    updateFirebase.deleteItem(toDoItem);
                    getActivity().finish();

                    // This intent is for the dual pane mode, to refresh the UI
                    getActivity().startActivity(new Intent(v.getContext(), MainActivity.class));
                    getActivity().overridePendingTransition(0, 0);*/

            });

            editTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddTodoItem.class);
                    intent.putExtra("item", currentToDoItem);
                    intent.putExtra("content", content);
                    intent.putExtra("reminder", reminder);
                    intent.putExtra("hasReminder", hasReminder);
                    intent.putExtra("done", done);
                    intent.putExtra("id", mItemId);
                    getActivity().finish();
                    startActivity(intent);
                }
            });
        }
    }

    // Save the current state
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString("currentContent", content);
        currentState.putString("currentReminder", reminder);
        currentState.putBoolean("currenthasReminder", hasReminder);
        currentState.putBoolean("currentDone", done);
        currentState.putString("currentItemId", mItemId);

    }
}
