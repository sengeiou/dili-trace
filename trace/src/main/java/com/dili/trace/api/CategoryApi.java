package com.dili.trace.api;

import java.util.List;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.dto.OperatorUser;
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
import com.dili.trace.service.AssetsRpcService;

import io.swagger.annotations.ApiOperation;

/**
 * 品类查询接口
 */
//@RestController
//@RequestMapping(value = "/api/categoryApi")
public class CategoryApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryApi.class);
    @Autowired
    private AssetsRpcService categoryService;
    @Autowired
    BaseInfoRpcService baseInfoRpcService;
    @Autowired
    RUserCategoryService rUserCategoryService;
    @Autowired
    private LoginSessionContext sessionContext;



}
