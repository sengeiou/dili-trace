package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.Brand;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

@Service
public class BrandService extends BaseServiceImpl<Brand, Long> {

    public Optional<Long> createOrUpdateBrand(String brandName, Long userId) {
        if (StringUtils.isBlank(brandName)) {
            return Optional.empty();
        }
        Brand query = new Brand();
        query.setUserId(userId);
        query.setBrandName(brandName);
        Brand brandItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            Brand brand = new Brand();
            brand.setUserId(userId);
            brand.setBrandName(brandName);
            this.insertSelective(brand);
            return brand;
        });
        return Optional.of(brandItem.getId());
    }

    public Map<Long, Brand> findBrandMapByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Maps.newHashMap();
        }
        Example e = new Example(Brand.class);
        e.and().andIn("id", idList);
        return StreamEx.of(this.getDao().selectByExample(e)).toMap(Brand::getId, Function.identity());
    }
}
