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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

    private static class ViewHolder {
        TextView content;
        CheckBox checkDone;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDoItem toDoItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(resourceID, parent, false);
            viewHolder.checkDone = (CheckBox) convertView.findViewById(R.id.checkDone);
            viewHolder.content = (TextView) convertView.findViewById(R.id.todoContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(toDoItem.getContent());
        viewHolder.checkDone.setChecked(toDoItem.getDone());
        return convertView;
    }
}
