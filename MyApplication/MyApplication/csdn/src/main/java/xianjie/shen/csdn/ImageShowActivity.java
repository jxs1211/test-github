package xianjie.shen.csdn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.polites.android.GestureImageView;

import xianjie.shen.csdn.util.FileUtil;
import xianjie.shen.csdn.util.Http;

/**
 * Created by shen on 2016/6/10.
 */
public class ImageShowActivity extends Activity
{
    private String mUrl;
    private ProgressBar mLoading;
    private GestureImageView mGestureImageView;
    private Bitmap mBitmap;
    private ImageView mDownloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show);

        //拿到图片链接
        mUrl = getIntent().getExtras().getString("imgLink");


        initViews();
        initDatas();
        initEvents();
    }

    private void initViews()
    {
        mLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mGestureImageView = (GestureImageView) findViewById(R.id.giv_img);
        mDownloadBtn = (ImageView) findViewById(R.id.iv_download);
    }

    private void initDatas()
    {
    }

    private void initEvents()
    {
        new downloadImgTask().execute();
        mDownloadBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                downloadImg(view);
            }
        });
    }

    /**
     * 点击返回按钮返回
     *
     * @param view
     */
    public void back2(View view)
    {
        finish();
    }

    /**
     * 点击下载按钮
     *
     * @param view
     */
    public void downloadImg(View view)
    {
        mGestureImageView.setDrawingCacheEnabled(true);
        if (FileUtil.writeSDcard(mUrl, mGestureImageView.getDrawingCache()))
        {
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
        mGestureImageView.setDrawingCacheEnabled(false);
    }

    class downloadImgTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
//            mLoading.setVisibility(View.VISIBLE);
            mBitmap = Http.HttpGetBmp(mUrl);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            mGestureImageView.setImageBitmap(mBitmap);
            mLoading.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }
}
