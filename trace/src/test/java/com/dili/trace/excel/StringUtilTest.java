package com.dili.trace.excel;

public class StringUtilTest {

	public static void main(String[] args) {
		System.out.println(ChineseStringUtil.full2Half(ChineseStringUtil.cToe(" a。,b ,c ")).replaceAll(" ", ""));
		String s = "nihaoｈｋ　｜　　　ｎｉｈｅｈｅ　，　７８　　７　。";
		System.out.println(ChineseStringUtil.full2Half(ChineseStringUtil.cToe(s)).replaceAll(" ", ""));
	}
}
