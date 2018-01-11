package redlor.it.minitask.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import redlor.it.minitask.R;


/**
 * Created by Hp on 08/12/2017.
 */

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    final LinearLayout.LayoutParams params;
    private final LinearLayout layout;
    @BindView(R.id.taskList)
    public LinearLayout taskList;
    @BindView(R.id.todoContent)
    public
    TextView content;
    @BindView(R.id.checkDone)
    public
    CheckBox checkDone;
    @BindView(R.id.clockReminder)
    public
    ImageView clockReminder;
    private FirebaseViewHolder.ClickListener mClickListener;

    public FirebaseViewHolder(final View view) {
        super(view);
        ButterKnife.bind(this, view);
        layout = (LinearLayout) itemView.findViewById(R.id.taskList);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

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

    public void setOnClickListener(FirebaseViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;


    }

    public void LayoutHide() {
        params.height = 0;
        layout.setLayoutParams(params);
    }

    //Interface to send callbacks...
    public interface ClickListener {
        public void onItemClick(View view, int position);

        public void onItemLongClick(View view, int position);
    }

}
