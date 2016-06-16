package shen.xianjie.myapplication;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhy.utils.T;

import java.util.List;

import shen.xianjie.myapplication.FolderBean.FolderBean;

/**
 * Created by JXS on 2016/3/3.
 */
public class ListImgDirPopWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mDatas;
    private PopupWindowListViewAdapter mAdapter;
    private Context mContext;

    public interface OnDirSelectedListener {
        void onDirSelected(FolderBean folderBean);
    }


    public void setOnDirSelectedListener(OnDirSelectedListener listener) {
        this.mListener = listener;
    }

    private OnDirSelectedListener mListener;


    public ListImgDirPopWindow(Context context, List<FolderBean> datas) {
        calWidthAndHeight(context);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_window_listview, null);
        mDatas = datas;
        mContext = context;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//在popupWindow外部点击，就收起popupWindow
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, "popwindow item " + position + " click");
                if (mListener != null) mListener.onDirSelected(mDatas.get(position));
            }
        });
    }

    /**
     * 计算popupWindow的宽高
     *
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        //获取屏幕的宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.7);

    }

    private void initViews() {
        mListView = (ListView) mConvertView.findViewById(R.id.lv_popup_window_main);
    }

    private void initDatas() {
        mAdapter = new PopupWindowListViewAdapter(mContext, mDatas, R.layout.popup_window_listview_item);
        mListView.setAdapter(mAdapter);
    }
}
