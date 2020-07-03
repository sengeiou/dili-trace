package com.dili.trace.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.BizNumberFunction;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.AppException;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.dto.BatchAuditDto;
import com.dili.trace.dto.BatchResultDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.enums.VerifyTypeEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.BrandService;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserQrItemService;
import com.dili.trace.service.UsualAddressService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
//import org.hibernate.SQLQuery.ReturnProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class RegisterBillServiceImpl extends BaseServiceImpl<RegisterBill, Long> implements RegisterBillService {
	private static final Logger logger = LoggerFactory.getLogger(RegisterBillServiceImpl.class);
	@Autowired
	BizNumberFunction bizNumberFunction;
	@Autowired
	ImageCertService imageCertService;
	@Autowired
	UserPlateService userPlateService;
	@Autowired
	CodeGenerateService codeGenerateService;
	@Autowired
	UsualAddressService usualAddressService;
	@Autowired
	UserQrItemService userQrItemService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	TradeDetailService tradeDetailService;
	@Autowired
	BrandService brandService;
	

	public RegisterBillMapper getActualDao() {
		return (RegisterBillMapper) getDao();
	}

	@Transactional
	@Override
	public List<Long> createBillList(List<CreateRegisterBillInputDto> registerBills, User user,
			OperatorUser operatorUser) {
		return StreamEx.of(registerBills).nonNull().map(dto -> {
			logger.info("循环保存登记单:" + JSON.toJSONString(dto));
			RegisterBill registerBill = dto.build(user);
			return this.createRegisterBill(registerBill, dto.getImageCertList(), operatorUser);
		}).toList();

	}

	@Transactional
	@Override
	public Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList,
			OperatorUser operatorUser) {
		this.checkBill(registerBill);

		registerBill.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		registerBill.setVerifyType(VerifyTypeEnum.NONE.getCode());
		registerBill.setState(RegisterBillStateEnum.NEW.getCode());
		registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
		registerBill.setVersion(1);
		registerBill.setCreated(new Date());
		registerBill.setYn(YnEnum.YES.getCode());

		registerBill.setOperatorName(operatorUser.getName());
		registerBill.setOperatorId(operatorUser.getId());

		registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
		// 车牌转大写
		String plate=StreamEx.ofNullable(registerBill.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull().map(String::toUpperCase).findFirst().orElse(null);
		registerBill.setPlate(plate);
		this.userPlateService.checkAndInsertUserPlate(registerBill.getUserId(), plate);

		int result = super.saveOrUpdate(registerBill);
		if (result == 0) {
			logger.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
			throw new TraceBusinessException("创建失败");
		}
		if (imageCertList != null) {
			this.imageCertService.insertImageCert(imageCertList, registerBill.getId());
		}

		// 创建/更新品牌信息并更新brandId字段值
		this.brandService.createOrUpdateBrand(registerBill.getBrandName(), registerBill.getUserId())
				.ifPresent(brandId -> {
					RegisterBill bill = new RegisterBill();
					bill.setBrandId(brandId);
					bill.setId(registerBill.getId());
					this.updateSelective(bill);
				});
		return registerBill.getId();
	}

	private BaseOutput checkBill(RegisterBill registerBill) {

		if (!BillTypeEnum.fromCode(registerBill.getBillType()).isPresent()) {
			throw new TraceBusinessException("单据类型错误");
		}
		if (!TruckTypeEnum.fromCode(registerBill.getTruckType()).isPresent()) {
			throw new TraceBusinessException("装车类型错误");
		}
		if (TruckTypeEnum.POOL.equalsToCode(registerBill.getTruckType())) {
			if (StringUtils.isBlank(registerBill.getPlate())) {
				throw new TraceBusinessException("车牌不能为空");
			}
		}
		if (!PreserveTypeEnum.fromCode(registerBill.getPreserveType()).isPresent()) {
			throw new TraceBusinessException("商品类型错误");
		}
		if (StringUtils.isBlank(registerBill.getName())) {
			logger.error("业户姓名不能为空");
			throw new TraceBusinessException("业户姓名不能为空");
		}
		if (registerBill.getUserId() == null) {
			logger.error("业户ID不能为空");
			throw new TraceBusinessException("业户ID不能为空");
		}

		if (StringUtils.isBlank(registerBill.getProductName()) || registerBill.getProductId() == null) {
			logger.error("商品名称不能为空");
			throw new TraceBusinessException("商品名称不能为空");
		}
		if (StringUtils.isBlank(registerBill.getOriginName()) || registerBill.getOriginId() == null) {
			logger.error("商品产地不能为空");
			throw new TraceBusinessException("商品产地不能为空");
		}

		if (registerBill.getWeight() == null) {
			logger.error("商品重量不能为空");
			throw new TraceBusinessException("商品重量不能为空");
		}

		if (BigDecimal.ZERO.compareTo(registerBill.getWeight()) >= 0) {
			logger.error("商品重量不能小于0");
			throw new TraceBusinessException("商品重量不能小于0");
		}
		if (registerBill.getWeightUnit() == null) {
			logger.error("重量单位不能为空");
			throw new TraceBusinessException("重量单位不能为空");
		}
		return BaseOutput.success();
	}

	@Override
	public List<RegisterBill> findByProductName(String productName) {
		RegisterBill registerBill = new RegisterBill();
		registerBill.setProductName(productName);
		return list(registerBill);
	}

	@Override
	public RegisterBill findByCode(String code) {
		RegisterBill registerBill = new RegisterBill();
		registerBill.setCode(code);
		List<RegisterBill> list = list(registerBill);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public RegisterBill findBySampleCode(String sampleCode) {
		if (StringUtils.isBlank(sampleCode)) {
			return null;
		}
		RegisterBill registerBill = new RegisterBill();
		registerBill.setSampleCode(sampleCode.trim());
		List<RegisterBill> list = list(registerBill);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int auditRegisterBill(Long id, Boolean pass) {
		RegisterBill registerBill = get(id);
		return auditRegisterBill(pass, registerBill);
	}

	private int auditRegisterBill(Boolean pass, RegisterBill registerBill) {
		if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
			if (pass) {

			} else {
				registerBill.setState(-1);
			}
			return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	private String getNextSampleCode() {
		String sampleCode = this.codeGenerateService.nextSampleCode();
		// String
		// sampleCode=this.bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL_SAMPLE_CODE);
		return sampleCode;
	}

	@Override
	public int undoRegisterBill(Long billId) {
		RegisterBill registerBill = get(billId);
		if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()
				|| registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()) {
			UserTicket userTicket = getOptUser();
			logger.info(userTicket.getDepName() + ":" + userTicket.getRealName() + "删除登记单"
					+ JSON.toJSON(registerBill).toString());
			this.delete(billId);
			return this.separateSalesRecordService.deleteSeparateSalesRecordByBillId(billId);
			// return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	@Override
	public int autoCheckRegisterBill(Long id) {
		RegisterBill registerBill = get(id);
		return autoCheckRegisterBill(registerBill);
	}

	private int autoCheckRegisterBill(RegisterBill registerBill) {
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
	public BaseOutput doBatchAutoCheck(List<Long> idList) {
		BatchResultDto<String> dto = new BatchResultDto<>();
		for (Long id : idList) {
			RegisterBill registerBill = get(id);
			if (registerBill == null) {
				continue;
			}
			try {
				this.autoCheckRegisterBill(registerBill);
				dto.getSuccessList().add(registerBill.getCode());
			} catch (Exception e) {
				dto.getFailureList().add(registerBill.getCode());
			}
		}

		return BaseOutput.success().setData(dto);

	}

	@Override
	public BaseOutput doBatchSamplingCheck(List<Long> idList) {
		BatchResultDto<String> dto = new BatchResultDto<>();
		for (Long id : idList) {
			RegisterBill registerBill = get(id);
			if (registerBill == null) {
				continue;
			}
			try {
				this.samplingCheckRegisterBill(registerBill);
				dto.getSuccessList().add(registerBill.getCode());
			} catch (Exception e) {
				dto.getFailureList().add(registerBill.getCode());
			}
		}
		return BaseOutput.success().setData(dto);

	}

	@Transactional
	@Override
	public BaseOutput doBatchAudit(BatchAuditDto batchAuditDto) {
		BatchResultDto<String> dto = new BatchResultDto<>();

		// id转换为RegisterBill,并通过条件判断partition(true:只有产地证明，且需要进行批量处理,false:其他)
		Map<Boolean, List<RegisterBill>> partitionedMap = CollectionUtils
				.emptyIfNull(batchAuditDto.getRegisterBillIdList()).stream().filter(Objects::nonNull).map(id -> {
					RegisterBill registerBill = get(id);
					return registerBill;
				}).filter(Objects::nonNull).filter(registerBill -> {
					if (Boolean.FALSE.equals(batchAuditDto.getPassWithOriginCertifiyUrl())) {
						// if (StringUtils.isNotBlank(registerBill.getOriginCertifiyUrl())
						// && StringUtils.isBlank(registerBill.getDetectReportUrl())) {
						return false;
						// }
					}
					return true;
				}).collect(Collectors.partitioningBy((registerBill) -> {

					if (Boolean.TRUE.equals(batchAuditDto.getPassWithOriginCertifiyUrl())) {
						// if (StringUtils.isNotBlank(registerBill.getOriginCertifiyUrl())
						// && StringUtils.isBlank(registerBill.getDetectReportUrl())) {
						return true;
						// }
					}
					return false;
				}));

		// 只有产地证明，且需要进行批量处理
		CollectionUtils.emptyIfNull(partitionedMap.get(Boolean.TRUE)).forEach(registerBill -> {
			if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
				UserTicket userTicket = getOptUser();
				registerBill.setOperatorName(userTicket.getRealName());
				registerBill.setOperatorId(userTicket.getId());
				registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
				registerBill.setDetectState(null);
				this.updateSelective(registerBill);
				dto.getSuccessList().add(registerBill.getCode());
			} else {
				dto.getFailureList().add(registerBill.getCode());
			}

		});
		// 其他登记单
		CollectionUtils.emptyIfNull(partitionedMap.get(Boolean.FALSE)).forEach(registerBill -> {
			try {
				this.auditRegisterBill(batchAuditDto.getPass(), registerBill);
				dto.getSuccessList().add(registerBill.getCode());
			} catch (Exception e) {
				dto.getFailureList().add(registerBill.getCode());
			}

		});

		return BaseOutput.success().setData(dto);
	}

	@Override
	public int samplingCheckRegisterBill(Long id) {
		RegisterBill registerBill = get(id);
		return samplingCheckRegisterBill(registerBill);
	}

	private int samplingCheckRegisterBill(RegisterBill registerBill) {
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
		if (registerBill.getState().intValue() != RegisterBillStateEnum.ALREADY_CHECK.getCode().intValue()) {
			throw new AppException("操作失败，数据状态已改变");
		}

		boolean updateState = false;
		// 第一次复检
		if (registerBill.getDetectState().intValue() == BillDetectStateEnum.NO_PASS.getCode().intValue()) {
			updateState = true;
		} else if (registerBill.getDetectState().intValue() == BillDetectStateEnum.REVIEW_NO_PASS.getCode().intValue()
		// && StringUtils.isBlank(registerBill.getHandleResult())
		) {
			// 多次复检
			updateState = true;
		}
		if (updateState) {
			UserTicket userTicket = getOptUser();
			registerBill.setOperatorName(userTicket.getRealName());
			registerBill.setOperatorId(userTicket.getId());
			registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
			registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
			// registerBill.setExeMachineNo(null);
			return update(registerBill);
		} else {
			throw new AppException("操作失败，数据状态已改变");
		}
	}

	UserTicket getOptUser() {
		return SessionContext.getSessionContext().getUserTicket();
	}

	@Override
	public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill) {
		logger.info("获取登记单信息信息" + JSON.toJSONString(registerBill));

		if (registerBill == null) {
			return null;
		}
		RegisterBillOutputDto outputDto = RegisterBillOutputDto.build(registerBill, new ArrayList<TradeDetail>());
		return outputDto;
	}

	@Override
	public Long saveHandleResult(RegisterBill input) {
		if (input == null || input.getId() == null) {
			// || StringUtils.isAnyBlank(input.getHandleResult(),
			// input.getHandleResultUrl())) {
			throw new AppException("参数错误");
		}
		// if (input.getHandleResult().trim().length() > 1000) {
		// throw new AppException("处理结果不能超过1000");
		// }
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new AppException("数据错误");
		}

		// RegisterBill example = new RegisterBill();
		// example.setId(item.getId());
		// example.setHandleResult(input.getHandleResult());
		// example.setHandleResultUrl(input.getHandleResultUrl());
		// this.updateSelective(example);

		return item.getId();

	}

	// @Override
	// public Long doModifyRegisterBill(RegisterBill input) {
	// if (input == null || input.getId() == null) {
	// throw new AppException("参数错误");
	// }
	// if (StringUtils.isBlank(input.getOriginCertifiyUrl()) &&
	// StringUtils.isBlank(input.getDetectReportUrl())) {
	// throw new AppException("请上传报告");
	// }
	// RegisterBill item = this.get(input.getId());
	// if (item == null) {
	// throw new AppException("数据错误");
	// }
	//
	// RegisterBill example = new RegisterBill();
	// example.setId(item.getId());
	// example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
	// example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
	// this.updateSelective(example);
	//
	// return example.getId();
	// }

	@Override
	public Long doAuditWithoutDetect(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new AppException("参数错误");
		}
		RegisterBill registerBill = this.get(input.getId());
		if (registerBill == null) {
			throw new AppException("数据错误");
		}
		// if (StringUtils.isBlank(registerBill.getOriginCertifiyUrl())) {
		// throw new AppException("请上传产地证明");
		// }
		if (registerBill.getState().intValue() != RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
			throw new AppException("数据状态错误");
		}
		registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
		// registerBill.setDetectState(null);
		this.updateSelective(registerBill);

		return registerBill.getId();
	}

	@Override
	public Long doEdit(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new AppException("参数错误");
		}
		RegisterBill billItem = this.get(input.getId());
		if (billItem == null) {
			throw new AppException("数据错误");
		}
		if (BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())
				|| BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
		} else {
			throw new AppException("当前状态不能修改数据");
		}
		this.updateSelective(input);
		this.brandService.createOrUpdateBrand(input.getBrandName(), billItem.getUserId());
		return input.getId();
	}

	@Override
	public Long doUploadDetectReport(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new AppException("参数错误");
		}
		// if (StringUtils.isBlank(input.getOriginCertifiyUrl()) &&
		// StringUtils.isBlank(input.getDetectReportUrl())) {
		// throw new AppException("请上传报告");
		// }
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new AppException("数据错误");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
			throw new AppException("状态错误,不能上传检测报告");
		}

		RegisterBill example = new RegisterBill();
		example.setId(item.getId());
		// example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
		// example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
		this.updateSelective(example);

		return example.getId();
	}

	@Override
	public Long doUploadOrigincertifiy(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new AppException("参数错误");
		}
		// if (StringUtils.isBlank(input.getOriginCertifiyUrl()) &&
		// StringUtils.isBlank(input.getDetectReportUrl())) {
		// throw new AppException("请上传报告");
		// }
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new AppException("数据错误");
		}
		// if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
		// throw new AppException("状态错误,不能上传产地证明");
		// }
		RegisterBill example = new RegisterBill();
		example.setId(item.getId());
		// example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
		// example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
		this.updateSelective(example);

		return example.getId();
	}

	@Override
	public BaseOutput doRemoveReportAndCertifiy(Long id, String deleteType) {
		RegisterBill item = this.get(id);
		if (item == null) {
			throw new AppException("数据错误");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
			throw new AppException("状态错误,不能删除产地证明和检测报告");
		}
		// if ("all".equalsIgnoreCase(deleteType)) {
		// item.setOriginCertifiyUrl(null);
		// item.setDetectReportUrl(null);
		// } else if ("originCertifiy".equalsIgnoreCase(deleteType)) {
		// item.setOriginCertifiyUrl(null);
		// } else if ("detectReport".equalsIgnoreCase(deleteType)) {
		// item.setDetectReportUrl(null);
		// } else {
		// // do nothing
		// return BaseOutput.success();
		// }
		// this.getActualDao().doRemoveReportAndCertifiy(item);
		this.userQrItemService.updateUserQrStatus(item.getUserId());
		return BaseOutput.success();
	}

	private RegisterBillDto preBuildDTO(RegisterBillDto dto) {
		if (StringUtils.isNotBlank(dto.getAttrValue())) {
			switch (dto.getAttr()) {
				case "code":
					dto.setCode(dto.getAttrValue());
					break;
				// case "plate":
				// registerBill.setPlate(registerBill.getAttrValue());
				// break;
				// case "tallyAreaNo":
				//// registerBill.setTallyAreaNo(registerBill.getAttrValue());
				// registerBill.setLikeTallyAreaNo(registerBill.getAttrValue());
				// break;
				case "latestDetectOperator":
					dto.setLatestDetectOperator(dto.getAttrValue());
					break;
				case "name":
					dto.setName(dto.getAttrValue());
					break;
				case "productName":
					dto.setProductName(dto.getAttrValue());
					break;
				case "likeSampleCode":
					dto.setLikeSampleCode(dto.getAttrValue());
					break;
			}
		}
		// if (registerBill.getHasReport() != null) {
		// if (registerBill.getHasReport()) {
		// registerBill.mset(IDTO.AND_CONDITION_EXPR,
		// " (detect_report_url is not null AND detect_report_url<>'')");
		// } else {
		// registerBill.mset(IDTO.AND_CONDITION_EXPR, " (detect_report_url is null or
		// detect_report_url='')");
		// }
		// }

		StringBuilder sql = this.buildDynamicCondition(dto);
		if (sql.length() > 0) {
			dto.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
		}

		return dto;
	}

	@Override
	public RegisterBill findFirstWaitAuditRegisterBillCreateByCurrentUser(RegisterBillDto input) throws Exception {
		RegisterBillDto dto = new RegisterBillDto();
		UserTicket userTicket = getOptUser();
		dto.setOperatorId(userTicket.getId());
		dto.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
		dto.setRows(1);
		dto.setSort("code");
		dto.setOrder("desc");
		return this.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
	}

	@Override
	public String listPage(RegisterBillDto input) throws Exception {
		RegisterBillDto dto = this.preBuildDTO(input);

		return this.listEasyuiPageByExample(dto, true).toString();
	}

	private StringBuilder buildDynamicCondition(RegisterBillDto registerBill) {
		StringBuilder sql = new StringBuilder();

		return sql;
	}

	@Override
	public int batchInsert(List<RegisterBill> list) {
		int v = super.batchInsert(list);
		return v;
	}

	@Override
	public int batchUpdate(List<RegisterBill> list) {
		int v = super.batchUpdate(list);
		return v;
	}

	@Override
	public int batchUpdateSelective(List<RegisterBill> list) {
		int v = super.batchUpdateSelective(list);
		return v;
	}

	// @Override
	// public int delete(Long key) {
	// int v= super.delete(key);
	// return v;
	// }

	// @Override
	// public int delete(List<Long> ids) {
	// int v= super.delete(ids);
	// return v;
	// }

	// @Override
	// public int deleteByExample(RegisterBill t) {
	// int v= super.deleteByExample(t);
	// return v;
	// }

	@Override
	public int insert(RegisterBill t) {
		int v = super.insert(t);
		// this.updateUserQrItemDetail(t.getId());
		return v;
	}

	@Override
	public int insertExact(RegisterBill t) {
		int v = super.insertExact(t);
		// this.updateUserQrItemDetail(t.getId());
		return v;
	}

	@Override
	public int insertExactSimple(RegisterBill t) {
		int v = super.insertExactSimple(t);
		// this.updateUserQrItemDetail(t.getId());
		return v;
	}

	@Override
	public int insertSelective(RegisterBill t) {
		int v = super.insertSelective(t);
		// this.updateUserQrItemDetail(t.getId());
		return v;
	}

	@Override
	public int update(RegisterBill condtion) {
		int v = super.update(condtion);
		// this.updateUserQrItemDetail(condtion.getId());
		return v;
	}

	@Override
	public int updateByExample(RegisterBill domain, RegisterBill condition) {
		int v = super.updateByExample(domain, condition);
		// this.updateUserQrItemDetailByCondition(condition);
		return v;
	}

	@Override
	public int updateExact(RegisterBill record) {
		int v = super.updateExact(record);
		// this.updateUserQrItemDetail(record.getId());
		return v;
	}

	@Override
	public int updateExactByExample(RegisterBill domain, RegisterBill condition) {
		int v = super.updateExactByExample(domain, condition);
		// this.updateUserQrItemDetailByCondition(condition);
		return v;
	}

	@Override
	public int updateExactByExampleSimple(RegisterBill domain, RegisterBill condition) {
		int v = super.updateExactByExampleSimple(domain, condition);
		// this.updateUserQrItemDetailByCondition(condition);
		return v;
	}

	@Override
	public int updateExactSimple(RegisterBill record) {
		int v = super.updateExactSimple(record);
		// this.updateUserQrItemDetail(record.getId());
		return v;
	}

	@Override
	public int updateSelective(RegisterBill condtion) {
		int v = super.updateSelective(condtion);
		// this.updateUserQrItemDetail(condtion.getId());
		return v;
	}

	@Override
	public int updateSelectiveByExample(RegisterBill domain, RegisterBill condition) {
		int v = super.updateSelectiveByExample(domain, condition);
		// this.updateUserQrItemDetailByCondition(condition);
		return v;
	}

	/**
	 * 基于ID更新二维码状态
	 */
	private void updateUserQrItemDetail(Long registerBillId) {
		if (registerBillId != null) {
			RegisterBill bill = this.get(registerBillId);
			if (bill != null && bill.getUserId() != null) {
				// this.userQrItemService.updateUserQrStatus(bill.getUserId());
			}
		}

	}

	/**
	 * 基于条件更新二维码状态
	 */
	private void updateUserQrItemDetailByCondition(RegisterBill condition) {
		this.listByExample(condition).stream().forEach(bill -> {
			if (bill != null && bill.getUserId() != null) {
				// this.userQrItemService.updateUserQrStatus(bill.getUserId());
			}
		});

	}

	@Override
	public Long doVerifyBeforeCheckIn(RegisterBill input, OperatorUser operatorUser) {
		if (input == null || input.getId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		Long billId = input.getId();
		BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(input.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("参数错误"));

		RegisterBill item = this.get(billId);
		if (item == null) {
			throw new TraceBusinessException("数据不存在");
		}
		BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCode(item.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("数据错误"));

		logger.info("from {} to {}", fromVerifyState, toVerifyState);
		if (fromVerifyState == toVerifyState) {
			throw new TraceBusinessException("状态不能相同");
		}
		if (!BillVerifyStatusEnum.canDoVerify(item.getVerifyStatus())) {
			throw new TraceBusinessException("当前状态不能进行数据操作");
		}
		this.createHistoryRegisterBillForVerify(item, toVerifyState, input.getReason(),
				VerifyTypeEnum.VERIFY_BEFORE_CHECKIN, operatorUser);
		this.tradeDetailService.doUpdateTradeDetailSaleStatus(operatorUser, billId);
		return billId;
	}

	@Override
	public Long doVerifyAfterCheckIn(RegisterBill input, OperatorUser operatorUser) {
		if (input == null || input.getId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		Long billId = input.getId();
		BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(input.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("参数错误"));

		RegisterBill item = this.get(billId);
		if (item == null) {
			throw new TraceBusinessException("数据不存在");
		}
		if (toVerifyState == BillVerifyStatusEnum.fromCode(item.getVerifyStatus()).orElse(null)) {
			throw new TraceBusinessException("状态不能相同");
		}
		if (!BillVerifyStatusEnum.canDoVerify(item.getVerifyStatus())) {
			throw new TraceBusinessException("当前状态不能进行数据操作");
		}
		this.createHistoryRegisterBillForVerify(item, toVerifyState, input.getReason(),
				VerifyTypeEnum.VERIFY_AFTER_CHECKIN, operatorUser);
		this.tradeDetailService.doUpdateTradeDetailSaleStatus(operatorUser, billId);

		return billId;

	}

	private Long createHistoryRegisterBillForVerify(RegisterBill item, BillVerifyStatusEnum toVerifyState,
			String returnedReason, VerifyTypeEnum verifyType, OperatorUser operatorUser) {
		RegisterBill historyBill = new RegisterBill();
		try {
			BeanUtils.copyProperties(historyBill, item);
			historyBill.setId(null);
			historyBill.setCode("h_" + historyBill.getCode());
			historyBill.setSampleCode(null);
			historyBill.setYn(YnEnum.NO.getCode());
			this.insertSelective(historyBill);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new TraceBusinessException("创建查询数据出错");
		}
		RegisterBill bill = new RegisterBill();
		bill.setId(item.getId());
		bill.setVerifyStatus(toVerifyState.getCode());
		bill.setVerifiedHistoryBillId(historyBill.getId());
		bill.setVerifyType(verifyType.getCode());
		bill.setOperatorId(operatorUser.getId());
		bill.setOperatorName(operatorUser.getName());
		bill.setYn(YnEnum.YES.getCode());
		bill.setReason(StringUtils.trimToNull(returnedReason));
		this.updateSelective(bill);
		return bill.getId();
	}

	@Override
	public List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBill query) {
		if(query==null||query.getVerifyType()==null){
			throw new TraceBusinessException("参数错误");
		}
		List<VerifyStatusCountOutputDto> dtoList = this.getActualDao().countByVerifyStatus(query);
		Map<Integer,Integer>verifyStatusNumMap=StreamEx.of(dtoList).toMap(VerifyStatusCountOutputDto::getVerifyStatus, VerifyStatusCountOutputDto::getNum);
		return StreamEx.of(BillVerifyStatusEnum.values()).flatMap(verifystatus -> {
			return StreamEx.ofNullable(VerifyTypeEnum.fromCode(query.getVerifyType())).flatMapToEntry(verifyType -> {
				return EntryStream.of(verifyType, verifystatus).toMap();
			});
		}).map(e -> {
			VerifyStatusCountOutputDto dto = VerifyStatusCountOutputDto.buildDefault(e.getKey(), e.getValue());
			if(verifyStatusNumMap.containsKey(dto.getVerifyStatus())){
				dto.setNum(verifyStatusNumMap.get(dto.getVerifyStatus()));
			}
			return dto;
		}).toList();
	}

}