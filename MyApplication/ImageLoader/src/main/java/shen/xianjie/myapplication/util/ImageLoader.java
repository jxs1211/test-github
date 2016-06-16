package shen.xianjie.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhy.utils.L;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类
 * Created by JXS on 2016/2/18.
 */
public class ImageLoader
{
    private static ImageLoader mInstance;
    /**
     * 缓存图片的核心类
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池:执行加载图片的任务
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**
     * 队列调度方式
     */
    private Type mType = Type.LIFO;

    public enum Type
    {
        FIFO, LIFO//先进先出，后进先出
    }

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;//LinkedList有从头部和尾部取元素的方法

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);//信号量为0表示必须release执行完成后才能释放给acquire，如果为3，表示代码每次执行到release时信号量减一，代码可以最多并行执行3次，第四次执行时信号量为0就停止，直到释放一个信号量，给acquire方法一个可以继续运行的permit
    private Semaphore mSemaphoreThreadPool;
    /**
     * UI线程中的handler
     */
    private Handler mUIHandler;


    private ImageLoader()
    {
        init(DEFAULT_THREAD_COUNT, Type.LIFO);
    }

    /**
     * @param threadCount 同时加载图片的数量
     * @param type
     */
    public ImageLoader(int threadCount, Type type)
    {
        init(threadCount, type);
    }

    /**
     * 初始化ImageLoader的准备工作：
     * 1、开启一个多线程用于接收和执行任务mPoolThreadHandler
     * 2、定义线程的大小
     * 3、定义可以同时执行的线程个数
     * 4、定义缓存图片的内存大小
     * 5、什么任务队列mTaskQueue
     *
     * @param threadCount 线程数量
     * @param type        取任务队列的方式
     */
    private void init(int threadCount, Type type)
    {
        //后台轮询线程
        mPoolThread = new Thread()
        {
            @Override
            public void run()
            {
                //用Handler、looper、message实现
                Looper.prepare();
//                if (mPoolThreadHandler == null)
                mPoolThreadHandler = new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        try
                        {
                            //利用信号量控制任务队列,只有当前任务执行完成后再从pool中取任务（为防止Task被添加到ThreadPool后，就被mThreadPool执行）
                            mSemaphoreThreadPool.acquire();// 此处设置的mSemaphore为1，即同时运行1个线程执行任务，当执行完了以后才能获得permit继续向下执行
                        } catch (InterruptedException e)
                        {
                        }
                        //在线程池中取出一个任务（Runnable）去执行
                        mThreadPool.execute(getTask());
                    }
                };
                //mPoolThreadHandler初始化完成，返回给acquire方法，让其继续往下执行
                mSemaphorePoolThreadHandler.release();
                //让looper在后台不断轮询
                Looper.loop();
            }
        };
        mPoolThread.start();
        //应用可用的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;//分配给图片的内存
        L.e("shenshen", "cacheMemory: " + cacheMemory);
        mLruCache = new LruCache<String, Bitmap>(cacheMemory)
        {
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                //获取每张bm内存大小
                int picSize = value.getRowBytes() * value.getHeight();
                L.e("shenshen", "picSize: " + picSize);
                return picSize;
            }
        };
        //定义线程池大小
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        //定义允许最多可以同时执行的thread数量
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 从任务队列（mTaskQueue）取一个任务执行
     *
     * @return
     */
    private Runnable getTask()
    {
        if (mType == Type.FIFO)
        {
            return mTaskQueue.removeFirst();
        } else if (mType == Type.LIFO)
        {//从last取出Runnable执行
            return mTaskQueue.removeLast();
        }
        return null;
    }

    /**
     * 单例模式获取ImageLoader对象
     *
     * @return ImageLoader的实例对象
     */
    public static ImageLoader getInstance()
    {
        if (mInstance == null)
        {
            synchronized (ImageLoader.class)//同步处理：多个线程逐个的进入执行
            {//双重判断防止实例化多个ImageLoader对象
                if (mInstance == null)
                {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type)
    {
        if (mInstance == null)
        {
            synchronized (ImageLoader.class)
            {//双重判断防止实例化多个ImageLoader对象
                if (mInstance == null)
                {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    /**
     * 在UI线程为imageView设置图片
     *
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView)
    {
        //标记请求path对应的imageView,用于后续做检验
        imageView.setTag(path);
        if (mUIHandler == null)
        {
            mUIHandler = new Handler()
            {//用sendMessage才会调用到这里
                @Override
                public void handleMessage(Message msg)
                {
                    //获取LruCache中的图片，为imageView设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    String path = holder.path;
                    ImageView imageview = holder.imageView;
                    Bitmap bitmap = holder.bm;
                    if (imageview.getTag().toString().equals(path))
                    {//判断path是不是与getTag存储的一致，保证滑动时刷新显示的是新path的图片
                        imageview.setImageBitmap(bitmap);
                        L.e("shen", "path: " + path);
                    }
                }
            };
        }
        Bitmap bm = getBitmapFromLruCache(path);

        if (bm != null)
        {
            refreshBitmap(path, imageView, bm);
        } else
        {
            addTask(new Runnable()
            {
                @Override
                public void run()
                {
                    //加载图片
                    //压缩图片
                    //1、获取图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2、获取压缩后的图片
                    Bitmap bm = decodeBitmapInSampleSize(path, imageSize.width, imageSize.height);
                    L.e("shen", "imageSize.width: " + imageSize.width + ",imageSize.h: " + imageSize.height);
                    //3、加载压缩后的图片到LruCache
                    mLruCache.put(path, bm);
                    L.e("shen", "mLruCache.size(): " + mLruCache.size());
                    refreshBitmap(path, imageView, bm);

                    //mSemaphore此例设置为1，即当执行完1个task后，释放信号量，让acquire继续往下执行，继续从pool中取任务
                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    /**
     * sendMessage让UIHandler更新界面
     *
     * @param path
     * @param imageView
     * @param bm
     */
    private void refreshBitmap(String path, ImageView imageView, Bitmap bm)
    {
        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bm = bm;
        holder.path = path;
        holder.imageView = imageView;//imageView在开始设置了tag为图片的path
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    private Bitmap decodeBitmapInSampleSize(String path, int reqWidth, int reqHeight)
    {
        //获取用于压缩bm的option，但不加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//获取option宽高尺寸但不加载到内存中
        BitmapFactory.decodeFile(path, options);//获取path的图片实际尺寸

        options.inSampleSize = getInSampleSize(options, reqWidth, reqHeight);
        //得到inSampleSize后，恢复option的inJustDecodeBounds，并加载到内存
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        return bm;
    }

    /**
     * 根据需求的size和图片实际的size设置sampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int getInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        //图片实际尺寸
        int width = options.outWidth;
        int height = options.outHeight;

        int sampleSize = 1;

        if (width > reqWidth || height > reqHeight)
        {//图片的实际尺寸大于了imageView的期望尺寸
            int widthRadio = Math.round(width * 1.0f / reqWidth);//Math.round 四舍五入
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            //最大值：节省内存空间
            //最小值：降低图片失真率
            //此处可以根据项目的实际情况选择最大值或最小值
            sampleSize = Math.max(widthRadio, heightRadio);
        }
        L.e("shen sampleSize", "sampleSize: " + sampleSize);
        return sampleSize;
    }

    /**
     * 压缩前获取显示图片的ImageView宽高
     *
     * @param imageView
     * @return
     */
    private ImageSize getImageViewSize(ImageView imageView)
    {
        DisplayMetrics displayMetric = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        ImageSize imageSize = new ImageSize();

//        int width = imageView.getWidth();
//        int height = imageView.getHeight();
        //width
        int width = imageView.getWidth();//获取imageView的实际宽度
        if (width <= 0)
        {//getWidth获取不到，就尝试在layout中获取
            width = lp.width;//imageView.getWidth没有成功，尝试通过layout布局获取
        }
        if (width <= 0)
        {//layout设置为matchparent（-1）或者wrapcontent（-2），就尝试用反射获取mMaxWidth的值
            //width = imageView.getMaxWidth();
            //通过反射的方法获取到ImageView的mMaxWidth值，这样可以兼容api16一下的sdkF
            width = getViewFieldValue("mMaxWidth", imageView);//再尝试通过最大宽度获取
        }
        if (width <= 0)
        {
            width = displayMetric.widthPixels;//最后仍然获取不了，就根据根据屏幕宽度设置宽度
        }

        //height
        int height = imageView.getHeight();
        if (height <= 0)
        {
            height = lp.height;
        }
        if (height <= 0)
        {
//            height = imageView.getMaxHeight();
            height = getViewFieldValue("mMaxHeight", imageView);
        }
        if (height <= 0)
        {
            height = displayMetric.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    private int getViewFieldValue(String fieldName, ImageView imageView)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(imageView);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
            {
                return value = fieldValue;
            }
        } catch (Exception e)
        {
        }
        return 0;
    }

    /**
     * imageView尺寸类
     */
    private class ImageSize
    {
        int width;
        int height;
    }

    private synchronized void addTask(Runnable runnable)
    {
        mTaskQueue.add(runnable);
        if (mPoolThreadHandler == null)
        {
//            mPoolThreadHandler = new Handler();
            try
            {
                //如果mPoolThreadHandler为空，暂停继续往下执行，发出信号量请求，等待release方法的响应，再继续往下执行
                mSemaphorePoolThreadHandler.acquire();
            } catch (InterruptedException e)
            {
            }
        }
        mPoolThreadHandler.sendEmptyMessage(0x1211);
    }

    private Bitmap getBitmapFromLruCache(String path)
    {
        return mLruCache.get(path);
    }

    class ImageBeanHolder
    {
        String path;
        Bitmap bm;
        ImageView imageView;
    }
}

