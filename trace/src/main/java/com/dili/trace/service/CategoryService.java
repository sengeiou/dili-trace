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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger= LoggerFactory.getLogger(CategoryService.class);
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
		return getCategories(cusQuery);

	}

	/**
	 * 远程查询品类
	 * @param cusQuery
	 * @return
	 */
	private List<Category> getCategories(CusCategoryQuery cusQuery) {
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
			logger.error(e.getMessage(),e);
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
		CusCategoryQuery cusQuery=new CusCategoryQuery();
		cusQuery.setKeyword(keyword);
		List<Category> list = this.getCategories(cusQuery);
		return list;

	}
	/**
	 * 根据关键字查询 品类
	 *
	 * @param cusQuery
	 * @return
	 */
	public List<Category> listCategoryByCondition(CusCategoryQuery cusQuery) {
		if (cusQuery==null) {
			return new ArrayList<>();
		}
		List<Category> list = this.getCategories(cusQuery);
		return list;

	}
}