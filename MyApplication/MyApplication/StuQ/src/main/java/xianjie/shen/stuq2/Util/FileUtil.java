package xianjie.shen.stuq2.Util;

import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shen on 2016/6/11.
 */
public class FileUtil
{
    public static String getDataFolderPath(Context context) {
        return Environment.getDataDirectory() + "/data/"
                + context.getPackageName() + "/files";
    }

    public static String getMyFileDir(Context context){
        return context.getFilesDir().toString();
    }

    public static String getMyCacheDir(Context context){
        return context.getCacheDir().toString();
    }

    /**
     * @desc 保存内容到文件中
     * @param fileName
     * @param content
     * @throws Exception
     */
    public static void save(Context context, String fileName, String content, int module) {
        try {
            FileOutputStream os = context.openFileOutput(fileName, module);
            os.write(content.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc 读取文件内容
     * @param fileName
     * @return
     */
    public static String read(Context context, String fileName){

        try {
            FileInputStream fis = context.openFileInput(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while((len = fis.read(b)) != -1){
                bos.write(b, 0, len);
            }
            byte[] data = bos.toByteArray();
            fis.close();
            bos.close();
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * @desc 将文本内容保存到sd卡的文件中
     * @param context
     * @param fileName
     * @param content
     * @throws IOException
     */
    public static void saveToSDCard(Context context, String fileName, String content) throws IOException{

        File file = new File(Environment.getExternalStorageDirectory(),fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * @desc 读取sd卡文件内容
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFromSDCard(String fileName) throws IOException
    {

        File file = new File(Environment.getExternalStorageDirectory(),fileName);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer =  new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1)
        {
            bos.write(buffer, 0, len);
        }
        byte[]  data = bos.toByteArray();
        fis.close();
        bos.close();

        return new String(data);
    }

}
