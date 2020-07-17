package com.dili.trace.service;

import java.util.Date;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
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
		userQrHistory.setCreated(new Date());
		userQrHistory.setModified(new Date());
		userQrHistory.setContent("最近七天无报备"+",变为" + color + "码");
		return userQrHistory;
	}
	public UserQrHistory createUserQrHistoryForUserRegist(Long userId,Integer qrstatus) {
		if (userId == null) {
			return null;
		}
		User userItem = this.userService.get(userId);
		if (userItem == null) {
			return null;
		}
		String color = UserQrStatusEnum.fromCode(userItem.getQrStatus()).map(UserQrStatusEnum::getDesc)
				.orElse(UserQrStatusEnum.BLACK.getDesc());
		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(userItem.getQrStatus());
		userQrHistory.setCreated(new Date());
		userQrHistory.setModified(new Date());
		userQrHistory.setContent("完成注册,默认为" + color + "码");
		return userQrHistory;
	}

	public UserQrHistory createUserQrHistoryForVerifyBill(RegisterBill bill,Integer qrStatus) {
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


		if(qrStatus.equals(userItem.getPreQrStatus())){
			return null;
		}
		User user = DTOUtils.newDTO(User.class);
		user.setId(userItem.getId());
		user.setQrStatus(qrStatus);
		user.setPreQrStatus(userItem.getQrStatus());
		this.userService.updateSelective(user);



		String color = UserQrStatusEnum.fromCode(userItem.getQrStatus()).map(UserQrStatusEnum::getDesc)
				.orElse(UserQrStatusEnum.BLACK.getDesc());
		UserQrHistory userQrHistory = new UserQrHistory();
		userQrHistory.setUserId(userItem.getId());
		userQrHistory.setUserName(userItem.getName());
		userQrHistory.setQrStatus(userItem.getQrStatus());
		userQrHistory.setCreated(new Date());
		userQrHistory.setModified(new Date());
		userQrHistory.setContent("最新报备单当前审核状态是"+billVerifyStatusEnum.getName()+",变为" + color + "码");
		this.insertSelective(userQrHistory);
		return userQrHistory;
	}

}