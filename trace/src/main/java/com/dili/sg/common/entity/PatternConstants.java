package com.dili.sg.common.entity;

import cn.hutool.core.util.ReUtil;

/**
 * 项目中正则验证规则 和小程序验证一致
 */
public class PatternConstants {
    //手机号
    public static final String PHONE="^1[3-9]\\d{9}$";
    //身份证号
    public static final String CARD_NO="(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    //中文
    public static final String C="[\\u4e00-\\u9fa5]*";
    //字母 数字 中文
    public static final String CNC="[a-zA-Z0-9\\u4e00-\\u9fa5]*";
    //字母 数字 中文 斜杠
    public static final String CNCS="[a-zA-Z0-9/\\u4e00-\\u9fa5]*";
    //理货区号
    public static final String TALLY_AREA_NO="^\\d{3}(,\\d{3})*$";

    public static void main(String[] args) {
        System.out.println(ReUtil.isMatch(PatternConstants.TALLY_AREA_NO,"001,123,000"));
    }
}
