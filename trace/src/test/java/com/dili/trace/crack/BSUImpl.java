//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dili.trace.crack;

import bsh.Interpreter;
import com.dili.ss.java.BSUI;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.util.Base64;
import java.util.Random;

public class BSUImpl implements BSUI {
    private static final Interpreter i = new Interpreter();
    private static final String charset = "UTF-8";
    private static final String algorithm = "DES";
    private static final BSUImpl bsu = new BSUImpl();

    public BSUImpl() {
    }

    public static final BSUImpl me() {
        return bsu;
    }

    @Override
    public Object g(String var1) {
        try {
            return i.get(var1);
        } catch (Exception var3) {
            return null;
        }
    }

    @Override
    public void s(String var1, Object var2) {
        try {
            i.set(var1, var2);
        } catch (Exception var4) {
        }

    }

    @Override
    public void sc(String var1) throws Exception {
        i.source(var1);
    }

    @Override
    public void e(String var1) {
        try {
            i.eval(var1);
        } catch (Exception var3) {
        }

    }

    @Override
    public void ef(String var1) {
        try {
            URL var2 = BSUImpl.class.getClassLoader().getResource(var1);
            InputStream var3 = (InputStream)var2.getContent();
            byte[] var4 = new byte[var3.available()];

            for(int var5 = var3.read(var4); var5 != -1; var5 = var3.read(var4)) {
            }

            i.eval(new String(var4));
        } catch (Exception var6) {
        }

    }

    @Override
    public void ex(String var1) throws Exception {
        i.eval(var1);
    }

    @Override
    public void dae(String var1, String var2) {
        try {
            i.eval(this.decrypt(var1, var2));
        } catch (Exception var4) {
        }

    }

    @Override
    public void dae(String var1) {
        this.dae(var1, "()Ljava/lang/Object;");
    }

    @Override
    public void daex(String var1, String var2) throws Exception {
        i.eval(this.decrypt(var1, var2));
    }

    @Override
    public void daex(String var1) throws Exception {
        this.daex(var1, "()Ljava/lang/Object;");
    }

    @Override
    public int r(int var1) {
        Random var2 = new Random();
        return Math.abs(var2.nextInt(var1));
    }

    public String encrypt(String var1, String var2) {
        String var3 = null;

        try {
            var3 = Base64.getEncoder().encodeToString(this.encryptByte(var1.getBytes("UTF-8"), var2));
            return var3;
        } catch (Exception var5) {
            throw new RuntimeException("encrypt exception", var5);
        }
    }

    public String encryptQuietly(String var1, String var2) {
        String var3 = null;

        try {
            var3 = Base64.getEncoder().encodeToString(this.encryptByteQuietly(var1.getBytes("UTF-8"), var2));
            return var3;
        } catch (Exception var5) {
            return null;
        }
    }

    public String decrypt(String var1, String var2) {
        String var3 = null;

        try {
            var3 = new String(this.decryptByte(Base64.getDecoder().decode(var1), var2), "UTF-8");
            System.out.println(var3);
            return var3;
        } catch (Exception var5) {
            throw new RuntimeException("decrypt exception", var5);
        }
    }

    public String decryptQuietly(String var1, String var2) {
        String var3 = null;

        try {
            var3 = new String(this.decryptByteQuietly(Base64.getDecoder().decode(var1), var2), "UTF-8");
            return var3;
        } catch (Exception var5) {
            return null;
        }
    }

    public byte[] encryptByte(byte[] var1, String var2) {
        Object var3 = null;
        Cipher var4 = null;

        byte[] var11;
        try {
            var4 = Cipher.getInstance("DES");
            var4.init(1, this.generateKey(var2));
            var11 = var4.doFinal(var1);
        } catch (Exception var9) {
            throw new RuntimeException("encryptByte exception", var9);
        } finally {
            var4 = null;
        }

        return var11;
    }

    public byte[] encryptByteQuietly(byte[] var1, String var2) {
        Object var3 = null;
        Cipher var4 = null;

        byte[] var11;
        try {
            var4 = Cipher.getInstance("DES");
            var4.init(1, this.generateKey(var2));
            var11 = var4.doFinal(var1);
        } catch (Exception var9) {
            throw new RuntimeException("encryptByteQuietly exception", var9);
        } finally {
            var4 = null;
        }

        return var11;
    }

    public byte[] decryptByte(byte[] var1, String var2) {
        Object var4 = null;

        Cipher var3;
        byte[] var11;
        try {
            var3 = Cipher.getInstance("DES");
            var3.init(2, this.generateKey(var2));
            var11 = var3.doFinal(var1);
        } catch (Exception var9) {
            throw new RuntimeException("decryptByte exception", var9);
        } finally {
            var3 = null;
        }

        return var11;
    }

    public byte[] decryptByteQuietly(byte[] var1, String var2) {
        Object var4 = null;

        Cipher var3;
        byte[] var11;
        try {
            var3 = Cipher.getInstance("DES");
            var3.init(2, this.generateKey(var2));
            var11 = var3.doFinal(var1);
        } catch (Exception var9) {
            throw new RuntimeException("decryptByteQuietly exception", var9);
        } finally {
            var3 = null;
        }

        return var11;
    }

    public Key generateKey(String var1) {
        try {
            DESKeySpec var2 = new DESKeySpec(var1.getBytes("UTF-8"));
            SecretKeyFactory var3 = SecretKeyFactory.getInstance("DES");
            return var3.generateSecret(var2);
        } catch (Exception var4) {
            throw new RuntimeException("generateKey exception", var4);
        }
    }
}
