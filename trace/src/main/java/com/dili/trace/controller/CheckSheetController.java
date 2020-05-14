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

import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.CheckSheetDetail;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.dto.CheckSheetQueryDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.CheckSheetDetailService;
import com.dili.trace.service.CheckSheetService;
import com.dili.trace.service.RegisterBillService;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(UpStreamController.class);

	@Autowired
	CheckSheetService checkSheetService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	ApproverInfoService approverInfoService;
	@Autowired
	CheckSheetDetailService checkSheetDetailService;

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
	public String edit(ModelMap modelMap, @RequestParam("registerBillIdList") List<Long> registerBillIdList) {
		if (registerBillIdList != null && !registerBillIdList.isEmpty()) {
			RegisterBillDto queryDto = DTOUtils.newDTO(RegisterBillDto.class);
			queryDto.setIdList(registerBillIdList);
			List<RegisterBill> itemList = this.registerBillService.listByExample(queryDto).stream()
					.filter(item -> item.getCheckSheetId() == null)
					.filter(bill -> BillDetectStateEnum.PASS.getCode().equals(bill.getDetectState())
							|| BillDetectStateEnum.REVIEW_PASS.getCode().equals(bill.getDetectState()))
					.collect(Collectors.toList());

			List<String> detectOperatorNameList = itemList.stream().map(RegisterBill::getLatestDetectOperator)
					.filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());

			List<RegisterBill> registerUserList = itemList.stream()
					.collect(Collectors.groupingBy(RegisterBill::getIdCardNo)).entrySet().stream().map((entry) -> {

						if (!entry.getValue().isEmpty()) {

							return entry.getValue().get(0);
						}
						return null;

					}).filter(Objects::nonNull).collect(Collectors.toList());

			modelMap.put("itemList", itemList);
			modelMap.put("registerUserList", registerUserList);
			modelMap.put("detectOperatorNameList", detectOperatorNameList);
		} else {
			modelMap.put("itemList", Collections.emptyList());
			modelMap.put("registerUserList", Collections.emptyList());
			modelMap.put("detectOperatorNameList", Collections.emptyList());
		}
		List<ApproverInfo> approverInfoList = this.approverInfoService
				.listByExample(DTOUtils.newDTO(ApproverInfo.class));
		modelMap.put("approverInfoList", approverInfoList);

		return "checkSheet/edit";
	}

	@ApiOperation(value = "分页查询CheckSheet", notes = "分页查询CheckSheet，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(CheckSheetQueryDto checkSheet) throws Exception {
		if(StringUtils.isNotBlank(checkSheet.getLikeApproverUserName())) {
			checkSheet.mset(IDTO.AND_CONDITION_EXPR, "  (approver_info_id in (select id from approver_info where user_name='"+checkSheet.getLikeApproverUserName().trim()+"')) ");
		}
		EasyuiPageOutput out = this.checkSheetService.listEasyuiPageByExample(checkSheet, true);
		return out.toString();
	}

	@ApiOperation("新增CheckSheet")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody CheckSheetInputDto input) {
		try {
			Map resultMapDto = this.checkSheetService.createCheckSheet(input);
			return BaseOutput.success("新增成功").setData(resultMapDto);
		} catch (BusinessException e) {
			LOGGER.error("checksheet", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure();
		}
	}

	@ApiOperation("预览CheckSheet")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/prePrint.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Object> prePrint(@RequestBody CheckSheetInputDto input) {
		try {
			Map resultMapDto = this.checkSheetService.prePrint(input);
			return BaseOutput.success().setData(resultMapDto);
		} catch (BusinessException e) {
			LOGGER.error("checksheet", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure();
		}
	}
	@ApiOperation("预览CheckSheet")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "CheckSheet", paramType = "form", value = "CheckSheet的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/findPrintableCheckSheet.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Object> findPrintableCheckSheet(CheckSheetInputDto input) {
		try {
			Map resultMapDto = this.checkSheetService.findPrintableCheckSheet(input.getId());
			return BaseOutput.success().setData(resultMapDto);
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
		modelMap.put("item", null);
		modelMap.put("checkSheetDetailList", Collections.emptyList());
		modelMap.put("registerBillIdMap", Collections.emptyMap());
		if (id != null) {
			CheckSheet checkSheet = this.checkSheetService.get(id);
			modelMap.put("item", checkSheet);
			if (checkSheet != null) {
				ApproverInfo approverInfo = this.approverInfoService.get(checkSheet.getApproverInfoId());
				modelMap.put("approverInfo", approverInfo);
				CheckSheetDetail detailQuery = DTOUtils.newDTO(CheckSheetDetail.class);
				detailQuery.setCheckSheetId(checkSheet.getId());
				List<CheckSheetDetail> checkSheetDetailList = this.checkSheetDetailService.listByExample(detailQuery).stream().map(detail->{
					Integer detectState=detail.getDetectState();
					if(BillDetectStateEnum.PASS.getCode().equals(detectState)||BillDetectStateEnum.REVIEW_PASS.getCode().equals(detectState)) {
						detail.setDetectStateView("合格");	
					}else {
						detail.setDetectStateView("未知");
					}
					return detail;
					
				}).collect(Collectors.toList());
				modelMap.put("checkSheetDetailList", checkSheetDetailList);

				List<Long> registerBillIdList = checkSheetDetailList.stream().map(CheckSheetDetail::getRegisterBillId)
						.collect(Collectors.toList());
				if (!registerBillIdList.isEmpty()) {
					RegisterBillDto registerBillQuery = DTOUtils.newDTO(RegisterBillDto.class);
					registerBillQuery.setIdList(registerBillIdList);
					List<RegisterBill>registerBillList=this.registerBillService.listByExample(registerBillQuery);
					Map<Long, RegisterBill> registerBillIdMap = registerBillList.stream()
							.collect(Collectors.toMap(RegisterBill::getId, Function.identity()));
					modelMap.put("registerBillIdMap", registerBillIdMap);
				}

			}
		}

		return "checkSheet/view";
	}
	@ApiOperation("跳转到CheckSheet二维码请求页面")
	@RequestMapping(value = "/detail/{checkSheetCode}", method = RequestMethod.GET)
	public String detail(ModelMap modelMap, @PathVariable(required = false) String checkSheetCode) {
		modelMap.put("item", new HashMap<String,Object>());
		this.checkSheetService.findCheckSheetByCode(checkSheetCode).ifPresent(checkSheet->{
			Map resultMap = this.checkSheetService.findPrintableCheckSheet(checkSheet.getId());
			modelMap.put("item", resultMap);
		});

		return "checkSheet/detail";
	}
}