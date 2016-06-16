package xianjie.shen.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DistrictGridAdapter extends RecyclerView.Adapter<DistrictGridAdapter.MyViewHolder>
{
    public static final String TEXT_COLOR = "#88000000";
    private Context mContext;
    protected List<?> mDatas;
    private ArrayList<Integer> mHeights;
    private LayoutInflater mInflater;
    private boolean mIsStaggered;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClickListener(View view, int pos, MyViewHolder holder);

        void onItemLongClickListener(View view, int pos, MyViewHolder holder);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }

    public DistrictGridAdapter(Context paramContext, List<?> paramList, int paramInt, boolean paramBoolean)
    {
        this.mContext = paramContext;
        this.mIsStaggered = paramBoolean;
        this.mLayoutId = paramInt;
        this.mDatas = paramList;
        this.mInflater = LayoutInflater.from(paramContext);
        if (paramBoolean)
        {
            this.mHeights = new ArrayList();
            paramInt = 0;
            while (paramInt < this.mDatas.size())
            {
                this.mHeights.add(Integer.valueOf((int) (200.0D + Math.random() * 300.0D)));
                paramInt += 1;
            }
        }
    }

    private void setStaggeredGridAdapter(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
        LayoutParams localLayoutParams = paramViewHolder.itemView.getLayoutParams();
        paramViewHolder.getLayoutPosition();
        localLayoutParams.height = ((Integer) this.mHeights.get(paramInt)).intValue();
        paramViewHolder.itemView.setLayoutParams(localLayoutParams);
    }

    private void setUpItemClickListener(final MyViewHolder paramMyViewHolder)
    {
        if (this.mOnItemClickListener != null)
        {
            paramMyViewHolder.itemView.setOnClickListener(new OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    int i = paramMyViewHolder.getLayoutPosition();
                    DistrictGridAdapter.this.mOnItemClickListener.onItemClickListener(paramAnonymousView, i, paramMyViewHolder);
                }
            });
            paramMyViewHolder.itemView.setOnLongClickListener(new OnLongClickListener()
            {
                public boolean onLongClick(View paramAnonymousView)
                {
                    int i = paramMyViewHolder.getLayoutPosition();
                    DistrictGridAdapter.this.mOnItemClickListener.onItemLongClickListener(paramAnonymousView, i, paramMyViewHolder);
                    return false;
                }
            });
        }
    }

    public int getItemCount()
    {
        return this.mDatas.size();
    }

    public void onBindViewHolder(MyViewHolder paramMyViewHolder, int paramInt)
    {
        paramMyViewHolder.iv.setImageResource(((DistrictFragmentItemBean) this.mDatas.get(paramInt)).getImgId());
        paramMyViewHolder.tv.setText(((DistrictFragmentItemBean) this.mDatas.get(paramInt)).getName());
        setUpItemClickListener(paramMyViewHolder);
        if (this.mIsStaggered)
            setStaggeredGridAdapter(paramMyViewHolder, paramInt);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
        return new MyViewHolder(this.mInflater.inflate(this.mLayoutId, paramViewGroup, false));
    }

    public void setOnItemCLickListener(OnItemClickListener paramOnItemClickListener)
    {
        this.mOnItemClickListener = paramOnItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv;
        TextView tv;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.iv = ((ImageView) itemView.findViewById(myapplication.R.id.iv_img));
            this.tv = ((TextView) itemView.findViewById(myapplication.R.id.tv_district));
        }
    }

}

/* Location:           E:\tools\apktool\dex2jar-2.0\classes-dex2jar.jar
 * Qualified Name:     shen.xianjie.myapplication.DistrictGridAdapter
 * JD-Core Version:    0.6.2
 */