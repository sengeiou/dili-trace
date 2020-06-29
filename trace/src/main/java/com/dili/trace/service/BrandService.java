package com.dili.trace.service;

import java.util.Optional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.Brand;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class BrandService extends BaseServiceImpl<Brand, Long> {
   
    public Optional<Long> createOrUpdateBrand(String brandName,Long userId) {
        if(StringUtils.isBlank(brandName)){
            return Optional.empty();
        }
        Brand query=new Brand();
        query.setUserId(userId);
        query.setBrandName(brandName);
        Brand brandItem=StreamEx.of(this.listByExample(query)).findFirst().orElseGet(()->{
            Brand brand=new Brand();
            brand.setUserId(userId);
            brand.setBrandName(brandName);
            this.insertSelective(brand);
            return brand;
        });
        return Optional.of(brandItem.getId());
    }
}
