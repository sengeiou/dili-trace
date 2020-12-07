package com.dili.trace.api.commission;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.entity.LoginSessionContext;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CommissionBillInputDto;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.sg.trace.glossary.BillTypeEnum;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.sg.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.CommissionBillService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BeanMapUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 委托单查询接口
 */
@RestController
@RequestMapping(value = "/api/commission/commissionBillApi")
@InterceptConfiguration
public class CommissionBillApi {
    private static final Logger logger = LoggerFactory.getLogger(CommissionBillApi.class);
    @Autowired
    CommissionBillService commissionBillService;
    @Autowired
    BillService billService;
    @Autowired
    UserService userService;
    @Autowired
    LoginSessionContext sessionContext;

    /**
     * 通过小程序接口，用户创建委托单
     *
     * @param createListBillParam 小程序创建委托单信息
     * @return 创建结果
     */
    @RequestMapping(value = "/createCommissionBill.api", method = RequestMethod.POST)
    public BaseOutput<?> createCommissionBill(@RequestBody CreateListBillParam createListBillParam) {

        try {
            Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            if (!UserTypeEnum.COMMISSION_USER.equalsToCode(this.userService.get(userId).getUserType())) {
                return BaseOutput.failure("您不是场外用户");
            }
            User user = userService.get(sessionContext.getAccountId());
            if (user == null) {
                return BaseOutput.failure("未登陆用户");
            }
            List<CreateRegisterBillInputDto> inputList = StreamEx.ofNullable(createListBillParam).filter(Objects::nonNull).map(CreateListBillParam::getRegisterBills).nonNull().flatCollection(Function.identity()).map(bill -> {
                bill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());
                bill.setUserId(userId);
                return bill;
            }).toList();
            if (inputList.isEmpty()) {
                return BaseOutput.failure("参数错误");
            }
            List<RegisterBill> inputBillList = StreamEx.of(inputList).map(inut -> {
                logger.info("循环保存登记单:" + JSON.toJSONString(inut));
                RegisterBill registerBill = new RegisterBill();
                registerBill.setOperatorName(user.getName());
                registerBill.setOperatorId(user.getId());
                registerBill.setUserId(user.getId());
                registerBill.setName(user.getName());
                registerBill.setAddr(user.getAddr());
                registerBill.setIdCardNo(user.getCardNo());
                if (registerBill.getRegisterSource() == null) {
                    // 小程序默认理货区
                    registerBill.setRegisterSource(RegisterSourceEnum.TALLY_AREA.getCode());
                }
                if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
                    registerBill.setTallyAreaNo(user.getTallyAreaNos());
                }
                registerBill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());

                return registerBill;
            }).toList();
            List<RegisterBill> outlist = this.commissionBillService.createCommissionBillByUser(inputBillList);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 获取登记单列表
     *
     * @param input 查询条件
     * @return 登记单列表
     * @throws Exception
     */
    @ApiOperation(value = "获取登记单列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
    @RequestMapping(value = "/list.api", method = RequestMethod.POST)
    public BaseOutput<EasyuiPageOutput> list(@RequestBody RegisterBillDto input) throws Exception {

        try {

            RegisterBillDto registerBill = BeanMapUtil.trimBean(input);
            logger.info("获取登记单列表:{}", JSON.toJSON(registerBill).toString());

            Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            if (!UserTypeEnum.COMMISSION_USER.equalsToCode(this.userService.get(userId).getUserType())) {
                return BaseOutput.failure("您不是场外用户");
            }

            registerBill.setUserId(userId);
            registerBill.setBillType(BillTypeEnum.COMMISSION_BILL.getCode());
            if (StringUtils.isBlank(registerBill.getOrder())) {
                registerBill.setOrder("desc");
                registerBill.setSort("id");
            }
            EasyuiPageOutput easyuiPageOutput = billService.listEasyuiPageByExample(registerBill, true);
            return BaseOutput.success().setData(easyuiPageOutput);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 通过登记单ID获取登记单详细信息
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "通过登记单ID获取登记单详细信息")
    @RequestMapping(value = "/detail.api", method = RequestMethod.POST)
    public BaseOutput<RegisterBillOutputDto> detail(@RequestBody CommissionBillInputDto input) {

        try {
            Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            User user = userService.get(userId);
            if (!UserTypeEnum.COMMISSION_USER.equalsToCode(user.getUserType())) {
                return BaseOutput.failure("您不是场外用户");
            }

            logger.info("获取登记单详情:{}", input.getBillId());
            RegisterBill registerBill = billService.get(input.getBillId());
            if (registerBill == null) {
                return BaseOutput.failure("数据不存在");
            }
            RegisterBillOutputDto outdto = new RegisterBillOutputDto();
            try {
                BeanUtils.copyProperties(outdto, registerBill);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
                return BaseOutput.failure("服务端出错");
            }

            return BaseOutput.success().setData(outdto);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }


    }

}
