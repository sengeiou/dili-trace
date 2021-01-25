package com.dili.trace.util;

import com.dili.common.exception.TraceBizException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {
    public static boolean isPlate(String carNo) {
        Pattern p = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}([A-Z0-9]){4,5}[A-Z0-9挂学警港澳]{1}$");
        Matcher m = p.matcher(carNo);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isValidInput(String input) {
        //String str = "abcDD_-34中";
        String regex = "^(\\w|[\\u4e00-\\u9fa5]|-)+$";
        return Pattern.matches(regex, input);
    }
}
