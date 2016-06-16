package xianjie.shen.zhifubaorecord;

/**
 * Created by shen on 2016/6/13.
 */
public class Bean
{
    private String title;
    private String content;
    private String jine;
    private Type type;

    public enum Type
    {
        NORMAL, DETAIL, QUES
    }

    public String getJine()
    {
        return jine;
    }

    public void setJine(String jine)
    {
        this.jine = jine;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}
