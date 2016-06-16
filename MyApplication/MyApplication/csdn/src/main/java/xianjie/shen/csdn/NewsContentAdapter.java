package xianjie.shen.csdn;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import xianjie.shen.bean.News;

/**
 * Created by shen on 2016/6/10.
 */
public class NewsContentAdapter extends BaseAdapter
{

    private List<News> mNewses = new ArrayList<News>();
    private LayoutInflater mInflater;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    public NewsContentAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.images)
                .showImageForEmptyUri(R.drawable.images).showImageOnFail(R.drawable.images)
                .cacheInMemory().cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (mNewses.get(position).getType())
        {
            case News.NewsType.TITLE:
                return 0;
            case News.NewsType.SUMMARY:
                return 1;
            case News.NewsType.CONTENT:
                return 2;
            case News.NewsType.IMG:
                return 3;
            case News.NewsType.BOLD_TITLE:
                return 4;
        }
        return -1;//都不是就return -1
    }

    @Override
    public int getViewTypeCount()
    {
        return 5;
    }

    @Override
    public boolean isEnabled(int position)
    {
        if (mNewses.get(position).getType() == News.NewsType.IMG)
        {
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    public int getCount()
    {
        return mNewses.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return mNewses.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        News news = mNewses.get(pos);//获取当前的数据

        Log.e("present news", news.toString());

        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            switch (news.getType())
            {
                case News.NewsType.TITLE:
                    convertView = mInflater.inflate(R.layout.news_content_title_item, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
                    break;
                case News.NewsType.SUMMARY:
                    convertView = mInflater.inflate(R.layout.news_content_summary_item, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
                    break;
                case News.NewsType.CONTENT:
                    convertView = mInflater.inflate(R.layout.news_content_item, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
                    break;
                case News.NewsType.IMG:
                    convertView = mInflater.inflate(R.layout.news_content_img_item, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.iv_img);
                    break;
                case News.NewsType.BOLD_TITLE:
                    convertView = mInflater.inflate(R.layout.news_content_bold_title_item, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
                    break;
            }
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (news != null)
        {
            switch (news.getType())
            {
                case News.NewsType.TITLE:
                    holder.textView.setText(news.getTitle());
                    break;
                case News.NewsType.SUMMARY:
                    holder.textView.setText(news.getSummary());
                    break;
                case News.NewsType.CONTENT:
                    holder.textView.setText("/u3000/u3000" + Html.fromHtml(news.getContent()));
                    break;
                case News.NewsType.IMG:
                    imageLoader.displayImage(news.getImgLink(), holder.imageView, options);
                    break;
                case News.NewsType.BOLD_TITLE:
                    holder.textView.setText("u3000/u3000" + Html.fromHtml(news.getContent()));
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    public void addList(List<News> mNewses)
    {
        mNewses.addAll(mNewses);
    }

    private class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }
}
