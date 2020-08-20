package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface UserMapper extends MyMapper<User> {
    public List<UserOutput> countGroupByValidateState(User user);

    public List<UserOutput> listUserByQuery(UserInput user);


    public List<UserOutput> groupByQrStatus(@Param("qrStatusList")List<Integer>qrStatusList);

    public List<UserOutput> listUserByStoreName(@Param("userId") Long userId, @Param("queryCondition") String queryCondition);

    UserOutput getUserByUserId(Long id);

}