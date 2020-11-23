package com.dili.sg.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.sg.trace.dao.UserTallyAreaMapper;
import com.dili.sg.trace.domain.UserTallyArea;
import com.dili.sg.trace.service.UserTallyAreaService;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-10-11 10:05:11.
 */
@Service
public class UserTallyAreaServiceImpl extends BaseServiceImpl<UserTallyArea, Long> implements UserTallyAreaService {

    public UserTallyAreaMapper getActualDao() {
        return (UserTallyAreaMapper)getDao();
    }
}