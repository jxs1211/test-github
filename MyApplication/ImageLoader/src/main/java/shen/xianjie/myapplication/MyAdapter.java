package shen.xianjie.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shen.xianjie.myapplication.CommonAdapter.CommonAdapter;
import shen.xianjie.myapplication.CommonAdapter.CommonViewHolder;
import shen.xianjie.myapplication.util.ImageLoader;

/**
 * Created by JXS on 2016/2/24.
 */
public class MyAdapter extends CommonAdapter<String>
{
    //每次同时加载THREAD_COUNT张图片
    public static final int THREAD_COUNT = 3;
    private String mDirPath;
    protected List<String> mDatas;
    private int mScreenWidth;
    //用HashSet保存被选中的图片
    //这里一定要用static静态修饰，否则每次实例化adapter时都会被重新实例化，导致数据集为空无法记录之前选中的状态（static表示不要实例化就可以使用，被static修饰的成员变量和成员方法独立于该类的任何对象。也就是说，它不依赖类特定的实例，被类的所有实例共享。）
    private static Set<String> mSelectedImgs = new HashSet<String>();
//    private Set<String> mSelectedImgs = new HashSet<String>();

    public MyAdapter(Context context, ArrayList<String> datas, int layoutId)
    {
        super(context, datas, layoutId);
    }

    public MyAdapter(Context context, List datas, int layoutId, String dirPath)
    {
        super(context, datas, layoutId, dirPath);
        this.mDatas = datas;
        this.mDirPath = dirPath;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mScreenWidth = outMetrics.widthPixels;
    }


    @Override
    public void convert(CommonViewHolder holder, final int pos)
    {
        final String imageName = mDatas.get(pos);
        final ImageView imageView = (ImageView) holder.getView(R.id.iv_item_img);
        ///加载itemView前将item中相关控件设置成默认状态，防止加载下一屏item时，imageView等控件显示的仍是上一次该item时的状态
        holder.setImageResource(R.id.iv_item_img, R.drawable.ic_menu_add)
                .setImageResource(R.id.ib_select, R.drawable.c);
        imageView.setColorFilter(null);
        imageView.setMaxWidth(mScreenWidth / 3);

        //item加载显示的图片
        ImageLoader.getInstance(THREAD_COUNT, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + imageName, imageView);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mSelectedImgs.contains(mDirPath + imageName))
                {//图片的完整路径(mDirPath + imageName)查找是否被选中，避免在不同文件夹里有同名图片而无法添加到mSelectedImgs中
                    //已选中的图片,取消选中
                    mSelectedImgs.remove(mDirPath + imageName);
                    imageView.setColorFilter(null);
                } else
                {   //未选中的图片，置为选中
                    mSelectedImgs.add(mDirPath + imageName);
                    imageView.setColorFilter(Color.parseColor("#77000000"));
                }
            }
        });
        //确保在文件之间切换时，原来被选中的图片状态能保持
        if (mSelectedImgs.contains(mDirPath + imageName))
        {
            imageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}

