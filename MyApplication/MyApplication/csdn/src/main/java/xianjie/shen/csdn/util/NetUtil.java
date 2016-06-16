package xianjie.shen.csdn.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断当前手机的联网渠道
 * Created by shen on 2016/6/9.
 */
public class NetUtil
{
    public static boolean checkNet(Context context)
    {
        //判断链接方式
        boolean isWifiConnected = isWIFIConnected(context);
        boolean isMobileConnected = isMobileConnected(context);
        if (!isMobileConnected && !isMobileConnected)
        {
            // 如果都没有连接返回false，提示用户当前没有网络
            return false;
        }
        return true;

    }

    /**
     * 判断手机是否采用mobile连接
     *
     * @param context
     * @return
     */
    private static boolean isMobileConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null && info.isConnected())
        {
            return true;
        }
        return false;
    }

    /**
     * 判断手机是否采用wifi连接
     *
     * @param context
     * @return
     */
    private static boolean isWIFIConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected())
        {
            return true;
        }
        return false;
    }
}
