package com.dili.trace.excel;

import java.io.File;

import com.alibaba.excel.EasyExcel;

import org.junit.jupiter.api.Test;

public class ExcelParseTest {
    @Test
    public void testReadExcel() {
        String fileName = "‪E:/Downloads/农溯安市场经营户预安装台账7月14日.xls";
        // // 这里默认读取第一个sheet
        EasyExcel.read(new File(fileName), IndexOrNameData.class, new IndexOrNameDataListener()).sheet().doRead();
    }

}