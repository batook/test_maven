package com.batook.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Mocking {

    @Mock
    private A injectA;
    @InjectMocks
    private Core core;

    @Test
    public void test() {
        Core core2 = new Core();
        A mockA = mock(A.class);
        when(injectA.getName()).thenReturn("injectA");
        core.invoke();
        System.out.println(">" + core.getName());
        System.out.println(">" + injectA.getName());
        System.out.println(">" + mockA.getName());
        core2.invoke();
    }

}

class Core {
    private A a = new A();
    private C c = new C();

    public void invoke() {
        C localC = new C();
        System.out.println(this + localC.getName());
        System.out.println(this + c.getName());
        System.out.println(this + a.getName());
    }

    String getName() {
        return this + c.getName();
    }

}

class A extends C {
    private B b;
}

class B extends C {

}

class C {
    public String getName() {
        return getClass().getName();
    }
}