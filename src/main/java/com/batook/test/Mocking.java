package com.batook.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Mocking {
    Logger log = Logger.getLogger(Mocking.class);

    @Mock
    private A mock;
    @InjectMocks
    private Core core;

    @Test
    public void test() {
        Core mockCore = mock(Core.class);
        A mockA = mock(A.class);
        when(mock.getName()).thenReturn("mockGetName");
        when(mockA.getName()).thenReturn("mockAGetName");
        //when(core.getName()).thenReturn("doesnt work");
        when(mockCore.getName()).thenReturn("its working");
        core.invoke();
        log.info(">" + core.getName());
        log.info(">" + mockCore.getName());
        log.info(">" + mock.getName());
        log.info(">" + mockA.getName());
        System.out.printf("mockCore.invoke()");
        mockCore.invoke();
    }

}

class Core {
    private A a = new A();
    private C c = new C();

    public void invoke() {
        System.out.printf(">>>> invoke");
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