package xianjie.shen.csdn.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shen on 2016/6/10.
 */
public class FileUtil
{
    public static String filePath = Environment.getExternalStorageDirectory() + "/CSDNDownload";

    public static String getFileName(String str)
    {
        //去除url中的符号作为文件名返回
        str = str.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
        System.out.println("filename= " + str);
        return str + ".png";
    }

    public static boolean writeSDcard(String filename, Bitmap bitmap)
    {
        File file = new File(filePath);
        if (!file.exists()) file.mkdirs();

        File imgFile = new File(filePath, getFileName(filename));
        if (imgFile.exists()) return true;

        InputStream is = bitmap2InputStream(bitmap);
        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(imgFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        byte[] buffer = new byte[512];
        int count = 0;
        try
        {
            while ((count = is.read()) > 0)
            {
                fileOutputStream.write(buffer, 0, count);
                fileOutputStream.flush();
                fileOutputStream.close();
                is.close();
                System.out.println("writeToSD success");
            }
        } catch (IOException e)
        {
            System.out.println("writeToSD fail");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Bitmap转换成byte[]
    public static byte[] bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static InputStream bitmap2InputStream(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Write a compressed version of the bitmap to the specified
        // outputstream.
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return bais;
    }

}
