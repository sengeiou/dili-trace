package com.dili.trace.controller;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.rpc.service.CityRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 神农系统中相关基础信息
 */
@RestController
@RequestMapping(value = "/city")
public class CityController {
    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    CityRpcService cityService;

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
