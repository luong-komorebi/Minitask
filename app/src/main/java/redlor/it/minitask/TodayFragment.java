package redlor.it.minitask;

import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import redlor.it.minitask.Utils.SimpleDividerItemDecoration;
import redlor.it.minitask.viewholder.FirebaseViewHolder;

import static redlor.it.minitask.MainActivity.mTwoPane;


public class TodayFragment extends Fragment {


    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter mFirebaseAdapter;
    Date today;
    String day;
    String myDay;
    private RecyclerView mRecyclerView;
    private View view;
    private Toast mToast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the tab view with these fragments
        view = inflater.inflate(R.layout.fragment_today, container, false);
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            firebaseLoadToday();

        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            mFirebaseAdapter.stopListening();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void firebaseLoadToday() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = firebaseUser.getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("toDoItems");
        mDatabaseReference.keepSynced(true);

        final Query query = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("toDoItems")
                .limitToLast(50);

        final FirebaseRecyclerOptions<ToDoItem> options =
                new FirebaseRecyclerOptions.Builder<ToDoItem>()
                        .setQuery(query, ToDoItem.class)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ToDoItem, FirebaseViewHolder>(options
        ) {
            @Override
            public FirebaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todo_item, parent, false);
                return new FirebaseViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(final FirebaseViewHolder viewHolder, final int position, final ToDoItem toDoItem) {
                boolean hasReminder = toDoItem.getHasReminder();
                Calendar calendar = Calendar.getInstance();
                today = calendar.getTime();
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                myDay = myFormat.format(today);
                String completeTime = toDoItem.getReminderDate();
                if (completeTime != null && !completeTime.equals(" ")) {
                    String[] parts = completeTime.split(" ");
                    day = parts[0];
                }

                if (hasReminder && myDay.equals(day)) {
                    boolean todayB = true;
                    System.out.println(myDay + " " + day);
                    // set the content of the item
                    viewHolder.content.setText(toDoItem.getContent());
                    // set the checkbox status of the item
                    viewHolder.checkDone.setChecked(toDoItem.getDone());
                    // check if checkbox is checked, then strike through the text
                    // this is for the first time UI render
                    if (viewHolder.checkDone.isChecked()) {
                        viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                    }
                    // render the clock icon if the item has a reminder
                    if (toDoItem.getHasReminder())
                        viewHolder.clockReminder.setVisibility(View.VISIBLE);
                    else
                        viewHolder.clockReminder.setVisibility(View.INVISIBLE);

                    viewHolder.checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                            final String id = mFirebaseAdapter.getRef(position).getKey();

                            if (b) {
                                toDoItem.setDone(true);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("done", true);
                                mDatabaseReference.child(id).updateChildren(map);
                                viewHolder.checkDone.setOnCheckedChangeListener(null);

                            } else {
                                toDoItem.setDone(false);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("done", false);
                                mDatabaseReference.child(id).updateChildren(map);
                                viewHolder.checkDone.setOnCheckedChangeListener(null);

                            }
                        }
                    });

                    viewHolder.setOnClickListener(new FirebaseViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            final String itemId = mFirebaseAdapter.getRef(position).getKey();
                            if (mTwoPane) {
                                DetailFragment newDetailFragment = new DetailFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Bundle bundle = new Bundle();
                                bundle.putString("content", toDoItem.getContent());
                                bundle.putString("reminder", toDoItem.getReminderDate());
                                bundle.putBoolean("hasReminder", toDoItem.getHasReminder());
                                bundle.putBoolean("done", toDoItem.getDone());
                                bundle.putString("itemId", itemId);
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
                                intent.putExtra("itemId", itemId);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                            if (mToast != null) {
                                mToast.cancel();
                            }
                            String reminder = toDoItem.getReminderDate();
                            mToast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.reminder_info) + " " + reminder, Toast.LENGTH_LONG);
                            mToast.show();
                        }
                    });


                } else {
                    viewHolder.LayoutHide();
                }

            }
        };

        mRecyclerView = (RecyclerView) view.findViewById(R.id.to_do_list_today);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.startListening();
    }
}
