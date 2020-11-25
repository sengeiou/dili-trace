package com.dili.trace.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.service.QrCodeService;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.dto.IDTO;
import com.dili.trace.dto.ECommerceBillPrintOutput;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.sg.trace.glossary.BillDetectStateEnum;
import com.dili.sg.trace.glossary.BillTypeEnum;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.sg.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;

import one.util.streamex.StreamEx;

@Service
public class ECommerceBillService {
	private static final Logger logger = LoggerFactory.getLogger(ECommerceBillService.class);

	@Autowired
	CodeGenerateService codeGenerateService;

	@Autowired
	BillService billService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	QrCodeService qrCodeService;

	@Value("${current.baseWebPath}")
	private String baseWebPath;

	public String listPage(RegisterBillDto input) throws Exception {
		RegisterBillDto dto = this.preBuildDTO(input);
		dto.setBillType(this.supportedBillType().getCode());
		return this.billService.listEasyuiPageByExample(dto, true).toString();
	}

	private RegisterBillDto preBuildDTO(RegisterBillDto dto) {
		String attr = StringUtils.trimToEmpty(dto.getAttr());
		String attrValue = dto.getAttrValue();
		if (attrValue != null && (StringUtils.isNotBlank(attrValue))) {
			switch (attr) {
			case "code":
				dto.setCode(attrValue);
				break;
			case "latestDetectOperator":
				dto.setLatestDetectOperator(attrValue);
				break;
			case "name":
				dto.setName(attrValue);
				break;
			case "likeSampleCode":
				dto.setLikeSampleCode(attrValue);
				break;
			}
		}

		StringBuilder sql = new StringBuilder();
		Boolean hasCheckSheet = dto.getHasCheckSheet();
		if (hasCheckSheet != null) {
			if (sql.length() > 0) {
				sql.append(" AND ");
			}
			if (hasCheckSheet) {
				sql.append("  (check_sheet_id is not null) ");
			} else {
				sql.append("  (check_sheet_id is null) ");
			}
		}
		if (sql.length() > 0) {
			dto.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
		}

		return dto;
	}

	public BillTypeEnum supportedBillType() {
		return BillTypeEnum.E_COMMERCE_BILL;
	}

	public RegisterBill findHighLightEcommerceBill(RegisterBillDto input, OperatorUser operatorUser) throws Exception {
		RegisterBillDto dto = new RegisterBillDto();
		dto.setOperatorId(operatorUser.getId());
		dto.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
		dto.setBillType(this.supportedBillType().getCode());
		dto.setRows(1);
		dto.setSort("code");
		dto.setOrder("desc");
		return this.billService.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
	}

	@Transactional
	public Long doAuditEcommerceBill(RegisterBill inputBill, OperatorUser operatorUser) {

		RegisterBill item = this.billService.get(inputBill.getId());
		if (item == null) {
			throw new TraceBizException("没有找到数据，可能已经被删除");
		}
		if (!this.supportedBillType().equalsToCode(item.getBillType())) {
			throw new TraceBizException("数据错误,登记单类型错误");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.equalsToCode(item.getState())) {
			throw new TraceBizException("登记单状态错误");
		}

		RegisterBill updatable = new RegisterBill();
		updatable.setModified(new Date());
		updatable.setId(item.getId());
		updatable.setOperatorId(operatorUser.getId());
		updatable.setOperatorName(operatorUser.getName());

		if (RegisterBillStateEnum.ALREADY_CHECK.equalsToCode(inputBill.getState())
				&& BillDetectStateEnum.PASS.equalsToCode(inputBill.getDetectState())) {
			if (StringUtils.isNotBlank(item.getDetectReportUrl())
					|| StringUtils.isNotBlank(item.getOriginCertifiyUrl())) {
				updatable.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
				updatable.setDetectState(BillDetectStateEnum.PASS.getCode());
				updatable.setLatestDetectOperator(operatorUser.getName());
				updatable.setLatestDetectTime(new Date());
				updatable.setLatestPdResult("100%");
			} else {
				throw new TraceBizException("参数错误");
			}

		} else if (RegisterBillStateEnum.WAIT_CHECK.equalsToCode(inputBill.getState())
				&& inputBill.getDetectState() == null) {
			updatable.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
		} else {
			throw new TraceBizException("参数错误");
		}
		updatable.setSampleCode(this.codeGenerateService.nextECommerceBillSampleCode());

		this.billService.updateSelective(updatable);

		return updatable.getId();
	}
	
	/**
	 * 删除电商登记单
	 * @param inputBill
	 * @param operatorUser
	 * @return
	 */
	@Transactional
	public Long doDeleteEcommerceBill(RegisterBill inputBill, OperatorUser operatorUser) {
		if (inputBill == null || inputBill.getId() == null) {

			throw new TraceBizException("参数错误");
		}
		RegisterBill item = this.billService.get(inputBill.getId());
		if (item == null) {
			throw new TraceBizException("没有找到数据，可能已经被删除");
		}
		if (!this.supportedBillType().equalsToCode(item.getBillType())) {
			throw new TraceBizException("数据错误,登记单类型错误");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.equalsToCode(item.getState())) {
			throw new TraceBizException("登记单状态错误");
		}

		this.billService.delete(item.getId());

		return item.getId();
	}
	/**
	 * 查询详情
	 * @param billId
	 * @return
	 */
	public RegisterBillOutputDto detailEcommerceBill(Long billId) {
		
		RegisterBill query = new RegisterBill();
		query.setId(billId);
		query.setBillType(this.supportedBillType().getCode());
		RegisterBillOutputDto data = this.billService.listByExample(query).stream().findFirst().map(bill -> {
			RegisterBillOutputDto output = new RegisterBillOutputDto();
			try {
				BeanUtils.copyProperties(output, bill);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error(e.getMessage(), e);
				throw new TraceBizException("服务器出错，请重试");
			}

			return output;

		}).orElseThrow(() -> {
			return new TraceBizException("数据不存在");
		});
		List<SeparateSalesRecord> separateSalesRecordList = this.separateSalesRecordService
				.findByRegisterBillCode(data.getCode());
		data.setSeparateSalesRecords(separateSalesRecordList);
		return data;
	}

	/**
	 * 创建登记单
	 *
	 * @param registerBill
	 * @return
	 */
	@Transactional
	public Long createECommerceBill(RegisterBill registerBill, List<SeparateSalesRecord> separateInputList,
			OperatorUser operatorUser) {

		if (registerBill == null) {
			throw new TraceBizException("参数错误");
		}
		Long billId = this.createBill(registerBill, operatorUser);
		BigDecimal totalSalesWeight = StreamEx.of(CollectionUtils.emptyIfNull(separateInputList)).nonNull().filter(r->{
			return !StringUtils.isAllBlank(r.getTallyAreaNo(),r.getSalesUserName(),r.getSalesPlate());
		}).map(r -> {
			if (r.getSalesWeight() == null || r.getSalesWeight().compareTo(BigDecimal.ZERO) < 0) {
				throw new TraceBizException("分销重量不能小于零");
			}
			return r;
		}).map(SeparateSalesRecord::getSalesWeight).reduce(BigDecimal.ZERO,BigDecimal::add);
		if (totalSalesWeight.compareTo(registerBill.getWeight()) >0) {
			throw new TraceBizException("分销重量不能超过总重量");
		}
		this.createSeparateSalesRecord(registerBill.getCode(), separateInputList);
		return billId;

	}

	/**
	 * 查询并返回登记单可打印不干胶信息
	 * 
	 * @param billId
	 * @return
	 */
	public ECommerceBillPrintOutput prePrint(Long billId) {

		if (billId == null) {
			throw new TraceBizException("参数错误");
		}
		RegisterBill bill = this.billService.get(billId);
		if (bill == null) {
			throw new TraceBizException("数据不存在");
		}
		if (!this.supportedBillType().getCode().equals(bill.getBillType())) {
			throw new TraceBizException("登记单类型错误");
		}
		String content = this.baseWebPath + "/ecommerceBill/billQRDetail.html?id=" + billId;

		try {
			InputStream logoFileInputStream = new ClassPathResource("static/resources/qrlogo/logo.png")
					.getInputStream();
			String base64Qrcode = this.qrCodeService.getBase64QrCode(content, logoFileInputStream, 174, 174);
			ECommerceBillPrintOutput out = ECommerceBillPrintOutput.build(bill, base64Qrcode);

			return out;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new TraceBizException("生成二维码出错");
		}

	}

	/**
	 * 创建分销单
	 * 
	 * @param registerBillCode
	 * @param separateDtoList
	 * @return
	 */
	private List<SeparateSalesRecord> createSeparateSalesRecord(String registerBillCode,
			List<SeparateSalesRecord> separateDtoList) {
		return StreamEx.of(CollectionUtils.emptyIfNull(separateDtoList)).map(separateSalesRecord -> {
			separateSalesRecord.setId(null);
			separateSalesRecord.setRegisterBillCode(registerBillCode);
			separateSalesRecord.setCreated(new Date());
			separateSalesRecord.setModified(new Date());
			separateSalesRecord.setSalesCityId(0L);
			separateSalesRecord.setSalesCityName("");
//			separateSalesRecord.setSalesPlate("");
//			separateSalesRecord.setSalesUserId(0L);
//			separateSalesRecord.setSalesUserName("");
//			separateSalesRecord.setSalesWeight(0);
//			separateSalesRecord.setTradeNo("");
			this.separateSalesRecordService.insertSelective(separateSalesRecord);
			return separateSalesRecord;

		}).toList();

	}

	// 创建电商登记单
	private Long createBill(RegisterBill bill, OperatorUser operatorUser) {
		if (bill.getWeight() == null || bill.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
			throw new TraceBizException("总重量不能小于零");
		}

		bill.setRegisterSource(RegisterSourceEnum.OTHERS.getCode());
		if (!RegisterBilCreationSourceEnum.fromCode(bill.getCreationSource()).isPresent()) {
			throw new TraceBizException("登记单来源类型错误");
		}
		bill.setCode(this.codeGenerateService.nextECommerceBillCode());

		bill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
		bill.setCreated(new Date());
		bill.setModified(new Date());
		bill.setBillType(this.supportedBillType().getCode());

		bill.setOperatorId(operatorUser.getId());
		bill.setOperatorName(operatorUser.getName());
		bill.setPlate(StringUtils.trimToEmpty(bill.getPlate()));
		this.billService.insertSelective(bill);
		return bill.getId();
	}

}
