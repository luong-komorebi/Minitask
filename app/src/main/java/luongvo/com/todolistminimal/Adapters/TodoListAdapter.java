package luongvo.com.todolistminimal.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.R;
import luongvo.com.todolistminimal.ToDoItem;

/**
 * Created by luongvo on 19/07/2017.
 */

public class TodoListAdapter extends ArrayAdapter<ToDoItem> {

    private Context context;
    private int resourceID;

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
        ToDoItem toDoItem = getItem(position);
        ViewHolder viewHolder;
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
        if (toDoItem.getHasReminder())
            viewHolder.clockReminder.setVisibility(View.VISIBLE);
        else
            viewHolder.clockReminder.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
