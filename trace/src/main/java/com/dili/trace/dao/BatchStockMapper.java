package com.dili.trace.dao;

import java.util.Optional;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.BatchStock;

import org.apache.ibatis.annotations.Select;

public interface BatchStockMapper extends MyMapper<BatchStock>{
     /**
     * 通过ID悲观锁定数据
     */
    @Select("select * from batch_stock where id = #{id} for update")
    public Optional<BatchStock> selectByIdForUpdate(Long id);
}