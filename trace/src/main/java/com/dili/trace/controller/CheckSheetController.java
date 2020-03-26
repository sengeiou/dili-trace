package com.dili.trace.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.BusinessException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.service.CheckSheetService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.MaskUserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/checkSheet")
@Controller
@RequestMapping("/checkSheet")
public class CheckSheetController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckSheetController.class);

	@Autowired
	UserService userService;
	@Autowired
	UserPlateService userPlateService;
	@Resource
	DefaultConfiguration defaultConfiguration;
	@Autowired
	BaseInfoRpcService baseInfoRpcService;
	@Autowired
	CheckSheetService checkSheetService;

	@ApiOperation("跳转到CheckSheet页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		LocalDateTime now=LocalDateTime.now();
		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
		modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
		
		
		return "checkSheet/index";
	}

	@ApiOperation(value = "分页查询CheckSheet", notes = "分页查询CheckSheet，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(CheckSheet checkSheet) throws Exception {
		EasyuiPageOutput out=this.checkSheetService.listEasyuiPageByExample(checkSheet,true);
		return out.toString();
	}

	@ApiOperation("新增CheckSheet")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody CheckSheetInputDto input) {
		try {
			return BaseOutput.success("新增成功");
		} catch (BusinessException e) {
			LOGGER.error("checksheet", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error( e.getMessage(),e);
			return BaseOutput.failure();
		}
	}

	
	@ApiOperation("跳转到CheckSheet页面")
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap,@PathVariable Long id) {
		User userItem=this.userService.get(id);
		String userPlateStr=this.userPlateService.findUserPlateByUserId(id).stream().map(UserPlate::getPlate).collect(Collectors.joining(","));
		if(userItem!=null) {
			userItem.setAddr(MaskUserInfo.maskAddr(userItem.getAddr()));
			userItem.setCardNo(MaskUserInfo.maskIdNo(userItem.getCardNo()));
			userItem.setPhone(MaskUserInfo.maskPhone(userItem.getPhone()));
		}
		
		modelMap.put("userItem", userItem);
		modelMap.put("userPlates", userPlateStr);
		
		return "checkSheet/view";
	}
}