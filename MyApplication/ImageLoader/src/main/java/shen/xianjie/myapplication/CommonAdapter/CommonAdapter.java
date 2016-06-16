package shen.xianjie.myapplication.CommonAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import shen.xianjie.myapplication.util.ImageLoader;
import shen.xianjie.myapplication.R;

public abstract class CommonAdapter<T> extends BaseAdapter
{
    //每次同时加载THREAD_COUNT张图片
    protected LayoutInflater mInflater;
    protected List<T> mDatas;
    protected Context mContext;
    protected int mLayoutId;

    public CommonAdapter(Context context, List datas, int layoutId)
    {
//        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mContext = context;
        this.mLayoutId = layoutId;
    }

    public CommonAdapter(Context context, List datas, int layoutId, String dirPath)
    {
        this(context, datas, layoutId);
//        this.mDirPath = dirPath;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return mDatas.get(arg0);
    }

    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CommonViewHolder holder = CommonViewHolder.get(mContext, convertView,
                parent, mLayoutId, position);

        convert(holder, position);
        //按Lifo的策略，每次同时加载THREAD_COUNT张图片
//        ImageLoader.getInstance(THREAD_COUNT, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mDatas.get(position), (ImageView) holder.getView(R.id.iv_item_img));
        return holder.getConvertView();
    }

    public void convert(CommonViewHolder holder, int position)
    {
//        holder.setImageResource(R.id.iv_item_img, R.drawable.ic_menu_add)
//                .setImageResource(R.id.ib_select, R.drawable.c);
//        ImageLoader.getInstance(THREAD_COUNT, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mDatas.get(position), (ImageView) holder.getView(R.id.iv_item_img));
    }
}
