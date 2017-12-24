package luongvo.com.todolistminimal;

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

import java.util.ArrayList;
import java.util.HashMap;

import luongvo.com.todolistminimal.Adapters.RecyclerViewAdapter;
import luongvo.com.todolistminimal.Utils.SimpleDividerItemDecoration;
import luongvo.com.todolistminimal.viewholder.FirebaseViewHolder;

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

    DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    public static ArrayList<ToDoItem> toDoItems;
    private View view;

    FirebaseRecyclerAdapter  mFirebaseAdapter;

    private Toast mToast;


    // each tab is a fragment. this function make a new instance of each when view created.
    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        // get int to decide what page to render.
   //     args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
     //   fragment.setArguments(args);
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println(mTwoPane);
            //    toDoItems = new ArrayList<>();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = firebaseUser.getUid();

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("toDoItems");
                final Query query = FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(uid)
                        .child("toDoItems")
                        .limitToLast(50);

                FirebaseRecyclerOptions<ToDoItem> options =
                        new FirebaseRecyclerOptions.Builder<ToDoItem>()
                                .setQuery(query, ToDoItem.class)
                                .build();

// mDatabaseReference.keepSynced(true);

                mFirebaseAdapter = new FirebaseRecyclerAdapter<ToDoItem, FirebaseViewHolder> (options
                ) {

                    @Override
                    public FirebaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.todo_item, parent, false);
                        return new FirebaseViewHolder(view);
                    }


                    @Override
                    protected void onBindViewHolder(final FirebaseViewHolder viewHolder, final int position, final ToDoItem toDoItem) {
                        // set the content of the item
                        viewHolder.content.setText(toDoItem.getContent());
                        // set the checkbox status of the item
                        viewHolder.checkDone.setChecked(toDoItem.getDone());
                        // check if checkbox is checked, then strike through the text
                        // this is for the first time UI render
                if (viewHolder.checkDone.isChecked()) {
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
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
                        System.out.println("id "+ id);

                        if (b) {
                            toDoItem.setDone(true);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("done", true);
                            mDatabaseReference.child(id).updateChildren(map);
                            viewHolder.checkDone.setOnCheckedChangeListener(null);
                            // mDatabaseReference.child(id).setValue(toDoItem);
                            //   mFirebaseAdapter.notifyDataSetChanged();
                            //     mDatabaseReference.child(id).child("done").setValue(true);
                        }
                        else {
                            toDoItem.setDone(false);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("done", false);
                            mDatabaseReference.child(id).updateChildren(map);
                            viewHolder.checkDone.setOnCheckedChangeListener(null);

                        }
                           /* HashMap<String, Object> map = new HashMap<>();
                            map.put("done", false);
                            mDatabaseReference.child(id).updateChildren(map);*/
                        // mFirebaseAdapter.notifyDataSetChanged();
                        //  mDatabaseReference.child(id).setValue(toDoItem);
                        //   mDatabaseReference.child(id).child("done").setValue(false);


                      /*  if (b) {
                            doneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String key = dataSnapshot.getKey();
                                    System.out.println("chiave " + key);
                                    //    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    if (key.equals(id)) {
                                        mDatabaseReference.child(id).child("done").setValue(true);
                                        doneQuery.removeEventListener(this);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            doneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String key = dataSnapshot.getKey();
                                    System.out.println("chiave " + key);
                                    //    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    if (key.equals(id)) {
                                        mDatabaseReference.child(id).child("done").setValue(false);
                                        doneQuery.removeEventListener(this);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            }
*/



                                /*addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Toast.makeText(getActivity(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
                                    String id = mFirebaseAdapter.getRef(position).getKey();
                                    System.out.println("id "+ id);
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        if (dataSnapshot1.getRef().getKey().equals(id)) {
                                            System.out.println("datasnap "+ dataSnapshot1.getRef().getKey());
                                            if (b) {
                                             //   toDoItem.setDone(true);
                                           //     viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                mDatabaseReference.child(id).child("done").setValue(true);
                                                query.removeEventListener(this);
                                            } else {
                                            //    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                              //  toDoItem.setDone(false);
                                                mDatabaseReference.child(id).child("done").setValue(false);
                                                query.removeEventListener(this);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
*/
                        // mDatabaseReference.addListenerForSingleValueEvent(valueEventListener);


                         /*   String key = mFirebaseAdapter.getRef(position).getKey();
                            mDatabaseReference.child(key).child("done").setValue(true);
                            System.out.println("chiave "+ key);*/
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
                            System.out.println("id in page fragment: " + itemId);
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
                            System.out.println("id in page fragment: " + itemId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        boolean hasReminder = toDoItem.getHasReminder();
                        if (hasReminder) {
                            String reminder = toDoItem.getReminderDate();
                            mToast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.reminder_info) + ": " + reminder, Toast.LENGTH_SHORT);
                            mToast.show();
                        } else {
                            mToast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_reminder), Toast.LENGTH_SHORT);
                            mToast.show();
                        }
                    }
                });
            }
        };


        mRecyclerView = (RecyclerView) view.findViewById(R.id.to_do_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mRecyclerView.setAdapter(mFirebaseAdapter);




      /*  mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //    toDoItems.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    ToDoItem toDoItem = dsp.getValue (ToDoItem.class);
                    toDoItems.add(toDoItem);
                    System.out.println("called page fragment");
                    System.out.println("list" + toDoItems);
                }
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
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });*/



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


 /*       mRecyclerView = view.findViewById(R.id.to_do_list);
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

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("toDoItems");
        mDatabaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        ToDoItem toDoItem = dataSnapshot.getValue(ToDoItem.class);
                        toDoItems.add(toDoItem);
                        mRecyclerView.scrollToPosition(toDoItems.size() - 1);
                        mAdapter.notifyItemInserted(toDoItems.size() - 1);
                    } catch (Exception e) {
                        Log.e("PageFragment", e.getMessage());
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ToDoItem toDoItem = dataSnapshot.getValue(ToDoItem.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ToDoItem toDoItem = dataSnapshot.getValue(ToDoItem.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                ToDoItem toDoItem = dataSnapshot.getValue(ToDoItem.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


         }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        //  fragmentPagerAdapter.notifyDataSetChanged();
    }


}
