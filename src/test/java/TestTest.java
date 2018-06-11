import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestTest {

    @BeforeClass
    public static void bc() {
        System.out.println("BeforeClass");
    }

    @Before
    public void init() {
        System.out.println("Before");
    }

    @After
    public void close() {
        System.out.println("After");
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