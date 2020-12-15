package com.dili.trace.controller;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.common.util.MD5Util;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.TallyingAreaRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.jobs.UpdateUserQrStatusJob;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.rpc.service.TallyingAreaRpcService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.MarketUtil;
import com.dili.trace.util.MaskUserInfo;
import com.dili.uap.sdk.rpc.UserRpc;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
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
    CustomerRpcService customerRpcService;

    /**
     * 跳转到User页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到User页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        // Date now = new Date();
        // modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        // modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        LocalDateTime now = LocalDateTime.now();
//		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1)
//				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("createdStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));

        // modelMap.put("cities", this.queryCitys());
        modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));

        return "user/index";
    }

    /**
     * 分页查询User
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(UserListDto user) throws Exception {
        // 设置市场查询条件
        user.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput out = this.userService.listEasyuiPageByExample(user);
        return out.toString();
    }

    /**
     * 新增User
     *
     * @param user
     * @return
     */
    @ApiOperation("新增User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Long> insert(@RequestBody User user) {
        try {
            user.setPassword(MD5Util.md5(defaultConfiguration.getPassword()));
            user.setState(EnabledStateEnum.ENABLED.getCode());
            userService.register(user, false);
            return BaseOutput.success("新增成功").setData(user.getId());
        } catch (TraceBizException e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 修改User
     *
     * @param user
     * @return
     */
    @ApiOperation("修改User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(User user) {
        try {
            userService.updateUser(user);
            return BaseOutput.success("修改成功");
        } catch (TraceBizException e) {
            LOGGER.error("修改用户", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用户", e);
            return BaseOutput.failure(e.getMessage());
        }

    }

    /**
     * 删除User
     *
     * @param id
     * @return
     */
    @ApiOperation("删除User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long")})
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 业户条件查询
     *
     * @param userListDto
     * @return
     */
    @RequestMapping(value = "/listByCondition.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput listByCondition(UserListDto userListDto) {
        return BaseOutput.success().setData(userService.listByExample(userListDto));
    }

    /**
     * 业户keyword查询
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
     * @param id
     * @param enable 是否启用
     * @return
     */
    @RequestMapping(value = "/doEnable.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doEnable(Long id, Boolean enable) {
        try {
            userService.updateEnable(id, enable);
            return BaseOutput.success("修改用户状态成功");
        } catch (TraceBizException e) {
            LOGGER.error("修改用户状态", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用户状态", e);
            return BaseOutput.failure();
        }

    }

    /**
     * 找回密码【接口已通】
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "找回密码【接口已通】", notes = "找回密码")
    @RequestMapping(value = "/resetPassword.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput<Boolean> resetPassword(Long id) {
        User user = DTOUtils.newDTO(User.class);
        user.setId(id);
        user.setPassword(MD5Util.md5(defaultConfiguration.getPassword()));
        userService.updateSelective(user);
        return BaseOutput.success().setData(true);
    }

    /**
     * 查询车牌
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
            LOGGER.error("查询失败", e);
            return BaseOutput.failure();
        }

    }

    /**
     * 查看详情
     *
     * @param modelMap
     * @param id
     * @return
     */
    @ApiOperation("跳转到User页面")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @PathVariable Long id) {
        User userItem = this.userService.get(id);
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
     * 跳转到edit页面
     *
     * @param modelMap
     * @param id
     * @return
     */
    @ApiOperation("跳转到edit页面")
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        modelMap.put("item", DTOUtils.newDTO(User.class));
        if (id != null) {
            User user = this.userService.get(id);
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
     * 出发定时任务
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
     * 获得用户二维码信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUserQrCode.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput getUserQrCode(Long id) {
        try {
            UserQrOutput userQrOutput = this.userService.getUserQrCodeWithName(id);
            return BaseOutput.success().setData(userQrOutput);

        } catch (Exception e) {
            LOGGER.error("查询失败", e);
            return BaseOutput.failure();
        }

    }

    /**
     * 激活用户
     *
     * @param id
     * @param is_active
     * @return
     */
    @RequestMapping(value = "/activeUser.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput activeUser(Long id, Integer is_active) {
        try {
            User user = DTOUtils.newDTO(User.class);
            user.setId(id);
            user.setIsActive(is_active);
            int count = this.userService.updateSelective(user);
            if (count == 0) {
                return BaseOutput.failure();
            }
            return BaseOutput.success();
        } catch (Exception e) {
            LOGGER.error("激活失败", e);
            return BaseOutput.failure();
        }

    }

    /**
     * 通过理货区号查询数据
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
                    CustomerExtendDto customer = customerRpcService.findCustomerByIdOrEx(tr.getCustomerId(), MarketUtil.returnMarket());
                    if (null != customer) {
                        List<UserPlate> userPlateList = userPlateService.findUserPlateByUserId(customer.getId());
                        StreamEx.of(userPlateList).nonNull().forEach(p -> {
                            DTO dto = new DTO();
                            dto.put("userName", customer.getName() == null ? "" : customer.getName());
                            dto.put("plate", p.getPlate());
                            dto.put("tallyAreaNo", tr.getAssetsName());
                            data.add(dto);
                        });
                    }
                });
            }
            //List<DTO>data=this.userService.queryByTallyAreaNo(query);
            return BaseOutput.success().setData(data);

        } catch (Exception e) {
            LOGGER.error("查询失败", e);
            return BaseOutput.failure();
        }

    }
}