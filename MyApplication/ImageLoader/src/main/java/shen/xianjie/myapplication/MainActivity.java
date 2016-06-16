package shen.xianjie.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.utils.L;
import com.zhy.utils.Main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shen.xianjie.myapplication.FolderBean.FolderBean;


public class MainActivity extends AppCompatActivity
{
    private static final int IMAGE_LOAD_COMPLETED = 0x1211;
    //包括所有图片最大数量
    private int mMaxCount = 0;
    //当前图片的文件夹
    private File mCurrentDir;
    private GridView mGridView;
    private MyAdapter mAdapter;
    private RelativeLayout mBottom;
    private TextView mDirName;
    private TextView mDirCount;
    private ProgressDialog mProgressDialog;
    private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();
    private List mImgs;
    private ListImgDirPopWindow pw;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            mProgressDialog.dismiss();

            bindDataToView();

            //完成图片扫描和数据绑定后，初始化popupWindow
            initPopupWindow();
        }
    };

    private void initPopupWindow()
    {
        pw = new ListImgDirPopWindow(this, mFolderBeans);
        pw.setAnimationStyle(R.style.PopupWindowAnim);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                //收起popWindow,显示主界面
//                lightOn();
//                lightOff();
                changeLightBrightness(1.0f);
            }
        });
        pw.setOnDirSelectedListener(new ListImgDirPopWindow.OnDirSelectedListener()
        {
            @Override
            public void onDirSelected(FolderBean folderBean)
            {
                mCurrentDir = new File(folderBean.getDir());
                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter()
                {
                    @Override
                    public boolean accept(File dir, String filename)
                    {
                        if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png"))
                            return true;
                        return false;
                    }
                }));
//                mAdapter.notifyDataSetChanged();
                mAdapter = new MyAdapter(MainActivity.this, mImgs, R.layout.item_gridview, mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mAdapter);

                mDirName.setText(folderBean.getName());
                mDirCount.setText("共 " + folderBean.getCount() + " 张");
                pw.dismiss();
            }
        });
    }

    /**
     * 调整MainActivity的亮度
     *
     * @param brightness
     */
    public void changeLightBrightness(float brightness)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = brightness;
        getWindow().setAttributes(lp);
    }

    private void lightOff()
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = .3f;
        getWindow().setAttributes(lp);

    }

    private void lightOn()
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 绑定数据到gridview
     */
    private void bindDataToView()
    {
        if (mCurrentDir.list() == null)
        {
            Toast.makeText(this, "没有扫描到图片", Toast.LENGTH_SHORT).show();
            return;//没有找到图片就直接吐司并返回
        }
        //把包含图片路径的String数组转换为ArrayList，准备GridView数据集
        mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String filename)
            {
                if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png"))
                    return true;
                return false;
            }
        }));
        L.e("shen-currentPath", "mCurrentDir: " + mCurrentDir);
        L.e("shen-currentPath", "mCurrentDir.getAbsolutePath: " + mCurrentDir.getAbsolutePath());
        L.e("shen-mImgs", "mImgs: " + mImgs.size());
        L.e("shen-mImgs", "mImgs: " + mImgs);
        //绑定数据到gridview
        mAdapter = new MyAdapter(this, mImgs, R.layout.item_gridview, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mAdapter);

        //设置底部dir的t(mCurrentDir.getName());
        mDirCount.setText("共 " + mMaxCount + " 张");
        mDirName.setText(mCurrentDir.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();
        initEvents();

    }

    private void initViews()
    {
        mGridView = (GridView) findViewById(R.id.gv_main);
        mBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        mDirName = (TextView) findViewById(R.id.tv_dir_name);
        mDirCount = (TextView) findViewById(R.id.tv_dir_count);

    }

    /**
     * 通过ContentProvider扫描insdcard中所有的图片
     */
    private void initDatas()
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "请确认存储卡！", Toast.LENGTH_SHORT).show();
            return;
        }
        //如果有存储卡，就扫描图片
        mProgressDialog = ProgressDialog.show(this, null, "努力加载中...");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = MainActivity.this.getContentResolver();
                //查询MIME_TYPE为"image/jpeg"或者"image/png"的图片，并按日期被修改的时间进行排序
                Cursor cursor = cr.query(imgUri, null, MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? ", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                //mDirPaths存储在set中，用于防止同一个文件下多张图片重复遍历
                Set<String> mDirPaths = new HashSet<String>();
                while (cursor.moveToNext())
                {//拿到图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;//如果path路径所指图片的父控件为空，结束当前循环，进入下一次循环
                    //拿到图片的父路径
                    String dirPath = parentFile.getAbsolutePath();

                    FolderBean folderBean = null;
                    if (mDirPaths.contains(dirPath))
                    {//扫描的图片是相同parentFile下，就只记录一次dirPath
                        continue;
                    } else
                    {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);//只有每一个文件夹下的第一张图片才会setFirstImgPath
                    }
                    //测试发现通过图片所属path拿到的parentFile是有可能为空，所以这里要判空一下，以免异常
                    if (parentFile.list() == null)
                    {
                        continue;
                    }//确定parentFile不为空再获取list的值
                    int picCount = parentFile.list(new FilenameFilter()
                    {
                        @Override
                        public boolean accept(File dir, String filename)
                        {//过滤并只获取所有图片的计数
                            if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png"))
                                return true;
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picCount);
                    mFolderBeans.add(folderBean);
                    //刷新包含最多图片的文件夹名称和所包含图片的数量
                    if (picCount > mMaxCount)
                    {
                        mMaxCount = picCount;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();
            }
        }
        ).start();

        //扫描完毕后，通知主线程更新ui和gridView的数据集
        mHandler.sendEmptyMessage(IMAGE_LOAD_COMPLETED);
    }

    private void initEvents()
    {
        mBottom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (pw != null && pw.isShowing())
                {
                    pw.dismiss();
                } else
                {
//                    pw = new ListImgDirPopWindow(MainActivity.this, mFolderBeans);
                    pw.showAsDropDown(mBottom, 0, 0);
                    changeLightBrightness(.3f);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        switch (id)
//        {
//            case R.id.add:
//                mLoadToast = new LoadToast(this);
//                mLoadToast.setBackgroundColor(getResources().getColor(R.color.load_toast_background_color))
//                        .setProgressColor(getResources().getColor(R.color.load_toast_progress_color))
//                        .setText("Adding item ...")
//                        .setTranslationY(100)
//                        .show();
//                break;
//            case R.id.delete:
//                mLoadToast.success();
//                break;
//            case R.id.listView:
//                mLoadToast.error();
//                break;
//        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
