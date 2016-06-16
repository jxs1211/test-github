package xianjie.shen.robot;

import java.text.SimpleDateFormat;
import java.util.List;

import xianjie.shen.robot.bean.ChatMessage;
import xianjie.shen.robot.bean.ChatMessage.Type;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter
{

	private List<ChatMessage>	mDatas;
	private LayoutInflater		mInflater;

	public ChatMessageAdapter(Context context, List<ChatMessage> datas)
	{
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
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

    /**
     * 获取itemView的类型
     * @param position
     * @return
     */
	@Override
	public int getItemViewType(int position)
	{

		if (mDatas.get(position).getType() == Type.RECEIVE)
		{
			return 0;
		}
		return 1;
	}

    /**
     * 获取itemView类型的数量
     * @return
     */
	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent)
	{
		// 拿到当前position的chatMsg，用于后面设置给viewHolder
		ChatMessage chatMessage = mDatas.get(position);
		ViewHolder holder = null;
		if (converView == null)
		{
			if (getItemViewType(position) == 0)
			{
				// 选用带false参数的inflate方法，不然会报错，应该是因为2个参数的inflate只支持它自己已经定义好的rootViewGroup的模板，如果自定义layout控件布局比较复杂就无法匹配
				converView = mInflater.inflate(R.layout.list_item_fromchat,
						parent, false);
				// 不要忘了new viewHolder
				holder = new ViewHolder();
				holder.mDate = (TextView) converView
						.findViewById(R.id.id_tv_xiaojie_date);
				holder.mMsg = (TextView) converView
						.findViewById(R.id.id_tv_xiaojie_text);
			} else
			{
				converView = mInflater.inflate(R.layout.list_item_tochat,
						parent, false);
				holder = new ViewHolder();
				holder.mDate = (TextView) converView
						.findViewById(R.id.id_tv_me_date);
				holder.mMsg = (TextView) converView
						.findViewById(R.id.id_tv_me_text);
			}
			converView.setTag(holder);
		} else
		{
			holder = (ViewHolder) converView.getTag();
		}
		// 将chatMessage的数据设置给viewHolder
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.mDate.setText(sf.format(chatMessage.getDate()));
        holder.mMsg.setText(chatMessage.getText());
        return converView;
	}

	private class ViewHolder
	{
		TextView	mDate;
		TextView	mMsg;
	}
}
