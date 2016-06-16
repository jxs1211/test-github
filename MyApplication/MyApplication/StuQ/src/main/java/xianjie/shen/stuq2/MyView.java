package xianjie.shen.stuq2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shen on 2016/6/11.
 */
public class MyView extends View
{
    private int mPosition;
    private int mNumber;
    private Paint mPaint;

    public MyView(Context context)
    {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(defStyleAttr, R.styleable.my_view);

        for (int i = 0; i < array.getIndexCount(); i++)
        {
            int attr = array.getIndex(i);
            switch (attr)
            {
                case R.styleable.my_view_num:
                    mNumber = array.getInt(attr, 0);
                    break;
                case R.styleable.my_view_position:
                    mPosition = array.getInt(attr, 0);
                    break;
            }
        }
        initPaint();
    }

    private void initPaint()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * view初始化的时候，会被多次调用
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * view初始化的时候，会被多次调用
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//        canvas.drawBitmap(bitmap,0,1,mPaint);
    }
}
