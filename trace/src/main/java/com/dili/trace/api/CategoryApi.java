package com.dili.trace.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.service.CategoryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/categoryApi")
public class CategoryApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryApi.class);
	@Autowired
    private CategoryService categoryService;
	@Autowired
	BaseInfoRpcService baseInfoRpcService;

	/**
	 * 品类接口查询
	 * 
	 * @param category
	 * @return
	 */
	@ApiOperation(value = "品类接口查询【接口已通】", notes = "品类接口查询")
	@RequestMapping(value = "/listCategoryByCondition", method = RequestMethod.POST)
	public BaseOutput<List<Category>> listCategoryByCondition(@RequestBody CategoryListInput category) {
		try {
			List<Category> list = this.categoryService.listCategoryByKeyword(category.getKeyword(),category.getLevel(),category.getParentId());
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			LOGGER.error("listCityByCondition", e);
			return BaseOutput.failure(e.getMessage());
		}
	}
}
