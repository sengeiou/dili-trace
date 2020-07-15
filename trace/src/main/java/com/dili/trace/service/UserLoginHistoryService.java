package com.dili.trace.service;

import java.util.Date;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserLoginHistory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLoginHistoryService extends BaseServiceImpl<UserLoginHistory,Long>{
        public UserLoginHistory createLoginHistory(User user){
            UserLoginHistory loginHistory=new UserLoginHistory();

            loginHistory.setUserId(user.getId());
            loginHistory.setUserName(user.getName());
            loginHistory.setCreated(new Date());
            loginHistory.setModified(new Date());
            this.insertSelective(loginHistory);
            return loginHistory;
        }
}