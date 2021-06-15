package com.dili.trace.crack;

import com.dili.ss.java.B;
import com.dili.ss.java.BH;
import com.dili.ss.java.BI;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Proxy;

public class B_Test {
    @Test
    public void b_test() {
        BI bi = (BI) Proxy.newProxyInstance(BI.class.getClassLoader(), new Class<?>[]{BI.class}, new BH());
        ReflectionTestUtils.setField(B.class, "b", new BSUImpl());
        ReflectionTestUtils.setField(B.class, "bi", bi);
    }

}
