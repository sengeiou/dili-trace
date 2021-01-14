package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.Brand;
import com.dili.trace.util.RegUtils;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

/**
 * 品牌信息
 */
@Service
public class BrandService extends BaseServiceImpl<Brand, Long> {

    /**
     * 创建或者更新品牌
     * @param brandName
     * @param userId
     * @param marketId
     * @return
     */
    public Optional<Long> createOrUpdateBrand(String brandName, Long userId, Long marketId) {
        if (StringUtils.isBlank(brandName)) {
            return Optional.empty();
        }
        if(!RegUtils.isValidInput(brandName)){
            throw new TraceBizException("品牌名称包含非法字符");
        }
        Brand query = new Brand();
        // query.setUserId(userId);
        query.setBrandName(brandName);
        query.setMarketId(marketId);
        Brand brandItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            Brand brand = new Brand();
            brand.setUserId(userId);
            brand.setBrandName(brandName);
            brand.setMarketId(marketId);
            brand.setCreated(new Date());
            this.insertSelective(brand);
            return brand;
        });
        return Optional.of(brandItem.getId());
    }

    /**
     * 根据ID查询品牌信息
     * @param idList
     * @return
     */
    public Map<Long, Brand> findBrandMapByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Maps.newHashMap();
        }
        Example e = new Example(Brand.class);
        e.and().andIn("id", idList);
        return StreamEx.of(this.getDao().selectByExample(e)).toMap(Brand::getId, Function.identity());
    }
}
