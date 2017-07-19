package luongvo.com.todolistminimal;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import luongvo.com.todolistminimal.Adapters.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager) ViewPager pager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.descriptImage) ImageView descriptImage;

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
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void changeColor(int position) {
        switch (position) {
            case 0:
                applyNewColor("#303f9f", "#757de8", "#3f51b5");
                break;
            case 1:
                applyNewColor("#a00037", "#ff5c8d", "#d81b60");
                break;
            case 2:
                applyNewColor("#4b2c20", "#a98274", "#795548");
                break;
            default:
                Log.d("shit", "Not found");
        }
    }

    private void applyNewColor (String actionBarColor, String tabStripColor, String indicatorColor) {
        ActionBar actionBar = getSupportActionBar();
        Window window = this.getWindow();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(actionBarColor)));
        window.setStatusBarColor(Color.parseColor(indicatorColor));
        tabStrip.setBackground(new ColorDrawable((Color.parseColor(tabStripColor))));
        tabStrip.setIndicatorColor(Color.parseColor(indicatorColor));

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
