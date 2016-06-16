package xianjie.shen.csdn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity
{
    private TabPageIndicator mIndicator;
    private ViewPager mPager;
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews();
        initDatas();
        initEvents();

    }


    private void initViews()
    {
        mIndicator = (TabPageIndicator) findViewById(R.id.vpi_indicator);
        mPager = (ViewPager) findViewById(R.id.vp_main_content);
    }

    private void initDatas()
    {
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
    }

    private void initEvents()
    {
        mIndicator.setViewPager(mPager, 0);//设置indicator根据mPager监听的滑动事件而滑动自己
    }
}
