package com.dili.trace.crack;

import com.dili.ss.java.BH;
import com.dili.ss.java.BI;
import com.dili.ss.java.BSUI;
import org.springframework.core.env.Environment;

import java.lang.reflect.Proxy;

public class B {

    public static final BSUI b;
    public static final BI bi;

    static {
        bi = (BI) Proxy.newProxyInstance(BI.class.getClassLoader(), new Class<?>[]{BI.class}, new BH());
        b = b();
    }

    public static void daeif(String s, String k, Environment e) {
        bi.daeif(s, k, e);
    }

    private static final BSUI b() {
        if (b != null) {
            return b;
        }
        //"script/water"
        return new BSUImpl();
    }


    private static String d(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return temp;
    }
}