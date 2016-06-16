package xianjie.shen.zhifubaorecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import xianjie.shen.stuq2.R;

/**
 * Created by shen on 2016/6/13.
 */
public class MyAdapter extends BaseAdapter
{
    private List<?> mDatas;
    private LayoutInflater mInflater;

    public MyAdapter()
    {
    }

    public MyAdapter(Context context, List<?> datas)
    {
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (((Bean) mDatas.get(position)).getType() == Bean.Type.NORMAL)
        {
            return 0;
        } else if (((Bean) mDatas.get(position)).getType() == Bean.Type.DETAIL)
        {
            return 1;
        } else if (((Bean) mDatas.get(position)).getType() == Bean.Type.QUES)
        {
            return 2;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
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
            holder = new ViewHolder();
            switch (getItemViewType(pos))
            {
                case 0:
                    convertView = mInflater.inflate(R.layout.zhifubao_record_list_item, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.title.setText(((Bean) mDatas.get(pos)).getTitle());
                    holder.content.setText(((Bean) mDatas.get(pos)).getContent());
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.zhifubao_record_list_item, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.jine = (TextView) convertView.findViewById(R.id.tv_jine);
                    holder.title.setText(((Bean) mDatas.get(pos)).getTitle());
                    holder.content.setText(((Bean) mDatas.get(pos)).getContent());
                    holder.jine.setText("1192.00");
                    holder.jine.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    convertView = mInflater.inflate(R.layout.zhifubao_record_list_item_question, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.title.setText("对此订单有疑问?");
            }
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder
    {
        TextView title;
        TextView content;
        TextView jine;
    }
}
