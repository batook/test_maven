import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestTest {

    @BeforeClass
    public static void bc() {
        System.out.println("0");
    }

    @Before
    public void init() {
        System.out.println("1");
    }

    @After
    public void close() {
        System.out.println("2");
    }

    @Test
    public void go() {
        assertTrue(true);
    }

    @Test
    public void go2() {
        assertTrue(true);
    }

    @Test
    public void go3() {
        assertTrue(true);
    }
}