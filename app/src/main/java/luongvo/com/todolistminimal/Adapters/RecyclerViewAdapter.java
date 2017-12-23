package luongvo.com.todolistminimal.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.R;
import luongvo.com.todolistminimal.ToDoItem;
import luongvo.com.todolistminimal.Utils.UpdateDatabase;

/**
 * Created by Redlor on 26/11/2017.
 * This class build the RecyclerView adapter for the list of To Dos
 * It contains two interfaces for click and long click
 * When an item is check or unchecked, the value is passed to Firebase Database
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = firebaseUser.getUid();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("toDoItems");

    // a util to do update stuffs in the database
    UpdateDatabase updateUtil = new UpdateDatabase();
    private Context mContext;
    private int mResourceID;
    private ArrayList<ToDoItem> mToDoItemList;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    // Constructor
    public RecyclerViewAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<ToDoItem> toDoItemList, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        super();
        this.mContext = context;
        this.mResourceID = resource;
        this.mToDoItemList = toDoItemList;
        this.mListener = listener;
        this.mLongListener = longClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ToDoItem toDoItem = mToDoItemList.get(position);
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


                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            String id = dsp.getKey();
                            System.out.println("id " + id);
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
              /*  if (b) {
                    toDoItem.setDone(true);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("done", true);
                    mDatabaseReference.child(id).updateChildren(map);
                    viewHolder.checkDone.setOnCheckedChangeListener(null);
                    // mDatabaseReference.child(id).setValue(toDoItem);
                    //   mFirebaseAdapter.notifyDataSetChanged();
                    //     mDatabaseReference.child(id).child("done").setValue(true);
                } else {
                    toDoItem.setDone(false);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("done", false);
                    mDatabaseReference.child(id).updateChildren(map);
                    viewHolder.checkDone.setOnCheckedChangeListener(null);
*/
                }
      /*  // set the content of the item
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

        viewHolder.checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    toDoItem.setDone(true);
                    updateUtil.updateDoneInDatabase(toDoItem.getContent(), toDoItem.getReminderDate(),
                            toDoItem.getDone(), toDoItem.getItemId(), mContext);
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    // **New** Change done value in Firebase Database, set true
                  //  Query query = databaseReference.equalTo(toDoItem.getItemId().);


                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                ToDoItem toDoItem1 = dataSnapshot1.getValue(ToDoItem.class);

                                String key = dataSnapshot1.getRef().getKey();
                              //  System.out.println("key " + key);
                                String id = dataSnapshot1.getRef().getKey();

                                    databaseReference.child(key).child("done").setValue(true);

                           //     System.out.println("todoitem1 " + toDoItem1 + id);
                             //   dataSnapshot1.getRef().child("done").setValue(isChecked);
                             //   System.out.println(databaseReference.child(id));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                *//*    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("datasnapshot " + dataSnapshot);
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                dataSnapshot1.getRef().child("done").setValue(true);
                            }
System.out.println(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
*//*
                } else {
                    toDoItem.setDone(false);
                    updateUtil.updateDoneInDatabase(toDoItem.getContent(), toDoItem.getReminderDate(),
                            toDoItem.getDone(), toDoItem.getItemId(), mContext);

                    // **New** Change done value in Firebase Database, set false
           *//*         Query query = databaseReference.equalTo(toDoItem.getItemId());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                dataSnapshot1.getRef().child("done").setValue(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*//*
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });*/

            });
                viewHolder.bind(mToDoItemList.get(position), mListener, mLongListener);

    }

    @Override
    public int getItemCount() {
        return mToDoItemList.size();
    }

    // Create an onItemClickListener for the RecyclerView
    public interface OnItemClickListener {
        void onItemClick(ToDoItem toDoItem);
    }

    // Create an OnItemLongClickListener for the RecyclerView
    public interface OnItemLongClickListener {
        boolean onItemLongClicked(ToDoItem toDoItem);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.todoContent)
        TextView content;
        @BindView(R.id.checkDone)
        CheckBox checkDone;
        @BindView(R.id.clockReminder)
        ImageView clockReminder;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        // Binf the click listener to the view
        void bind(final ToDoItem toDoItem, final OnItemClickListener listener, final OnItemLongClickListener longClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(toDoItem);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClicked(toDoItem);
                    return true;
                }
            });
        }
    }


}
