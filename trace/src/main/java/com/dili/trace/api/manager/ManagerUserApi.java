package com.dili.trace.api.manager;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.PageOutput;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CustomerExtendOutPutDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.rpc.service.CustomerRpcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理用户接口
 */
@RestController
@RequestMapping(value = "/api/manager/user")
@Api(value = "/api/manager/user", description = "用户管理相关接口")
@AppAccess(role = Role.Manager, url = "dili-trace-app-auth", subRoles = {})
public class ManagerUserApi {
    private static final Logger logger = LoggerFactory.getLogger(ManagerUserApi.class);
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    CustomerRpcService customerRpcService;

//    /**
//     * 商户审核统计概览
//     *
//     * @param user
//     * @return
//     */
//    @ApiOperation(value = "商户审核统计概览")
//    @RequestMapping(value = "/userCertCount.api", method = RequestMethod.POST)
//    public BaseOutput<List<UserOutput>> countGroupByValidateState(@RequestBody User user) {
//        try {
////            sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
//            return userService.countGroupByValidateState(user);
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return BaseOutput.failure("操作失败：服务端出错");
//        }
//    }
//
//    /**
//     * 获得用户审核列表
//     *
//     * @param user
//     * @return
//     */
//    @ApiOperation(value = "获得用户审核列表")
//    @RequestMapping(value = "/listUserCertByQuery.api", method = RequestMethod.POST)
//    public BaseOutput<BasePage<UserOutput>> listUserCertByQuery(@RequestBody UserInput user) {
//        try {
////            sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
//            BasePage<UserOutput> data = userService.pageUserByQuery(user);
//            return BaseOutput.success().setData(data);
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return BaseOutput.failure("操作失败：服务端出错");
//        }
//    }
//
//    /**
//     * 获得用户资料详情
//     *
//     * @param input
//     * @return
//     */
//    @ApiOperation(value = "获得用户资料详情")
//    @RequestMapping(value = "/userCertDetail.api", method = RequestMethod.POST)
//    public BaseOutput<User> userCertDetail(@RequestBody UserInput input) {
//        if (input == null || input.getId() == null) {
//            return BaseOutput.failure("参数错误");
//        }
//        try {
////            sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
//            User data = userService.get(input.getId());
//            data.setPassword(null);
//            return BaseOutput.success().setData(data);
//
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return BaseOutput.failure("操作失败：服务端出错");
//        }
//    }
//
//    /**
//     * 审核用户资料
//     *
//     * @param input
//     * @return
//     */
//    @ApiOperation(value = "审核用户资料")
//    @RequestMapping(value = "/verifyUserCert.api", method = RequestMethod.POST)
//    public BaseOutput<Long> verifyUserCert(@RequestBody UserInput input) {
//        try {
//            SessionData sessionData = this.sessionContext.getSessionData();
//            OperatorUser operatorUser =new OperatorUser(sessionData.getUserId(),sessionData.getUserName());
//            return userService.verifyUserCert(input, operatorUser);
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return BaseOutput.failure("操作失败：服务端出错");
//        }
//
//    }

    /**
     * 查询经营户信息
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "查询经营户信息")
    @RequestMapping(value = "/listSeller.api", method = RequestMethod.POST)
    public PageOutput<List<CustomerExtendOutPutDto>> listSeller(@RequestBody CustomerQueryInput input) {
        try {
            PageOutput<List<CustomerExtendDto>> pageOutput = this.customerRpcService.listSeller(input, this.sessionContext.getSessionData().getMarketId());
            PageOutput<List<CustomerExtendOutPutDto>> page = new PageOutput<>();
            List<CustomerExtendDto> customerList = pageOutput.getData();
            List<CustomerExtendOutPutDto> customerOutputList = new ArrayList<>();
            customerList.forEach(c -> {
                CustomerExtendOutPutDto customerOutput = new CustomerExtendOutPutDto();
                BeanUtils.copyProperties(c, customerOutput);
                customerOutput.setMarketId(this.sessionContext.getSessionData().getMarketId());
                customerOutput.setMarketName(this.sessionContext.getSessionData().getMarketName());
                customerOutputList.add(customerOutput);
            });
            BeanUtils.copyProperties(pageOutput, page);
            page.setData(customerOutputList);
            return page;
        } catch (TraceBizException e) {
            return PageOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return PageOutput.failure("操作失败：服务端出错");
        }

    }

}
