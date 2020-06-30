package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserMapper extends MyMapper<User> {
    public List<UserOutput> countGroupByValidateState(User user);

    public List<UserOutput> listUserByQuery(UserInput user);
}