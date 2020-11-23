package com.dili.trace.service;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.dto.CategoryListInput;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author admin
 */
@Service
public class CategoryService extends BaseServiceImpl<Category, Long> {
	@Autowired(required = false)
	AssetsRpc assetsRpc;
	@Autowired
	GlobalVarService globalVarService;

	/**
	 * 查询品类
	 *
	 * @param query
	 * @return
	 */
	public List<Category> listCategoryByCondition(CategoryListInput query) {
		CusCategoryQuery cusQuery = new CusCategoryQuery();
		if (StringUtils.isNotBlank(query.getKeyword())) {
			cusQuery.setKeyword(query.getKeyword());
		} else if (StringUtils.isNotBlank(query.getKeyWordStr())) {
			cusQuery.setKeyword(query.getKeyWordStr());
		} else {

		}
		cusQuery.setMarketId(this.globalVarService.getMarketId());
		try {
			BaseOutput<List<CusCategoryDTO>> out = this.assetsRpc.listCusCategory(cusQuery);
			if (!out.isSuccess()) {
				return Lists.newArrayList();
			}
			return StreamEx.ofNullable(out.getData()).nonNull().flatCollection(Function.identity()).nonNull().filter(c -> {
				return !c.getName().contains("蔬菜") && !c.getName().equals("椒类");
			}).map(Category::convert).toList();
		} catch (Exception e) {
			return Lists.newArrayList();
		}

	}

	/**
	 * 根据关键字查询 品类
	 *
	 * @param keyword
	 * @return
	 */
	public List<Category> listCategoryByCondition(String keyword) {
		if (StringUtils.isBlank(keyword)) {
			return new ArrayList<>();
		}
		CategoryListInput query = new CategoryListInput();
		query.setKeyword(keyword);
		List<Category> list = this.listCategoryByCondition(query);
		return list;

	}
}