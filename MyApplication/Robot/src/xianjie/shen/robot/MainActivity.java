package xianjie.shen.robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xianjie.shen.robot.bean.ChatMessage;
import xianjie.shen.robot.bean.ChatMessage.Type;
import xianjie.shen.robot.utils.HttpRequestUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;
    private Context mContext;
    private EditText mSendMsg;
    private Button mSendMsgBtn;
    protected Handler mHandler;
    private String WELCOMEMSG = "你好，我是小捷！很高兴为您服务！";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();
        initEvents();
        // T.showLong(this,
        // AppUtils.getAppName(this)+AppUtils.getVersionName(this));
        // Log.e("shen", WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE +
        // "!");
    }

    private void initEvents()
    {
        mMsgs.setAdapter(mAdapter);
        mSendMsgBtn.setOnClickListener(this);
        mSendMsg.setOnClickListener(this);
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                // Log.e("shen", ((ChatMessage) msg.obj).getText().toString());
                mDatas.add((ChatMessage) msg.obj);
                mAdapter.notifyDataSetChanged();
//				mMsgs.setSelection(mDatas.size() - 1);
            }
        };
    }

    private void initDatas()
    {
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage(WELCOMEMSG, Type.RECEIVE, new Date()));
        // mDatas.add(new ChatMessage("你好", Type.SEND, new Date()));
        // ChatMessageAdapter的context参数一定要传this，不要用mContext
        mAdapter = new ChatMessageAdapter(this, mDatas);
    }

    private void initViews()
    {
        mMsgs = (ListView) findViewById(R.id.id_listView);
        mSendMsg = (EditText) findViewById(R.id.id_editText);
        mSendMsgBtn = (Button) findViewById(R.id.id_sendBtn);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View arg0)
    {
        // int sInputStatus = getWindow().getAttributes().softInputMode;
        // Log.e("shen", "Log");
        // 点击id_editText时，就将listView滑到底部最新消息的位置
        final String sendMsg = mSendMsg.getText().toString();
        if (arg0.getId() == R.id.id_editText)
        {
            mMsgs.setSelection(mDatas.size() - 1);
        }
        if (arg0.getId() == R.id.id_sendBtn)
        {
            if (TextUtils.isEmpty(sendMsg))
            {
                Toast.makeText(MainActivity.this, "输入内容不能为空，请重新输入！", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            // 判断网络是否打开,没打开就提示打开网络
            if (!isNetworkOk())
            {
                Toast.makeText(this, "开启网络后才能和小捷对话哟！", Toast.LENGTH_SHORT).show();
                return;
            }
            //发送消息,添加一条消息到ArrayList中并将发送消息刷新到listView上
            mDatas.add(new ChatMessage(sendMsg, Type.SEND, new Date()));
            mAdapter.notifyDataSetChanged();
//            mMsgs.setSelection(mDatas.size() - 1);
            // 每次输入后要清除之前输入内容
            mSendMsg.setText("");
            // Log.e("shen", sendMsg + "!");
            //接受返回消息并将返回消息显示到listView
            // 获取http请求的耗时操作放入多线程中执行
            new Thread()
            {
                public void run()
                {
                    // Log.e("shen", "shen");
                    // Log.e("shen", sendMsg);
                    ChatMessage receiveMsg = HttpRequestUtils
                            .sendMessage(sendMsg);
                    Message m = Message.obtain();
                    m.obj = receiveMsg;
                    // Log.e("shen", receiveMsg.getText());
                    mHandler.sendMessage(m);
                }

                ;
            }.start();// 切记不要忘了启动多线程
        }
    }

    private boolean isNetworkOk()
    {
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
        {
            return info.isAvailable();
        }
        return false;
    }

}
