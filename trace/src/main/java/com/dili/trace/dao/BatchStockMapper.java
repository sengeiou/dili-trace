package com.dili.trace.dao;

import java.util.Optional;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.ProductStore;

import org.apache.ibatis.annotations.Select;

public interface BatchStockMapper extends MyMapper<ProductStore>{
     /**
     * 通过ID悲观锁定数据
     */
    @Select("select * from product_store where id = #{id} for update")
    public Optional<ProductStore> selectByIdForUpdate(Long id);
}