package com.dili.trace.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.BizNumberFunction;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.enums.VerifyTypeEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.BrandService;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillHistoryService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
@Transactional
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
	TradeDetailService tradeDetailService;
	@Autowired
	BrandService brandService;
	@Autowired
	RegisterBillHistoryService registerBillHistoryService;
	@Autowired
	UserService userService;
	@Autowired
	TradeService tradeService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	CheckinOutRecordService checkinOutRecordService;

	public RegisterBillMapper getActualDao() {
		return (RegisterBillMapper) getDao();
	}

	@Transactional
	@Override
	public Optional<RegisterBill> selectByIdForUpdate(Long id) {
		return this.getActualDao().selectByIdForUpdate(id);
	}

	@Override
	public List<Long> createBillList(List<CreateRegisterBillInputDto> registerBills, User user,
	Optional<OperatorUser> operatorUser) {
		if (!ValidateStateEnum.PASSED.equalsToCode(user.getValidateState())) {
			throw new TraceBusinessException("用户未审核通过不能创建报备单");
		}

		return StreamEx.of(registerBills).nonNull().map(dto -> {
			logger.info("循环保存登记单:" + JSON.toJSONString(dto));
			RegisterBill registerBill = dto.build(user);
			return this.createRegisterBill(registerBill, dto.getImageCertList(), operatorUser);
		}).toList();

	}

	@Transactional
	@Override
	public Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList,
			Optional<OperatorUser> operatorUser) {
		this.checkBill(registerBill);

		registerBill.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		registerBill.setVerifyType(VerifyTypeEnum.NONE.getCode());
		registerBill.setState(RegisterBillStateEnum.NEW.getCode());
		registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
		registerBill.setVersion(1);
		registerBill.setCreated(new Date());
		registerBill.setIsCheckin(YnEnum.NO.getCode());
		operatorUser.ifPresent(op->{
			registerBill.setOperatorName(op.getName());
			registerBill.setOperatorId(op.getId());
		});
		registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
		// 车牌转大写
		String plate = StreamEx.ofNullable(registerBill.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
				.map(String::toUpperCase).findFirst().orElse(null);
		registerBill.setPlate(plate);
		registerBill.setModified(new Date());
		// 保存车牌
		this.userPlateService.checkAndInsertUserPlate(registerBill.getUserId(), plate);

		// 保存报备单
		int result = super.saveOrUpdate(registerBill);
		if (result == 0) {
			logger.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
			throw new TraceBusinessException("创建失败");
		}
		// 创建审核历史数据
		this.registerBillHistoryService.createHistory(registerBill.getBillId());
		// 保存图片
		if (imageCertList != null) {
			this.imageCertService.insertImageCert(imageCertList, registerBill.getBillId());
		}

		// 创建/更新品牌信息并更新brandId字段值
		this.brandService.createOrUpdateBrand(registerBill.getBrandName(), registerBill.getUserId())
				.ifPresent(brandId -> {
					RegisterBill bill = new RegisterBill();
					bill.setBrandId(brandId);
					bill.setId(registerBill.getId());
					this.updateSelective(bill);
				});
		this.updateUserQrStatusByUserId(registerBill.getUserId());
		return registerBill.getId();
	}

	/**
	 * 检查用户输入参数
	 * 
	 * @param registerBill
	 * @return
	 */
	private BaseOutput checkBill(RegisterBill registerBill) {

		if (!BillTypeEnum.fromCode(registerBill.getBillType()).isPresent()) {
			throw new TraceBusinessException("单据类型错误");
		}
		if (!TruckTypeEnum.fromCode(registerBill.getTruckType()).isPresent()) {
			throw new TraceBusinessException("装车类型错误");
		}
		if (registerBill.getUpStreamId() == null) {
			throw new TraceBusinessException("上游企业不能为空");
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
	public RegisterBill findByCode(String code) {
		RegisterBill registerBill = new RegisterBill();
		registerBill.setCode(code);
		List<RegisterBill> list = list(registerBill);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private UserTicket getOptUser() {
		return SessionContext.getSessionContext().getUserTicket();
	}

	@Transactional
	@Override
	public Long doEdit(RegisterBill input,List<ImageCert> imageCertList,Optional<OperatorUser> operatorUser) {
		if (input == null || input.getId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		RegisterBill billItem = this.get(input.getId());
		if (billItem == null) {
			throw new TraceBusinessException("数据错误");
		}
		if (BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())
				|| BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
			// 待审核，或者已退回状态可以进行数据修改
		} else {
			throw new TraceBusinessException("当前状态不能修改数据");
		}
		// 车牌转大写
		String plate = StreamEx.ofNullable(input.getPlate()).filter(StringUtils::isNotBlank).map(p -> p.toUpperCase())
				.findFirst().orElse(null);
		input.setPlate(plate);
		// 保存车牌
		this.userPlateService.checkAndInsertUserPlate(input.getUserId(), plate);
		input.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		input.setModified(new Date());

		input.setOperatorName(null);
		input.setOperatorId(null);
		input.setReason("");
		operatorUser.ifPresent(op->{
			input.setOperatorName(op.getName());
			input.setOperatorId(op.getId());
		});
		this.updateSelective(input);
		this.registerBillHistoryService.createHistory(billItem.getBillId());
		// 保存图片
		if (imageCertList != null) {
			this.imageCertService.insertImageCert(imageCertList, input.getId());
		}

		this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId()).ifPresent(td -> {
			TradeDetail updatableRecord = new TradeDetail();
			updatableRecord.setId(td.getId());
			updatableRecord.setModified(new Date());
			updatableRecord.setStockWeight(input.getWeight());
			updatableRecord.setTotalWeight(input.getWeight());
			this.tradeDetailService.updateSelective(updatableRecord);
		});

		this.brandService.createOrUpdateBrand(input.getBrandName(), billItem.getUserId());
		this.updateUserQrStatusByUserId(billItem.getUserId());
		return input.getId();
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

	@Transactional
	@Override
	public Long doVerifyBeforeCheckIn(RegisterBill input, Optional<OperatorUser> operatorUser) {
		if (input == null || input.getId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		RegisterBill billItem = Optional.ofNullable(this.get(input.getId())).orElseThrow(() -> {
			return new TraceBusinessException("数据不存在");
		});

		if(!BillTypeEnum.NONE.equalsToCode(billItem.getBillType())){
			throw new TraceBusinessException("当前报备单是补单,只能场内审核");
		}
		if (!BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())) {
			throw new TraceBusinessException("当前状态不能进行数据操作");
		}
		this.doVerify(billItem, input.getVerifyStatus(),input.getReason(), operatorUser);
		return billItem.getId();
	}

	@Transactional
	@Override
	public Long doVerifyAfterCheckIn(Long billId,Integer verifyStatus,String reason,Optional<OperatorUser> operatorUser) {
		if (billId == null || verifyStatus == null) {
			throw new TraceBusinessException("参数错误");
		}

		RegisterBill billItem = Optional.ofNullable(this.get(billId)).orElseThrow(() -> {
			return new TraceBusinessException("数据不存在");
		});
		if(!BillTypeEnum.SUPPLEMENT.equalsToCode(billItem.getBillType())){
			throw new TraceBusinessException("当前报备单不是补单,不能场内审核");
		}
		if (!BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())) {
			throw new TraceBusinessException("当前状态不能进行数据操作");
		}
		this.checkinOutRecordService.doCheckin(operatorUser, Lists.newArrayList(billItem.getBillId()));
		this.doVerify(billItem,verifyStatus,reason, operatorUser);
		return billItem.getId();

	}

	private void doVerify(RegisterBill billItem,Integer verifyStatus, String reason,Optional<OperatorUser> operatorUser) {
		BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("数据错误"));

		BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(verifyStatus)
				.orElseThrow(() -> new TraceBusinessException("参数错误"));

		logger.info("审核: billId: {} from {} to {}", billItem.getBillId(), fromVerifyState.getName(),
				toVerifyState.getName());
		if (BillVerifyStatusEnum.NONE == toVerifyState) {
			throw new TraceBusinessException("不支持的操作");
		}
		if (fromVerifyState == toVerifyState) {
			throw new TraceBusinessException("状态不能相同");
		}



		// 更新当前报务单数据
		RegisterBill bill = new RegisterBill();
		bill.setId(billItem.getId());
		bill.setVerifyStatus(toVerifyState.getCode());
		operatorUser.ifPresent(op->{
			bill.setOperatorId(op.getId());
			bill.setOperatorName(op.getName());
		});

		bill.setReason(StringUtils.trimToEmpty(reason));
		if (BillVerifyStatusEnum.PASSED == toVerifyState) {
			if (YnEnum.YES.equalsToCode(billItem.getIsCheckin())) {
				bill.setVerifyType(VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode());
			} else {
				bill.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
			}
		}
		bill.setModified(new Date());
		this.updateSelective(bill);
		// 创建审核历史数据
		this.registerBillHistoryService.createHistory(billItem.getId());

		// 创建相关的tradeDetail及batchStock数据
		this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId()).ifPresent(tradeDetailItem -> {
			this.tradeService.createBatchStockAfterVerifiedAndCheckin(billItem.getId(), tradeDetailItem.getId(),
					operatorUser);
		});

		// 更新用户颜色码
		this.updateUserQrStatusByUserId(billItem.getUserId());

	}

	/**
	 * 根据用户最新报备单审核状态更新颜色码
	 * 
	 * @param userId
	 */
	public void updateUserQrStatusByUserId(Long userId) {
		if (userId == null) {
			return;
		}
		RegisterBill query = new RegisterBill();
		query.setPage(1);
		query.setRows(1);
		query.setSort("created");
		query.setOrder("desc");
		query.setUserId(userId);
		RegisterBill billItem = this.listPageByExample(query).getDatas().stream().findFirst().orElse(null);
		if (billItem == null) {
			return;
		}
		BillVerifyStatusEnum verifyStatus = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus()).orElse(null);
		UserQrStatusEnum userQrStatus = UserQrStatusEnum.BLACK;
		switch (verifyStatus) {
			case PASSED:
				userQrStatus = UserQrStatusEnum.GREEN;
				break;
			case NO_PASSED:
				userQrStatus = UserQrStatusEnum.RED;
				break;
			case RETURNED:
				userQrStatus = UserQrStatusEnum.YELLOW;
				break;
			case NONE:
				userQrStatus = UserQrStatusEnum.YELLOW;
				break;
			default:
				throw new TraceBusinessException("错误");
		}
		User user = DTOUtils.newDTO(User.class);
		user.setId(userId);
		user.setQrStatus(userQrStatus.getCode());
		this.userService.updateSelective(user);
	}

	/**
	 * 根据报备单数量更新用户状态到黑码
	 * 
	 * @param dto
	 * @return
	 */
	public void updateAllUserQrStatusByRegisterBillNum(Date createdStart, Date createdEnd) {
		UserListDto dto = DTOUtils.newDTO(UserListDto.class);
		dto.setQrStatus(UserQrStatusEnum.BLACK.getCode());
		dto.setCreatedStart(createdStart);
		dto.setCreatedEnd(createdEnd);
		this.getActualDao().updateAllUserQrStatusByRegisterBillNum(dto);
	}

	@Override
	public BasePage<RegisterBill> listPageBeforeCheckinVerifyBill(RegisterBillDto query) {
		return this.listPageByExample(query);
	}

	@Override
	public BasePage<RegisterBill> listPageAfterCheckinVerifyBill(RegisterBillDto query) {
		query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLAfterCheckIn());
		return this.listPageByExample(query);
	}

	@Override
	public List<VerifyStatusCountOutputDto> countByVerifyStatuseBeforeCheckin(RegisterBillDto query) {
		if (query == null) {
			throw new TraceBusinessException("参数错误");
		}
		return this.countByVerifyStatus(query);
	}

	@Override
	public List<VerifyStatusCountOutputDto> countByVerifyStatuseAfterCheckin(RegisterBillDto query) {
		if (query == null) {
			throw new TraceBusinessException("参数错误");
		}
		// query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLAfterCheckIn());
		return this.countByVerifyStatus(query);
	}

	public RegisterBillOutputDto viewTradeDetailBill(Long billId, Long tradeDetailId) {
		if (billId == null && tradeDetailId == null) {
			throw new TraceBusinessException("参数错误");
		}

		TradeDetail tradeDetailItem = StreamEx.ofNullable(tradeDetailId).nonNull().map(tdId -> {
			return this.tradeDetailService.get(tdId);
		}).findFirst().orElse(new TradeDetail());

		RegisterBill registerBill = StreamEx.ofNullable(billId).append(tradeDetailItem.getBillId()).nonNull()
				.map(bId -> {
					return this.get(bId);
				}).findFirst().orElse(new RegisterBill());

		List<ImageCert> imageCertList = StreamEx.ofNullable(registerBill.getId()).nonNull().flatMap(bid -> {
			return this.imageCertService.findImageCertListByBillId(bid).stream();
		}).toList();

		String upStreamName = StreamEx.ofNullable(registerBill.getUpStreamId()).nonNull().map(upStreamId -> {
			return this.upStreamService.get(upStreamId);
		}).nonNull().findAny().map(UpStream::getName).orElse("");

		if (tradeDetailItem.getId() != null && registerBill.getId() != null) {
			RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
			outputdto.setImageCertList(imageCertList);
			outputdto.setUpStreamName(upStreamName);
			outputdto.setWeight(tradeDetailItem.getTotalWeight());
			return outputdto;
		} else if (registerBill.getId() != null) {
			RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
			outputdto.setUpStreamName(upStreamName);
			outputdto.setImageCertList(imageCertList);
			outputdto.setWeight(registerBill.getWeight());
			return outputdto;
		} else {
			throw new TraceBusinessException("没有数据");
		}

	}

	private String dynamicSQLAfterCheckIn() {
		return "(not ( bill_type=" + BillTypeEnum.SUPPLEMENT.getCode() + " or (verify_type="
				+ VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode() + " and verify_status ="
				+ BillVerifyStatusEnum.PASSED.getCode() + ") or is_checkin=" + YnEnum.NO.getCode() + "))";

	}

	private List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBillDto query) {
		List<VerifyStatusCountOutputDto> dtoList = this.getActualDao().countByVerifyStatus(query);
		Map<Integer, Integer> verifyStatusNumMap = StreamEx.of(dtoList)
				.toMap(VerifyStatusCountOutputDto::getVerifyStatus, VerifyStatusCountOutputDto::getNum);
		return StreamEx.of(BillVerifyStatusEnum.values()).map(e -> {
			VerifyStatusCountOutputDto dto = VerifyStatusCountOutputDto.buildDefault(e);
			if (verifyStatusNumMap.containsKey(dto.getVerifyStatus())) {
				dto.setNum(verifyStatusNumMap.get(dto.getVerifyStatus()));
			}
			return dto;
		}).toList();
	}

}