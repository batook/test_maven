package com.batook.test;

import org.junit.Test;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTest {
    static final Logger logger = LoggerFactory.getLogger(TestTest.class);

    @BeforeClass
    public static void bc() {
        System.out.println("BeforeClass");
    }

    @AfterClass
    public static void ac() {
        System.out.println("AfterClass");
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
    public void test() {
        logger.info("test");
        com.batook.test.Test.main(null);
        com.batook.test.Test.deadlock();
        com.batook.test.Test.fibonacci2(1);
        com.batook.test.Test.fibonacci2(2);
        com.batook.test.Test.fibonacci2(5);
        com.batook.test.Test.factorialStreams(1L);
        com.batook.test.Test.factorialRecursive(1L);
        com.batook.test.Test.factorialRecursive(2L);
        com.batook.test.Test.charFreq();
        com.batook.test.Test.missingN();
        com.batook.test.Test.test();
        com.batook.test.Test t = new com.batook.test.Test("A");
        assertEquals(false, t.equals(null));
        assertEquals(false, t.equals(new Object()));
        assertEquals(false, t.equals(new com.batook.test.Test("B")));
        assertEquals(true, t.equals(t));
        assertTrue(true);
    }

    @Test
    public void arrayReversalDemo() {
        com.batook.test.ArrayReversalDemo.main(null);
        assertTrue(true);
    }

    @Test
    public void testEnum() {
        com.batook.test.TestEnum.main(null);
        assertTrue(true);
    }

    @Test
    public void tesClasses() {
        DecoratorTest t = new DecoratorTest();
        DecoratorTest.main(null);
        SingletonTest.main(null);
        TT.main(null);
        new TT().testReduceCollect();
        TestLambda.main(null);
    }
}