package luongvo.com.todolistminimal.Adapters;

/**
 * Created by Hp on 13/12/2017.
 */

/**//*public class FirebaseAdapter extends FirebaseRecyclerAdapter<ToDoItem, FirebaseAdapter.ToDoViewHolder> {
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = firebaseUser.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("toDoItems");


    *//**//**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     *//**//*
    public FirebaseAdapter(FirebaseRecyclerOptions<ToDoItem> options, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        super(options);
        this.mClickListener = onItemClickListener;
        this.mLongClickListener = onItemLongClickListener;

    }

    @Override
    protected void onBindViewHolder(final ToDoViewHolder viewHolder, final int position, final ToDoItem toDoItem) {
        //   final ToDoItem toDoItem = mToDoItemList.get(position);

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

        viewHolder.checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    toDoItem.setDone(true);
                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    toDoItem.setDone(false);


                    viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });


                }




    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(view);
    }*//*



    public  class FirebaseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.todoContent)
        TextView content;
        @BindView(R.id.checkDone)
        CheckBox checkDone;
        @BindView(R.id.clockReminder)
        ImageView clockReminder;



        public FirebaseViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mClickListener.onItemLongClick(view, getAdapterPosition());
                    return true;
                }
            });
        }

        private FirebaseViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(FirebaseViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }


    }

}*/
