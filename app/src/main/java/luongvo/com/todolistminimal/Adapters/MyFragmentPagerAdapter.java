package luongvo.com.todolistminimal.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import luongvo.com.todolistminimal.PageFragment;

/**
 * Created by luongvo on 18/07/2017.
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final int PAGE_COUNT = 3;
    private String[] tabTitles = new String[] {"Inbox", "Today", "Next 7 days"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return PageFragment.newInstance(0);
            case 1: return PageFragment.newInstance(1);
            case 2: return PageFragment.newInstance(2);
            default: return null;
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
