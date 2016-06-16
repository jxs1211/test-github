package shen.xianjie.myapplication;

import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shen.xianjie.myapplication.CommonAdapter.CommonAdapter;
import shen.xianjie.myapplication.CommonAdapter.CommonViewHolder;
import shen.xianjie.myapplication.FolderBean.FolderBean;

/**
 * Created by JXS on 2016/3/3.
 */
public class PopupWindowListViewAdapter extends CommonAdapter<FolderBean>
{

    private List<FolderBean> mDatas;
    //记录被选中的imgs
    private Set<String> mSelectedImgs=new HashSet<String>();

    public PopupWindowListViewAdapter(Context context, List datas, int layoutId)
    {
        super(context, datas, layoutId);
        this.mDatas = datas;
    }

    @Override
    public void convert(CommonViewHolder holder, int position)
    {
        //加载图片前将imageView设置成默认图片，防止加载下一屏图片时，imageView显示的仍是上一屏加载图片时的图片
        holder.setImageResource(R.id.iv_popup_item_img, R.drawable.ic_menu_add);

//        if (mSelectedImgs.contains(mDatas.get(position).getDir()))
        holder.setImageURI(R.id.iv_popup_item_img, mDatas.get(position).getFirstImgPath())
                .setText(R.id.tv_popup_item_dirname, mDatas.get(position).getName())
                .setText(R.id.tv_popup_item_count, "共" + mDatas.get(position).getCount() + "张")
                .setImageResource(R.id.iv_popup_item_img_select, R.drawable.c);
    }
}
