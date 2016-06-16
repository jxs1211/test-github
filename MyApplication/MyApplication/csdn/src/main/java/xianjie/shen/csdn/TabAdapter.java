package xianjie.shen.csdn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by shen on 2016/6/5.
 */
public class TabAdapter extends FragmentPagerAdapter
{
    public static final String[] TITLES = new String[]{"业界", "移动开发", "云计算", "软件研发", "程序员"};

    public TabAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        MainFragment fragment = new MainFragment(position);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return TITLES[position];
    }
}
