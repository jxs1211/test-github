package xianjie.shen.stuq2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import xianjie.shen.bean.CommonException;
import xianjie.shen.csdn.DataUtil;
import xianjie.shen.stuq2.Util.FileGet;
import xianjie.shen.stuq2.Util.FileUtil;

public class MainActivity extends AppCompatActivity
{
    private static final int GET_SUCCESS = 1;
    private static final int GET_FAIL = 2;
    private TextView mTextView;
    private Button mGet;
    private Button mClear;
    private String mUrl = "https://zhuanlan.zhihu.com/p/21324679";
    private Handler mUIHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case GET_SUCCESS:
                    mTextView.setText(((String) msg.obj));
                    break;
                case GET_FAIL:
                    mTextView.setText("get content fail");
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
        mTextView = (TextView) findViewById(R.id.tv_text);
        mGet = (Button) findViewById(R.id.btn_click);
        mClear = (Button) findViewById(R.id.btn_clear);
    }

    private void initDatas()
    {
//        mTextView.setText(getTextFromUrl(mUrl));
    }

    private void saveToSdcard()
    {
        try
        {
            FileUtil.saveToSDCard(MainActivity.this, "自学编程这几点非常重要", mTextView.getText().toString());
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("saveToSDCard fail");
        }
    }

    private String getTextFromUrl(String url)
    {
        String s = null;
        try
        {
            s = DataUtil.doGet(url);
        } catch (CommonException e)
        {
            e.printStackTrace();
            return "error";
        }
        return s;
    }

    private void initEvents()
    {
        mGet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContentFromFile("自学编程这几点非常重要");
            }
        });

        mClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mTextView.setText("");
//                mTextView.setText(getTextFromUrl(mUrl));
            }
        });
    }


    private void getContentFromFile(String fileName)
    {
        FileGet.getFileContent(fileName, new FileGet.CallBack()
        {
            @Override
            public void success(Object result)
            {
                Message msg = Message.obtain();
                msg.what = GET_SUCCESS;
                msg.obj = result;
                mUIHandler.sendMessage(msg);
            }

            @Override
            public void error(Exception e)
            {
                Message msg = Message.obtain();
                msg.what = GET_FAIL;
                msg.obj = e;
                mUIHandler.sendMessage(msg);
            }
        });
    }

    private String getFileContent(String filePath)
    {
//        getFileList();
        StringBuffer sb = null;
        try
        {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String strLine;
            //read file by line
            while ((strLine = br.readLine()) != null)
            {
                sb.append(strLine);
            }
            fis.close();
            br.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 手动处理config的变化，防止activity销毁再重新创建
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setContentView(R.layout.portrait_activity);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setContentView(R.layout.landscape_activity);
        }
    }
}
