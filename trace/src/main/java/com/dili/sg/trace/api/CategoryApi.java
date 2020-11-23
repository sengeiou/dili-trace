package com.dili.sg.trace.api;

import com.dili.sg.trace.domain.Category;
import com.dili.sg.trace.dto.CategoryListInput;
import com.dili.sg.trace.service.CategoryService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品查询接口
 */
@RestController
@RequestMapping(value = "/api/categoryApi")
public class CategoryApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryApi.class);


	@Autowired
	CategoryService categoryService;

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
//            return baseInfoRpc.listCategoryByCondition(category);
			List<Category> list = this.categoryService.listCategoryByCondition(category);
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			LOGGER.error("listCityByCondition", e);
			return BaseOutput.failure(e.getMessage());
		}
	}
}
