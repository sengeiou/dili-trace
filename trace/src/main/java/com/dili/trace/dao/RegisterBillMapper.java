package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.UserListDto;

public interface RegisterBillMapper extends MyMapper<RegisterBill> {
    /**
     * 统计不同审核状态报备单数据
     * 
     * @param query
     * @return
     */
    public List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBill query);

    /**
     * 根据报备单数量更新用户状态到黑码
     * @param dto
     * @return
     */
    public int updateAllUserQrStatusByRegisterBillNum(UserListDto user);
}