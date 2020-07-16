package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.glossary.UserQrStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class UserQrHistoryService extends BaseServiceImpl<UserQrHistory, Long> implements CommandLineRunner {
	@Autowired
	UserService userService;
	@Autowired
	RegisterBillService registerBillService;

	public void run(String... args) {

	}
	public UserQrHistory createUserQrHistoryForWithousBills(User user) {
		if (user == null || user.getId() == null) {
			return null;
		}
		User userItem = this.userService.get(user.getId());
		if (userItem == null) {
			return null;
		}
		String color = UserQrStatusEnum.fromCode(userItem.getQrStatus()).map(UserQrStatusEnum::getDesc)
				.orElse(UserQrStatusEnum.BLACK.getDesc());
		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(userItem.getQrStatus());
		userQrHistory.setContent("最近七天无报备"+",变为" + color + "码");
		return userQrHistory;
	}
	public UserQrHistory createUserQrHistoryForUserRegist(User user) {
		if (user == null || user.getId() == null) {
			return null;
		}
		User userItem = this.userService.get(user.getId());
		if (userItem == null) {
			return null;
		}
		String color = UserQrStatusEnum.fromCode(userItem.getQrStatus()).map(UserQrStatusEnum::getDesc)
				.orElse(UserQrStatusEnum.BLACK.getDesc());
		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(userItem.getQrStatus());
		userQrHistory.setContent("完成注册,默认为" + color + "码");
		return userQrHistory;
	}

	public UserQrHistory createUserQrHistoryForVerifyBill(RegisterBill bill) {
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
		String color = UserQrStatusEnum.fromCode(userItem.getQrStatus()).map(UserQrStatusEnum::getDesc)
				.orElse(UserQrStatusEnum.BLACK.getDesc());
		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(userItem.getQrStatus());
		userQrHistory.setContent("最新报备单当前审核状态是"+billVerifyStatusEnum.getName()+",变为" + color + "码");
		this.insertSelective(userQrHistory);
		return userQrHistory;
	}

}