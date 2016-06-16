package shen.xianjie.myapplication.FolderBean;

/**
 * Created by JXS on 2016/2/22.
 */
public class FolderBean
{
    private String dir;
    private String firstImgPath;
    private String name;
    private int Count;

    public String getDir()
    {
        return dir;
    }

    public void setDir(String dir)
    {
        this.dir = dir;
        int indexOfLast = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(indexOfLast + 1);

    }

    public String getFirstImgPath()
    {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath)
    {
        this.firstImgPath = firstImgPath;
    }

    public String getName()
    {
        return name;
    }

    public int getCount()
    {
        return Count;
    }

    public void setCount(int count)
    {
        Count = count;
    }
}
