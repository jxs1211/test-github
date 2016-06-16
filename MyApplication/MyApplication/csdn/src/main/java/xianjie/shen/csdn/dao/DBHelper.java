package xianjie.shen.csdn.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shen on 2016/6/9.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "csdn_app";

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /**
         * id,title,link,date,imgLink,content,newstype
         */
        String sql = "create table tb_newsItem( _id integer primary key autoincrement , " + " title text , link text , date text , imgLink text , content text , newsType integer );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {

    }
}
