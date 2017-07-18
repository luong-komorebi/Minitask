package luongvo.com.todolistminimal;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.Adapters.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager) ViewPager pager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    private void initializeComponents() {
        ButterKnife.bind(this);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
