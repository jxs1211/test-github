package xianjie.shen.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xianjie.shen.myapplication.DistrictFragmentItemBean;
import xianjie.shen.myapplication.PopupWindowListViewAdapter;
import xianjie.shen.myapplication.SlideMenuItemBean;
import xianjie.shen.myapplication.SlideMenuPopWindow;
import xianjie.shen.myapplication.TopItemBean;
import xianjie.shen.myapplication.ViewPagerFragment;
import xianjie.shen.myapplication.ViewPagerIndicator;

public class MainActivity extends FragmentActivity
{
    private static final int COLOR_TEXT_NORMAL = 2013265919;
    private static final int COLOR_TEXT_SELECTED = -1;
    private static final int DEFAULT_ANCHORED_GRAVITY = 8388611;
    public static final int FIRST_FRAGMENT = 0;
    public static final int GRAVITY = 3;
    private static final int GRID_LAYOUT = 1;
    public static final String INDICATOR_BACKGROUND_COLOR = "#ffdadada";
    private static final int LISTVIEW_LAYOUT = 2;
    public static final int RECT_INDICATOR = 1;
    public static final int SECOND_FRAGMENT = 1;
    private static final int STAGGERED_GRID_LAYOUT = 0;
    private static final int TAB_VISIALE_COUNT = 2;
    public static final int THIRD_FRAGMENT = 2;
    public static final String TOPBAR_TITLE = "#ff000000";
    public static final int TRIANGLE_INDICATOR = 0;
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private FragmentPagerAdapter mAdapter;
    private List<ViewPagerFragment> mContents = new ArrayList();
    private List<List> mDatas3;
    private List<DistrictFragmentItemBean> mDistrictDatas;
    private TypedArray mDistrictImgs;
    private String[] mDistrictNames;
    private long mExitTime = 0L;
    private String[] mFoodNames;
    private List<DistrictFragmentItemBean> mFoodTypeDatas;
    private TypedArray mFoodTypeImgs;
    private ViewPagerFragment mFragment;
    private ViewPagerIndicator mIndicator;
    private List<SlideMenuItemBean> mSlideMenuItemBeans;
    private String[] mSlideMenuItemDescs;
    private int[] mSlideMenuItemImgs;
    private int[] mSlideMenuItemTags;
    private String[] mSlideMenuItemTitles;
    private SlideMenuPopWindow mSlideMenuPopWindow;
    private List<String> mTitles;
    private String[] mTopAddresses;
    private ImageView mTopBarLeft;
    private ImageView mTopBarRight;
    private List<TopItemBean> mTopDatas;
    private TypedArray mTopImgs;
    private String[] mTopNames;
    private String[] mTopZanCount;
    private RelativeLayout mTopbar;
    private TextView mTopbarTitle;
    private ViewPager mViewPager;

    private boolean continueClickExitApp(int paramInt, KeyEvent paramKeyEvent)
    {
        if ((paramInt == 4) && (paramKeyEvent.getAction() == 0))
        {
            if (System.currentTimeMillis() - this.mExitTime > 2000L)
            {

                Toast.makeText(getApplicationContext(), myapplication.R.string.exit_tips_one_more_time, 0).show();
                this.mExitTime = System.currentTimeMillis();
            }
        } else
            return false;
        finish();
        System.exit(0);
        return true;
    }

    private void initDatas()
    {
        this.mDistrictDatas = new ArrayList();
        this.mFoodTypeDatas = new ArrayList();
        this.mTopDatas = new ArrayList();
        this.mDatas3 = new ArrayList();
        this.mSlideMenuItemBeans = new ArrayList();
        this.mDistrictImgs = getResources().obtainTypedArray(myapplication.R.array.district_item_imgs);
        this.mDistrictNames = getResources().getStringArray(myapplication.R.array.district_names);
        this.mFoodNames = getResources().getStringArray(myapplication.R.array.foodtype_names);
        this.mFoodTypeImgs = getResources().obtainTypedArray(myapplication.R.array.foodtype_imgs);
        this.mTopImgs = getResources().obtainTypedArray(myapplication.R.array.top_imgs);
        this.mTopNames = getResources().getStringArray(myapplication.R.array.top_names);
        this.mTopAddresses = getResources().getStringArray(myapplication.R.array.top_addresses);
        this.mTopZanCount = getResources().getStringArray(myapplication.R.array.top_zan_count);
        int i = 0;

        while (i < this.mDistrictNames.length)
        {
            DistrictFragmentItemBean bean = new DistrictFragmentItemBean(this.mDistrictImgs.getResourceId(i, 0), this.mDistrictNames[i]);
            this.mDistrictDatas.add(bean);
            i += 1;
        }
        i = 0;
        while (i < this.mFoodNames.length)
        {
            DistrictFragmentItemBean bean = new DistrictFragmentItemBean(this.mFoodTypeImgs.getResourceId(i, 0), this.mFoodNames[i]);
            this.mFoodTypeDatas.add(bean);
            i += 1;
        }
        i = 0;
        while (i < this.mTopNames.length)
        {
            TopItemBean bean = new TopItemBean(this.mTopImgs.getResourceId(i, 0), myapplication.R.drawable.map_zan, this.mTopNames[i], this.mDistrictNames[i], this.mTopZanCount[i]);
            this.mTopDatas.add(bean);
            i += 1;
        }
        this.mDatas3.add(0, this.mDistrictDatas);
        this.mDatas3.add(1, this.mFoodTypeDatas);
        this.mDatas3.add(2, this.mTopDatas);
        ViewPagerFragment localObject = ViewPagerFragment.newInstance(this.mDistrictDatas, myapplication.R.layout.district_main, 0);
        ViewPagerFragment localViewPagerFragment = ViewPagerFragment.newInstance(this.mFoodTypeDatas, myapplication.R.layout.district_main, 1);
        this.mContents.add(localObject);
        this.mContents.add(localViewPagerFragment);
        this.mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            public int getCount()
            {
                return MainActivity.this.mContents.size();
            }

            public Fragment getItem(int paramAnonymousInt)
            {
                return (Fragment) MainActivity.this.mContents.get(paramAnonymousInt);
            }
        };
        this.mViewPager.setAdapter(this.mAdapter);
        this.mSlideMenuItemTags = getResources().getIntArray(myapplication.R.array.slide_menu_item_tags);
        this.mSlideMenuItemImgs = getResources().getIntArray(myapplication.R.array.slide_menu_item_imgs);
        this.mSlideMenuItemTitles = getResources().getStringArray(myapplication.R.array.slide_menu_item_titles);
        this.mSlideMenuItemDescs = getResources().getStringArray(myapplication.R.array.slide_menu_item_descs);
        i = 0;
        while (i < this.mSlideMenuItemTags.length)
        {
            SlideMenuItemBean bean = new SlideMenuItemBean(this.mSlideMenuItemTags[i], this.mSlideMenuItemImgs[i], this.mSlideMenuItemTitles[i], this.mSlideMenuItemDescs[i]);
            this.mSlideMenuItemBeans.add(bean);
            i += 1;
        }
    }

    private void initEvents()
    {
        this.mIndicator.setViewPager(this.mViewPager, 0);
        this.mTopBarLeft.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint({"NewApi"})
            public void onClick(View paramAnonymousView)
            {
                if ((MainActivity.this.mSlideMenuPopWindow != null) && (MainActivity.this.mSlideMenuPopWindow.isShowing()))
                {
                    MainActivity.this.mSlideMenuPopWindow.dismiss();
                    return;
                }
                MainActivity.this.changeLightBrightness(0.3F);
            }
        });
    }

    private void initSlideMenuPopWindow()
    {
        this.mSlideMenuPopWindow = new SlideMenuPopWindow(this, this.mSlideMenuItemBeans);
        this.mSlideMenuPopWindow.setAnimationStyle(2131558535);
        this.mSlideMenuPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            public void onDismiss()
            {
                MainActivity.this.changeLightBrightness(1.0F);
            }
        });
        this.mSlideMenuPopWindow.setOnItemClickListener(new SlideMenuPopWindow.OnItemClickListener()
        {
            public void onItemClick(View paramAnonymousView, int paramAnonymousInt, PopupWindowListViewAdapter.MyViewHolder paramAnonymousMyViewHolder)
            {
                Toast.makeText(MainActivity.this, "onItemClick: " + paramAnonymousInt, Toast.LENGTH_SHORT).show();
                MainActivity.this.mSlideMenuPopWindow.dismiss();
            }

            public void onItemLongClick(View paramAnonymousView, int paramAnonymousInt, PopupWindowListViewAdapter.MyViewHolder paramAnonymousMyViewHolder)
            {
                Toast.makeText(MainActivity.this, "onItemLongClick: " + paramAnonymousInt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews()
    {
        this.mTitles = Arrays.asList(getResources().getStringArray(myapplication.R.array.tab_titles));
        this.mViewPager = ((ViewPager) findViewById(myapplication.R.id.vp_content));
        this.mIndicator = ((ViewPagerIndicator) findViewById(myapplication.R.id.vp_indicator));
        this.mTopbar = ((RelativeLayout) findViewById(myapplication.R.id.rl_topbar));
        this.mIndicator.setTabItem(this.mTitles, 2, 0);
        this.mTopbarTitle = ((TextView) findViewById(myapplication.R.id.tv_title));
        this.mTopbarTitle.setText(myapplication.R.string.app_name);
        this.mTopbarTitle.setTextColor(getResources().getColor(myapplication.R.color.text_color_black));
        this.mTopBarLeft = ((ImageView) findViewById(myapplication.R.id.iv_left));
        this.mTopBarLeft.setImageResource(myapplication.R.drawable.ic_menu_add);
        this.mTopBarRight = ((ImageView) findViewById(myapplication.R.id.iv_right));
        this.mTopBarRight.setImageResource(myapplication.R.drawable.ic_menu_delete);
        Log.e("shen", "Activity setTabItem");
    }

    private void setTabViewAndViewPagerSelected(int paramInt)
    {
        this.mIndicator.setTabViewSelected(paramInt);
        this.mViewPager.setCurrentItem(paramInt, false);
    }

    private void showExitAppTip()
    {
        new AlertDialog.Builder(this).setTitle(myapplication.R.string.exit_dialog_title).setMessage(myapplication.R.string.exit_dialog_message).setPositiveButton(myapplication.R.string.exit_dialog_positive_button, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                MainActivity.this.finish();
                System.exit(0);
            }
        }).setNegativeButton(myapplication.R.string.exit_dialog_negative_button, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                paramAnonymousDialogInterface.dismiss();
            }
        }).show();
    }

    public void changeLightBrightness(float alpha)
    {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.alpha = alpha;
        getWindow().setAttributes(localLayoutParams);
    }

    public void finish()
    {
        super.finish();
        overridePendingTransition(this.activityCloseEnterAnimation, this.activityCloseExitAnimation);
    }

    public void onBackPressed()
    {
    }

    protected void onCreate(Bundle paramBundle)
    {
        Log.e("shen", "Activity onCreate");
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(myapplication.R.layout.activity_main_without_textview);
        Log.e("shen", "setContentView");
        initViews();
        initDatas();
        initEvents();
        setTabViewAndViewPagerSelected(0);
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
        if (continueClickExitApp(paramInt, paramKeyEvent))
            return true;
        return super.onKeyDown(paramInt, paramKeyEvent);
    }
}

/* Location:           E:\tools\apktool\dex2jar-2.0\classes-dex2jar.jar
 * Qualified Name:     xianjie.myapplication.MainActivity
 * JD-Core Version:    0.6.2
 */