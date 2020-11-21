package com.dili.trace.controller;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.dto.UserLoginHistoryQueryDto;
import com.dili.trace.service.UserLoginHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户登录历史信息
 */
@Controller
@RequestMapping("/userLoginHistory")
public class UserLoginHistoryController {
    @Autowired
    UserLoginHistoryService userLoginHistoryService;

    /**
     * 跳转到index
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.put("createdStart", "2020-01-12");
        modelMap.put("createdEnd", "2020-12-12");
        return "userLoginHistory/index";
    }

    /**
     * 分页查询信息
     * @param input
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String listPage(UserLoginHistoryQueryDto input) throws Exception {
        EasyuiPageOutput out = this.userLoginHistoryService.listEasyuiPageByExample(input);
        return out.toString();
    }
}