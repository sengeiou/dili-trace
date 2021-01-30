package com.dili.trace.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.google.common.base.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class UserQrHistoryService extends BaseServiceImpl<UserQrHistory, Long> implements CommandLineRunner {
	@Autowired
	UserService userService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	TradeRequestService tradeRequestService;

	public void run(String... args) {

	}

	public UserQrHistory createUserQrHistoryForWithousBills(Long userId) {
		if (userId == null) {
			return null;
		}
		UserInfo userItem = this.userService.get(userId);
		if (userItem == null) {
			return null;
		}
		Integer qrStatus = UserQrStatusEnum.BLACK.getCode();
		this.updateUserQrStatus(userItem.getId(), qrStatus);
		return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
			return Objects.equal(qrhis.getQrStatus(), qrStatus);
		}).orElseGet(() -> {
			String color = UserQrStatusEnum.fromCode(qrStatus).map(UserQrStatusEnum::getDesc)
					.orElse(UserQrStatusEnum.BLACK.getDesc());

			UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatus);
			userQrHistory.setContent("最近七天无报备且无交易单" + ",变为" + color + "码");
			this.insertSelective(userQrHistory);
			return userQrHistory;
		});

	}

	public UserQrHistory createUserQrHistoryForUserRegist(Long userId, Integer qrStatus) {
		if (userId == null) {
			return null;
		}
		UserInfo userItem = this.userService.get(userId);
		if (userItem == null) {
			return null;
		}

		this.updateUserQrStatus(userItem.getId(), qrStatus);

		return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
			return Objects.equal(qrhis.getQrStatus(), qrStatus);
		}).orElseGet(() -> {
			String color = UserQrStatusEnum.fromCode(qrStatus).map(UserQrStatusEnum::getDesc)
					.orElse(UserQrStatusEnum.BLACK.getDesc());

			UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatus);
			userQrHistory.setContent("完成注册,默认为" + color + "码");
			this.insertSelective(userQrHistory);
			return userQrHistory;
		});
	}

	public UserQrHistory createUserQrHistoryForVerifyBill(RegisterBill billItem,Long userId) {
		if (billItem == null) {
			return null;
		}
		BillVerifyStatusEnum billVerifyStatusEnum = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
				.orElse(null);
		if (billVerifyStatusEnum == null) {
			return null;
		}
		UserQrStatusEnum userQrStatus = UserQrStatusEnum.BLACK;
		switch (billVerifyStatusEnum) {
			case PASSED:
				userQrStatus = UserQrStatusEnum.GREEN;
				break;
			case NO_PASSED:
				userQrStatus = UserQrStatusEnum.RED;
				break;
			case RETURNED:
				userQrStatus = UserQrStatusEnum.YELLOW;
				break;
			case WAIT_AUDIT:
				userQrStatus = UserQrStatusEnum.YELLOW;
				break;
			default:
				throw new TraceBizException("错误");
		}
		Integer qrStatus = userQrStatus.getCode();
		UserInfo userItem = this.userService.get(userId);
		if (userItem == null) {
			return null;
		}

		this.updateUserQrStatus(userItem.getId(), qrStatus);
		return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
			return Objects.equal(qrhis.getQrStatus(), qrStatus);
		}).orElseGet(() -> {
			String color = UserQrStatusEnum.fromCode(qrStatus).map(UserQrStatusEnum::getDesc)
					.orElse(UserQrStatusEnum.BLACK.getDesc());
			UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatus);
			userQrHistory.setBillId(billItem.getId());
			userQrHistory.setVerifyStatus(billVerifyStatusEnum.getCode());
			userQrHistory.setContent("最新操作报备单审核状态是" + billVerifyStatusEnum.getName() + ",变为" + color + "码");
			this.insertSelective(userQrHistory);
			return userQrHistory;
		});
	}

	public UserQrHistory createUserQrHistoryForOrder(Long tradeRequestId, Long userId) {
		if (userId == null) {
			return null;
		}
		UserInfo userItem = this.userService.get(userId);
		if (userItem == null) {
			return null;
		}
		Integer qrStatus = UserQrStatusEnum.GREEN.getCode();
		this.updateUserQrStatus(userItem.getId(), UserQrStatusEnum.GREEN.getCode());

		return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
			return Objects.equal(qrhis.getQrStatus(), qrStatus);
		}).orElseGet(() -> {
			String color = UserQrStatusEnum.fromCode(qrStatus).map(UserQrStatusEnum::getDesc)
					.orElse(UserQrStatusEnum.GREEN.getDesc());

			UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatus);
			userQrHistory.setContent("订单交易完成, 变为" + color + "码");
			userQrHistory.setTradeRequestId(tradeRequestId);
			this.insertSelective(userQrHistory);
			return userQrHistory;
		});
	}


	public void rollbackUserQrStatusForOrderReturn(Long tradeRequestId, Long userId) throws ParseException {
		if (tradeRequestId != null) {
			UserQrHistory domain = new UserQrHistory();
			// domain.setBillId(deletedBillId);
			domain.setIsValid(YesOrNoEnum.NO.getCode());

			UserQrHistory condition = new UserQrHistory();
			condition.setTradeRequestId(tradeRequestId);
			condition.setUserId(userId);
			this.updateSelectiveByExample(domain, condition);

			UserQrHistory query = new UserQrHistory();
			query.setUserId(userId);
			query.setPage(1);
			query.setRows(1);
			query.setSort("id");
			query.setOrder("desc");
			query.setIsValid(YesOrNoEnum.YES.getCode());
			Integer userQrStatus = this.listPageByExample(query).getDatas().stream().findFirst()
					.map(UserQrHistory::getQrStatus).orElse(UserQrStatusEnum.BLACK.getCode());
			this.updateUserQrStatus(userId, userQrStatus);

		}
	}

	protected Date start(LocalDateTime now) {
		Date start = Date.from(
				now.minusDays(6).atZone(ZoneId.systemDefault()).toInstant());
		return start;
	}

	protected Date end(LocalDateTime now) {
		Date end = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		return end;
	}


	/**
	 * 禁止删除的报备单对应的状态记录，并恢复到前一个有效的用户二维码颜色
	 * 
	 * @param deletedBillId
	 * @param userId
	 */
	public void rollbackUserQrStatus(Long deletedBillId, Long userId) {
		if (deletedBillId != null) {
			UserQrHistory domain = new UserQrHistory();
			// domain.setBillId(deletedBillId);
			domain.setIsValid(YesOrNoEnum.NO.getCode());

			UserQrHistory condition = new UserQrHistory();
			condition.setBillId(deletedBillId);
			condition.setUserId(userId);
			this.updateSelectiveByExample(domain, condition);

			UserQrHistory query = new UserQrHistory();
			query.setUserId(userId);
			query.setPage(1);
			query.setRows(1);
			query.setSort("id");
			query.setOrder("desc");
			query.setIsValid(YesOrNoEnum.YES.getCode());
			Integer userQrStatus = this.listPageByExample(query).getDatas().stream().findFirst()
					.map(UserQrHistory::getQrStatus).orElse(UserQrStatusEnum.BLACK.getCode());
			this.updateUserQrStatus(userId, userQrStatus);

		}

	}

	private void updateUserQrStatus(Long userId, Integer qrStatus) {

		UserInfo user = new UserInfo();
		user.setId(userId);
		user.setQrStatus(qrStatus);
		// user.setPreQrStatus(userItem.getQrStatus());
		this.userService.updateSelective(user);

	}

	private UserQrHistory buildUserQrHistory(UserInfo userItem, Integer qrStatus) {

		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(qrStatus);
		userQrHistory.setCreated(new Date());
		userQrHistory.setModified(new Date());
		userQrHistory.setIsValid(YesOrNoEnum.YES.getCode());
		return userQrHistory;

	}

	private Optional<UserQrHistory> findLatestUserQrHistoryByUserId(Long userId) {
		UserQrHistory query = new UserQrHistory();
		query.setUserId(userId);
		query.setSort("id");
		query.setOrder("desc");
		query.setPage(1);
		query.setRows(1);
		query.setIsValid(YesOrNoEnum.YES.getCode());
		return StreamEx.of(this.listPageByExample(query).getDatas()).findFirst();
	}

}