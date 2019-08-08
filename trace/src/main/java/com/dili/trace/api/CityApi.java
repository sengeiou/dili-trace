package com.dili.trace.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.City;
import com.dili.trace.dto.CityListInput;
import com.dili.trace.rpc.BaseInfoRpc;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/cityApi")
public class CityApi {
    private static final Logger LOGGER= LoggerFactory.getLogger(CityApi.class);

    @Resource
    private BaseInfoRpc baseInfoRpc;

    /**
     * 城市接口查询
     * @param city
     * @return
     */
    @ApiOperation(value ="城市接口查询【接口已通】", notes = "城市接口查询")
    @RequestMapping(value = "/listCityByCondition",method = RequestMethod.POST)
    public BaseOutput<List<City>> listCityByCondition(@RequestBody CityListInput city){
        try{
            return baseInfoRpc.listCityByCondition(city);
        }catch (Exception e){
            LOGGER.error("listCityByCondition",e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
