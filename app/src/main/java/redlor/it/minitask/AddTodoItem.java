package redlor.it.minitask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import redlor.it.minitask.Utils.MyDateTimeUtils;
import redlor.it.minitask.Utils.UpdateFirebase;

public class AddTodoItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Bind components
    @BindView(R.id.todoEditText)
    MaterialEditText materialTextInput;
    @BindView(R.id.buttonSetDate)
    Button buttonSetDate;
    @BindView(R.id.buttonSetTime)
    Button buttonSetTime;
    @BindView(R.id.reminderSwitch)
    Switch reminderSwitch;
    @BindView(R.id.reminderText)
    TextView reminderText;
    @BindView(R.id.addTodoBtn)
    FloatingActionButton addTodoBtn;


    ToDoItem toDoItem;
    MyDateTimeUtils dateTimeUtils;

    // Declaring the class with Firebase methods
    UpdateFirebase updateFirebase;

    String content;
    String date;
    String time;

    // Old content for edit function
    String oldContent = "";
    String oldReminder = "";
    String oldItemId = "";
    boolean oldHasReminder;
    boolean oldDone;
    boolean existingData;

    // New variable to store Firebase Id
    String mItemId;

    // rowID after adding into database
    private long newRowId;

    // rowID after deleting from database
    private long oldRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_item);
        initializeComponents();
        existingData = loadDataIfExist();

    }

    // attach view with controllers
    private void initializeComponents() {
        getSupportActionBar().setTitle(R.string.add_todo_item);
        ButterKnife.bind(this);

        dateTimeUtils = new MyDateTimeUtils();
        date = "";
        time = "";

        // Instantiate a new UpdateFirebase class
        updateFirebase = new UpdateFirebase();

        // If switch is checked then reveal data and time picker
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    buttonSetDate.setVisibility(View.GONE);
                    buttonSetTime.setVisibility(View.GONE);
                    reminderText.setVisibility(View.GONE);
                    reminderText.setText(getString(R.string.reminder_set_at));
                    date = ""; // reset date time field
                    time = "";
                } else {
                    buttonSetDate.setVisibility(View.VISIBLE);
                    buttonSetTime.setVisibility(View.VISIBLE);
                    reminderText.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddTodoItem.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.show(getFragmentManager(), "DatepickerDialog");
            }
        });

        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = dateTimeUtils.fillDateIfEmpty(date);
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddTodoItem.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.vibrate(true);
                tpd.dismissOnPause(true);
                tpd.show(getFragmentManager(), "TimepickerDialog");
            }
        });

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate all input field
                if (!validateAllInput())
                    return;
                // get content input from user
                content = materialTextInput.getText().toString();
                // no error found, start adding to database

                // this check is used for update/edit a todoItem case
                // If we are editing an existing item, run update method from UpdateFirebase class
                if (existingData) {

                    String newContent = materialTextInput.getText().toString();
                    String newReminderDate = date + " " + time;
                    boolean newHasReminder;
                    if (newReminderDate.equals(" ")) {
                        newHasReminder = false;
                    } else {
                        newHasReminder = true;
                    }
                    updateFirebase.updateItem(newContent, newHasReminder, newReminderDate, oldItemId);
                } else {
                    // If it is a new item, add it to the database
                    addItemToDatabase();
                }

                // schedule a notification if date and time is set
                if (!(date + " " + time).equals(" "))
                    dateTimeUtils.ScheduleNotification(dateTimeUtils.getNotification(content, AddTodoItem.this),
                            AddTodoItem.this, (int) newRowId, date + " " + time);

                // This intent is to refresh the activity when in Dual Pane mode
                Intent intent = new Intent(AddTodoItem.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private Boolean validateAllInput() {
        // check if user chose date but didn't choose time
        if (time.equals("") && !date.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(AddTodoItem.this).create();
            alertDialog.setTitle(getString(R.string.time_error));
            alertDialog.setMessage(getString(R.string.time_error_purpose));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            // user choose our default time
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CHOOSE DEFAULT 9 A.M.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // explicitly call onTimeSet to take advantage of time check
                    onTimeSet(null, 9, 0, 0); // default picked time at 9:00 A.M
                }
            });
            alertDialog.show();
            return false;
        }
        // first validate if the input is empty or not
        if (!materialTextInput.validateWith(
                new RegexpValidator("String must not be empty", "^(?!\\s*$).+"))) {
            Toast.makeText(AddTodoItem.this, "Empty task detected! No task added!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // check if old content is the same as new content
        // if yes then user didn't do any action and should press back rather than add
        if ((oldContent.equals(content) || content == null) && oldReminder.equals(date + " " + time)) {
            finish(); // finish activity instead of returning anything - UX
        }
        // everything is OK.
        return true;
    }

    // load data passed from another activity, return success or not
    // this one is to distinguish between add and edit activity
    private Boolean loadDataIfExist() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) return false;
        // get attributes from intent
        oldContent = extras.getString("content");
        oldReminder = extras.getString("reminder");
        oldHasReminder = extras.getBoolean("hasReminder");
        oldDone = extras.getBoolean("done");
        oldItemId = extras.getString("id");

        materialTextInput.setText(oldContent);
        // stop here if no reminder is set, else continue
        if (oldReminder.equals(" "))
            return true;
        // split string to get specific date and time
        date = oldReminder.split("\\s+")[0];
        time = oldReminder.split("\\s+")[1];
        // change text on screen and make date time picker visible
        reminderText.setText(getString(R.string.reminder_set_at) + " " + date + " " + time);
        reminderSwitch.setChecked(true);
        buttonSetDate.setVisibility(View.VISIBLE);
        buttonSetTime.setVisibility(View.VISIBLE);
        reminderText.setVisibility(View.VISIBLE);
        return true;
    }

    // this function is to add an item into database
    private void addItemToDatabase() {
        // concat date and time
        String reminderDate = date + " " + time;

        // when insert into database, also construct a new object for notifydatasetchanged()
        if (reminderDate.equals(" ")) // no reminder
            toDoItem = new ToDoItem(content, false, reminderDate, false, mItemId);
        else  //  with reminder
            toDoItem = new ToDoItem(content, false, reminderDate, true, mItemId);

        // Add the new item to Firebase Database
        updateFirebase.addItem(toDoItem);

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // if the date is in the past tell user to choose again
        if (dateTimeUtils.checkInvalidDate(year, monthOfYear, dayOfMonth)) {
            AlertDialog alertDialog = new AlertDialog.Builder(AddTodoItem.this).create();
            alertDialog.setTitle("Date not valid!");
            alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
            alertDialog.setMessage("You are selecting a time a point of time in the past!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }

        // set date value to the value user selected and change the text
        date = dateTimeUtils.dateToString(year, monthOfYear, dayOfMonth);
        reminderText.setText(getString(R.string.reminder_set_at) + " " + date + " " + time);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        // if date is not chosen first but time is chosen -> make today the default date.
        // also check for valid time, must be today but not the past hour or minutes.
        if (date.equals(dateTimeUtils.fillDateIfEmpty("")) && dateTimeUtils.checkInvalidTime(hourOfDay, minute)) {
            AlertDialog alertDialog = new AlertDialog.Builder(AddTodoItem.this).create();
            alertDialog.setTitle("Time not valid!");
            alertDialog.setMessage("You are selecting a point of time in the past!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }

        // set time value and update text
        time = dateTimeUtils.timeToString(hourOfDay, minute);
        reminderText.setText(getString(R.string.reminder_set_at) + " " + date + " " + time);
    }


}
