package com.dili.trace.api;

import java.util.Arrays;
import java.util.List;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.service.RUserCategoryService;
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

import javax.annotation.Resource;

/**
 * 品类查询接口
 */
@RestController
@RequestMapping(value = "/api/categoryApi")
public class CategoryApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryApi.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    BaseInfoRpcService baseInfoRpcService;
    @Autowired
    RUserCategoryService rUserCategoryService;
    @Autowired
    private LoginSessionContext sessionContext;

    /**
     * 查询品类
     *
     * @param category
     * @return
     */
    @ApiOperation(value = "品类接口查询【接口已通】", notes = "品类接口查询")
    @RequestMapping(value = "/listCategoryByCondition", method = RequestMethod.POST)
    public BaseOutput<List<Category>> listCategoryByCondition(@RequestBody CategoryListInput category) {
        //TODO
/*		try {
			List<Category> list = this.categoryService.listCategoryByKeyword(category.getKeyword(), category.getLevel(), category.getParentId(), category.getMarketId());
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			LOGGER.error("listCityByCondition", e);
			return BaseOutput.failure(e.getMessage());
		}*/
        return BaseOutput.success();
    }


    /**
     * 添加个人经营品类
     */
    @RequestMapping(value = "/addUserCategory.api", method = RequestMethod.POST)
    public BaseOutput addUserCategory(@RequestBody List<RUserCategory> input) {
        try {
            Long id = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            return rUserCategoryService.dealBatchAdd(input, id);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 删除个人经营品类
     */
    @RequestMapping(value = "/delUserCategory.api", method = RequestMethod.POST)
    public BaseOutput delUserCategory(@RequestBody List<Long> input) {
        try {
            this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            int rows = rUserCategoryService.delete(input);
            return rows > 0 ? BaseOutput.success() : BaseOutput.failure();
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 查询个人经营品类列表
     *
     * @param rUserCategory
     * @return
     */
    @ApiOperation(value = "查询个人经营品类列表", notes = "个人经营品类列表")
    @RequestMapping(value = "/userCategory.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<RUserCategory>> userCategory(@RequestBody RUserCategory rUserCategory) {
        if (rUserCategory == null) {
            rUserCategory = new RUserCategory();
        }
        try {
            Long id = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            if (rUserCategory.getUserId() == null) {
                rUserCategory.setUserId(id);
            }
            rUserCategory.setSort("create_time");
            rUserCategory.setOrder("desc");
            BasePage<RUserCategory> out = rUserCategoryService.listPageByExample(rUserCategory);
            return BaseOutput.success().setData(out);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("quit", e);
            return BaseOutput.failure();
        }
    }
}
