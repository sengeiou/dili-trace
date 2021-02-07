package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.dto.query.UserQrHistoryQueryDto;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserQrHistoryMapper extends MyMapper<UserQrHistory> {

    public List<Long> selectUserInfoIdWithoutHistory(UserQrHistoryQueryDto historyQueryDto);

    public List<UserQrHistory> listByExample(UserQrHistoryQueryDto historyQueryDto);

}
