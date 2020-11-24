package com.dili.trace.util;

import com.dili.common.exception.TraceBizException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

/**
 * 小程序手机号码解密
 *
 * @author admin
 */
public class AppletAesUtil {
    private static Logger logger = LoggerFactory.getLogger(AppletAesUtil.class);
    /**
     * 算法名称
     */

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/模式/填充方式
     */
    private static final String ALGORITHMSTR = "AES/CBC/PKCS7Padding";
    /**
     * key
     */
    private static Key key;
    private static Cipher cipher;

    public static void init(byte[] keyBytes) {
        try {
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base
                        + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }

            Security.addProvider(new BouncyCastleProvider());
            key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHMSTR);
        } catch (Exception e) {
            logger.error("init Applet Key error", e);
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedDataStr 要解密的字符串
     * @param keyBytesStr      解密密钥
     * @return
     */
    public static String decrypt(String encryptedDataStr, String keyBytesStr,
                                 String ivStr) {
        String decryStr = "";
        try {
            byte[] sessionkey = Base64.decodeBase64(keyBytesStr);
            byte[] encryptedData = Base64.decodeBase64(encryptedDataStr);
            byte[] iv = Base64.decodeBase64(ivStr);
            init(sessionkey);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] encryptedText = cipher.doFinal(encryptedData);
            if (encryptedText != null) {
                decryStr = new String(encryptedText, "UTF-8");
            }
        } catch (Exception e) {
            logger.error("decrypt error", e);
            throw new TraceBizException("解密手机号码错误", e);
        }
        return decryStr;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//       String content = "W90bkymzEPP2Whbr62X0OfJU10aLtHK80GBgV+QKDDn+gAg1HEROqW7isycf01nUgDUJVUVQZQYCIcBFH2Ovhf0usJ8DMqK4S/zDh9krOfApqm4p0N53TC5A2cWZSMhvBlPyxBJpzFnsbh8o8zTvjHiRE0EZ8zc5StUAgXbPQiYxXSom1uf6/xRoDhOJGMSs47VOccNaGuNBlu8cd2UGjg==";
//       String key = "Iq4E7+CZlRkuvB1RaU+qpA==";
//       String iv = "EopC2cY9UI8FQiDYqECR9A==";


        String content = "R6sEw39bC4LpuW1Rd3r74SCFqRRwMQXz63nDnGrSEuokggzfa/KVRYU/rDOkWADGlMYZJxi888GyYo0+rgT6jW/Y4cXZmsFk+dTymqcebTXhaiY1fTyCkd161xZlnAjgskWj/0u8RqSGWGyAPbvjwrNb87GhlATaFm29qsiXq361BxVFDKEVFeQd4PE3llCQDzzHtjQTy0+MGK8C8v8pog==";
        String key = "2It8+4LNyM/XuqDsrMLznQ==";
        String iv = "mmnNX8Aq/QW10JxkjbFEiQ==";
        String result = AppletAesUtil.decrypt(content, key, iv);
        System.out.println("result:" + result);
    }

}
