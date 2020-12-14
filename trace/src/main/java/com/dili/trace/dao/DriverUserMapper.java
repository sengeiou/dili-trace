package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserDriverRef;

import java.util.List;

/**
 * @author asa.lee
 */
public interface DriverUserMapper extends MyMapper<UserDriverRef> {
    /**
     * 获取司机端货主列表
     * @param user
     * @return
     */
    List<UserDriverRef> getDriverUserList(UserDriverRef user);

    /**
     * 更新司机与用户关联
     * @param updateObj
     */
    void updateUserDriverRefByIDParam(UserDriverRef updateObj);
}
