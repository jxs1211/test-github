package xianjie.shen.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.Iterator;
import java.util.List;

public class ViewPagerIndicator extends LinearLayout
{
  private static final int COLOR_TEXT_NORMAL = 2013265919;
  private static final int COLOR_TEXT_SELECTED = -1;
  private static int DEFAULT_VISIABLE_TAB_COUNT = 0;
  public static final String PAINT_COLOR = "#ffffffff";
  public static final int RADIO_RECT_HEIGHT = 40;
  private static final float RADIO_RECT_WIDTH = 0.5F;
  private static final float RADIO_TRIANGLE_WIDTH = 0.125F;
  public static final int RECT_INDICATOR = 1;
  public static final int TRIANGLE_INDICATOR = 0;
  private final int TRIANGLE_WIDTH_MAX = (int)(getScreenWidth() / 3 * 0.125F);
  private int mCustomIndicatorId;
  private int mCustomIndicatorTranslationX;
  private int mInitCustomIndicatorTranslationX;
  private PageChangedListener mListener;
  private ViewPager mPager;
  private Paint mPaint;
  private Path mPath;
  private int mRectHeight;
  private int mRectWidth;
  private int mTabVisiableCount;
  private int mTabWidth;
  private List<String> mTitles;
  private int mTriangleHeight;
  private int mTriangleWidth;

  public ViewPagerIndicator(Context paramContext)
  {
    this(paramContext, null);
    Log.e("shen", "ViewPagerIndicator(context)");
  }

  public ViewPagerIndicator(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Log.e("shen", "ViewPagerIndicator(context, attrs)");
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.indicator);
    int i = 0;
    while (i < paramContext.getIndexCount())
    {
      this.mTabVisiableCount = paramContext.getInteger(0, DEFAULT_VISIABLE_TAB_COUNT);
      i += 1;
    }
    paramContext.recycle();
    if (this.mTabVisiableCount == 0)
      this.mTabVisiableCount = DEFAULT_VISIABLE_TAB_COUNT;
    this.mPaint = new Paint();
    this.mPaint.setAntiAlias(true);
    this.mPaint.setColor(getResources().getColor(2131230751));
    this.mPaint.setStyle(Style.FILL);
    this.mPaint.setPathEffect(new CornerPathEffect(5.0F));
  }

  public ViewPagerIndicator(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet);
    Log.e("shen", "ViewPagerIndicator(context, attrs)");
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.indicator);
    int i = 0;
    while (i < paramContext.getIndexCount())
    {
      this.mTabVisiableCount = paramContext.getInteger(0, DEFAULT_VISIABLE_TAB_COUNT);
      i += 1;
    }
    paramContext.recycle();
    if (this.mTabVisiableCount == 0)
      this.mTabVisiableCount = DEFAULT_VISIABLE_TAB_COUNT;
    this.mPaint = new Paint();
    this.mPaint.setAntiAlias(true);
    this.mPaint.setColor(getResources().getColor(2131230751));
    this.mPaint.setStyle(Style.FILL);
    this.mPaint.setPathEffect(new CornerPathEffect(5.0F));
    this.mCustomIndicatorId = paramInt;
  }

  private View generateTextView(String paramString)
  {
    TextView localTextView = new TextView(getContext());
    LayoutParams localLayoutParams = new LayoutParams(-1, -1);
    localLayoutParams.width = (getScreenWidth() / this.mTabVisiableCount);
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setText(paramString);
    localTextView.setGravity(17);
    localTextView.setTextColor(getResources().getColor(2131230783));
    localTextView.setTextSize(2, 16.0F);
    return localTextView;
  }

  private int getScreenWidth()
  {
    WindowManager localWindowManager = (WindowManager)getContext().getSystemService("window");
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
    return localDisplayMetrics.widthPixels;
  }

  private void initRect()
  {
    this.mRectHeight = (this.mRectWidth / 40);
    this.mPath = new Path();
    this.mPath.moveTo(0.0F, 0.0F);
    this.mPath.lineTo(this.mRectWidth, 0.0F);
    this.mPath.lineTo(this.mRectWidth, -this.mRectHeight);
    this.mPath.lineTo(0.0F, -this.mRectHeight);
    this.mPath.close();
  }

  private void initTriangle()
  {
    Log.e("shen", "initTriangle");
    this.mTriangleHeight = (this.mTriangleWidth / 2);
    this.mPath = new Path();
    this.mPath.moveTo(0.0F, 0.0F);
    this.mPath.lineTo(this.mTriangleWidth, 0.0F);
    this.mPath.lineTo(this.mTriangleWidth / 2, -this.mTriangleHeight);
    this.mPath.close();
  }

  private void resetAllTabView()
  {
    int j = getChildCount();
    int i = 0;
    while (i < j)
    {
      ((TextView)getChildAt(i)).setTextColor(getResources().getColor(2131230783));
      i += 1;
    }
  }

  private void setTabVisibleCount(int paramInt)
  {
    this.mTabVisiableCount = paramInt;
  }

  private void updateUI()
  {
    if (Looper.getMainLooper() == Looper.myLooper())
    {
      invalidate();
      return;
    }
    postInvalidate();
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    Log.e("shen", "dispatchDraw");
    Log.e("shen", "canvas.getHeight():" + paramCanvas.getHeight() + " " + "canvas.getWidth():" + paramCanvas.getWidth());
    paramCanvas.save();
    paramCanvas.translate(this.mInitCustomIndicatorTranslationX + this.mCustomIndicatorTranslationX, getHeight() + 2);
    paramCanvas.drawPath(this.mPath, this.mPaint);
    paramCanvas.restore();
    super.dispatchDraw(paramCanvas);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    Log.e("shen", "onDraw");
    super.onDraw(paramCanvas);
  }

  protected void onFinishInflate()
  {
    Log.e("shen", "onFinishInflate");
    super.onFinishInflate();
    int j = getChildCount();
    Log.e("shen", "getChildCount：" + getChildCount());
    if (j == 0)
      return;
    int i = 0;
    while (i < j)
    {
      View localView = getChildAt(i);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      localLayoutParams.weight = 0.0F;
      localLayoutParams.width = (getScreenWidth() / this.mTabVisiableCount);
      localView.setLayoutParams(localLayoutParams);
      i += 1;
    }
    setTabViewOnClickEvent();
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Log.e("shen", "indicator onLayout");
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    Log.e("shen", "Indicator onMeasure");
    super.onMeasure(paramInt1, paramInt2);
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    Log.e("shen", "Indicator onRestoreInstanceState");
    super.onRestoreInstanceState(paramParcelable);
  }

  protected Parcelable onSaveInstanceState()
  {
    Log.e("shen", "Indicator onSaveInstanceState");
    return super.onSaveInstanceState();
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Log.e("shen", "onSizeChanged");
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    switch (this.mCustomIndicatorId)
    {
    default:
      return;
    case 0:
      this.mTriangleWidth = ((int)(paramInt1 / this.mTabVisiableCount * 0.125F));
      this.mTriangleWidth = Math.min(this.mTriangleWidth, this.TRIANGLE_WIDTH_MAX);
      this.mInitCustomIndicatorTranslationX = (paramInt1 / this.mTabVisiableCount / 2 - this.mTriangleWidth / 2);
      initTriangle();
      return;
    case 1:
    }
    this.mRectWidth = ((int)(paramInt1 / this.mTabVisiableCount * 0.5F));
    this.mInitCustomIndicatorTranslationX = (paramInt1 / this.mTabVisiableCount / 2 - this.mRectWidth / 2);
    initRect();
  }

  public void scroll(int paramInt, float paramFloat)
  {
    Log.e("shen", "Indicator scroll");
    this.mTabWidth = (getWidth() / this.mTabVisiableCount);
    Log.e("shen", "Indicator mTabWidth:" + this.mTabWidth);
    this.mCustomIndicatorTranslationX = ((int)(this.mTabWidth * paramFloat + this.mTabWidth * paramInt));
    if ((paramInt - (this.mTabVisiableCount - 2) >= 0) && (paramFloat > 0.0F) && (this.mTabVisiableCount < getChildCount()))
    {
      if (this.mTabVisiableCount == 1)
        break label148;
      scrollTo((paramInt - (this.mTabVisiableCount - 2)) * this.mTabWidth + (int)(this.mTabWidth * paramFloat), 0);
    }
    while (true)
    {
      updateUI();
      Log.e("shen", "Indicator updateUI");
      return;
      label148: scrollTo(this.mTabWidth * paramInt + (int)(this.mTabWidth * paramFloat), 0);
    }
  }

  public void setPageChangedListener(PageChangedListener paramPageChangedListener)
  {
    this.mListener = paramPageChangedListener;
  }

  public void setTabItem(List<String> paramList, int paramInt1, int paramInt2)
  {
    Log.e("shen", " setTabItem");
    this.mCustomIndicatorId = paramInt2;
    if ((paramList != null) && (paramList.size() > 0))
    {
      if (paramInt1 <= 0)
        setTabVisibleCount(DEFAULT_VISIABLE_TAB_COUNT);
      while (true)
      {
        removeAllViews();
        this.mTitles = paramList;
        paramList = this.mTitles.iterator();
        while (paramList.hasNext())
          addView(generateTextView((String)paramList.next()));
        setTabVisibleCount(paramInt1);
      }
    }
    setTabViewOnClickEvent();
  }

  public void setTabViewOnClickEvent()
  {
    int j = getChildCount();
    final int i = 0;
    while (i < j)
    {
      getChildAt(i).setOnClickListener(new OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          ViewPagerIndicator.this.setTabViewSelected(i);
          ViewPagerIndicator.this.mPager.setCurrentItem(i, false);
        }
      });
      i += 1;
    }
  }

  public void setTabViewSelected(int paramInt)
  {
    resetAllTabView();
    ((TextView)getChildAt(paramInt)).setTextColor(getResources().getColor(2131230751));
  }

  public void setViewPager(ViewPager paramViewPager, int paramInt)
  {
    this.mPager = paramViewPager;
    this.mPager.setOnPageChangeListener(new OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt)
      {
        if (ViewPagerIndicator.this.mListener != null)
          ViewPagerIndicator.this.mListener.onPageScrollStateChanged(paramAnonymousInt);
      }

      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
      {
        Log.e("shen", " onPageScrolled");
        Log.e("shen", " position：" + paramAnonymousInt1 + " " + "positionOffset:" + paramAnonymousFloat);
        ViewPagerIndicator.this.scroll(paramAnonymousInt1, paramAnonymousFloat);
        if (ViewPagerIndicator.this.mListener != null)
          ViewPagerIndicator.this.mListener.onPageScrolled(paramAnonymousInt1, paramAnonymousFloat, paramAnonymousInt2);
      }

      public void onPageSelected(int paramAnonymousInt)
      {
        Log.e("shen", " onPageSelected");
        Log.e("shen", " position：" + paramAnonymousInt);
        ViewPagerIndicator.this.setTabViewSelected(paramAnonymousInt);
        if (ViewPagerIndicator.this.mListener != null)
          ViewPagerIndicator.this.mListener.onPageSelected(paramAnonymousInt);
      }
    });
    this.mPager.setCurrentItem(paramInt);
  }

  public static abstract interface PageChangedListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);

    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);

    public abstract void onPageSelected(int paramInt);
  }
}

/* Location:           E:\tools\apktool\dex2jar-2.0\classes-dex2jar.jar
 * Qualified Name:     shen.xianjie.myapplication.ViewPagerIndicator
 * JD-Core Version:    0.6.2
 */