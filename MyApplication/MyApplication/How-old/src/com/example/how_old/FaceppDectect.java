package com.example.how_old;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.zhy.utils.L;

public class FaceppDectect
{

    public interface CallBack
    {
        void success(JSONObject result);

        void error(FaceppParseException e);
    }

    public void detect(final Bitmap bm, final CallBack callBack)
    {
        // detect bitmap为耗时操作，需要用new thread执行
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                JSONObject jsonObject = null;
                try
                {
                    HttpRequests requests = new HttpRequests(Constant.KEY,
                            Constant.SECRET, true, true);
                    // 创建一个bitmap，因为需要对其进行一些操作
                    Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0,
                            bm.getWidth(), bm.getHeight());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // 将bitmap压缩到一个ByteArray类型的stream中
                    bitmap.compress(CompressFormat.JPEG, 100, stream);
                    // 将stream转换成一个字节数组
                    byte[] arrays = stream.toByteArray();
                    // 将字节数组拼接成PostParameters
                    PostParameters parameters = new PostParameters();
                    parameters.setImg(arrays);
                    // 返送要解析的图片数据，通过之前创建的requests将parameters解析成我们需要的jsonObject
                    jsonObject = requests
                            .detectionDetect(parameters);
                    // 打个log，确认得到返回的jsonObject
//					Log.e("shen", jsonObject.toString());
                    if (callBack != null)
                    {
                        callBack.success(jsonObject);
                    }
                } catch (FaceppParseException e)
                {
                    if (callBack != null)
                    {
                        try
                        {
                            int response_code = jsonObject.getInt("response_code");
                            String resStr = Integer.toString(response_code);
                            L.e("shen", resStr);
                            if (resStr.startsWith("4"))//此处需要修改为判断是4或者5xx开头的requestCode，才能返回时client error或者server error
                                e = new FaceppParseException("get a error", 1, "client不可用", response_code);
                            else if (resStr.startsWith("5"))
                            {
                                e = new FaceppParseException("get a error", 1, "server不可用", response_code);
                            }
                        } catch (Exception e1)
                        {
                        }
                        e = new FaceppParseException("get a error", 1, "无法识别年龄", 400);
                        callBack.error(e);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
