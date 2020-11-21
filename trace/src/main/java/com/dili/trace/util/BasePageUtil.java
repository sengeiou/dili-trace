package com.dili.trace.util;

import java.util.List;
import java.util.function.Function;

import com.dili.ss.domain.BasePage;
import com.google.common.collect.Lists;

import one.util.streamex.StreamEx;

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
	public static <T, S> BasePage<T> empty(Integer page,Integer rows) {

		BasePage<T> result = new BasePage<>();
		result.setDatas(Lists.newArrayList());
		result.setPage(page);
		result.setRows(rows);
		result.setTotalItem(0L);
		result.setTotalPage(0);
		result.setStartIndex(0L);
		return result;

	}

	public static <T, S> BasePage<T> convert(BasePage<S> sourcePage, Function<S, T> converter) {
		List<T> dataList = StreamEx.of(sourcePage.getDatas()).map(item -> {
			if (item == null) {
				return null;
			}
			return converter.apply(item);

		}).toList();
		return BasePageUtil.convert(dataList,sourcePage);
	}

}
