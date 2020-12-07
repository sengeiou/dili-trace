package com.dili.trace.api.output;

import java.lang.reflect.InvocationTargetException;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.domain.Brand;

import org.apache.commons.beanutils.BeanUtils;

public class BrandOutputDto extends Brand {
    /**
     * 品牌id
     */
    private Long brandId;

    public static BrandOutputDto build(Brand brand) {
        if (brand == null) {
            return null;
        }
        BrandOutputDto dto = new BrandOutputDto();
        dto.setBrandId(brand.getId());
        try {
            BeanUtils.copyProperties(dto, brand);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TraceBizException("数据转换出错");
        }

        
        return dto;
    }

    /**
     * @return Long return the brandId
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

}