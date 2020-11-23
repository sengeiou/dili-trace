package com.dili.sg.trace.api;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.sg.trace.service.CityService;
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
 * 产地查询接口
 */
@RestController
@RequestMapping(value = "/api/cityApi")
public class CityApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityApi.class);

    @Autowired
    CityService cityService;

    /**
     * 城市接口查询
     *
     * @param city
     * @return
     */
    @ApiOperation(value = "城市接口查询【接口已通】", notes = "城市接口查询")
    @RequestMapping(value = "/listCityByCondition", method = RequestMethod.POST)
    public BaseOutput<List<CityDto>> listCityByCondition(@RequestBody CityDto city) {
        try {
            return BaseOutput.successData(cityService.listCityByCondition(city));
        } catch (Exception e) {
            LOGGER.error("listCityByCondition", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
