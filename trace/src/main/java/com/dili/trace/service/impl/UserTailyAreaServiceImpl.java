package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.UserTailyAreaMapper;
import com.dili.trace.domain.UserTailyArea;
import com.dili.trace.service.UserTailyAreaService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-10-11 10:05:11.
 */
@Service
public class UserTailyAreaServiceImpl extends BaseServiceImpl<UserTailyArea, Long> implements UserTailyAreaService {

    public UserTailyAreaMapper getActualDao() {
        return (UserTailyAreaMapper)getDao();
    }
}