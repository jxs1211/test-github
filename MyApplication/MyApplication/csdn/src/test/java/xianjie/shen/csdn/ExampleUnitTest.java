package xianjie.shen.csdn;

import org.junit.Test;

import xianjie.shen.bean.CommonException;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect() throws Exception
    {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test01()
    {
        try
        {
            String s = DataUtil.doGet("http://www.tuicool.com/articles/juIjue");
            if (s != null)
            {
                System.out.println("----------------------");
                System.out.println(s);
            }
        } catch (CommonException e)
        {
            e.printStackTrace();
        }
    }
}