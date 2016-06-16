package shen.xianjie.myapplication.CommonAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import shen.xianjie.myapplication.util.ImageLoader;

public class CommonViewHolder
{
    private View mConvertView;
    private LayoutInflater mInflater;
    private static int mPosition;
    private SparseArray<View> mViews;

    public CommonViewHolder(Context context, ViewGroup parent, int layoutId,
                            int position)
    {
        mViews = new SparseArray<View>();
        // holder是复用的，但position不是复用的，所以需要更新position
        mPosition = position;
        mInflater = LayoutInflater.from(context);
        mConvertView = mInflater.inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    ;

    public static CommonViewHolder get(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position)
    {
        if (convertView == null)
        {
            return new CommonViewHolder(context, parent, layoutId, position);
        } else
        {
            return (CommonViewHolder) convertView.getTag();
        }
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    /**
     * 封装设置textView的setText方法
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, CharSequence text)
    {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm)
    {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public CommonViewHolder setImageDrawable(int viewId, Drawable drawable)
    {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public CommonViewHolder setImageURI(int viewId, String uri)
    {
        ImageView view = getView(viewId);
        // 使用类似ImageLoader的第三方开源库
        ImageLoader.getInstance().loadImage(uri,view);
        return this;
    }

    public CommonViewHolder setChecked(int viewId, boolean checked)
    {
        CheckBox view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

//    public CommonViewHolder set(int viewId, int resId)
//
//    {
//        ImageButton imageButton = getView(viewId);
//        imageButton.setImageResource(resId);
//        return this;
//    }
}
