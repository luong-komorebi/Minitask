package luongvo.com.todolistminimal.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.R;
import luongvo.com.todolistminimal.ToDoItem;
import luongvo.com.todolistminimal.Utils.UpdateDatabase;

/**
 * Created by luongvo on 19/07/2017.
 */

/*
 * This is an adapter for todolist listview.
 * This class is not used in this pull. Redlor 26/11/2017
 */
public class TodoListAdapter extends ArrayAdapter<ToDoItem> {

    // a util to do update stuffs in the database
    UpdateDatabase updateUtil = new UpdateDatabase();
    private Context context;
    private int resourceID;

    // initiate the adapter with context, resourceID and todoItems.
    public TodoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ToDoItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceID = resource;
    }

    // Override GetView to manipulate UI
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ToDoItem toDoItem = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(resourceID, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // set the content of the item
        viewHolder.content.setText(toDoItem.getContent());
        // set the checkbox status of the item
        viewHolder.checkDone.setChecked(toDoItem.getDone());
        // check if checkbox is checked, then strike through the text
        // this is for the first time UI render
        if (viewHolder.checkDone.isChecked())
            viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        // render the clock icon if the item has a reminder
        if (toDoItem.getHasReminder())
            viewHolder.clockReminder.setVisibility(View.VISIBLE);
        else
            viewHolder.clockReminder.setVisibility(View.INVISIBLE);
        // if the checkbox is checked, we need to set the done status of a todoitem to true and
        // then update that item status in the database and strike through the text.
        // Note that this is not first time UI render. User is in our app and
        // manipulate the checkbox so we need to update things on clicked.
        viewHolder.checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    toDoItem.setDone(true);
                    updateUtil.updateDoneInDatabase(toDoItem.getContent(), toDoItem.getReminderDate(),
                            toDoItem.getDone(), toDoItem.getItemId(), context);

                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    toDoItem.setDone(false);
                    updateUtil.updateDoneInDatabase(toDoItem.getContent(), toDoItem.getReminderDate(),
                            toDoItem.getDone(), toDoItem.getItemId(), context);
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });


        return convertView;
    }

    /* A viewholder for later cache in listview. Implemented with Butterknife
     * Basically there are 3 elements in each item of the list:
     * Text content
     * A checkbox to check done or not
     * A clock icon to show if the item has a reminder or not
     */
    static class ViewHolder {
        @BindView(R.id.todoContent)
        TextView content;
        @BindView(R.id.checkDone)
        CheckBox checkDone;
        @BindView(R.id.clockReminder)
        ImageView clockReminder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
