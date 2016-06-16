package xianjie.shen.weixin60;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;

/**
 * 踩过的坑：
 * 坑：4.4.2默认设置OverflowButton为可见，无需再手动调用此方法设置
 * 坑：这里用true的话，tabView的颜色会有异常
 */
public class MainActivity extends FragmentActivity implements OnClickListener,
        OnPageChangeListener
{
    private Button mButton;                                            // 测试使用
    private boolean is;                                                // 测试使用
    private ViewPager mPager;
    private TabFragment mFragment;
    private FragmentPagerAdapter mAdapter;
    private TabView mTabView1;
    private TabView mTabView2;
    private TabView mTabView3;
    private TabView mTabView4;
    private List<TabView> mTabViews = new ArrayList<TabView>();
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] strings = new String[]{
            "Fragment1!", "Fragment2!", "Fragment3!", "Fragment4!"};
    private View mOverFlowButton;
    private static final int mOverFlowButtonId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 坑：4.4.2默认设置OverflowButton为可见，无需再手动调用此方法设置
//        setOverflowButtonAlwaysVisiable();
        // 设置不显示actionBar左侧icon（Home is presented as either an activity icon or
        // logo.）
        getActionBar().setDisplayShowHomeEnabled(false);
        Log.e("shen", is + "--defaultValue");

        initViews();
        initDatas();
        initEvents();

        // mPager.setCurrentItem(0, false);

    }

    private void initEvents()
    {
        mTabView1.setOnClickListener(this);
        mTabView2.setOnClickListener(this);
        mTabView3.setOnClickListener(this);
        mTabView4.setOnClickListener(this);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);
    }

    private void initDatas()
    {
        for (String s : strings)
        {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, s);
            tabFragment.setArguments(bundle);
            mTabs.add(tabFragment);
        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return strings.length;
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mTabs.get(arg0);
            }
        };
    }

    private void initViews()
    {
        mPager = (ViewPager) findViewById(R.id.id_viewPager);
        mTabView1 = (TabView) findViewById(R.id.id_tabView01);
        mTabView2 = (TabView) findViewById(R.id.id_tabView02);
        mTabView3 = (TabView) findViewById(R.id.id_tabView03);
        mTabView4 = (TabView) findViewById(R.id.id_tabView04);
        mButton = (Button) findViewById(R.id.id_button);
        mButton.setOnClickListener(this);
        mTabViews.add(mTabView1);
        mTabViews.add(mTabView2);
        mTabViews.add(mTabView3);
        mTabViews.add(mTabView4);
        // 不要使用mTabView1.setViewColorAlpha(1.0f);
        mTabViews.get(0).setViewColorAlpha(1.0f);
        // resetAllView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.e("xian", "item.getItemId()" + item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置optionMenuItem的icon为可见
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        // featureId The panel that the menu is in.
        // menu The menu that is opened.
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder"))
            {// Menu是接口类型，所以要找到它的实现类也就是MenuBuilder
                try
                {
                    // 切记用getDeclaredMethod而不是getMethod
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    Log.e("shen", "invoke");
                    m.invoke(menu, true);
                } catch (Exception e)
                {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowButtonAlwaysVisiable()
    {
        // 得到当前Context的ViewConfiguration里面有很多关于View的设置，通过sHasPermanentMenuKey设置OverflowButton是否可见
        ViewConfiguration config = ViewConfiguration.get(this);
        try
        {
            // 注意是getDeclaredField不是getField
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);//true为隐藏，false为可见
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View arg0)
    {
        // 将tabView点击切换单独提取出来作为一个clickTab(arg0)方法，为了防止如果再加个button点击启动新的activity后，再回到tabView界面时之前点击的tabView状态不能恢复，而又恢复到第一个tabView着色的状态
        switch (arg0.getId())
        {
            case R.id.id_button:
                // 其它非tabView的控件的onClick事件在这里执行
                Intent intent = new Intent(this, SecActivity.class);
                startActivity(intent);
                break;
            case mOverFlowButtonId:
                break;
            default:
                clickTab(arg0);
                break;
        }
    }

    private void clickTab(View arg0)
    {
        resetAllView();
        switch (arg0.getId())
        {
            case R.id.id_tabView01:
                // 把resetAllView()放到每个tabView的case里，而不放在switch外面，是为了防止如果再加个button点击启动新的activity后，再回到tabView界面时之前点击的tabView状态不能恢复，而又恢复到第一个tabView着色的状态
                mTabViews.get(0).setViewColorAlpha(1.0f);
                // 坑：这里用true的话，tabView的颜色会有异常
                mPager.setCurrentItem(0, false);
                break;
            case R.id.id_tabView02:
                // resetAllView();
                mTabViews.get(1).setViewColorAlpha(1.0f);
                mPager.setCurrentItem(1, false);
                break;
            case R.id.id_tabView03:
                // resetAllView();
                mTabViews.get(2).setViewColorAlpha(1.0f);
                mPager.setCurrentItem(2, false);
                break;
            case R.id.id_tabView04:
                // resetAllView();
                mTabViews.get(3).setViewColorAlpha(1.0f);
                mPager.setCurrentItem(3, false);
                break;
        }
    }

    private void resetAllView()
    {
        for (int i = 0; i < mTabViews.size(); i++)
        {
            mTabViews.get(i).setViewColorAlpha(0);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    /**
     * 从右往左划时（offset值[0~1]）
     * 从左往右划时（offset值[1~0]）
     * mTabViews.get(position)为左边的tabView
     * mTabViews.get(position+1)为右边的tabView
     *
     * @param position
     * @param offset
     * @param offsetPixels
     */
    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels)
    {
        if (offset > 0)
        {
            //第一页到第二页postion=0，offset[0~1]
            mTabViews.get(position).setViewColorAlpha(1 - offset);
            //第二页到第一页postion=0，offset[1~0]
            mTabViews.get(position + 1).setViewColorAlpha(offset);
            //无论是第一页到第二页还是第二页到第一页position不变的，所以通过get(position)拿到左边的tab，通过get(position+1)拿到右边的tab
            //第一页到第二页offset[0~1]，所以通过offset设置左边的tab从有色渐变到透明，设置右边的tab从透明到有色
            //第二页到第一页offset[1~0]，所以通过offset设置左边的tab从透明渐变到有色，设置右边的tab从有色到透明
        }
    }

    @Override
    public void onPageSelected(int arg0)
    {

    }

}
