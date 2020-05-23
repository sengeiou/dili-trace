package com.dili.trace.util;

import java.util.List;

import com.dili.ss.domain.BasePage;

public class BasePageUtil {
	public static <T, S> BasePage<T> convert(List<T> dataList, BasePage<S> sourcePage) {

		BasePage<T> result = new BasePage<>();
		result.setDatas(dataList);
		result.setPage(sourcePage.getPage());
		result.setRows(sourcePage.getRows());
		result.setTotalItem(sourcePage.getTotalItem());
		result.setTotalPage(sourcePage.getTotalPage());
		result.setStartIndex(sourcePage.getStartIndex());
		return result;

	}

}
