package xianjie.shen.robot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;

import xianjie.shen.robot.bean.ChatMessage;
import xianjie.shen.robot.bean.Result;

import com.google.gson.Gson;

public class HttpRequestUtils
{
    /**
     * API_KEY对应的帐号为：327411586@qq.com 密码为：Jxs9***3
     */
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "5013811148a1609d7d46afc554e61f61";

    /**
     * 根据请求的msg，返回服务器的解析结果
     *
     * @param msg
     * @return
     */
    public static String doGet(String msg)
    {
        String result = "";
        //拼接api地址、api key、msg成完整的url
        String fullUrl = getFullUrl(msg);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try
        {
            java.net.URL netUrl = new java.net.URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) netUrl
                    .openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();//核心代码
            int len = -1;
            byte[] buf = new byte[128];
            baos = new ByteArrayOutputStream();
            while ((len = is.read(buf)) != -1)
            {
                // 将字节数组读到本地缓存
                baos.write(buf, 0, len);//核心代码
            }
            baos.flush();
            // 将字节数组转换成字符串
            result = new String(baos.toByteArray());//核心代码
            // Log.e("sshen", result);
            return result;
        } catch (Exception e)
        {
        } finally
        {//用完后释放is和baos
            if (is != null)
            {
                try
                {
                    is.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (baos != null)
            {
                try
                {
                    baos.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return "HttpRequestUtils.doGet() failed";

    }

    private static String getFullUrl(String msg)
    {
        String url = "";
        try
        {
            url = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode(msg, "UTF-8");
            return url;
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "getFullUrl failed";
    }

    /**
     * 发送一个消息给小捷，小捷获取doGet返回值后转换成结果，设置好chatMessage类（结果会在后面的ListView中显示）
     *
     * @param msg
     * @return
     */
    public static ChatMessage sendMessage(String msg)
    {
        ChatMessage chatMessage = new ChatMessage();
        Gson gson = new Gson();
        Result result = null;
        try
        {
            // 通过gson将json字符串转换成我们定义的result类
            result = gson.fromJson(doGet(msg), Result.class);
            // 通过转换成result后的结果，设置chatMessage
            chatMessage.setText(result.getText());
        } catch (Exception e)
        {
            // 无论gson.fromJson的转换是否成功，都希望能返回一个chatMessage，这样就不会因为转换失败而让程序crush
            chatMessage.setText("Server is busy....");
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(xianjie.shen.robot.bean.ChatMessage.Type.RECEIVE);
        return chatMessage;
    }
}
