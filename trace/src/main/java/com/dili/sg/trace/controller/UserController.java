package com.dili.sg.trace.controller;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.util.MD5Util;
import com.dili.trace.util.MaskUserInfo;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.BeanMapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
	@Resource
	DefaultConfiguration defaultConfiguration;
	@Autowired
	UsualAddressService usualAddressService;

	/**
	 * 跳转到User页面
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到User页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		LocalDateTime now=LocalDateTime.now();
		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
		modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
		
		modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));
		
		return "user/index";
	}

	/**
	 * 分页查询User
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UserListDto user) throws Exception {
		EasyuiPageOutput out=this.userService.listEasyuiPageByExample(BeanMapUtil.trimBean(user));
		return out.toString();
	}


}