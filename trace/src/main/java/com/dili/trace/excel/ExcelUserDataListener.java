package com.dili.trace.excel;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUserDataListener extends AnalysisEventListener<ExcelUserData> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUserDataListener.class);
	/**
	 * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
	 */
	private static final int BATCH_COUNT = 5;
	private List<ExcelUserData> list = new ArrayList<ExcelUserData>();

	@Override
	public void invoke(ExcelUserData data, AnalysisContext context) {

		if (data.getOrderNum() != null) {
			try {
				Integer.parseInt(data.getOrderNum().trim());
				LOGGER.info("解析到一条数据:{}", data);
				list.add(data);
			} catch (Exception e) {
			}
		}

		if (list.size() >= BATCH_COUNT) {
			// saveData();
			// list.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		saveData();
		LOGGER.info("所有数据解析完成！");
	}

	/**
	 * 加上存储数据库
	 */
	private void saveData() {
		LOGGER.info("{}条数据，开始存储数据库！", list.size());
		LOGGER.info("存储数据库成功！");
	}

	public List<ExcelUserData> getExcelUserDatas() {
		return this.list;
	}
}