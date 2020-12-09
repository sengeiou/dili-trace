package com.dili.trace.controller;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.CityService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 神农系统中相关基础信息
 */
@RestController
@RequestMapping(value = "/city")
public class CityController {
    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    CityService cityService;

    @Autowired
    AssetsRpcService categoryService;
    @Autowired
    LoginSessionContext loginSessionContext;


    /**
     * 查询城市信息
     *
     * @return
     */
    @RequestMapping(value = "/listCities.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput<List<CityDto>> listCities(@RequestBody CityDto query) {
        try {
            return BaseOutput.successData(this.cityService.listCityByInput(query));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

}
