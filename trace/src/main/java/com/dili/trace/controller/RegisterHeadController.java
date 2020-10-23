package com.dili.trace.controller;

import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MaskUserInfo;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import com.google.common.collect.Lists;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 进门主台账单实现类
 *
 * @author Lily
 */
@Api("/registerHead")
@Controller
@RequestMapping("/registerHead")
public class RegisterHeadController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterHeadController.class);

	@Autowired
	RegisterHeadService registerHeadService;

	@Autowired
	UserService userService;

	@ApiOperation("跳转到RegisterHead页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {

		RegisterHeadDto query=new RegisterHeadDto();
		Date now = new Date();
		query.setCreatedEnd(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		query.setCreatedStart(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("query", query);

		return "registerHead/index";
	}

	@ApiOperation(value = "查询RegisterHead", notes = "查询RegisterHead，返回列表信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterHead", paramType = "form", value = "RegisterHead的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<RegisterHead> list(RegisterHeadDto registerHead) {
		return registerHeadService.list(registerHead);
	}

	@ApiOperation(value = "分页查询RegisterHead", notes = "分页查询RegisterHead，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterHead", paramType = "form", value = "RegisterHead的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	
	public @ResponseBody String listPage(RegisterHeadDto query) throws Exception {
		return registerHeadService.listEasyuiPageByExample(query, true).toString();
	}
	

	@ApiOperation("新增RegisterHead")
	@RequestMapping(value = "/insert.action", method = RequestMethod.POST)
	public @ResponseBody BaseOutput insert(@RequestBody List<RegisterHead> registerHeads) {
		logger.info("保存进门主台账单数据:" + registerHeads.size());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		for (RegisterHead registerHead : registerHeads) {

			User user = userService.get(registerHead.getUserId());
			if (user == null) {
				return BaseOutput.failure("用户不存在");
			}
			registerHead.setName(user.getName());
			registerHead.setIdCardNo(user.getCardNo());
			registerHead.setAddr(user.getAddr());
			registerHead.setUserId(user.getId());
			try {
				Long billId = registerHeadService.createRegisterHead(registerHead, new ArrayList<ImageCert>(),
						Optional.ofNullable(new OperatorUser(userTicket.getId(), userTicket.getRealName())));

			} catch (TraceBusinessException e) {
				return BaseOutput.failure(e.getMessage());

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return BaseOutput.failure("服务端出错");

			}
		}
		return BaseOutput.success("新增成功").setData(registerHeads);
	}

	@ApiOperation("修改RegisterHead")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterHead", paramType = "form", value = "RegisterHead的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(RegisterHead registerHead) {
		registerHeadService.updateSelective(registerHead);
		return BaseOutput.success("修改成功");
	}
}