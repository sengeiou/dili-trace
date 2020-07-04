package com.dili.trace.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.enums.VerifyTypeEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.BrandService;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillHistoryService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;

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
		registerBill.setVerifyType(VerifyTypeEnum.VERIFY_BEFORE_CHECKIN.getCode());
		registerBill.setState(RegisterBillStateEnum.NEW.getCode());
		registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
		registerBill.setVersion(1);
		registerBill.setCreated(new Date());
		registerBill.setIsCheckin(YnEnum.NO.getCode());

		registerBill.setOperatorName(operatorUser.getName());
		registerBill.setOperatorId(operatorUser.getId());

		registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
		// 车牌转大写
		String plate = StreamEx.ofNullable(registerBill.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
				.map(String::toUpperCase).findFirst().orElse(null);
		registerBill.setPlate(plate);
		// 保存车牌
		this.userPlateService.checkAndInsertUserPlate(registerBill.getUserId(), plate);

		// 保存报备单
		int result = super.saveOrUpdate(registerBill);
		if (result == 0) {
			logger.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
			throw new TraceBusinessException("创建失败");
		}
		// 保存图片
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
		if(registerBill.getUpStreamId()==null){
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
	public Long doEdit(RegisterBill input) {
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
		input.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
		this.updateSelective(input);
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
	public Long doVerifyBeforeCheckIn(RegisterBill input, OperatorUser operatorUser) {
		if (input == null || input.getId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		Long billId = input.getId();
		BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(input.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("参数错误"));

		RegisterBill billItem = this.get(billId);
		if (billItem == null) {
			throw new TraceBusinessException("数据不存在");
		}
		if (!VerifyTypeEnum.VERIFY_BEFORE_CHECKIN.equalsToCode(billItem.getVerifyType())) {
			throw new TraceBusinessException("当前报备单只能场内审核");
		}

		TradeDetail query = new TradeDetail();
		query.setBillId(billId);
		query.setBuyerId(billItem.getUserId());
		query.setTradeType(TradeTypeEnum.NONE.getCode());
		TradeDetail tradeDetailItem = this.tradeDetailService.listByExample(query).stream().findFirst().orElse(null);
		if (tradeDetailItem != null) {
			throw new TraceBusinessException("当前报备单已进门,不能预审核");
		}

		BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("数据错误"));

		logger.info("预审核: billId: {} from {} to {}", billId, fromVerifyState, toVerifyState);
		if (fromVerifyState == toVerifyState) {
			throw new TraceBusinessException("状态不能相同");
		}
		if (!BillVerifyStatusEnum.canDoVerify(billItem.getVerifyStatus())) {
			throw new TraceBusinessException("当前状态不能进行数据操作");
		}
		this.createHistoryRegisterBillForVerify(billItem, toVerifyState, input.getReason(),
				VerifyTypeEnum.VERIFY_BEFORE_CHECKIN, operatorUser);
		this.tradeDetailService.doUpdateTradeDetailSaleStatus(operatorUser, billId);
		this.updateUserQrStatusByUserId(billItem.getUserId());
		return billId;
	}

	@Transactional
	@Override
	public Long doVerifyAfterCheckIn(RegisterBill input, OperatorUser operatorUser) {
		if (input == null || input.getId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		Long billId = input.getId();
		BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(input.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("参数错误"));

		RegisterBill billItem = this.get(billId);
		if (billItem == null) {
			throw new TraceBusinessException("数据不存在");
		}
		if (!VerifyTypeEnum.VERIFY_AFTER_CHECKIN.equalsToCode(billItem.getVerifyType())) {
			throw new TraceBusinessException("当前报备单只能预审核");
		}
		TradeDetail query = new TradeDetail();
		query.setBillId(billId);
		query.setBuyerId(billItem.getUserId());
		query.setTradeType(TradeTypeEnum.NONE.getCode());
		TradeDetail tradeDetailItem = this.tradeDetailService.listByExample(query).stream().findFirst().orElse(null);
		if (tradeDetailItem == null) {
			throw new TraceBusinessException("当前报备单未进门,不能场内审核");
		}
		BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
				.orElseThrow(() -> new TraceBusinessException("数据错误"));

		if (toVerifyState == BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus()).orElse(null)) {
			throw new TraceBusinessException("状态不能相同");
		}
		if (!BillVerifyStatusEnum.canDoVerify(billItem.getVerifyStatus())) {
			throw new TraceBusinessException("当前状态不能进行数据操作");
		}
		logger.info("场内审核: billId: {} from {} to {}", billId, fromVerifyState, toVerifyState);
		this.createHistoryRegisterBillForVerify(billItem, toVerifyState, input.getReason(),
				VerifyTypeEnum.VERIFY_AFTER_CHECKIN, operatorUser);
		this.tradeDetailService.doUpdateTradeDetailSaleStatus(operatorUser, billId);
		this.updateUserQrStatusByUserId(billItem.getUserId());
		return billId;

	}

	private Long createHistoryRegisterBillForVerify(RegisterBill item, BillVerifyStatusEnum toVerifyState,
			String returnedReason, VerifyTypeEnum verifyType, OperatorUser operatorUser) {
		this.registerBillHistoryService.createHistory(item);
		RegisterBill bill = new RegisterBill();
		bill.setId(item.getId());
		bill.setVerifyStatus(toVerifyState.getCode());
		bill.setVerifyType(verifyType.getCode());
		bill.setOperatorId(operatorUser.getId());
		bill.setOperatorName(operatorUser.getName());
		bill.setReason(StringUtils.trimToNull(returnedReason));
		this.updateSelective(bill);
		return bill.getId();
	}

	private RegisterBillDto preHandleCondition(RegisterBillDto query) {
		if (query.getVerifyType() == null || VerifyTypeEnum.VERIFY_BEFORE_CHECKIN.equalsToCode(query.getVerifyType())) {
			// query.setMetadata(IDTO.AND_CONDITION_EXPR, "(
			// bill_type="+BillTypeEnum.SUPPLEMENT.getCode()+" OR (verify_type is not null)
			// )");
		} else if (VerifyTypeEnum.VERIFY_AFTER_CHECKIN.equalsToCode(query.getVerifyType())) {
			query.setMetadata(IDTO.AND_CONDITION_EXPR, "(not ( bill_type=" + BillTypeEnum.SUPPLEMENT.getCode()
					+ " or (verify_type="+VerifyTypeEnum.VERIFY_BEFORE_CHECKIN.getCode()+" and verify_status =" + BillVerifyStatusEnum.PASSED.getCode()+ ") or is_checkin="+YnEnum.NO.getCode()+"))");
		}
		query.setVerifyType(null);
		return query;
	}
	@Override
	public BasePage<RegisterBill> listPageVerifyBill(RegisterBillDto query) {
		query = this.preHandleCondition(query);
		return this.listPageByExample(query);
	}

	@Override
	public List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBillDto query) {
		if (query == null) {
			throw new TraceBusinessException("参数错误");
		}
		query = this.preHandleCondition(query);

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

}