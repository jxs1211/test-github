package xianjie.shen.stuq2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by shen on 2016/6/10.
 */
public class MyImageView extends ImageView
{
    /**
     * 图片的位置
     */
    private int mPosition;

    /**
     * 图片的数量
     */
    private int mNumber;


    public MyImageView(Context context)
    {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs)
    {
        this(context, null, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.my_imageview);
        for (int i = 0; i < array.getIndexCount(); i++)
        {
            int attr = array.getIndex(i);
            switch (array.getIndex(i))
            {
                case R.styleable.my_imageview_iv_position:
                    mPosition = array.getInt(attr, 0);
                    break;
                case R.styleable.my_imageview_iv_num:
                    mNumber = array.getInt(attr, 0);
                    break;

            }
        }
        array.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            //由系统测量
            width = widthSize;
        } else
        {
            //自己测量并设置width
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            //由系统测量得到
            height = heightSize;
        } else
        {
            //自己测量得到
        }

        setMeasuredDimension(width, height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
