package xianjie.shen.csdn.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xianjie.shen.bean.NewsItem;

/**
 * Created by shen on 2016/6/9.
 */
public class NewsItemDao
{
    private DBHelper dbHelper;

    public NewsItemDao(Context context)
    {
        this.dbHelper = new DBHelper(context);
    }

    public void add(NewsItem newsItem)
    {
        Log.e("add news newstype ", "" + newsItem.getNewsType());
        String sql = "insert into tb_newsItem (title,link,date,imgLink,content,newstype) values(?,?,?,?,?,?) ;";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, new Object[]{newsItem.getTitle(), newsItem.getLink(), newsItem.getDate(), newsItem.getImgLink(), newsItem.getContent(), newsItem.getNewsType()});
        db.close();
    }

    public void deleteAll(int newsType)
    {
        String sql = "delete from tb_newsItem from where newsType = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, new Object[]{newsType});
        db.close();
    }

    public void add(List<NewsItem> newsItems)
    {
        for (NewsItem newsItem : newsItems)
        {
            add(newsItem);
        }
    }

    public List<NewsItem> list(int newsType, int currentPage)
    {
        Log.e("newsType ", newsType + "");
        Log.e("currentPage ", currentPage + "");
        // 0 - 9 , 10 - 19 ,
        List<NewsItem> newsItems = new ArrayList<NewsItem>();

        try
        {
            int offset = 10 * (currentPage - 1);
            String sql = "select title,link,date,imgLink,content,newstype from tb_newsItem where newstype = ? limit ?,?";
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(sql, new String[]{newsType + "", offset + "", "" + (offset + 10)});

            NewsItem newsItem = null;
            while (c.moveToNext())
            {
                newsItem = new NewsItem();

                String title = c.getString(0);
                String link = c.getString(1);
                String date = c.getString(2);
                String imgLink = c.getString(3);
                String content = c.getString(4);
                Integer newstype = c.getInt(5);

                newsItem.setTitle(title);
                newsItem.setLink(link);
                newsItem.setDate(date);
                newsItem.setContent(content);
                newsItem.setImgLink(imgLink);
                newsItem.setNewsType(newsType);

                newsItems.add(newsItem);
            }
            db.close();
            c.close();
            Log.e("newsItem.size ", newsItems.size() + "");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return newsItems;
    }


}
