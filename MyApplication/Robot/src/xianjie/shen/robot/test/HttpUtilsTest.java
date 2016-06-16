package xianjie.shen.robot.test;

import xianjie.shen.robot.bean.ChatMessage;
import xianjie.shen.robot.utils.HttpRequestUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class HttpUtilsTest extends AndroidTestCase
{
    public void testHttpRequestUtils()
    {
        String result = HttpRequestUtils.doGet("你好");
		Log.e("shen", result);
        ChatMessage chatMessage = HttpRequestUtils.sendMessage("你好");
        Log.e("shen", "chatMessage:" + chatMessage.getText());
    }
}
