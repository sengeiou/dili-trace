package com.dili.trace.service;

import java.util.Date;
import java.util.List;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.dao.UserLoginHistoryMapper;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserLoginHistory;
import com.dili.trace.dto.UserLoginHistoryDto;
import com.dili.trace.dto.UserLoginHistoryQueryDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLoginHistoryService extends BaseServiceImpl<UserLoginHistory, Long> {
    public UserLoginHistory createLoginHistory(User user) {
        UserLoginHistory loginHistory = new UserLoginHistory();

        loginHistory.setUserId(user.getId());
        loginHistory.setUserName(user.getName());
        loginHistory.setCreated(new Date());
        loginHistory.setModified(new Date());
        this.insertSelective(loginHistory);
        return loginHistory;
    }

    public EasyuiPageOutput listEasyuiPageByExample(UserLoginHistoryQueryDto domain) throws Exception {
        UserLoginHistoryMapper dao = (UserLoginHistoryMapper) this.getDao();

        PageHelper.startPage(domain.getPage(), domain.getRows());
        List<UserLoginHistoryDto> list = dao.queryUserLoginHistory(domain);
        ;
        Page<UserLoginHistoryDto> page = (Page) list;
        BasePage<UserLoginHistoryDto> result = new BasePage<UserLoginHistoryDto>();
        result.setDatas(list);
        result.setPage(page.getPageNum());
        result.setRows(page.getPageSize());
        result.setTotalItem(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPage(page.getPages());
        result.setStartIndex(page.getStartRow());

        long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
        List results = ValueProviderUtils.buildDataByProvider(domain, list);
        return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
    }
}