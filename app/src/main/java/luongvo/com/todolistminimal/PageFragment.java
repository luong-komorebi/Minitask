package luongvo.com.todolistminimal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.Adapters.TodoListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

    @BindView(R.id.todoList) ListView todoList;

    TodoListAdapter fragmentPagerAdapter;
    public static ArrayList<ToDoItem> toDoItems;

    private static final String ARG_PAGE = "ARG_PAGE";

    // each tab is a fragment. this function make a new instance of each when view created.
    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        // get int to decide what page to render.
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the tab view with these fragments
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // an adapter for the list view
        fragmentPagerAdapter = new TodoListAdapter(getActivity().getApplicationContext(), R.layout.todo_item, toDoItems);
        todoList.setAdapter(fragmentPagerAdapter);
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // open the detail of each item if clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailTodoItem.class);
                ToDoItem item = toDoItems.get(position);
                intent.putExtra("content", item.getContent());
                intent.putExtra("reminder", item.getReminderDate());
                intent.putExtra("hasReminder", item.getHasReminder());
                intent.putExtra("done", item.getDone());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentPagerAdapter.notifyDataSetChanged();
        // important. see commit message ea6cfaf97c1ba3a96f55a514e612dc5f78e2da65
    }
}
