package com.dili.trace.service;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrItem;
import com.dili.trace.glossary.ColorEnum;
import com.dili.trace.glossary.QrItemActionEnum;
import com.dili.trace.glossary.QrItemTypeEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserQrStatusEnum;

@Service
public class UserQrItemService extends BaseServiceImpl<UserQrItem, Long> implements CommandLineRunner {
	@Autowired
	UserService userService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	RUserUpStreamService rUserUpStreamService;
	@Autowired
	RegisterBillMapper registerBillMapper;

	 
	public void run(String... args) {
		this.initRegisterBillComplete();
		this.intUserQrItem();
	}
	private void initRegisterBillComplete() {

		RegisterBill billQuery = new RegisterBill();
		billQuery.setMetadata(IDTO.AND_CONDITION_EXPR, " complete is null");
		billQuery.setPage(1);
		billQuery.setRows(50);
		while (true) {
			List<RegisterBill> billList = this.registerBillService.listByExample(billQuery);
			if (billList.isEmpty()) {
				break;
			}
			billList.stream().forEach(bill -> {
				RegisterBill updatable = new RegisterBill();
				updatable.setId(bill.getId());
				if (this.checkRegisterBill(bill) != null) {
					updatable.setComplete(0);
				} else {
					updatable.setComplete(1);
				}
				this.registerBillMapper.updateByPrimaryKeySelective(updatable);

			});
		}

	}

	/**
	 * 通过登记单来更新二维码状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateQrItemDetail(RegisterBill registerBill) {

		User userItem = this.userService.get(registerBill.getUserId());
		if (userItem == null) {
			throw new TraceBusinessException("未能查询到用户信息");
		}
		if(registerBill.getComplete()==null) {
			
			RegisterBill updatable = new RegisterBill();
			updatable.setId(registerBill.getId());
			if (this.checkRegisterBill(registerBill) != null) {
				updatable.setComplete(0);
			} else {
				updatable.setComplete(1);
			}
			this.registerBillMapper.updateByPrimaryKeySelective(updatable);
		}
		Long userId = userItem.getId();
		// 查询并添加UserQrItem
		UserQrItem qrItemCondition = new UserQrItem();
		qrItemCondition.setUserId(userItem.getId());
		qrItemCondition.setQrItemType(QrItemTypeEnum.BILL.getCode());
		UserQrItem qrItem = this.listByExample(qrItemCondition).stream().findFirst().orElse(null);

		boolean withoutUrl = this.checkRegisterBill(registerBill) != null;

		// TODO 红码逻辑
		// 30天内，累积检测不合格商品超50%以上，或检测不合格次数3次以上（待定）。*、
		// dto.setCreatedStart(
		// LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss")));
		// List<RegisterBill> billList = this.registerBillService.listByExample(dto);
		this.updateUserQrStatus(userId);
	}

	/*
	 *** 通过上游信息来更新二维码状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateQrItemDetail(UpStream upStream, Long userId) {
		User userItem = this.userService.get(userId);
		if (userItem == null) {
			throw new TraceBusinessException("未能查询到用户信息");
		}
		UpStream upStreamItem = this.upStreamService.get(upStream.getId());
		if (upStreamItem == null) {
			throw new TraceBusinessException("未能查询到上游信息");
		}

		UserQrItem qrItemCondition = new UserQrItem();
		qrItemCondition.setUserId(userItem.getId());
		qrItemCondition.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
		UserQrItem qrItem = this.listByExample(qrItemCondition).stream().findFirst().orElse(null);

		boolean withoutAllNessaryInfo = this.checkUpStream(upStream) != null;

		this.updateUserQrStatus(userId);

	}

	/**
	 * 通过用户信息来更新二维码状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateQrItemDetail(User user) {
		Long userId = user.getId();
		User userItem = this.userService.get(userId);
		if (userItem == null) {
			throw new TraceBusinessException("未能查询到用户信息");
		}
		UserQrItem qrItemCondition = new UserQrItem();
		qrItemCondition.setUserId(userItem.getId());
		qrItemCondition.setQrItemType(QrItemTypeEnum.USER.getCode());
		UserQrItem qrItem = this.listByExample(qrItemCondition).stream().findFirst().orElse(null);

		this.updateUserQrStatus(userId);

	}

	/**
	 * 通过当前条目判断并更新用户二维码状态
	 */
	public int updateUserQrStatus(Long userId) {

		UserQrStatusEnum userQrStatus = this.updateUserQrItem(userId);
		User user = DTOUtils.newDTO(User.class);
		user.setId(userId);
		user.setQrStatus(userQrStatus.getCode());

		return this.userService.updateSelective(user);
	}

	/**
	 * 初始化用户二维码
	 */
	private boolean intUserQrItem() {

		while (true) {
			User userQuery = DTOUtils.newDTO(User.class);
			userQuery.mset(IDTO.AND_CONDITION_EXPR, "id not in(select user_id from user_qr_item)");
			userQuery.setPage(1);
			userQuery.setRows(50);
			List<User> userList = this.userService.listByExample(userQuery);
			if (userList.isEmpty()) {
				break;
			}
			userList.stream().forEach(u -> {

				Stream.of(QrItemTypeEnum.BILL, QrItemTypeEnum.UPSTREAM, QrItemTypeEnum.USER).forEach(itemType -> {
					UserQrItem qrItem = new UserQrItem();
					qrItem.setUserId(u.getId());
					qrItem.setQrItemType(itemType.getCode());
					qrItem.setAction(QrItemActionEnum.DONOTHING.getCode());
					qrItem.setHasData(TFEnum.FALSE.getCode());
					qrItem.setValid(TFEnum.FALSE.getCode());

					if (this.listByExample(qrItem).isEmpty()) {
						qrItem.setColor(itemType.getDefaultColor().getCode());
						this.insertSelective(qrItem);
					}

				});
				this.updateUserQrStatus(u.getId());

			});
		}

		return true;
	}

	private UserQrStatusEnum updateUserQrItem(Long userId) {

		User user = this.userService.get(userId);

		UserQrItem userQrItemItem = new UserQrItem();
		userQrItemItem.setUserId(userId);
		userQrItemItem.setQrItemType(QrItemTypeEnum.USER.getCode());
		userQrItemItem = this.listByExample(userQrItemItem).stream().findFirst().orElse(userQrItemItem);

		userQrItemItem.setHasData(TFEnum.TRUE.getCode());
//		Long id=this.checkUser(user);
//		if(id==null) {
		userQrItemItem.setValid(TFEnum.TRUE.getCode());
		userQrItemItem.setColor(ColorEnum.GREEN.getCode());
		userQrItemItem.setAction(QrItemActionEnum.DONOTHING.getCode());
//			userQrItemItem.setObjects(String.valueOf(userId));
//		}else {
//			userQrItemItem.setValid(TFEnum.FALSE.getCode());
//			userQrItemItem.setColor(ColorEnum.YELLOW.getCode());
//			userQrItemItem.setAction(QrItemActionEnum.APPROVE.getCode());
//		}

		if(userQrItemItem.getId()==null) {
			this.insertSelective(userQrItemItem);
		}else {
			this.updateSelective(userQrItemItem);	
		}

		List<UpStream> upStreamList = this.upStreamService.queryUpStreamByUserId(userId);

		UserQrItem upStreamQrItemItem = new UserQrItem();
		upStreamQrItemItem.setUserId(userId);
		upStreamQrItemItem.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
		upStreamQrItemItem = this.listByExample(upStreamQrItemItem).stream().findFirst().orElse(upStreamQrItemItem);
		if (upStreamList.isEmpty()) {
			upStreamQrItemItem.setHasData(TFEnum.FALSE.getCode());
			upStreamQrItemItem.setValid(TFEnum.FALSE.getCode());
			upStreamQrItemItem.setColor(ColorEnum.RED.getCode());
			upStreamQrItemItem.setAction(QrItemActionEnum.CREATE.getCode());

		} else {
			upStreamQrItemItem.setHasData(TFEnum.TRUE.getCode());
//			List<Long>  withoutAllNessaryInfo = upStreamList.stream().map(this::checkUpStream).filter(Objects::nonNull).collect(Collectors.toList());
//			if(withoutAllNessaryInfo.isEmpty()) {
			upStreamQrItemItem.setValid(TFEnum.TRUE.getCode());
			upStreamQrItemItem.setColor(ColorEnum.GREEN.getCode());
			upStreamQrItemItem.setAction(QrItemActionEnum.DONOTHING.getCode());
//			}else {
//				upStreamQrItemItem.setValid(TFEnum.FALSE.getCode());
//				upStreamQrItemItem.setObjects(withoutAllNessaryInfo.stream().limit(1).map(String::valueOf).collect(Collectors.joining(",")));
//				upStreamQrItemItem.setColor(ColorEnum.YELLOW.getCode());
//				upStreamQrItemItem.setAction(QrItemActionEnum.DONOTHING.getCode());
//			}
		}
		if(upStreamQrItemItem.getId()==null) {
			this.insertSelective(upStreamQrItemItem);
		}else {
			this.updateSelective(upStreamQrItemItem);	
		}

		UserQrItem billQrItemItem = new UserQrItem();
		billQrItemItem.setUserId(userId);
		billQrItemItem.setQrItemType(QrItemTypeEnum.BILL.getCode());
		billQrItemItem = this.listByExample(billQrItemItem).stream().findFirst().orElse(billQrItemItem);

		RegisterBill billQuery = new RegisterBill();
		billQuery.setUserId(userId);
		int count = this.registerBillMapper.selectCount(billQuery);
		if (count == 0) {
			billQrItemItem.setHasData(TFEnum.FALSE.getCode());
			billQrItemItem.setValid(TFEnum.FALSE.getCode());

			billQrItemItem.setColor(ColorEnum.YELLOW.getCode());
			billQrItemItem.setAction(QrItemActionEnum.DONOTHING.getCode());

		} else {
			billQrItemItem.setHasData(TFEnum.TRUE.getCode());

			this.updateComplete(userId);
//			billQuery.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
			billQuery.setComplete(1);
			List<RegisterBill> registerBillList = this.registerBillService.listByExample(billQuery);
			if(registerBillList.size()==count) {
				billQrItemItem.setValid(TFEnum.TRUE.getCode());
				billQrItemItem.setColor(ColorEnum.GREEN.getCode());
				billQrItemItem.setAction(QrItemActionEnum.DONOTHING.getCode());
				
			}else {
				billQrItemItem.setValid(TFEnum.FALSE.getCode());
//				billQrItemItem.setObjects(withoutUrl.stream().limit(1).map(String::valueOf).collect(Collectors.joining(",")));
				billQrItemItem.setColor(ColorEnum.YELLOW.getCode());
				billQrItemItem.setAction(QrItemActionEnum.APPROVE.getCode());
				
			}

		}
		if(billQrItemItem.getId()==null) {
			this.insertSelective(billQrItemItem);
		}else {
			this.updateSelective(billQrItemItem);	
		}
		
		/*
		 * if(TFEnum.fromCode(userQrItemItem.getHasData())==TFEnum.TRUE
		 * &&TFEnum.fromCode(userQrItemItem.getValid())==TFEnum.TRUE
		 * &&TFEnum.fromCode(upStreamQrItemItem.getHasData())==TFEnum.TRUE &&
		 * TFEnum.fromCode(upStreamQrItemItem.getValid())==TFEnum.TRUE
		 * &&TFEnum.fromCode(billQrItemItem.getHasData())==TFEnum.TRUE
		 * &&TFEnum.fromCode(billQrItemItem.getValid())==TFEnum.TRUE) {
		 * 
		 * return UserQrStatusEnum.GREEN; }
		 * 
		 * 
		 * if (TFEnum.fromCode(upStreamQrItemItem.getHasData())==TFEnum.TRUE &&
		 * (TFEnum.fromCode(billQrItemItem.getHasData()
		 * )==TFEnum.FALSE||TFEnum.fromCode(billQrItemItem.getValid())==TFEnum.FALSE)) {
		 * return UserQrStatusEnum.YELLOW; }
		 */

		if (ColorEnum.RED.equalsCode(upStreamQrItemItem.getColor())
				&& TFEnum.fromCode(billQrItemItem.getHasData()) == TFEnum.FALSE) {
			return UserQrStatusEnum.RED;
		}

		if (ColorEnum.GREEN.equalsCode(userQrItemItem.getColor())
				&& ColorEnum.GREEN.equalsCode(upStreamQrItemItem.getColor())
				&& ColorEnum.GREEN.equalsCode(billQrItemItem.getColor())) {
			return UserQrStatusEnum.GREEN;

		} else {
			return UserQrStatusEnum.YELLOW;
		}

	}
	private void updateComplete(Long userId) {
//		RegisterBill billQuery = new RegisterBill();
//		billQuery.setUserId(userId);
//		billQuery.setMetadata(IDTO.AND_CONDITION_EXPR," complete is null or complete = 0");
//		this.registerBillService.listByExample(billQuery).stream().forEach(bill->{
//			Integer complete=0;
//			if(this.checkRegisterBill(bill)!=null) {
//				complete=0;
//			}else {
//				complete=1;
//			}
//			if(!complete.equals(bill.getComplete())) {
//				RegisterBill updatable = new RegisterBill();
//				updatable.setId(bill.getId());
//				updatable.setComplete(complete);
//				this.registerBillService.updateSelective(updatable);
//			}
//			
//		});
	}
	private Long checkUser(User user) {

		return null;

	}

	private Long checkRegisterBill(RegisterBill bill) {
//TODO
		return null;
//		return StringUtils.isAllBlank(bill.getOriginCertifiyUrl(), bill.getDetectReportUrl()) ? bill.getId() : null;

	}

	private Long checkUpStream(UpStream upstream) {

		if (UpStreamTypeEnum.CORPORATE.getCode().equals(upstream.getUpstreamType())) {
			if (StringUtils.isAnyBlank(upstream.getName(), upstream.getBusinessLicenseUrl(), upstream.getLicense(),
					upstream.getManufacturingLicenseUrl(), upstream.getOperationLicenseUrl(), upstream.getIdCard(),
					upstream.getLegalPerson(), upstream.getTelphone())) {
				return upstream.getId();
			}
		} else {
			if (StringUtils.isAnyBlank(upstream.getName(), upstream.getCardNoFrontUrl(), upstream.getCardNoBackUrl(),
					upstream.getIdCard(), upstream.getTelphone())) {
				return upstream.getId();
			}
		}
		return null;
	}

}