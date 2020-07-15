package com.dili.trace.excel;


 
public class ChineseStringUtil {
 
	/**
	 * 中文标点符号转英文字标点符号
	 * 
	 * @param str
	 *            原字符串
	 * @return str 新字符串
	 */
	public static final String cToe(String str) {
		String[] regs = { "！", "，", "。", "；", "~", "《", "》", "（", "）", "？",
				"”", "｛", "｝", "“", "：", "【", "】", "”", "‘", "’", "!", ",",
				".", ";", "`", "<", ">", "(", ")", "?", "'", "{", "}", "\"",
				":", "{", "}", "\"", "\'", "\'" };
		for (int i = 0; i < regs.length / 2; i++) {
			str = str.replaceAll(regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
 
	// " “  ""
	public static void main(String[] args) {
		// System.out.println(full2Half("你好"));
		// System.out.println(full2Half("ｊａｖａ"));
		// System.out.println("中文:" + full2Half("你好"));
		String s = " “中，。,. 国”，国‘家’。5：: ： ；；；；4  【】）";
		System.out.println(s);
		System.out.println(cToe(s));
	}
 
	/**
	 * 判断字符串是否为空或空字符串
	 * 
	 * @param str
	 *            原字符串
	 * @return
	 */
	private static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
 
	/**
	 * 全角转半角:
	 * 
	 * @param fullStr
	 * @return
	 */
	public static final String full2Half(String fullStr) {
		if (isEmpty(fullStr)) {
			return fullStr;
		}
		char[] c = fullStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			// System.out.println((int) c[i]);
			if (c[i] >= 65281 && c[i] <= 65374) {
				c[i] = (char) (c[i] - 65248);
			} else if (c[i] == 12288) { // 空格
				c[i] = (char) 32;
			}
		}
		return new String(c);
	}
 
	/**
	 * 半角转全角
	 * 
	 * @param halfStr
	 * @return
	 */
	public static final String half2Full(String halfStr) {
		if (isEmpty(halfStr)) {
			return halfStr;
		}
		char[] c = halfStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
			} else if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
}
