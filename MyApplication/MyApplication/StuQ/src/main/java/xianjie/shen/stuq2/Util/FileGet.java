package xianjie.shen.stuq2.Util;

import android.os.HandlerThread;

import java.io.IOException;

/**
 * Created by shen on 2016/6/11.
 */
public class FileGet

{
    public interface CallBack
    {
        void success(Object result);

        void error(Exception e);
    }

    public static void getFileContent(String filename, CallBack callBack)
    {
        HandlerThread handlerThread = new HandlerThread("light_work_thread");
        handlerThread.start();
        String result = null;
        try
        {
            result = FileUtil.readFromSDCard(filename);
            if (result != null) callBack.success(result);
        } catch (IOException e)
        {
            e.printStackTrace();
            callBack.error(e);
        }
    }
}
