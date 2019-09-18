package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.service.BizNumberFunction;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.User;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.dto.QualityTraceTradeBillOutDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.RegisterBillStaticsDto;
import com.dili.trace.glossary.*;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class RegisterBillServiceImpl extends BaseServiceImpl<RegisterBill, Long> implements RegisterBillService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillServiceImpl.class);
	@Autowired
	BizNumberFunction bizNumberFunction;
	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	DetectRecordService detectRecordService;

	public RegisterBillMapper getActualDao() {
		return (RegisterBillMapper) getDao();
	}

	@Override
	public BaseOutput createRegisterBill(RegisterBill registerBill) {
		BaseOutput recheck = checkBill(registerBill);
		if (!recheck.isSuccess()) {
			return recheck;
		}
		registerBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
		registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
		registerBill.setVersion(1);
		registerBill.setCreated(new Date());
		if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TRADE_AREA.getCode().intValue()) {
			// 交易区没有理货区号
			registerBill.setTallyAreaNo(null);
			// 交易区数据直接进行待检测状态
//			registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
//			registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());	
		}
		if (StringUtils.isBlank(registerBill.getOperatorName())) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
		}

		registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
		// 车牌转大写
		registerBill.setPlate(StringUtils.trimToEmpty(registerBill.getPlate()).toUpperCase());

		int result = saveOrUpdate(registerBill);
		if (result == 0) {
			LOGGER.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
			recheck = BaseOutput.failure("创建失败");
		}
		return recheck;
	}

	private BaseOutput checkBill(RegisterBill registerBill) {

		if (registerBill.getRegisterSource() == null || registerBill.getRegisterSource().intValue() == 0) {
			LOGGER.error("登记来源不能为空");
			return BaseOutput.failure("登记来源不能为空");
		}
		if (StringUtils.isBlank(registerBill.getName())) {
			LOGGER.error("业户姓名不能为空");
			return BaseOutput.failure("业户姓名不能为空");
		}
		if (StringUtils.isBlank(registerBill.getIdCardNo())) {
			LOGGER.error("业户身份证号不能为空");
			return BaseOutput.failure("业户身份证号不能为空");
		}
		if (StringUtils.isBlank(registerBill.getAddr())) {
			LOGGER.error("业户身份证地址不能为空");
			return BaseOutput.failure("业户身份证地址不能为空");
		}
		if (StringUtils.isBlank(registerBill.getProductName())) {
			LOGGER.error("商品名称不能为空");
			return BaseOutput.failure("商品名称不能为空");
		}
		if (StringUtils.isBlank(registerBill.getOriginName())) {
			LOGGER.error("商品产地不能为空");
			return BaseOutput.failure("商品产地不能为空");
		}

		if (registerBill.getWeight() == null) {
			LOGGER.error("商品重量不能为空");
			return BaseOutput.failure("商品重量不能为空");
		}

		if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
			if (registerBill.getWeight().longValue() <= 0L) {
				LOGGER.error("商品重量不能小于0");
				return BaseOutput.failure("商品重量不能小于0");
			}
		} else {
			if (registerBill.getWeight().longValue() < 0L) {
				LOGGER.error("商品重量不能为负");
				return BaseOutput.failure("商品重量不能为负");
			}
		}

		return BaseOutput.success();
	}

	@Override
	public List<RegisterBill> findByExeMachineNo(String exeMachineNo, int taskCount) {
		List<RegisterBill> exist = getActualDao().findByExeMachineNo(exeMachineNo);
		if (!exist.isEmpty()) {
			LOGGER.info("获取的任务已经有相应的数量了" + taskCount);
			if (exist.size() >= taskCount) {
				return exist.subList(0, taskCount);
			}
		}

		int fetchSize = taskCount - exist.size();
		LOGGER.info("还需要再拿多少个：" + fetchSize);

		List<Long> ids = getActualDao().findIdsByExeMachineNo(fetchSize);
		StringBuilder sb = new StringBuilder();
		sb.append(0);
		for (Long id : ids) {
			sb.append(",").append(id);
		}
		getActualDao().taskByExeMachineNo(exeMachineNo, sb.toString());
		return getActualDao().findByExeMachineNo(exeMachineNo);
	}

	@Override
	public List<RegisterBill> findByProductName(String productName) {
		RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
		registerBill.setProductName(productName);
		return list(registerBill);
	}

	@Override
	public RegisterBill findByCode(String code) {
		RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
		registerBill.setCode(code);
		List<RegisterBill> list = list(registerBill);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public RegisterBillOutputDto findByTradeNo(String tradeNo) {
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(tradeNo);
		if (qualityTraceTradeBill != null && StringUtils.isNotBlank(qualityTraceTradeBill.getRegisterBillCode())) {
			RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
			registerBill.setCode(qualityTraceTradeBill.getRegisterBillCode());
			List<RegisterBill> list = this.listByExample(registerBill);
			if (list != null && list.size() > 0) {
				return DTOUtils.as(list.get(0), RegisterBillOutputDto.class);
			}
		}
		return null;
	}

	@Override
	public int matchDetectBind(QualityTraceTradeBill qualityTraceTradeBill) {

		MatchDetectParam matchDetectParam = new MatchDetectParam();
		matchDetectParam.setTradeNo(qualityTraceTradeBill.getOrderId());
		matchDetectParam.setTradeTypeId(qualityTraceTradeBill.getTradetypeId());
		matchDetectParam.setTradeTypeName(qualityTraceTradeBill.getTradetypeName());
		matchDetectParam.setProductName(qualityTraceTradeBill.getProductName());
		matchDetectParam.setIdCardNo(qualityTraceTradeBill.getSellerIDNo());
		matchDetectParam.setEnd(qualityTraceTradeBill.getOrderPayDate());
		Date start = new Date(qualityTraceTradeBill.getOrderPayDate().getTime() - (48 * 3600000));
		matchDetectParam.setStart(start);
		LOGGER.info("进行匹配:" + matchDetectParam.toString());
		Long id = getActualDao().findMatchDetectBind(matchDetectParam);
		int rows = 0;
		if (null != id) {
			rows = getActualDao().matchDetectBind(qualityTraceTradeBill.getOrderId(),
					qualityTraceTradeBill.getNetWeight(), id);
		}
		return rows;
	}

	@Override
	public int auditRegisterBill(Long id, Boolean pass) {
		RegisterBill registerBill = get(id);
		if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
			if (pass) {
				registerBill.setSampleCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL_SAMPLE_CODE));
				registerBill.setState(RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue());
//				if (StringUtils.isNotBlank(registerBill.getDetectReportUrl())) {
//					// 有检测报告，直接通过检测
////					registerBill.setLatestDetectTime(new Date());
//					registerBill.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
//					registerBill.setDetectState(BillDetectStateEnum.PASS.getCode());
//				}
			} else {
				registerBill.setState(-1);
			}
			return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	@Override
	public int undoRegisterBill(Long id) {
		RegisterBill registerBill = get(id);
		if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			LOGGER.info(userTicket.getDepName() + ":" + userTicket.getRealName() + "删除登记单"
					+ JSON.toJSON(registerBill).toString());
			return delete(id);
			// return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	@Override
	public int autoCheckRegisterBill(Long id) {
		RegisterBill registerBill = get(id);
		if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
			registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
			registerBill.setSampleSource(SampleSourceEnum.AUTO_CHECK.getCode().intValue());
			return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	@Override
	public int samplingCheckRegisterBill(Long id) {
		RegisterBill registerBill = get(id);
		if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
			registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
			registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
			return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	@Override
	public int reviewCheckRegisterBill(Long id) {
		RegisterBill registerBill = get(id);
		if (registerBill.getState().intValue() == RegisterBillStateEnum.ALREADY_CHECK.getCode().intValue()
				&& registerBill.getDetectState().intValue() == BillDetectStateEnum.NO_PASS.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
			registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
			registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
			registerBill.setExeMachineNo(null);
			return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	UserTicket getOptUser() {
		return SessionContext.getSessionContext().getUserTicket();
	}

	@Override
	public QualityTraceTradeBillOutDto findQualityTraceTradeBill(String tradeNo) {
		if (StringUtils.isBlank(tradeNo)) {
			return null;
		}
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(tradeNo);
		if (qualityTraceTradeBill == null) {
			return null;
		}
		QualityTraceTradeBillOutDto dto = DTOUtils.newDTO(QualityTraceTradeBillOutDto.class);
		dto.setQualityTraceTradeBill(qualityTraceTradeBill);

		if (StringUtils.isNotBlank(qualityTraceTradeBill.getRegisterBillCode())) {
			RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);

			registerBill.setCode(qualityTraceTradeBill.getRegisterBillCode());
			List<RegisterBill> list = this.listByExample(registerBill);
			if (!list.isEmpty()) {
				dto.setRegisterBill(list.get(0));
			}
		}

//		RegisterBillOutputDto registerBill = findByTradeNo(tradeNo);

//		if (qualityTraceTradeBill.getBuyerIDNo().equalsIgnoreCase(cardNo)) {
//			if (registerBill == null) {
//				int result = matchDetectBind(qualityTraceTradeBill);
//				if (result == 1) {
//					registerBill = findByTradeNo(tradeNo);
//				}
//			}
//		}

//		if (registerBill != null) {
//			List<SeparateSalesRecord> records = separateSalesRecordService
//					.findByRegisterBillCode(registerBill.getCode());
//			registerBill.setSeparateSalesRecords(records);
//			registerBill.setDetectRecord(detectRecordService.findByRegisterBillCode(registerBill.getCode()));
//		} else {
//			registerBill = DTOUtils.newDTO(RegisterBillOutputDto.class);
//		}

		// 查询交易单信息
//		if(StringUtils.isNotBlank(registerBill.getCode())) {
//			QualityTraceTradeBill example = DTOUtils.newDTO(QualityTraceTradeBill.class);
//			example.setRegisterBillCode(registerBill.getCode());
//			List<QualityTraceTradeBill> qualityTraceTradeBillList = this.qualityTraceTradeBillService
//					.listByExample(example);
//
//			registerBill.setQualityTraceTradeBillList(qualityTraceTradeBillList);
//		}

//		registerBill.setQualityTraceTradeBill(qualityTraceTradeBill);
		return dto;
	}

	@Override
	public RegisterBillStaticsDto groupByState(RegisterBillDto dto) {
		return this.getActualDao().groupByState(dto);

	}

	@Override
	public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill) {
		LOGGER.info("获取登记单信息信息" + JSON.toJSONString(registerBill));
		RegisterBillOutputDto outputDto = null;
		if (registerBill == null) {
			return null;
		} else {
			outputDto = DTOUtils.as(registerBill, RegisterBillOutputDto.class);
		}
		// 查询交易单信息
		QualityTraceTradeBill example = DTOUtils.newDTO(QualityTraceTradeBill.class);
		example.setRegisterBillCode(registerBill.getCode());
		List<QualityTraceTradeBill> qualityTraceTradeBillList = this.qualityTraceTradeBillService
				.listByExample(example);

		outputDto.setQualityTraceTradeBillList(qualityTraceTradeBillList);
//		if (StringUtils.isNotBlank(registerBill.getTradeNo())) {
//			// 交易信息
//			QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService
//					.findByTradeNo(registerBill.getTradeNo());
//			outputDto.setQualityTraceTradeBill(qualityTraceTradeBill);
//		}
		// 分销信息
		if (registerBill.getSalesType() != null
				&& registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
			// 分销
			List<SeparateSalesRecord> records = separateSalesRecordService
					.findByRegisterBillCode(registerBill.getCode());
			outputDto.setSeparateSalesRecords(records);
		}

		// 检测信息
//		if (registerBill.getLatestDetectRecordId() != null) {
//			// 检测信息
//			outputDto.setDetectRecord(detectRecordService.findByRegisterBillCode(registerBill.getCode()));
//		}
		return outputDto;
	}

	@Override
	public Long saveHandleResult(RegisterBill input) {
		if (input == null || input.getId() == null
				|| StringUtils.isAnyBlank(input.getHandleResult(), input.getHandleResultUrl())) {
			throw new AppException("参数错误");
		}
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new AppException("数据错误");
		}

		RegisterBill example = DTOUtils.newDTO(RegisterBill.class);
		example.setId(item.getId());
		example.setHandleResult(input.getHandleResult());
		example.setHandleResultUrl(input.getHandleResultUrl());
		this.updateSelective(example);

		return example.getId();

	}

	@Override
	public Long doModifyRegisterBill(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new AppException("参数错误");
		}
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new AppException("数据错误");
		}

		RegisterBill example = DTOUtils.newDTO(RegisterBill.class);
		example.setId(item.getId());
		example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
		example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
		this.updateSelective(example);

		return example.getId();
	}
}