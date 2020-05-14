package com.dili.trace.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.CheckSheetDetail;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.dto.CheckSheetQueryDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.CheckSheetDetailService;
import com.dili.trace.service.CheckSheetService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UpStreamService;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/upStream")
@Controller
@RequestMapping("/upStream")
public class UpStreamController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpStreamController.class);

	@Autowired
	CheckSheetService checkSheetService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	ApproverInfoService approverInfoService;
	@Autowired
	CheckSheetDetailService checkSheetDetailService;
	@Autowired
	UpStreamService upStreamService;

	@ApiOperation("跳转到CheckSheet页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		LocalDateTime now = LocalDateTime.now();
		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
		modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));

		return "upStream/index";
	}

	@RequestMapping(value = "/insert.html", method = RequestMethod.GET)
	public String edit(ModelMap modelMap) {
		return "upStream/insert";
	}
	
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit(ModelMap modelMap, @RequestParam("id") Long id) {
		return "upStream/edit";
	}


	@ApiOperation(value = "分页查询CheckSheet", notes = "分页查询CheckSheet，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "UpStream", paramType = "form", value = "UpStream的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UpStream upStream) throws Exception {
		EasyuiPageOutput out = this.upStreamService.listEasyuiPageByExample(upStream, true);
		return out.toString();
	}

	@ApiOperation("新增CheckSheet")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "UpStream", paramType = "form", value = "UpStream的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody UpStream input) {
		try {
			this.upStreamService.createUpstream(input);
			return BaseOutput.success("新增成功");
		} catch (BusinessException e) {
			LOGGER.error("checksheet", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure();
		}
	}

	@ApiOperation("跳转到UpStream页面")
	@RequestMapping(value = "/view.html", method = RequestMethod.GET)
	public String view(ModelMap modelMap,  @RequestParam("id") Long id) {
		modelMap.put("item", null);
		return "upStream/view";
	}
}