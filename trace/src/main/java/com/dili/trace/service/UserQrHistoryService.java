package com.dili.trace.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
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
		User userItem = this.userService.get(userId);
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
			case NONE:
				userQrStatus = UserQrStatusEnum.YELLOW;
				break;
			default:
				throw new TraceBusinessException("错误");
		}
		Integer qrStatus = userQrStatus.getCode();
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
		User userItem = this.userService.get(userId);
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

	public UserQrHistory createUserQrHistoryForOrderReturn(Long tradeRequestId, Long userId) throws ParseException {
		if (userId == null) {
			return null;
		}
		User userItem = this.userService.get(userId);
		if (userItem == null) {
			return null;
		}
		LocalDateTime now = LocalDateTime.now();
		Date startDate = this.start(now);
		Date endDate = this.end(now);


		// 查询最近7天报价单报价单
		String createdSql = "created>='"+ DateUtils.format(startDate)+"' and created<='"+DateUtils.format(endDate)+"'";
		RegisterBill registerBill = new RegisterBill();
		registerBill.setUserId(userId);
		registerBill.setSort("id");
		registerBill.setOrder("desc");
		registerBill.setMetadata(IDTO.AND_CONDITION_EXPR,createdSql);
		registerBill.setIsDeleted(0);
		registerBill.setRows(1);
		registerBill.setPage(1);
		RegisterBill lastestBill = StreamEx.of(registerBillService.listPageByExample(registerBill).getDatas()).nonNull().findFirst().orElse(null);;

		// 查询最近7天交易单
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setBuyerId(userId);
		tradeRequest.setSort("id");
		tradeRequest.setOrder("desc");
		tradeRequest.setMetadata(IDTO.AND_CONDITION_EXPR,createdSql+" and return_status != 20");
		tradeRequest.setRows(1);
		tradeRequest.setPage(1);
		TradeRequest lastestTrade = StreamEx.of(tradeRequestService.listPageByExample(tradeRequest).getDatas()).nonNull().findFirst().orElse(null);
		UserQrStatusEnum userQrStatus = UserQrStatusEnum.BLACK;
		// 七天内无报价单和交易单
		if(lastestBill != null || lastestTrade != null)
		{
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			Date before = sdf1.parse("1900-01-01");
			Date tradeCreated = lastestTrade == null ? before : lastestTrade.getCreated();
			Date billCreated = lastestBill == null ? before : lastestBill.getCreated();
			if(tradeCreated.compareTo(billCreated) > 0)
			{
				userQrStatus = UserQrStatusEnum.GREEN;
			}
			else
			{
				BillVerifyStatusEnum billVerifyStatusEnum = BillVerifyStatusEnum.fromCode(lastestBill.getVerifyStatus())
						.orElse(null);
				if (billVerifyStatusEnum == null) {
					return null;
				}
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
					case NONE:
						userQrStatus = UserQrStatusEnum.YELLOW;
						break;
					default:
						throw new TraceBusinessException("错误");
				}
			}
		}
		if(!userItem.getQrStatus().equals(userQrStatus.getCode()))
		{
			String color = UserQrStatusEnum.fromCode(userQrStatus.getCode()).map(UserQrStatusEnum::getDesc).get();
			this.updateUserQrStatus(userItem.getId(), userQrStatus.getCode());
			UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, userQrStatus.getCode());
			userQrHistory.setContent("订单退回，还原码为" + color + "码");
			userQrHistory.setTradeRequestId(tradeRequestId);
			this.insertSelective(userQrHistory);
			return userQrHistory;
		}
		return null;
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
			domain.setIsValid(TFEnum.FALSE.getCode());

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
			query.setIsValid(TFEnum.TRUE.getCode());
			Integer userQrStatus = this.listPageByExample(query).getDatas().stream().findFirst()
					.map(UserQrHistory::getQrStatus).orElse(UserQrStatusEnum.BLACK.getCode());
			this.updateUserQrStatus(userId, userQrStatus);

		}

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
		userQrHistory.setIsValid(TFEnum.TRUE.getCode());
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