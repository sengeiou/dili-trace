package com.dili.trace.controller;

import java.util.List;
import java.util.function.Function;

import com.dili.trace.dto.SeperatePrintOutput;
import com.dili.common.exception.TraceBizException;
import com.dili.trace.dto.OperatorUser;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.sg.trace.domain.SeperatePrintReport;
import com.dili.trace.dto.SeperatePrintReportInputDto;
import com.dili.trace.dto.SeperatePrintReportOutputDto;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.BillService;
import com.dili.trace.service.SeperatePrintReportService;

import one.util.streamex.StreamEx;

/**
 * 分销报告打印
 */
@Controller
@RequestMapping("/seperatePrintReport")
public class SeperatePrintReportController {
	private static final Logger logger = LoggerFactory.getLogger(SeperatePrintReportController.class);
	@Autowired
	SeperatePrintReportService seperatePrintReportService;
	@Autowired
	BillService billService;
	@Autowired
	ApproverInfoService approverInfoService;

	/**
	 * 进入打印页面
	 * @param intputDto
	 * @return
	 */
	@RequestMapping("/previewPrintableData.action")
	@ResponseBody
	public BaseOutput<?> previewPrintableData(@RequestBody SeperatePrintReportInputDto intputDto) {
		if (intputDto == null || intputDto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		List<SeperatePrintReport> seperatePrintReportList = StreamEx.ofNullable(intputDto.getSeperatePrintReportList())
				.nonNull().flatCollection(Function.identity()).nonNull().map(r -> {
					r.setBillId(intputDto.getBillId());
					r.setValidPeriod(intputDto.getValidPeriod());
					r.setApproverInfoId(intputDto.getApproverInfoId());
					return r;
				}).toList();

		try {
			List<SeperatePrintReportOutputDto> resultList = this.seperatePrintReportService.buildPreViewOutputList(
					seperatePrintReportList, OperatorUser.build(SessionContext.getSessionContext()));
			SeperatePrintOutput data=SeperatePrintOutput.build(resultList);
			return BaseOutput.success().setData(resultList);
		} catch (TraceBizException e) {

			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 保存数据
	 * @param intputDto
	 * @return
	 */
	@RequestMapping("/submitPrintableData.action")
	@ResponseBody
	public BaseOutput<?> submitPrintableData(@RequestBody SeperatePrintReportInputDto intputDto) {
		if (intputDto == null || intputDto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		List<SeperatePrintReport> seperatePrintReportList = StreamEx.ofNullable(intputDto.getSeperatePrintReportList())
				.nonNull().flatCollection(Function.identity()).nonNull().map(r -> {
					r.setBillId(intputDto.getBillId());
					r.setValidPeriod(intputDto.getValidPeriod());
					r.setApproverInfoId(intputDto.getApproverInfoId());
					return r;
				}).toList();

		try {
			List<SeperatePrintReportOutputDto> resultList = this.seperatePrintReportService.saveSeperatePrintReportList(
					seperatePrintReportList, OperatorUser.build(SessionContext.getSessionContext()));

			return BaseOutput.success().setData(resultList);
		} catch (TraceBizException e) {

			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	/**
	 * 报告详情
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping("/seperateReportDetail.html")
	public String seperateReportDetail(ModelMap modelMap, Long id) {

		SeperatePrintReportOutputDto item = this.seperatePrintReportService.findPrintReportOutDtoById(id)
				.orElse(new SeperatePrintReportOutputDto());
		modelMap.put("item", item);

		return "seperatePrintReport/seperateReportDetail";
	}

}
