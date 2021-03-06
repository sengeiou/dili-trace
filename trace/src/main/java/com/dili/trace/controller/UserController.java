package com.dili.trace.controller;


import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.common.util.MD5Util;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.customer.sdk.rpc.TallyingAreaRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.domain.Market;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.jobs.UpdateUserQrStatusJob;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.rpc.service.TallyingAreaRpcService;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.MarketUtil;
import com.dili.trace.util.MaskUserInfo;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ???MyBatis Generator?????????????????? This file was generated on 2019-07-26 09:20:35.
 */
@Api("/user")
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    DefaultConfiguration defaultConfiguration;
    @Autowired
    BaseInfoRpcService baseInfoRpcService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    TallyingAreaRpcService tallyingAreaRpcService;
    @Autowired
    TallyingAreaRpc tallyingAreaRpc;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    UserRpc userRpc;
    @Autowired
    MarketService marketService;

    /**
     * ?????????User??????
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("?????????User??????")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("createdStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));

        modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));

        return "user/index";
    }

    /**
     * ????????????User
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "????????????User", notes = "????????????User?????????easyui????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User???form??????", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    EasyuiPageOutput listPage(UserQueryDto user) throws Exception {
        // ????????????????????????
        user.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput out = this.userService.listEasyuiPageByExample(user);
        return out;
    }

    /**
     * ??????????????????
     *
     * @param userListDto
     * @return
     */
    @RequestMapping(value = "/listByCondition.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput listByCondition(@RequestBody UserQueryDto userListDto) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setMarketId(marketService.getCurrentLoginMarketId());
        customer.setName(userListDto.getLikeName());
        return BaseOutput.success().setData(customerRpc.list(customer).getData());
    }

    /**
     * ??????keyword??????
     *
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/listByKeyword.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput listByCondition(@RequestParam(name = "query") String keyword) {
        return BaseOutput.success().setData(userService.findUserByNameOrPhoneOrTallyNo(keyword));
    }

    /**
     * ????????????
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/findPlates.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput findPlates(Long userId) {
        try {
            List<String> plateList = this.userPlateService.findUserPlateByUserId(userId).stream()
                    .map(UserPlate::getPlate).collect(Collectors.toList());
            return BaseOutput.success().setData(plateList);

        } catch (Exception e) {
            LOGGER.error("????????????", e);
            return BaseOutput.failure();
        }

    }

    /**
     * ????????????
     *
     * @param modelMap
     * @param id
     * @return
     */
    @ApiOperation("?????????User??????")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @PathVariable Long id) {
        UserInfo userItem = this.userService.get(id);
        String userPlateStr = this.userPlateService.findUserPlateByUserId(id).stream().map(UserPlate::getPlate)
                .collect(Collectors.joining(","));
        if (userItem != null) {
            userItem.setAddr(MaskUserInfo.maskAddr(userItem.getAddr()));
            userItem.setCardNo(MaskUserInfo.maskIdNo(userItem.getCardNo()));
            userItem.setPhone(MaskUserInfo.maskPhone(userItem.getPhone()));

        }
        UserTypeEnum userType = UserTypeEnum.fromCode(userItem.getUserType());
        modelMap.put("userTypeDesc", userType == null ? "" : userType.getDesc());
        modelMap.put("userItem", userItem);
        modelMap.put("userPlates", userPlateStr);

        return "user/view";
    }


    /**
     * ?????????edit??????
     *
     * @param modelMap
     * @param id
     * @return
     */
    @ApiOperation("?????????edit??????")
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        modelMap.put("item", new UserInfo());
        if (id != null) {
            UserInfo user = this.userService.get(id);
            modelMap.put("item", user);
        }
        modelMap.put("userTypeMap", Stream.of(UserTypeEnum.values())
                .collect(Collectors.toMap(UserTypeEnum::getCode, UserTypeEnum::getDesc)));

        String userPlateStr = this.userPlateService.findUserPlateByUserId(id).stream().map(UserPlate::getPlate)
                .collect(Collectors.joining(","));

        modelMap.put("userPlates", userPlateStr);

        modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));
        return "user/edit";
    }

    @Autowired
    UpdateUserQrStatusJob job;

    /**
     * ??????????????????
     *
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/triggerJob.html", method = RequestMethod.GET)
    @ResponseBody
    public BaseOutput triggerJob(ModelMap modelMap, Long id) {
        job.execute();
        return BaseOutput.success();
    }

    /**
     * ????????????
     *
     * @param id
     * @param is_active
     * @return
     */
    @RequestMapping(value = "/activeUser.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput activeUser(Long id, Integer is_active) {
        try {
            UserInfo user = new UserInfo();
            user.setId(id);
            user.setIsActive(is_active);
            int count = this.userService.updateSelective(user);
            if (count == 0) {
                return BaseOutput.failure();
            }
            return BaseOutput.success();
        } catch (Exception e) {
            LOGGER.error("????????????", e);
            return BaseOutput.failure();
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/queryByTallyAreaNo.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput queryByTallyAreaNo(String query) {
        try {
            TallyingArea tallyingArea = new TallyingArea();
            tallyingArea.setAssetsName(query);
            BaseOutput<List<TallyingArea>> result = tallyingAreaRpc.listByExample(tallyingArea);
            List<TallyingArea> tallyingAreaList = new ArrayList<>();
            if (null != result) {
                tallyingAreaList = result.getData();
            }
            List<DTO> data = new ArrayList<>(16);
            if (CollectionUtils.isNotEmpty(tallyingAreaList)) {
                StreamEx.of(tallyingAreaList).nonNull().forEach(tr -> {
                    BaseOutput<Customer> customerResult = customerRpc.getById(tr.getCustomerId());
                    if (customerResult.isSuccess()) {
                        Customer customer = customerResult.getData();
                        if (null != customer) {
                            DTO dto = new DTO();
                            dto.put("userName", customer.getName() == null ? "" : customer.getName());
                            dto.put("tallyAreaNo", tr.getAssetsName());
                            List<UserPlate> userPlateList = userPlateService.findUserPlateByUserId(customer.getId());
                            StreamEx.of(userPlateList).nonNull().forEach(p -> {
                                dto.put("plate", p.getPlate());
                            });
                            data.add(dto);
                        }
                    }
                });
            }
            //List<DTO>data=this.userService.queryByTallyAreaNo(query);
            return BaseOutput.success().setData(data);

        } catch (Exception e) {
            LOGGER.error("????????????", e);
            return BaseOutput.failure().setErrorData(e.getMessage());
        }

    }


    /**
     * ??????????????????????????????
     *
     * @param name
     * @return
     */
    @RequestMapping("/userQuery.action")
    @ResponseBody
    public Map<String, ?> listByName(String name) {
        Map map = Maps.newHashMap();
        try {
            CustomerQueryInput queryInput = new CustomerQueryInput();
            queryInput.setName(name);
            //queryInput.setContactsPhone(name);
            queryInput.setMarketId(MarketUtil.returnMarket());
            BaseOutput<List<CustomerExtendDto>> listByExample = customerRpc.list(queryInput);
            List<Map<String, Object>> list = Lists.newArrayList();
            if (null != listByExample) {
                List<CustomerExtendDto> userList = listByExample.getData();
                for (CustomerExtendDto user : userList) {
                    Map<String, Object> obj = Maps.newHashMap();
                    obj.put("id", user.getId());
                    obj.put("userName", user.getName());
                    obj.put("user", user);
                    String val = bulldShowVal(user);
                    obj.put("value", val);
                    list.add(obj);
                }
            }
            map.put("suggestions", list);
            return map;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return map;
    }

    /**
     * ???????????????
     *
     * @param user
     * @return
     */
    private String bulldShowVal(CustomerExtendDto user) {
        StringBuilder showVal = new StringBuilder();
        showVal.append(user.getName());
        showVal.append("  |  ");
        showVal.append(null == user.getCertificateNumber() ? "(????????????)" : user.getCertificateNumber());
        showVal.append("  |  ");
        if (null != user.getCustomerMarket()) {
            showVal.append(null == user.getCustomerMarket().getProfessionName() ? "(???????????????)" : user.getCustomerMarket().getProfessionName());
        } else {
            showVal.append("(?????????)");
        }
        showVal.append("  |  ");
        showVal.append(null == user.getCorporationName()?"(???)":user.getCorporationName());
        return showVal.toString();
    }
}