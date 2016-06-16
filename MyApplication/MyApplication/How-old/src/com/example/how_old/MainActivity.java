package com.example.how_old;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.zhy.utils.AppUtils;
import com.zhy.utils.L;
import com.zhy.utils.T;

public class MainActivity extends Activity implements OnClickListener
{

    protected static final int CALLBACK_SUCCESS = 0;
    protected static final int CALLBACK_ERROR = 1;
    private Button mGetImage;
    private Button mDetect;
    private ImageView mPhoto;
    private View mWaitting;
    private int mRequestCode = 0x11;
    private static final int mCAMERA_SUCCESS = 1;
    private static final int mPHOTO_SUCCESS = 2;
    private String mCurrentPhotoPath;
    private Bitmap mBitmap;
    private ProgressBar mBar;
    private Paint mPaint;
    private Uri imageUri;

    // private TextView mtag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appName = AppUtils.getAppName(this);
        L.e(appName);
        T.showLong(this, "Welcom to " + appName);
        initViews();
        initEvents();
    }

    private void initEvents()
    {
        mGetImage.setOnClickListener(this);
        mDetect.setOnClickListener(this);
    }

    private void initViews()
    {
        mGetImage = (Button) findViewById(R.id.id_getImage);
        mDetect = (Button) findViewById(R.id.id_detect);
        mPhoto = (ImageView) findViewById(R.id.id_imageView);
        mWaitting = findViewById(R.id.id_waitting);
        mBar = (ProgressBar) findViewById(R.id.id_progressBar);
        mPaint = new Paint();
        // mPaint.setColor(Paint.ANTI_ALIAS_FLAG);
        //绘制人脸框使用的paint
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(5);
        // mtag = (TextView) mWaitting.findViewById(R.id.id_tag);
    }

    // 解析返回的JsonObject，让UI主线程进行界面更新
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            mBar.setVisibility(View.GONE);
            switch (msg.what)
            {
                case CALLBACK_SUCCESS:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    Log.e("shen", "prepareJsToBm");
                    prepareJsToBm(jsonObject);
                    // 将处理完成后的图片（带有年龄和性别标记）设置在主界面
                    mPhoto.setImageBitmap(mBitmap);

                    break;
                case CALLBACK_ERROR:
//												mBar.setVisibility(View.GONE);
                    String errString = (String) msg.obj;
                    if (errString.isEmpty())
                    {
                        // mTip.setText("error.");
                        Toast.makeText(
                                MainActivity.this,
                                "error", Toast.LENGTH_SHORT)
                                .show();
                    } else
                    {
                        // mTip.setText(errString);
                        Toast.makeText(
                                MainActivity.this,
                                errString, Toast.LENGTH_SHORT)
                                .show();
                    }

                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.id_detect:
                L.e("she-id_detect", "onClick id_detect");
                // 点击detect后让进度条加载状态可见
                // mWaitting.setVisibility(View.VISIBLE);
                mBar.setVisibility(View.VISIBLE);
                if (mCurrentPhotoPath == null
                        && mCurrentPhotoPath.trim().equals(""))
                {//如果没点击get按钮获得图片路径，就设置bitmap为默认图片
                    mBitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.t4);
                }
                //拿到图片后上传服务器解析
                detectBitmap();
                break;
            case R.id.id_getImage:
                L.e("she-id_getImage", "onClick id_getImage");
                Log.e("she-id_getImage", "onClick id_getImage");
                getImage();
                break;
        }
    }

    private void detectBitmap()
    {
        FaceppDectect faceppDectect = new FaceppDectect();
        Log.e("shen", "faceppDectect.detect");
        // 将resizePhoto()得到的mBitmap发给faceppDectect解析
        faceppDectect.detect(mBitmap, new FaceppDectect.CallBack()
        {
            @Override
            public void success(JSONObject result)
            {//从服务器获取返回数据
                Message msg = Message.obtain();
                msg.what = CALLBACK_SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }

            @Override
            public void error(FaceppParseException e)
            {//从服务器获取数据失败
//                e = new FaceppParseException("get a error", 1, "无法识别年龄", 400);
                Message msg = Message.obtain();
                msg.what = CALLBACK_ERROR;
                msg.obj = e.getErrorMessage();
                mHandler.sendMessage(msg);
            }
        });
    }

    protected void prepareJsToBm(JSONObject jsonObject)
    {
        // 创建一个与mBitmap尺寸、配置一样的bitmap，bitmap是空的
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(),
                mBitmap.getHeight(), mBitmap.getConfig());

        // Bitmap bitmap = Bitmap.createBitmap(mBitmap, 0, 0,
        // mBitmap.getWidth(),
        // mBitmap.getHeight());
        // 准备在空的bitmap上画图
        Canvas canvas = new Canvas(bitmap);
        // left ：The position of the left side of the bitmap being drawn
        // top ：The position of the top side of the bitmap being drawn

        // 切记传入的不是bitmap而是原来没绘制前的mBitmap，因为bitmap是空的没有内容
        // 将mbitmap画到canvas上
        canvas.drawBitmap(mBitmap, 0, 0, null);
        try
        {
            JSONArray faces = jsonObject.getJSONArray("face");
            // mTip.setText("find " + faces.length());
            Toast.makeText(MainActivity.this,
                    "在该图片中检测到" + faces.length() + "张脸", Toast.LENGTH_LONG).show();
            // 遍历每个face并对其进行描述标记性别和年龄
            for (int i = 0; i < faces.length(); i++)
            {
                JSONObject object = faces.getJSONObject(i);
                // 这四个值是表示相对bitmap的百分比
                float cx = (float) object.getJSONObject("position")
                        .getJSONObject("center").getDouble("x");
                float cy = (float) object.getJSONObject("position")
                        .getJSONObject("center").getDouble("y");
                float faceWidth = (float) object.getJSONObject("position")
                        .getDouble("width");
                float faceHeight = (float) object.getJSONObject("position")
                        .getDouble("height");
                // 切记不能写成：cx = cx / bitmap.getWidth() * 100
                cx = cx / 100 * bitmap.getWidth();
                cy = cy / 100 * bitmap.getHeight();
                faceWidth = faceWidth / 100 * bitmap.getWidth();
                faceHeight = faceHeight / 100 * bitmap.getHeight();

                float left = cx - faceWidth / 2;
                float top = cy - faceHeight / 2;
                float right = cx + faceWidth / 2;
                float bottom = cy + faceHeight / 2;
                // 框出脸部
                canvas.drawLine(left, top, left, bottom, mPaint);//画左边竖线
                canvas.drawLine(left, top, right, top, mPaint);//画上横线
                canvas.drawLine(right, top, right, bottom, mPaint);//画右边竖线
                canvas.drawLine(left, bottom, right, bottom, mPaint);//下横线
                // 标记年龄和性别
                String gender = object.getJSONObject("attribute")
                        .getJSONObject("gender").getString("value");
                int age = object.getJSONObject("attribute")
                        .getJSONObject("age").getInt("value");
                /*
                 * mPaint.setColor(Color.RED); String tag = gender + " " + age;
				 * canvas.drawText(tag, left, top - faceHeight / 4, mPaint);
				 */
                Bitmap tagBitmap = getFaceTagBm(age, "Male".equals(gender));
                // 设定tagBitmap的尺寸随图片bitmap的尺寸大小调整,防止bitmap太小时tagBitmap过大

                int tx = tagBitmap.getWidth();
                int ty = tagBitmap.getHeight();
                if (mBitmap.getWidth() < mPhoto.getWidth()
                        && mBitmap.getHeight() < mPhoto.getHeight())
                {//如果bitmap比imageView小，按照两者宽比和两者高比更大的，也就是更接近入imageView尺寸的值设置ratio
                    // 取两者比例的最大值作为缩放比例
                    // Log.e("shen",
                    // "i1:" + mBitmap.getWidth() / mPhoto.getWidth()
                    // + "i2:" + mBitmap.getHeight()
                    // / mPhoto.getHeight() + "mBitmap.getWidth:"
                    // + mBitmap.getWidth() + "mPhoto.getWidth:"
                    // + mPhoto.getWidth() + "mBitmap.getHeight:"
                    // + mBitmap.getHeight() + "mPhoto.getHeight:"
                    // + mPhoto.getHeight());

                    float ratio = Math.max(
                            (float) mBitmap.getWidth()
                                    / (float) mPhoto.getWidth(),
                            (float) mBitmap.getHeight()
                                    / (float) mPhoto.getHeight());
                    // int ratio1 = Math.max(
                    // mBitmap.getWidth() / mPhoto.getWidth(),
                    // mBitmap.getHeight() / mPhoto.getHeight());
                    int dstWidth = (int) (tx * ratio);
                    int dstHeight = (int) (ty * ratio);
                    tagBitmap = Bitmap.createScaledBitmap(tagBitmap, dstWidth,
                            dstHeight, false);
                }
                // 画上标有tag（年龄和性别）的bitmap
                canvas.drawBitmap(tagBitmap, cx - tagBitmap.getWidth() / 2, cy
                        - faceHeight / 2 - tagBitmap.getHeight(), null);
                L.e("shen-tagBm", "w:" + tagBitmap.getWidth() + " h:" + tagBitmap.getHeight());
                L.e("shen-bitmap", "w:" + bitmap.getWidth() + " h:" + bitmap.getHeight());
            }
            mBitmap = bitmap;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private Bitmap getFaceTagBm(int age, boolean isMale)
    {
        TextView tag = (TextView) mWaitting.findViewById(R.id.id_tag);
        tag.setText(age + "");
        //在显示年龄的字体左边绘制性别图片
        if (isMale)
        {
            tag.setCompoundDrawablesWithIntrinsicBounds(getResources()
                    .getDrawable(R.drawable.male), null, null, null);
        } else
        {
            tag.setCompoundDrawablesWithIntrinsicBounds(getResources()
                    .getDrawable(R.drawable.female), null, null, null);
        }
        // 将textView转换成bitmap(切记布局文件中android:visibility=invisible不是gone，否则getDrawingCache为null)
        tag.setDrawingCacheEnabled(true);
        Bitmap tagBitmap = Bitmap.createBitmap(tag.getDrawingCache());
        // Frees the resources used by the drawing cache.
        tag.destroyDrawingCache();

        return tagBitmap;
    }

    private void getImage()
    {
        final CharSequence[] items = new String[]{"相机拍摄", "手机相册"};
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择图片")
                .setItems(items, new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 1)
                        {
                            Log.e("shen", "图片");
                            openActivity(Intent.ACTION_PICK);
                        } else
                        {
                            Log.e("shen", "拍照");
                            openActivity("android.media.action.IMAGE_CAPTURE");
                        }
                    }
                }).show();
        // Intent intent = new Intent(Intent.ACTION_PICK);
        // intent.setType("image/*");
        // startActivityForResult(intent, mRequestCode);
    }

    private void openActivity(String intentType)
    {//相册获取图片
        Intent intent = new Intent(intentType);
        if (intentType == Intent.ACTION_PICK)
        {
            intent.setType("image/*");
            startActivityForResult(intent, mPHOTO_SUCCESS);
        } else if (intentType == "android.media.action.IMAGE_CAPTURE")
        {//拍照获取图片
            File capture = new File(Environment.getExternalStorageDirectory(),
                    getPhotoFileName());
            Log.e("shen", getPhotoFileName());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capture));
            // imageUri = Uri.fromFile(capture);
            startActivityForResult(intent, mCAMERA_SUCCESS);
        }
    }

    private String getPhotoFileName()
    {
        Date date = new Date(java.lang.System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return format.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent)
    {
        if (requestCode == mPHOTO_SUCCESS)
        {
            if (intent != null)
            {
                Uri uri = intent.getData();
                Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null);
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                // 得到图片的路径
                mCurrentPhotoPath = cursor.getString(idx);
                cursor.close();
                L.e("she-mPHOTO_SUCCESS", mCurrentPhotoPath);
                // resizePhoto();// return a resized bitmap
//                mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                if (mBitmap == null)
                {
                    L.e("she-mBitmap", "mBitmap null");
//					Log.e("shen-mBitmap", "mBitmap null");
                }
//				mPhoto.setImageBitmap(mBitmap);
                // mTip.setText("click Button Detect -->");
            }
        } else if (requestCode == mCAMERA_SUCCESS)
        {
            if (intent != null)
            {
                Bundle extras = intent.getExtras();
                imageUri = (Uri) extras.get(MediaStore.EXTRA_OUTPUT);
                L.e("shen1", imageUri.toString());
                Cursor cursor = getContentResolver().query(imageUri, null,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                // 得到图片的路径
                mCurrentPhotoPath = cursor.getString(idx);
                L.e("she-mCAMERA_SUCCESS", mCurrentPhotoPath);
                cursor.close();
            }
        }
        resizePhoto();// make mBitmap to a resized bitmap(the size of bitmap cannot larger than 3M)
        mPhoto.setImageBitmap(mBitmap);
//		super.onActivityResult(requestCode, resultCode, intent);
    }

    private void resizePhoto()
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只decode尺寸,不加载到内存
        options.inJustDecodeBounds = true;
        // 让图片尺寸不要超过1024
        float ratio = Math.max(options.outWidth / 1024f,
                options.outHeight / 1024f);
        // 取压缩值的整数
        // @inSampleSize:If set to a value > 1, requests the decoder to
        // subsample the original image, returning a smaller image to save
        // memory.
        options.inSampleSize = (int) Math.ceil(ratio);
        // decode前将options修改回inJustDecodeBounds为false,不然不能decode出图片
        options.inJustDecodeBounds = false;
        L.e("shen inSampleSize", "inSampleSize: " + options.inSampleSize);
        mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
    }
}
