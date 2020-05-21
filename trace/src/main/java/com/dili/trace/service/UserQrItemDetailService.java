package com.dili.trace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrItem;
import com.dili.trace.domain.UserQrItemDetail;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.QrItemStatusEnum;
import com.dili.trace.glossary.QrItemTypeEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.UserTypeEnum;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserQrItemDetailService extends BaseServiceImpl<UserQrItemDetail, Long> {
	@Autowired
	UserService userService;
	@Autowired
	UserQrItemService userQrItemService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	RUserUpStreamService rUserUpStreamService;
	@Autowired
	RegisterBillMapper registerBillMapper;

	@PostConstruct
	public void init() {
		while (true) {
			User userQuery = DTOUtils.newDTO(User.class);
			userQuery.mset(IDTO.AND_CONDITION_EXPR, "id not in(select user_id from user_qr_item)");
			userQuery.setPage(1);
			userQuery.setRows(50);
			List<User> userList = this.userService.listByExample(userQuery);
			if (userList.isEmpty()) {
				break;
			}
			userList.stream().forEach(u -> this.intUserQrItem(u.getId()));
		}

	}

	/**
	 * 通过登记单来更新二维码状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateQrItemDetail(RegisterBill registerBill) {

		User userItem = this.userService.get(registerBill.getUserId());
		if (userItem == null) {
			throw new BusinessException("未能查询到用户信息");
		}
		Long userId = userItem.getId();
		// 查询并添加UserQrItem
		this.intUserQrItem(userId);
		UserQrItem qrItemCondition = new UserQrItem();
		qrItemCondition.setUserId(userItem.getId());
		qrItemCondition.setQrItemType(QrItemTypeEnum.BILL.getCode());
		UserQrItem qrItem = this.userQrItemService.listByExample(qrItemCondition).stream().findFirst().orElse(null);

		
		
		boolean withoutUrl = this.checkRegisterBill(registerBill);
		
		// 查询并添加UserQrItemDetail
		UserQrItemDetail qrItemDetail = new UserQrItemDetail();
		qrItemDetail.setObjectId(String.valueOf(registerBill.getId()));
		qrItemDetail.setUserQrItemId(qrItem.getId());
		
		
		long count=this.listByExample(qrItemDetail).stream().count();
		
		if(withoutUrl&&count==0) {
			this.insertSelective(qrItemDetail);
		}else if(!withoutUrl&&count>0) {
			this.deleteByExample(qrItemDetail);
		}

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
			throw new BusinessException("未能查询到用户信息");
		}
		UpStream upStreamItem = this.upStreamService.get(upStream.getId());
		if (upStreamItem == null) {
			throw new BusinessException("未能查询到上游信息");
		}

		UserQrItem qrItemCondition = new UserQrItem();
		qrItemCondition.setUserId(userItem.getId());
		qrItemCondition.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
		UserQrItem qrItem = this.userQrItemService.listByExample(qrItemCondition).stream().findFirst().orElse(null);

		
		boolean withoutAllNessaryInfo =this.checkUpStream(upStream);
		
		// 查询所有UserQrItemDetail并进行状态判断
		UserQrItemDetail qrItemDetail = new UserQrItemDetail();
		qrItemDetail.setUserQrItemId(qrItem.getId());
		qrItemDetail.setObjectId(String.valueOf(upStream.getId()));
		long count=this.listByExample(qrItemDetail).stream().count();
		if(withoutAllNessaryInfo&&count==0) {
			this.insertSelective(qrItemDetail);
		}else if(!withoutAllNessaryInfo&&count>0) {
			this.deleteByExample(qrItemDetail);
		}
		
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
			throw new BusinessException("未能查询到用户信息");
		}
		this.intUserQrItem(userId);
		UserQrItem qrItemCondition = new UserQrItem();
		qrItemCondition.setUserId(userItem.getId());
		qrItemCondition.setQrItemType(QrItemTypeEnum.USER.getCode());
		UserQrItem qrItem = this.userQrItemService.listByExample(qrItemCondition).stream().findFirst().orElse(null);

		boolean withoutNessaryInfo=this.checkUser(user);
		UserQrItemDetail qrItemDetail = new UserQrItemDetail();
		qrItemDetail.setObjectId(String.valueOf(userId));
		qrItemDetail.setUserQrItemId(qrItem.getId());
		long count=this.listByExample(qrItemDetail).stream().count();
		
		if(withoutNessaryInfo&&count==0) {
			this.insertSelective(qrItemDetail);
		}else if(!withoutNessaryInfo&&count>0) {
			this.deleteByExample(qrItemDetail);
		}
		
		this.updateUserQrStatus(userId);

	}

	public List<UserQrItemDetail> findByUserQrItemIdList(List<Long> qrItemIdList) {
		List<Long> userQrItemIdList = CollectionUtils.emptyIfNull(qrItemIdList).stream().filter(Objects::nonNull)
				.collect(Collectors.toList());
		if (userQrItemIdList.isEmpty()) {
			return new ArrayList<>(0);
		}
		Example example = new Example(UserQrItemDetail.class);
		example.and().andIn("userQrItemId", userQrItemIdList);
		return super.getDao().selectByExample(example);
	}

	/**
	 * 通过当前条目判断并更新用户二维码状态
	 */
	private int updateUserQrStatus(Long userId) {
		this.checkAndUpdateQrItemType(userId);
		
		UserQrStatusEnum userQrStatus = this.checkQrItemStatusEnum(userId);
		User user = DTOUtils.newDTO(User.class);
		user.setId(userId);
		user.setQrStatus(userQrStatus.getCode());

		return this.userService.updateSelective(user);
	}

	/**
	 * 初始化用户二维码
	 */
	private boolean intUserQrItem(Long userId) {

		Stream.of(QrItemTypeEnum.BILL, QrItemTypeEnum.UPSTREAM, QrItemTypeEnum.USER).forEach(itemType -> {
			UserQrItem qrItem = new UserQrItem();
			qrItem.setUserId(userId);
			qrItem.setQrItemType(itemType.getCode());
			// 默认为红色(RED)状态
			if (this.userQrItemService.listByExample(qrItem).isEmpty()) {
				qrItem.setQrItemStatus(itemType.getDefaultStatus().getCode());
				this.userQrItemService.insertSelective(qrItem);
			}
		});
		return true;
	}


	private void checkAndUpdateQrItemType(Long userId) {
		
		
		UserQrItem userQrItemItem = new UserQrItem();
		userQrItemItem.setUserId(userId);
		userQrItemItem.setQrItemType(QrItemTypeEnum.USER.getCode());
		userQrItemItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
		this.userQrItemService.updateSelective(userQrItemItem);
		

		
		UserQrItem billQrItem = new UserQrItem();
		billQrItem.setUserId(userId);
		billQrItem.setQrItemType(QrItemTypeEnum.BILL.getCode());

		
		RegisterBill billQuery = DTOUtils.newDTO(RegisterBill.class);
		billQuery.setUserId(userId);
		int count = this.registerBillMapper.selectCount(billQuery);
		if(count>0) {
			billQuery.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
			List<RegisterBill> registerBillList = this.registerBillService.listByExample(billQuery);
			if (registerBillList.isEmpty()) {
				billQrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
			}else {
				
				boolean withoutUrl = registerBillList.stream().anyMatch(this::checkRegisterBill);
				if (withoutUrl) {
					billQrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
				}else {
					billQrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
				}
			}
		}else {
			billQrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
		}
		this.userQrItemService.updateSelective(billQrItem);
		
		
		UserQrItem upStreamQrItem = new UserQrItem();
		upStreamQrItem.setUserId(userId);
		upStreamQrItem.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
		List<UpStream> upStreamList = this.upStreamService.queryUpStreamByUserId(userId);
		
		if (upStreamList.isEmpty()) {
			billQrItem.setQrItemStatus(QrItemStatusEnum.RED.getCode());
		}else {
			boolean withoutAllNessaryInfo = upStreamList.stream().anyMatch(this::checkUpStream);
			if (withoutAllNessaryInfo) {
				billQrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
			}else {
				billQrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
			}
			
			
		}
		
	}
	private UserQrStatusEnum checkQrItemStatusEnum(Long userId) {

		User user = this.userService.get(userId);

		List<UpStream> upStreamList = this.upStreamService.queryUpStreamByUserId(userId);

		RegisterBill billQuery = DTOUtils.newDTO(RegisterBill.class);
		billQuery.setUserId(userId);
		int count = this.registerBillMapper.selectCount(billQuery);

		if (upStreamList.isEmpty() && count == 0) {
			return UserQrStatusEnum.RED;
		}

		if (!upStreamList.isEmpty() && count == 0) {
			return UserQrStatusEnum.YELLOW;
		}

		billQuery.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
		List<RegisterBill> registerBillList = this.registerBillService.listByExample(billQuery);
		if (registerBillList.isEmpty()) {
			return UserQrStatusEnum.YELLOW;
		}

		boolean withoutUrl = registerBillList.stream().anyMatch(this::checkRegisterBill);
		if (withoutUrl) {
			return UserQrStatusEnum.YELLOW;
		}

		boolean withoutAllNessaryInfo = upStreamList.stream().anyMatch(this::checkUpStream);
		if (withoutAllNessaryInfo) {
			return UserQrStatusEnum.YELLOW;
		}

		return UserQrStatusEnum.GREEN;
	}
	private boolean checkUser(User user) {

		return false;

	}
	private boolean checkRegisterBill(RegisterBill bill) {

		return StringUtils.isAllBlank(bill.getOriginCertifiyUrl(), bill.getDetectReportUrl());

	}

	private boolean checkUpStream(UpStream upstream) {

		if (UpStreamTypeEnum.CORPORATE.getCode().equals(upstream.getUpstreamType())) {
			if (StringUtils.isAnyBlank(upstream.getName(), upstream.getBusinessLicenseUrl(), upstream.getLicense(),
					upstream.getManufacturingLicenseUrl(), upstream.getOperationLicenseUrl(), upstream.getIdCard(),
					upstream.getLegalPerson(), upstream.getTelphone())) {
				return true;
			}
		} else {
			if (StringUtils.isAnyBlank(upstream.getName(), upstream.getCardNoFrontUrl(), upstream.getCardNoBackUrl(),
					upstream.getIdCard(), upstream.getTelphone())) {
				return true;
			}
		}
		return false;
	}

}