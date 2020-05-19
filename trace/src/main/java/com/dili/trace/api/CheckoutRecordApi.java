package com.dili.trace.api;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckoutApiListQuery;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.CheckinRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "/api/checkoutRecordApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/checkinRecordApi")
public class CheckoutRecordApi {
    private static final Logger logger = LoggerFactory.getLogger(UserApi.class);
    @Resource
    private UserService userService;
    @Resource
    private SessionContext sessionContext;
    @Autowired
    RegisterBillService registerBillService;

    

    /**
     * 分页查询需要出场查询的信息
     */
    @RequestMapping(value = "/listPagedAvailableCheckOutData.api", method = { RequestMethod.POST, RequestMethod.GET })
    public BaseOutput<BasePage<Object>> listPagedAvailableCheckOutData(@RequestBody CheckoutApiListQuery query) {
        if(query==null||query.getUserId()==null){
            return BaseOutput.failure("参数错误");
        }
        
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }

        return BaseOutput.success();
    }

    /**
     * 进场通过
     */
    @RequestMapping(value = "/doCheckout.api", method = { RequestMethod.POST, RequestMethod.GET })
    public BaseOutput<BasePage<UpStream>> doCheckin(@RequestBody CheckInApiInput input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
           
        } catch (BusinessException e) {

            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

        return null;
    }
}