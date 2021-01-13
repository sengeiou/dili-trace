package com.dili.trace.api;

import java.util.List;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.dto.CityQueryDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Countries;

import com.dili.trace.rpc.service.CityRpcService;
import com.dili.trace.service.CountriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

/**
 * 城市查询接口
 */
//@RestController
//@RequestMapping(value = "/api/cityApi")
public class CityApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityApi.class);

    @Autowired
    private CityRpcService cityService;

    @Autowired
    private CountriesService countriesService;

    /**
     * 城市接口查询
     *
     * @param city
     * @return
     */
    @ApiOperation(value = "城市接口查询【接口已通】", notes = "城市接口查询")
    @RequestMapping(value = "/listCityByCondition", method = RequestMethod.POST)
    public BaseOutput<List<CityDto>> listCityByCondition(@RequestBody CityQueryDto city) {
        try {
            return BaseOutput.successData(this.cityService.listCityByCondition(city));
        } catch (Exception e) {
            LOGGER.error("listCityByCondition", e);
            return BaseOutput.failure(e.getMessage());
        }
    }


    /**
     * 城市接口查询(国外)
     *
     * @return
     */
    @ApiOperation(value = "城市接口查询(国外)【接口已通】", notes = "城市接口查询(国外)")
    @RequestMapping(value = "/listCountryByCondition", method = RequestMethod.POST)
    public BaseOutput<List<Countries>> listCountryByCondition() {
        try {
            List<Countries> countryList = countriesService.listByExample(new Countries());
            return BaseOutput.success().setData(countryList);
        } catch (Exception e) {
            LOGGER.error("listCityByCondition", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
