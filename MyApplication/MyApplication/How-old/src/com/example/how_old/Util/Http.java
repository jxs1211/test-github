package com.example.how_old.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by shen on 2016/6/11.
 */
public class Http
{
    public static String cookieName;
    public static String cookieValue;
    public static String hostBase;

    public static Bitmap HttpGetBm(String url)
    {
        HttpGet httpGet = new HttpGet(url);
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 3000);

        Bitmap bitmp = null;

        HttpClient httpClient = new DefaultHttpClient(params);
        try
        {
            HttpResponse response = httpClient.execute(httpGet);
            InputStream is = response.getEntity().getContent();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int count = 0;
            while ((count = is.read(buffer)) != -1)
            {
                baos.write(buffer, 0, count);
            }
            byte[] bytes = baos.toByteArray();
            bitmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmp;
    }
}
