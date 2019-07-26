package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.User;
import com.dili.trace.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    public UserMapper getActualDao() {
        return (UserMapper)getDao();
    }
}