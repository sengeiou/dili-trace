package com.dili.trace.controller;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 测试接口
 */
@RestController
@RequestMapping(value = "/testController")
@AppAccess(role = Role.NONE)
public class TestController {
    /**
     * 测试用
     *
     * @return
     */
    @RequestMapping(value = "/test.action", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput test() {
        RegisterBill rb = new RegisterBill();
        rb.setCreated(new Date());
        return BaseOutput.successData(rb);
    }
}
