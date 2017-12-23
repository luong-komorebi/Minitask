package luongvo.com.todolistminimal.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import luongvo.com.todolistminimal.PageFragment;
import luongvo.com.todolistminimal.TodayFragment;
import luongvo.com.todolistminimal.WeekFragment;

/**
 * Created by luongvo on 18/07/2017.
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final int PAGE_COUNT = 3;
    private String[] tabTitles = new String[]{"Inbox", "Today", "Next 7 days"};  // titles for 3 tabs

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PageFragment pageFragment = new PageFragment();
                return pageFragment;
            case 1:
                TodayFragment todayFragment =  new TodayFragment();
                return todayFragment;
            case 2:
                WeekFragment weekFragment = new WeekFragment();
                return weekFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
