package xianjie.shen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity
    {
    private static final long DELAY_START_TIME = 1500L;
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private boolean isFirstIn = false;
    private Handler mHandler = new Handler();

    private void initViews()
        {
        this.mHandler.postDelayed(new Runnable()
                                      {
                                      public void run()
                                          {
                                          WelcomeActivity.this.openActivity(MainActivity.class);
                                          }
                                      }
                , 1500L);
        }

    private void openActivity(Class paramClass)
        {
        startActivity(new Intent(this, paramClass));
//        overridePendingTransition(this.activityCloseEnterAnimation, this.activityCloseExitAnimation);
        finish();
        overridePendingTransition(this.activityCloseEnterAnimation, this.activityCloseExitAnimation);
        }

    private void setActivityEnterAndExitAnimation()
        {
//        TypedArray localTypedArray = getTheme().obtainStyledAttributes(new int[]{16842926});
//        int i = localTypedArray.getResourceId(0, 0);
//        localTypedArray.recycle();
//        localTypedArray = getTheme().obtainStyledAttributes(i, new int[]{16842938, 16842939});
//        this.activityCloseEnterAnimation = localTypedArray.getResourceId(0, 0);
//        this.activityCloseExitAnimation = localTypedArray.getResourceId(1, 0);
//        localTypedArray.recycle();
        }

    public void finish()
        {
        super.finish();
        overridePendingTransition(this.activityCloseEnterAnimation, this.activityCloseExitAnimation);
        }

    protected void onCreate(Bundle paramBundle)
        {
        super.onCreate(paramBundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(myapplication.R.layout.welcome);
        initViews();
        }
    }

/* Location:           E:\tools\apktool\dex2jar-2.0\classes-dex2jar.jar
 * Qualified Name:     shen.xianjie.myapplication.xianjie.shen.myapplication.WelcomeActivity
 * JD-Core Version:    0.6.2
 */