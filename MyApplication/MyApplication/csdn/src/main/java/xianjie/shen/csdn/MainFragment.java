package xianjie.shen.csdn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import xianjie.shen.bean.CommonException;
import xianjie.shen.bean.NewsItem;
import xianjie.shen.biz.NewsItemBiz;
import xianjie.shen.csdn.adapter.NewsItemAdapter;
import xianjie.shen.csdn.dao.NewsItemDao;
import xianjie.shen.csdn.util.AppUtil;
import xianjie.shen.csdn.util.NetUtil;

/**
 * Created by shen on 2016/6/5.
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore
{
    private static final int LOAD_MORE = 0x110;
    private static final int LOAD_REFRESH = 0x111;

    private static final int TIP_ERROR_NO_NETWORK = 0X112;
    private static final int TIP_ERROR_SERVER = 0X113;

    /**
     * 是否为第一次进入
     */
    private boolean isFirstIn = true;

    /**
     * 是否连接上网络
     */
    private boolean isConnNet = false;

    /**
     * 当前数据是否从网络获取
     */
    private boolean isLoadingDataFromNetwork = false;

    /**
     * 默认的newsType
     */
    private int newsType = Constant.NEWS_TYPE_YEJIE;

    /**
     * 当前页面
     */
    private int currentPage = 1;

    /**
     * 处理新闻的业务类
     */
    private NewsItemBiz mNewsItemBiz;

    /**
     * 与数据库交互
     */
    private NewsItemDao mNewsItemDao;

    /**
     * 扩展的listView
     */
    private XListView mXListView;

    /**
     * 新闻数据的适配器
     */
    private NewsItemAdapter mNewsItemAdapter;

    /**
     * 新闻数据
     */
    private List<NewsItem> mNewsItems = new ArrayList<NewsItem>();

    /**
     * @param newsType
     */
    public MainFragment(int newsType)
    {
        this.newsType = newsType;
        Log.e("newsType ", "" + newsType);
        mNewsItemBiz = new NewsItemBiz();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tab_item_fragment_main, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mNewsItemDao = new NewsItemDao(getActivity());
        mNewsItemAdapter = new NewsItemAdapter(getActivity(), mNewsItems);
        /**
         * 初始化
         */
        mXListView = (XListView) getView().findViewById(R.id.xlv_XListView);
        mXListView.setAdapter(mNewsItemAdapter);
        mXListView.setPullRefreshEnable(this);
        mXListView.setPullLoadEnable(this);
        mXListView.setRefreshTime(AppUtil.getRefreshTime(getActivity(), newsType));

        if (isFirstIn)
        {
            /**
             * 进来时直接刷新
             */
            mXListView.startRefresh();
            isFirstIn = false;
        } else
        {
            mXListView.NotRefreshAtBegin();
        }

        mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
            {
                NewsItem newsItem = mNewsItems.get(pos - 1);
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                intent.putExtra("url", newsItem.getLink());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadMore()
    {
        new LoadDatasTask().execute(LOAD_MORE);
    }

    @Override
    public void onRefresh()
    {
        new LoadDatasTask().execute(LOAD_REFRESH);
    }

    /**
     * 加载数据的异步任务
     */
    private class LoadDatasTask extends AsyncTask<Integer, Void, Integer>
    {

        @Override
        protected Integer doInBackground(Integer... parmas)
        {
            /**
             * 根据url链接地址加载newsItem相关数据
             */
            switch (parmas[0])
            {
                case LOAD_MORE:
                    loadMoreData();
                    break;
                case LOAD_REFRESH:
                    return refreshData();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            switch (result)
            {
                case TIP_ERROR_NO_NETWORK:
                    Toast.makeText(getActivity(), "没有网络连接！", Toast.LENGTH_SHORT).show();
                    mNewsItemAdapter.setDatas(mNewsItems);
                    mNewsItemAdapter.notifyDataSetChanged();
                    break;
                case TIP_ERROR_SERVER:
                    Toast.makeText(getActivity(), "服务器错误！", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }

            mXListView.setRefreshTime(AppUtil.getRefreshTime(getActivity(), newsType));
            mXListView.stopRefresh();
            mXListView.stopLoadMore();
        }
    }

    /**
     * 会根据当前网络情况，判断是从数据库加载还是从网络继续获取
     */
    private void loadMoreData()
    {
        //数据从网络加载
        if (isLoadingDataFromNetwork)
        {
            currentPage += 1;

            try
            {
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                //将网络加载的数据，插入我们的app的数据库中
                mNewsItemDao.add(newsItems);
                mNewsItemAdapter.addAll(newsItems);
            } catch (CommonException e)
            {
                e.printStackTrace();
            }
        } else
        {
            currentPage += 1;
            List<NewsItem> newsItems = mNewsItemDao.list(newsType, currentPage);
            mNewsItemAdapter.addAll(newsItems);
        }
    }

    /**
     * 下拉刷新
     */
    public Integer refreshData()
    {
        if (NetUtil.checkNet(getActivity()))
        {
            isConnNet = true;
            try
            {
                //获取最新数据
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                mNewsItemAdapter.setDatas(newsItems);
                //如果有的话从网络加载，并清空该类型新闻的数据，更新数据库缓存数据。
                isLoadingDataFromNetwork = true;
                //设置刷新时间
                AppUtil.setRefreshTime(getActivity(), newsType);
                //清除数据库数据
                mNewsItemDao.deleteAll(newsType);
                //存入数据库
                mNewsItemDao.add(newsItems);
            } catch (CommonException e)
            {
                e.printStackTrace();
                isLoadingDataFromNetwork = false;
                return TIP_ERROR_SERVER;

            }
        } else
        {
            isConnNet = false;
            isLoadingDataFromNetwork = false;
            //从数据库中加载数据
            List<NewsItem> newsItems = mNewsItemDao.list(newsType, currentPage);
            mNewsItems = newsItems;
            return TIP_ERROR_NO_NETWORK;
        }

        return -1;
    }
}
