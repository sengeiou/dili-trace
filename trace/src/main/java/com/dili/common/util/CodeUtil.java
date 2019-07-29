package com.dili.common.util;

import java.util.UUID;

public class CodeUtil {

    private static String[] arr=new String[]{"0","1","2","3","4","5","6","7","8","9"};
    private static int arrLength=arr.length;

    /**
     * 获取指定位数的码
     * @param len 1-8的数字
     * @return
     */
    public static String get(int len){
        if(len<1 || len>8){
            len=8;
        }
        StringBuffer buff=new StringBuffer(len);
        String str= UUID.randomUUID().toString().replace("-","");
        for(int i=0;i<len;i++){
            String subStr=str.substring(i*4,i*4+4);
            int index=Integer.parseInt(subStr,16);
            buff.append(arr[index%arrLength]);
        }
        return buff.toString();
    }
}
