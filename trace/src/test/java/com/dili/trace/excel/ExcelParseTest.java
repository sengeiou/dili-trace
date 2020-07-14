package com.dili.trace.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import com.alibaba.excel.EasyExcel;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class ExcelParseTest {
	@Test
	public void testReadExcel() throws FileNotFoundException {
		String fileName = "‪E:\\a.xls";
		FileInputStream fs = new FileInputStream(fileName);

		// // 这里默认读取第一个sheet
		// EasyExcel.read(fs, IndexOrNameData.class, new
		// IndexOrNameDataListener()).sheet().doRead();
	}

	public static void main(String[] args) throws FileNotFoundException {
		String fileName = "E:\\a.xls";
		File f = new File(fileName);
		System.out.println(f.getAbsolutePath());
		FileInputStream fs = new FileInputStream(f);

		// // 这里默认读取第一个sheet
		EasyExcel.read(fs, IndexOrNameData.class, new IndexOrNameDataListener()).sheet().doRead();
	}

}