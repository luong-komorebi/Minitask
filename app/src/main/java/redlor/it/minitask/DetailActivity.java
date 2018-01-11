package redlor.it.minitask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Redlor on 26/11/2017.
 * This class replaces @ DetailTodoItem in order to implement a fragment for Dual Pane mode
 */

public class DetailActivity extends AppCompatActivity {

    String content;
    String reminder;
    Boolean hasReminder;
    Boolean done;
    String mItemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(getString(R.string.detail));

        if (savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            getDataFromIntent();

            // Send data to the DetailFragment
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            bundle.putString("reminder", reminder);
            bundle.putBoolean("hasReminder", hasReminder);
            bundle.putBoolean("done", done);
            bundle.putString("itemId", mItemId);
            detailFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.details_container, detailFragment)
                    .commit();

        }
    }

    private void getDataFromIntent() {
        // because we go from main activity to here
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        reminder = intent.getStringExtra("reminder");
        hasReminder = intent.getExtras().getBoolean("hasReminder");
        done = intent.getExtras().getBoolean("done");
        mItemId = intent.getExtras().getString("itemId");
    }
}
