package xianjie.shen.csdn;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.XListView;
import xianjie.shen.bean.CommonException;
import xianjie.shen.bean.News;
import xianjie.shen.biz.NewsItemBiz;

/**
 * Created by shen on 2016/6/10.
 */
public class NewsContentActivity extends Activity implements IXListViewLoadMore
{
    private XListView mXListView;

    /**
     * 当前页的url
     */
    private String mUrl;
    private NewsItemBiz mBiz;
    private List<News> mNewses;

    private ProgressBar mProgressBar;
    private NewsContentAdapter mAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        mBiz = new NewsItemBiz();
        mUrl = getIntent().getExtras().getString("url");

        initViews();
        initDatas();
        initEvents();
    }

    private void initViews()
    {
        mXListView = (XListView) findViewById(R.id.xlv_listView);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_newsContentPro);

    }

    private void initDatas()
    {
        mAdatper = new NewsContentAdapter(this);
        mXListView.setAdapter(mAdatper);
        mXListView.disablePullRefreash();
        mXListView.setPullLoadEnable(this);
    }

    private void initEvents()
    {
        mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
            {
                News news = mNewses.get(pos - 1);
                String imgLink = news.getImgLink();
                Intent intent = new Intent(NewsContentActivity.this, ImageShowActivity.class);
                intent.putExtra("imgLink", imgLink);
                startActivity(intent);
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        new loadDataTask().execute();
    }

    @Override
    public void onLoadMore()
    {

    }

    private class loadDataTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                mNewses = mBiz.getNews(mUrl).getNewses();
            } catch (CommonException e)
            {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if (mNewses == null) return;
            mAdatper.addList(mNewses);
            mAdatper.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 点击返回按钮
     *
     * @param view
     */
    public void back(View view)
    {
        finish();
    }

}
