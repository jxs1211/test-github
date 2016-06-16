package xianjie.shen.csdn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import xianjie.shen.bean.NewsItem;
import xianjie.shen.csdn.R;

/**
 * Created by shen on 2016/6/9.
 */
public class NewsItemAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<NewsItem> mDatas;

    /**
     * 使用github上开源的imageLoader进行数据加载
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public NewsItemAdapter(Context context, List<NewsItem> mDatas)
    {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.images)
                .showImageForEmptyUri(R.drawable.images).showImageOnFail(R.drawable.images)
                .cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    public void addAll(List<NewsItem> mDatas)
    {
        this.mDatas.addAll(mDatas);
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return mDatas.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.news_item_yidong, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.img = (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(mDatas.get(pos).getTitle());
        holder.content.setText(mDatas.get(pos).getContent());
        holder.date.setText(mDatas.get(pos).getDate());
        if (mDatas.get(pos).getImgLink() != null)
        {
            holder.img.setVisibility(View.VISIBLE);
            imageLoader.displayImage(mDatas.get(pos).getImgLink(), holder.img, options);
        } else
        {
            holder.img.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setDatas(List<NewsItem> newsItems)
    {
        this.mDatas.clear();
        this.mDatas.addAll(newsItems);
    }

    private final class ViewHolder
    {
        TextView title;
        TextView content;
        TextView date;
        ImageView img;
    }
}
