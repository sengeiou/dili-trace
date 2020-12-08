package com.dili.trace.service;

import cn.hutool.http.HttpUtil;
import com.dili.common.annotation.AppAccess;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.User;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.rpc.CardQueryInput;
import com.dili.trace.rpc.CardResultDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * web请求上下文环境变量接口
 */
@Service
public class CustomerRpcService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerRpcService.class);


    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    FirmRpcService firmRpcService;

    @Autowired
    GlobalVarService globalVarService;

    @Value("${accountService.url}")
    private String accountServiceUrl;

    /**
     * 查询当前登录用户信息
     *
     * @return
     */
    public Optional<SessionData> getCurrentCustomer() {
        try {
            //两个方法在没有使用JSF的项目中是没有区别的
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            //RequestContextHolder.getRequestAttributes();
            //从session里面获取对应的值
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();

            try {
                Long userId = Long.parseLong(request.getHeader("userId"));
                Long marketId = Long.parseLong(request.getHeader("marketId"));
                CustomerQueryInput query = new CustomerQueryInput();
                query.setId(userId);
                query.setMarketId(marketId);
                BaseOutput<List<CustomerExtendDto>> out = this.customerRpc.list(query);
                if (out.isSuccess()) {
                    return StreamEx.ofNullable(out.getData()).flatCollection(Function.identity()).nonNull().map(c -> {
                        SessionData sessionData = new SessionData();
                        sessionData.setUserId(c.getId());
                        sessionData.setUserName(c.getName());
                        sessionData.setSubRoles(this.convert(c.getCharacterTypeList()));
                        return sessionData;
                    }).findFirst();

                }


            } catch (Exception e) {
                throw new TraceBizException("还没有登录");
            }
        } catch (Exception e) {
            throw new TraceBizException("当前运行环境不是web请求环境");
        }
        return Optional.empty();
    }

    /**
     * 角色转换
     * @param characterTypeList
     * @return
     */
    private List<CustomerEnum.CharacterType> convert(List<CharacterType> characterTypeList) {
        List<CustomerEnum.CharacterType> subRoles = new ArrayList<>();
        subRoles.add(CustomerEnum.CharacterType.经营户);
        subRoles.add(CustomerEnum.CharacterType.买家);
        subRoles.add(CustomerEnum.CharacterType.其他类型);
        return subRoles;
    }

    /**
     * 是否有访问权限
     *
     * @param access
     * @return
     */
    public boolean hasAccess(AppAccess access) {
        return this.getCurrentCustomer().map(c -> {
            List<CustomerEnum.CharacterType> subRoleList = StreamEx.ofNullable(access.subRoles()).flatArray(Function.identity()).toList();
            return c.getSubRoles().containsAll(subRoleList);
        }).orElse(false);
    }

    /**
     * 查询客户
     *
     * @param customerId
     * @return
     */
    public CustomerExtendDto findCustomerByIdOrEx(Long customerId, Long marketId) {

        return this.findCustomerById(customerId,marketId).orElseThrow(()->{
            return new TraceBizException("查询客户信息失败");
        });
    }
    /**
     * 查询客户
     *
     * @param customerId
     * @return
     */
    public Optional<CustomerExtendDto> findCustomerById(Long customerId, Long marketId) {

        BaseOutput<CustomerExtendDto> out = this.customerRpc.get(customerId, marketId);

        if (out.isSuccess()) {
            return Optional.ofNullable(out.getData());
        }
        return Optional.empty();
    }

    /**
     * 分页查询
     *
     * @param queryInput
     * @return
     */
    public PageOutput<List<CustomerExtendDto>> listPage(CustomerQueryInput queryInput) {
        PageOutput<List<CustomerExtendDto>> page = this.customerRpc.listPage(queryInput);
        return page;
    }



    /**
     * 查询客户信息
     *
     * @param cust
     * @return
     */
    public Optional<com.dili.trace.domain.Customer> findCustomer(com.dili.trace.domain.Customer cust) {
        return this.listCustomers(cust.getCustomerId(), cust.getPrintingCard());
    }

    /**
     * 查询用户信息
     *
     * @param customerCode
     * @param cardNo
     * @return
     */
    private Optional<com.dili.trace.domain.Customer> listCustomers(String customerCode, String cardNo) {
        if (StringUtils.isAllBlank(customerCode, cardNo)) {
            return Optional.empty();
        }
        return this.queryCardInfoByCustomerCode(customerCode, cardNo).map(card -> {

            com.dili.trace.domain.Customer customer = new Customer();
            customer.setPrintingCard(card.getCardNo());
            customer.setCustomerId(customerCode);
            customer.setName(card.getCustomerName());
            customer.setIdNo(card.getCustomerCertificateNumber());

            Long customerId = card.getCustomerId();
            this.findCustomerById(customerId).ifPresent(cust -> {
                customer.setPhone(cust.getContactsPhone());
                customer.setAddress(cust.getCertificateAddr());
                customer.setCustomerId(cust.getCode());
            });
            return customer;

        });

    }

    /**
     * 查询用户信息
     *
     * @param customerId
     * @return
     */
    private Optional<CustomerExtendDto> findCustomerById(Long customerId) {
        if (customerId == null) {
            return Optional.empty();
        }
        CustomerQueryInput input = new CustomerQueryInput();
        input.setMarketId(this.globalVarService.getMarketId());
        input.setId(customerId);

        try {
            BaseOutput<List<CustomerExtendDto>> output = this.customerRpc.list(input);
            if (!output.isSuccess()) {
                return Optional.empty();
            }
            return StreamEx.ofNullable(output.getData()).nonNull()
                    .flatCollection(Function.identity()).nonNull()
                    .findFirst();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }


    /**
     * 查询卡信息
     *
     * @param customerCode
     * @throws IOException
     */
    private Optional<CardResultDto> queryCardInfoByCustomerCode(String customerCode, String cardNo) {
        CardQueryInput input = new CardQueryInput();
        input.setFirmId(this.globalVarService.getMarketId());
        input.setCustomerCode(StringUtils.trimToNull(customerCode));
        if (StringUtils.isNotBlank(cardNo)) {
            input.setCardNos(Lists.newArrayList(cardNo.trim()));
        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String jsonBody = mapper.writeValueAsString(input);
//            System.out.println(jsonBody);
            String respBody = HttpUtil.post(this.accountServiceUrl + "/api/account/getList", jsonBody);
//            System.out.println(respBody);
            BaseOutput<List<CardResultDto>> out = mapper.readValue(respBody, new TypeReference<BaseOutput<List<CardResultDto>>>() {
            });
            if (!"200".equals(out.getCode())) {
                return Optional.empty();
            }
            return StreamEx.ofNullable(out.getData()).nonNull().flatCollection(Function.identity()).nonNull().findFirst();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
