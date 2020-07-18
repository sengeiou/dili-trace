package com.dili.trace.service;

import java.util.Date;
import java.util.Optional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.enums.BillVerifyStatusEnum;
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

	public void run(String... args) {

	}

	public UserQrHistory createUserQrHistoryForWithousBills(User inputUser) {
		if (inputUser == null || inputUser.getId() == null) {
			return null;
		}
		User userItem = this.userService.get(inputUser.getId());
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
			userQrHistory.setContent("最近七天无报备" + ",变为" + color + "码");
			this.insertSelective(userQrHistory);
			return userQrHistory;
		});

	}

	public UserQrHistory createUserQrHistoryForUserRegist(Long userId, Integer qrStatus) {
		if (userId == null) {
			return null;
		}
		User userItem = this.userService.get(userId);
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

	public UserQrHistory createUserQrHistoryForVerifyBill(RegisterBill bill, Integer qrStatus) {
		if (bill == null || bill.getBillId() == null) {
			return null;
		}
		RegisterBill billItem = this.registerBillService.get(bill.getBillId());
		if (billItem == null) {
			return null;
		}
		BillVerifyStatusEnum billVerifyStatusEnum = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
				.orElse(null);

		User userItem = this.userService.get(billItem.getUserId());

		if (billVerifyStatusEnum == null) {
			return null;
		}
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
			userQrHistory.setContent("最新报备单当前审核状态是" + billVerifyStatusEnum.getName() + ",变为" + color + "码");
			this.insertSelective(userQrHistory);
			return userQrHistory;
		});
	}

	private void updateUserQrStatus(Long userId, Integer qrStatus) {

		User user = DTOUtils.newDTO(User.class);
		user.setId(userId);
		user.setQrStatus(qrStatus);
		// user.setPreQrStatus(userItem.getQrStatus());
		this.userService.updateSelective(user);

	}

	private UserQrHistory buildUserQrHistory(User userItem, Integer qrStatus) {

		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(qrStatus);
		userQrHistory.setCreated(new Date());
		userQrHistory.setModified(new Date());
		return userQrHistory;

	}

	private Optional<UserQrHistory> findLatestUserQrHistoryByUserId(Long userId) {
		UserQrHistory query = new UserQrHistory();
		query.setUserId(userId);
		query.setSort("id");
		query.setOrder("desc");
		query.setPage(1);
		query.setRows(1);
		return StreamEx.of(this.listPageByExample(query).getDatas()).findFirst();
	}

}