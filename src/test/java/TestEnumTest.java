import org.junit.Assert;
import org.junit.Test;

public class TestEnumTest {

    @Test
    public void test1() {
        Assert.assertEquals("SIT", TestEnum.Environment.get("https://sit.domain.com:2019/").toString());
        Assert.assertEquals("https://prod.domain.com:1088/", TestEnum.Environment.PROD.getUrl());
    }

}