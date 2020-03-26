package com.dili.trace.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.BusinessException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.CheckSheetService;
import com.dili.trace.service.RegisterBillService;
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
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	ApproverInfoService approverInfoService;

	@ApiOperation("跳转到CheckSheet页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		LocalDateTime now = LocalDateTime.now();
		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
		modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));

		return "checkSheet/index";
	}

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,@RequestParam("registerBillIdList") List<Long>registerBillIdList) {
		if(registerBillIdList!=null&&!registerBillIdList.isEmpty()) {
			RegisterBillDto queryDto=DTOUtils.newDTO(RegisterBillDto.class);
			queryDto.setIdList(registerBillIdList);
			List<RegisterBill>itemList=this.registerBillService.listByExample(queryDto);
			
			List<String>detectOperatorNameList=itemList.stream().map(RegisterBill::getLatestDetectOperator).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
			
			
			List<RegisterBill>registerUserList=itemList.stream().collect(Collectors.groupingBy(RegisterBill::getIdCardNo)).entrySet().stream().map((entry)->{
				
				if(!entry.getValue().isEmpty()) {
					
					return entry.getValue().get(0);
				}
				return null;
				
			}).filter(Objects::nonNull).collect(Collectors.toList());
			
			modelMap.put("itemList",itemList);
			modelMap.put("registerUserList",registerUserList);
			modelMap.put("detectOperatorNameList",detectOperatorNameList);
		}else {
			modelMap.put("itemList",Collections.emptyList());
			modelMap.put("registerUserList",Collections.emptyList());
			modelMap.put("detectOperatorNameList",Collections.emptyList());
		}
		List<ApproverInfo>approverInfoList=this.approverInfoService.listByExample(DTOUtils.newDTO(ApproverInfo.class));
		modelMap.put("approverInfoList",approverInfoList);
		
		return "checkSheet/edit";
	}

	@ApiOperation(value = "分页查询CheckSheet", notes = "分页查询CheckSheet，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(CheckSheet checkSheet) throws Exception {
		EasyuiPageOutput out = this.checkSheetService.listEasyuiPageByExample(checkSheet, true);
		return out.toString();
	}

	@ApiOperation("新增CheckSheet")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody CheckSheetInputDto input) {
		try {
			CheckSheet item=this.checkSheetService.createCheckSheet(input);
			return BaseOutput.success("新增成功").setData(item.getId());
		} catch (BusinessException e) {
			LOGGER.error("checksheet", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure();
		}
	}

	@ApiOperation("跳转到CheckSheet页面")
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

		modelMap.put("userItem", userItem);
		modelMap.put("userPlates", userPlateStr);

		return "checkSheet/view";
	}
}