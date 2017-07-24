package luongvo.com.todolistminimal.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.R;
import luongvo.com.todolistminimal.ToDoItem;
import luongvo.com.todolistminimal.Utils.UpdateDatabase;

/**
 * Created by luongvo on 19/07/2017.
 */

public class TodoListAdapter extends ArrayAdapter<ToDoItem> {

    private Context context;
    private int resourceID;
    UpdateDatabase updateUtil = new UpdateDatabase();

    public TodoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ToDoItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceID = resource;
    }

    static class ViewHolder {
        @BindView(R.id.todoContent) TextView content;
        @BindView(R.id.checkDone) CheckBox checkDone;
        @BindView(R.id.clockReminder) ImageView clockReminder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

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

        viewHolder.content.setText(toDoItem.getContent());
        viewHolder.checkDone.setChecked(toDoItem.getDone());
        if (viewHolder.checkDone.isChecked())
            viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        if (toDoItem.getHasReminder())
            viewHolder.clockReminder.setVisibility(View.VISIBLE);
        else
            viewHolder.clockReminder.setVisibility(View.INVISIBLE);
        viewHolder.checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Before : ", "" + toDoItem.getDone());
                if (isChecked) {
                    toDoItem.setDone(true);
                    updateUtil.updateDoneInDatabase(toDoItem.getContent(), toDoItem.getReminderDate(),
                            toDoItem.getDone(), context);
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    toDoItem.setDone(false);
                    updateUtil.updateDoneInDatabase(toDoItem.getContent(), toDoItem.getReminderDate(),
                            toDoItem.getDone(), context);
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        return convertView;
    }
}
