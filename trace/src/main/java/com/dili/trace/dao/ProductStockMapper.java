package com.dili.trace.dao;

import java.util.Optional;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.ProductStock;

import org.apache.ibatis.annotations.Select;

public interface ProductStockMapper extends MyMapper<ProductStock>{
     /**
     * 通过ID悲观锁定数据
     */
    @Select("select * from product_stock where id = #{id} for update")
    public Optional<ProductStock> selectByIdForUpdate(Long id);
}