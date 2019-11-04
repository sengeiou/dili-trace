package com.dili.trace.api;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.City;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.dto.CityListInput;
import com.dili.trace.rpc.BaseInfoRpc;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/categoryApi")
public class CategoryApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryApi.class);

//    @Resource
//    private BaseInfoRpc baseInfoRpc;
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
//            return baseInfoRpc.listCategoryByCondition(category);
			List<Category> list = this.baseInfoRpcService.listCategoryByCondition(category);
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			LOGGER.error("listCityByCondition", e);
			return BaseOutput.failure(e.getMessage());
		}
	}
}
