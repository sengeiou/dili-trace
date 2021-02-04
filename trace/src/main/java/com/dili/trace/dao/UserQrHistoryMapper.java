package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.dto.query.UserQrHistoryQueryDto;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserQrHistoryMapper extends MyMapper<UserQrHistory> {

    public int updateQrStatusByUserIdList(@Param("userIdList") List<Long>userIdList
            ,@Param("qrStatus")Integer qrStatus
            ,@Param("content")String content);

    public List<Long> selectUserIdWithoutHistory(UserQrHistoryQueryDto historyQueryDto);

}
