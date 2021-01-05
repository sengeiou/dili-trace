package com.dili.trace.util;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

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

		String s0 = getFirstSpell("分");
		String s1 = getFirstSpell("寿光");
		String s2 = getFirstSpell("sg");
		String s3 = getFirstSpell("杭水");
		String s4 = getFirstSpell("杭州水果");
		System.out.println("====s0:" + s0 + "====s1:" + s1 + "====s2:" + s2 + "=====s3:" + s3 + "====s4:" + s4);
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

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 *
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					output += temp[0];
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 获取汉字串拼音，英文字符不变
	 * @param chinese 汉字串
	 * @return 汉语拼音
	 */
	public static String getFullSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}

}
