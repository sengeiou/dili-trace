package com.dili.trace.rpc.service;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.dto.CityQueryDto;
import com.dili.assets.sdk.rpc.CityRpc;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 查询城市信息
 */
@Service
public class CityRpcService {
    private static final Logger logger = LoggerFactory.getLogger(CityRpcService.class);

    @Autowired(required = false)
    CityRpc cityRpc;

    /**
     * @param keyword
     * @return
     */
    public List<CityDto> listCityByCondition(String keyword) {
        CityQueryDto query = new CityQueryDto();
        query.setKeyword(keyword);
        return this.listCityByInput(query);

    }

    /**
     * @param condition
     * @return
     */
    public List<CityDto> listCityByCondition(CityQueryDto condition) {
        CityQueryDto query = new CityQueryDto();
        query.setId(condition.getId());
        query.setCityCode(condition.getCityCode());
        query.setLevelType(condition.getLevelType());
        query.setParentId(condition.getParentId());
        // 小程序需要模糊查询城市
        query.setKeyword(condition.getKeyword());
        List<CityDto> result = this.listCity(query);
        return result;

    }

    /**
     * @param query
     * @return
     */
    public List<CityDto> listCityByInput(CityQueryDto query) {
        List<CityDto> result = this.listCity(query);
        return result;

    }

    /**
     * 根据id查询城市信息
     *
     * @param id
     * @return
     */
    public Optional<CityDto> findCityById(Long id) {
        CityQueryDto cityDto = new CityQueryDto();
        cityDto.setId(id);
        return StreamEx.of(this.listCity(cityDto)).findFirst();
    }

    /**
     * 远程接口封装
     *
     * @param query
     * @return
     */
    private List<CityDto> listCity(CityQueryDto query) {
        try {
            BaseOutput<List<CityDto>> out = this.cityRpc.listByExample(query);
            if (!out.isSuccess()) {
                return Lists.newArrayList();
            }
            return StreamEx.ofNullable(out.getData()).nonNull()
                    .flatCollection(Function.identity()).nonNull().toList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Lists.newArrayList();
        }

    }

}