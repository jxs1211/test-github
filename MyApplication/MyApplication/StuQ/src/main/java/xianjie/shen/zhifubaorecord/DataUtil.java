package xianjie.shen.zhifubaorecord;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import xianjie.shen.stuq2.R;

/**
 * Created by shen on 2016/6/13.
 */
public class DataUtil
{

    public static List<Bean> getDatasFromRes(Context context)
    {
        List<String> mTitleDatas = new ArrayList<String>();
        List<String> mContentDatas = new ArrayList<String>();
        String[] mTitles = context.getResources().getStringArray(R.array.titles);
        String[] mContents = context.getResources().getStringArray(R.array.contents);

        for (int i = 0; i < mTitles.length; i++)
        {
            mTitleDatas.add(mTitles[i]);
        }
        for (int i = 0; i < mContents.length; i++)
        {
            mContentDatas.add(mContents[i]);
        }

        List<Bean> beans = new ArrayList<Bean>();

        for (int i = 0; i < mTitles.length; i++)
        {
            Bean bean = new Bean();
            bean.setTitle(mTitles[i]);
            bean.setContent(mContents[i]);
            switch (i)
            {
                case 2:
                    bean.setType(Bean.Type.DETAIL);
                    break;
                case 4:
                    bean.setType(Bean.Type.QUES);
                    break;
                default:
                    bean.setType(Bean.Type.NORMAL);
            }
            beans.add(bean);
        }
        return (List<Bean>) beans;

    }

}
