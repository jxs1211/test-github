package xianjie.shen.weixin60;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 踩过的坑：
 * 坑：Paint.setColor一定要放在Paint.setAlpha的前面
 */
public class TabView extends View
{

    private static final String INSTANCE_STATUS = "instance_status";
    private static final String ALPHA_STATUS = "alpha_status";
    private Bitmap mIcon;
    private int mColor = 0xF25A8E;
    private String mText = "微信";
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mIconBitmap;//创建内存的mBitmap(选择需要透明度的config)
    private float mAlpha;
    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;

    public TabView(Context context)
    {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    /**
     * 获取自定义属性的值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TabView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        Log.e("shen", "TabView(Context context, AttributeSet attrs, int defStyleAttr)");
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.tab_view, defStyleAttr, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.tab_view_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a
                            .getDrawable(attr);
                    mIcon = drawable.getBitmap();
                    break;
                case R.styleable.tab_view_color:
                    mColor = a.getColor(attr, 0xF25A8E);// F25A8E
                    break;
                case R.styleable.tab_view_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.tab_view_text_size:
                    mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                            getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#ff555555"));// 初始为灰色
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    /**
     * 1、在内存中新建bitmap并绘制颜色
     * 2、设置Xfermodes为dstin(该模式将后发进来的对象与步骤1的颜色重叠，并只显示重叠的部分)
     * 3、将mIcon绘制上去 最后得到内存中的一个只有icon部分有颜色的bitmap
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Log.e("shen", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取icon在View上可用于绘制的区域
        int drawableIconWidth = getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight();
        int drawableIconHeight = getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTextBound.height();
        //icon的边长，应该取宽高上可用区域的最小值
        int iconWidth = Math.min(drawableIconWidth, drawableIconHeight);//取大值无法放下并完整显示整个icon，text也无法显示
        // 设置icon的Rect
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        //画草图可见，（iconWidth+Text）/2的是整个view的中心点，再用getMeasuredHeight/2-这个值就得到了icon显示的top值（注意与view中心点对齐的是icon+text的整体中心点）
        int top = getMeasuredHeight() / 2 - (mTextBound.height() + iconWidth)
                / 2;
        int right = left + iconWidth;
        int bottom = top + iconWidth;
        // 确定绘制icon的区域
        mIconRect = new Rect(left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.e("shen", "onDraw");
        // super.onDraw(canvas);
        // 将mIcon绘制到onMeasure计算的mIconRect区域上(原图绘制)，可以看见icon正常显示
        //1 绘制原icon到canvas
        canvas.drawBitmap(mIcon, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        Log.e("shen", "alpha " + alpha);
        //2 在内存中绘制可变色的一个mIconBitmap并着色，mIconBitmap和canvas一样大，在canvas画icon的相同区域绘制一个着色的icon
        setupTargetBitmap(alpha);
        //3 绘制原text到canvas
        drawtext(canvas, alpha);
        //4 绘制着色后的text到canvas
        drawTargetText(canvas, alpha);
        //5 绘制mIconBitmap(其内容仅带有着色的icon)到canvas（mIconBitmap与canvas尺寸一致，并覆盖iconRect区域的原icon变成可变色的icon，text部分没有内容所以不覆盖，text的内容可以正常显示）
        canvas.drawBitmap(mIconBitmap, 0, 0, null);
    }

    private void drawTargetText(Canvas canvas, int alpha)
    {
        //坑：切记要先设置颜色后设置透明度，因为mColor包括了透明度，如果先设透明度再设颜色，mColor的透明度会覆盖alpha
        //坑：Paint.setColor一定要放在Paint.setAlpha的前面
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        float x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        float y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawtext(Canvas canvas, int alpha)
    {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        float x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        float y = mIconRect.bottom + mTextBound.height();
//        float y = mIconRect.bottom;
        canvas.drawText(mText, x, y, mTextPaint);

    }

    /**
     * 在内存绘制可变色的bitmap
     * 背景就是着色的mIconRect
     * 新画上去的图片 可以理解为前景就是src
     * PorterDuff.Mode.DST_IN表示取色为src、dst两层绘制的交集
     *
     * @param alpha
     */
    private void setupTargetBitmap(int alpha)
    {
        // 1创建内存的mBitmap(选择需要透明度的config)
        mIconBitmap = Bitmap.createBitmap(getMeasuredWidth(),
                getMeasuredHeight(), Config.ARGB_8888);
        //mIconBitmap for mCanvas to draw into
        mCanvas = new Canvas(mIconBitmap);
        // 2拿到onMeasure获得的Icon的Rect
        // 3在Rect上着色
        mPaint = new Paint();
        Log.e("shen", "setupTargetBitmap " + alpha);
        //坑：Paint.setColor一定要放在Paint.setAlpha的前面
        mPaint.setColor(mColor);
        mPaint.setAlpha(alpha);//参数alpha为透明度，取值范围为0~255，数值越小越透明
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mCanvas.drawRect(mIconRect, mPaint);//给mIconRect区域着色（dst）
        // 4设置Xformodes为dstin
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // 5在已着色的mIconRect区域，用Xfermode为DST_IN的mPaint画icon
//        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIcon, null, mIconRect, mPaint);//(src)
    }

    public void setViewColorAlpha(float alpha)
    {
//        Log.e("shen", "setViewColorAlpha invalidateView");
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView()
    {
        if (Looper.getMainLooper() == Looper.myLooper())
        {
            invalidate();
        } else
        {
            postInvalidate();
        }
    }

    /**
     * 如果要保存自己自定义的状态，切记不要忘了也要保存系统自己维护的状态
     */
    @Override
    protected Parcelable onSaveInstanceState()
    {
        Log.e("shen", "onSaveInstanceState");
        Bundle bundle = new Bundle();
//		保存系统要保持的状态
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
//		保存自定义要保持的状态
        bundle.putFloat(ALPHA_STATUS, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        Log.e("shen", "onRestoreInstanceState");
        if (state instanceof Bundle)
        {
//			恢复在onSaveInstanceState()中保持的系统状态
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(INSTANCE_STATUS));
//			恢复在onSaveInstanceState()中保持的自定义状态
            mAlpha = ((Bundle) state).getFloat(ALPHA_STATUS);
        } else
        {
            super.onRestoreInstanceState(state);
        }
    }
}
