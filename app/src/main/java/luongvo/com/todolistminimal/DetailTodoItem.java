package luongvo.com.todolistminimal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTodoItem extends AppCompatActivity {

    @BindView(R.id.todoInfo) TextView todoInfo;
    @BindView(R.id.reminderInfo) TextView reminderInfo;
    @BindView(R.id.editTodoBtn) FloatingActionButton editTodo;

    String content;
    String reminder;
    Boolean hasReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_todo_item);
        initComponents();
        getDataFromIntent();
        assignComponents();
    }

    private void initComponents() {
        ButterKnife.bind(this);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        reminder = intent.getStringExtra("reminder");
        hasReminder = intent.getExtras().getBoolean("hasReminder");
    }


    private void assignComponents() {
        todoInfo.setText(content);
        if (hasReminder)
            reminderInfo.setText(reminder);
        else reminderInfo.setText(getString(R.string.not_found));
        editTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddTodoItem.class);
                startActivity(intent);
            }
        });
    }
}
