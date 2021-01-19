package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.UserStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserStoreMapper extends MyMapper<UserStore> {
    public List<UserStore> listUserStoreByKeyword(@Param("keyword") String keyword, @Param("marketId") Long marketId);

}
