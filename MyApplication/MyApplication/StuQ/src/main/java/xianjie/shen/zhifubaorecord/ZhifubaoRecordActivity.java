package xianjie.shen.zhifubaorecord;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import xianjie.shen.stuq2.R;

/**
 * Created by shen on 2016/6/13.
 */
public class ZhifubaoRecordActivity extends Activity
{
    private TextView mMore;
    private String[] mTitles;
    private String[] mContents;
    private String[] mContents2;
    private String[] mContents3;
    private MyAdapter mAdapter;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zhifubao_record);

        initViews();
        initDatas();
        initEvents();
    }

    private void initViews()
    {
        mMore = (TextView) findViewById(R.id.iv_more);
        mList = (ListView) findViewById(R.id.lv_list);
    }

    private void initDatas()
    {
        mAdapter = new MyAdapter(this, DataUtil.getDatasFromRes(this));
        mList.setAdapter(mAdapter);
    }


    private void initEvents()
    {
        mMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                openActivity(ZhifubaoRecordActivity2.class );
            }
        });

    }

    private void openActivity(Class activity, ArrayList datas)
    {

    }
}
