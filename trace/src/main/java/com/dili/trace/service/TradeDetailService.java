package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.TradeDetailInputWrapperDto;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.CheckoutStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.service.impl.SeparateSalesRecordServiceImpl;

import one.util.streamex.StreamEx;

@Service
public class TradeDetailService extends BaseServiceImpl<TradeDetail, Long> {
	private static final Logger logger = LoggerFactory.getLogger(SeparateSalesRecordServiceImpl.class);

	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	UserService userService;
	@Autowired
	UpStreamService upStreamService;

	@Transactional
	public TradeDetail createTradeInfoForBill(Long billId) {

		RegisterBill registerBill = this.registerBillService.get(billId);

		TradeDetail queryCondition = new TradeDetail();
		queryCondition.setBillId(billId);
		queryCondition.setTradeType(TradeTypeEnum.NONE.getCode());

		TradeDetail item = this.listByExample(queryCondition).stream().findFirst().orElse(new TradeDetail());

		item.setParentId(null);
		item.setBillId(registerBill.getId());
		item.setTradeType(TradeTypeEnum.NONE.getCode());
		item.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
		item.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
		item.setSaleStatus(SaleStatusEnum.NONE.getCode());
		item.setStockWeight(registerBill.getWeight());
		item.setTotalWeight(registerBill.getWeight());
		item.setWeightUnit(registerBill.getWeightUnit());
		item.setBuyerId(registerBill.getUserId());
		item.setBuyerName(registerBill.getName());

		item.setModified(new Date());
		if (item.getId() == null) {
			item.setCreated(new Date());
			this.insertSelective(item);
		} else {

			this.updateSelective(item);
		}
		return item;
	}

	@Transactional
	public List<Long> createTradeList(TradeDetailInputWrapperDto input, Long sellerId) {
		if (input == null || sellerId == null || input.getBuyerId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		Long buyerId = input.getBuyerId();
		logger.info("seller userid={}", sellerId);
		logger.info("buyer userid={}", buyerId);

		return StreamEx.of(input.getTradeDetailInputList()).nonNull().map(record -> {
			// 理货区分销校验
			logger.info("parentId={}", record.getParentId());
			logger.info("salesWeight={}", record.getTradeWeight());
			if (record.getTradeWeight() == null || BigDecimal.ZERO.compareTo(record.getTradeWeight()) <= 0) {
				throw new TraceBusinessException("分销重量输入错误");
			}
			Long parentTradeId = record.getParentId();

			if (parentTradeId == null) {
				throw new TraceBusinessException("没有需要分销的登记单");
			}

			TradeDetail parentTradeInfo = this.get(parentTradeId);
			if (parentTradeInfo == null) {
				throw new TraceBusinessException("没有需要分销的数据");
			}

			if (!SaleStatusEnum.FOR_SALE.equalsToCode(parentTradeInfo.getSaleStatus())) {
				throw new TraceBusinessException("当前状态不能进行分销");
			}

			if (!parentTradeInfo.getBuyerId().equals(sellerId)) {
				throw new TraceBusinessException("没有权限分销");
			}
			BigDecimal tradeWeight = record.getTradeWeight();
			BigDecimal stockWeight = parentTradeInfo.getStockWeight();
			logger.info(">>>inventoryWeight={},tradeWeight={}", stockWeight, tradeWeight);
			if (stockWeight.compareTo(tradeWeight) < 0) {
				throw new TraceBusinessException("分销重量超过可分销重量");
			}
			logger.info("扫码分销，判断是否增加上游：salesUserId:{}", buyerId);
			User buyerUserItem = this.userService.get(buyerId);
			if (buyerUserItem == null) {
				throw new TraceBusinessException("买家用户不存在");
			}
			boolean hasUpStream = this.upStreamService.queryUpStreamByUserId(buyerId).stream()
					.anyMatch(up -> buyerUserItem.getId().equals(up.getSourceUserId()));

			// 扫码分销
			if (!hasUpStream) {
				UpStream upStream = this.upStreamService.queryUpStreamBySourceUserId(buyerUserItem.getId());
				UpStreamDto upStreamDto = new UpStreamDto();
				if (upStream == null) {
					if (UserTypeEnum.USER.getCode().equals(buyerUserItem.getUserType())) {
						upStreamDto.setUpstreamType(UpStreamTypeEnum.USER.getCode());
					} else {
						upStreamDto.setUpstreamType(UpStreamTypeEnum.CORPORATE.getCode());
					}

					upStreamDto.setIdCard(buyerUserItem.getCardNo());
					upStreamDto.setManufacturingLicenseUrl(buyerUserItem.getManufacturingLicenseUrl());
					upStreamDto.setOperationLicenseUrl(buyerUserItem.getOperationLicenseUrl());
					upStreamDto.setCardNoFrontUrl(buyerUserItem.getCardNoFrontUrl());
					upStreamDto.setCardNoBackUrl(buyerUserItem.getCardNoBackUrl());
					upStreamDto.setName(buyerUserItem.getName());
					upStreamDto.setSourceUserId(buyerUserItem.getId());
					upStreamDto.setLicense(buyerUserItem.getLicense());
					upStreamDto.setTelphone(buyerUserItem.getPhone());
					upStreamDto.setBusinessLicenseUrl(buyerUserItem.getBusinessLicenseUrl());
					upStreamDto.setUserIds(Arrays.asList(buyerId));
					upStreamDto.setCreated(new Date());
					upStreamDto.setModified(new Date());
					this.upStreamService.addUpstream(upStreamDto,
							new OperatorUser(buyerUserItem.getId(), buyerUserItem.getName()));
				} else {
					upStreamDto.setUserIds(Arrays.asList(buyerId));
					upStreamDto.setId(upStream.getId());
					this.upStreamService.addUpstreamUsers(upStreamDto,
							new OperatorUser(buyerUserItem.getId(), buyerUserItem.getName()));
				}

			}

			logger.info(">>>change tradeId:{} salesWeiht:{} as: {}", parentTradeInfo.getId(),
					parentTradeInfo.getStockWeight(), stockWeight.subtract(tradeWeight).intValue());

			// 更新被分销记录的剩余重量
			TradeDetail updatableRecord = new TradeDetail();
			updatableRecord.setStockWeight(stockWeight.subtract(tradeWeight));
			updatableRecord.setModified(new Date());
			updatableRecord.setId(parentTradeInfo.getId());
			this.updateSelective(updatableRecord);

			TradeDetail insertRecord = new TradeDetail();

			insertRecord.setBillId(parentTradeInfo.getBillId());
			insertRecord.setCreated(new Date());
			insertRecord.setModified(new Date());
			insertRecord.setStockWeight(tradeWeight);
			insertRecord.setTotalWeight(tradeWeight);

			insertRecord.setCheckinRecordId(parentTradeInfo.getCheckinRecordId());
			insertRecord.setTradeType(TradeTypeEnum.SEPARATE_SALES.getCode());
			this.saveOrUpdate(insertRecord);

			return insertRecord.getId();
		}).toList();
	}
}
