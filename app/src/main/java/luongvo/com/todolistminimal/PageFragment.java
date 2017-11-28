package luongvo.com.todolistminimal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import luongvo.com.todolistminimal.Adapters.RecyclerViewAdapter;
import luongvo.com.todolistminimal.Utils.SimpleDividerItemDecoration;

import static luongvo.com.todolistminimal.MainActivity.mTwoPane;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";
    public static ArrayList<ToDoItem> toDoItems;
    DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private View view;

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
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the tab view with these fragments
        view = inflater.inflate(R.layout.fragment_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Before
     /*   fragmentPagerAdapter = new TodoListAdapter(getActivity().getApplicationContext(), R.layout.todo_item, toDoItems);
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
        });*/

        System.out.println(mTwoPane);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //After, applying RecyclerView and OnLongItemClickListener
        mRecyclerView = (RecyclerView) view.findViewById(R.id.to_do_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mAdapter = new RecyclerViewAdapter(getContext(), R.layout.todo_item, toDoItems,
                new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ToDoItem toDoItem) {
                        if (mTwoPane) {
                            DetailFragment newDetailFragment = new DetailFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            Bundle bundle = new Bundle();
                            bundle.putString("content", toDoItem.getContent());
                            bundle.putString("reminder", toDoItem.getReminderDate());
                            bundle.putBoolean("hasReminder", toDoItem.getHasReminder());
                            bundle.putBoolean("done", toDoItem.getDone());
                            bundle.putString("itemId", toDoItem.getItemId());
                            newDetailFragment.setArguments(bundle);

                            fragmentManager.beginTransaction()
                                    .replace(R.id.details_container, newDetailFragment)
                                    .detach(newDetailFragment)
                                    .attach(newDetailFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                        } else {
                            Intent intent = new Intent(getContext(), DetailActivity.class);
                            intent.putExtra("content", toDoItem.getContent());
                            intent.putExtra("reminder", toDoItem.getReminderDate());
                            intent.putExtra("hasReminder", toDoItem.getHasReminder());
                            intent.putExtra("done", toDoItem.getDone());
                            intent.putExtra("itemId", toDoItem.getItemId());
                            startActivity(intent);
                        }
                    }
                }, new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(final ToDoItem toDoItem) {
                // TO DO
                return true;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        //  fragmentPagerAdapter.notifyDataSetChanged();
    }
}
