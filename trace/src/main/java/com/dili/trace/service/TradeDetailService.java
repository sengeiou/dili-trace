package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.dao.TradeDetailMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.TradeDetailInputDto;
import com.dili.trace.dto.TradeDetailInputWrapperDto;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeDetailStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.service.impl.SeparateSalesRecordServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Autowired
	CheckinOutRecordService checkinOutRecordService;
	@Autowired
	TradeDetailMapper tradeDetailMapper;

	public Long doUpdateSaleStatus(OperatorUser operateUser, Long billId) {

		RegisterBill billItem = this.registerBillService.get(billId);
		if (billItem == null) {
			throw new TraceBusinessException("没有找到登记单");
		}

		TradeDetail queryCondition = new TradeDetail();
		queryCondition.setBillId(billId);
		queryCondition.setTradeType(TradeTypeEnum.NONE.getCode());
		TradeDetail tradeInfoItem = this.listByExample(queryCondition).stream().findFirst().orElse(null);

		if (tradeInfoItem == null) {
			return billItem.getId();
		}
		if (CheckinStatusEnum.ALLOWED.equalsToCode(tradeInfoItem.getCheckinStatus())
				&& BillVerifyStatusEnum.PASSED.equalsToCode(billItem.getVerifyStatus())
				&& SaleStatusEnum.NONE.equalsToCode(tradeInfoItem.getSaleStatus())
				&& CheckoutStatusEnum.NONE.equalsToCode(tradeInfoItem.getCheckoutStatus())) {
			TradeDetail updatableRecord = new TradeDetail();
			updatableRecord.setId(tradeInfoItem.getId());
			updatableRecord.setModified(new Date());

			updatableRecord.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
			this.updateSelective(updatableRecord);
		} else {
			// throw new TraceBusinessException("数据状态错误");
		}

		return billItem.getId();

	}

	public TradeDetail createTradeDetailForBill(RegisterBill registerBill) {

		TradeDetail item = new TradeDetail();
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
		item.setCreated(new Date());
		this.insertSelective(item);

		return item;

	}

	/**
	 * 创建单个交易信息
	 */
	public TradeDetail createTradeDetail(Long tradeRequestId, Long tradeDetailId, BigDecimal tradeWeight, User seller,
			User buyer) {
		TradeDetail tradeDetailItem = this.get(tradeDetailId);
		if (tradeDetailItem == null) {
			throw new TraceBusinessException("数据不存在");
		}
		if (!tradeDetailItem.getBuyerId().equals(seller.getId())) {
			throw new TraceBusinessException("没有权限销售");
		}
		if (!SaleStatusEnum.FOR_SALE.equalsToCode(tradeDetailItem.getSaleStatus())) {
			throw new TraceBusinessException("当前状态不能销售");
		}
		if (tradeDetailItem.getStockWeight().compareTo(tradeWeight) < 0) {
			throw new TraceBusinessException("库存不足不能销售");
		}

		BigDecimal stockWeight = tradeDetailItem.getStockWeight().subtract(tradeWeight);
		tradeDetailItem.setStockWeight(tradeDetailItem.getStockWeight().subtract(tradeWeight));
		this.updateSelective(tradeDetailItem);

		TradeDetail tradeDetail = new TradeDetail();
		tradeDetail.setBatchStockId(tradeDetailItem.getBatchStockId());
		tradeDetail.setBillId(tradeDetailItem.getBillId());

		tradeDetail.setBuyerId(buyer.getId());
		tradeDetail.setBuyerName(buyer.getName());

		tradeDetail.setSellerId(seller.getId());
		tradeDetail.setSellerName(seller.getName());

		tradeDetail.setStockWeight(stockWeight);
		tradeDetail.setTotalWeight(stockWeight);
		tradeDetail.setCheckinRecordId(tradeDetailItem.getCheckinRecordId());
		tradeDetail.setCheckinStatus(tradeDetailItem.getCheckinStatus());
		tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
		tradeDetail.setCheckoutRecordId(null);
		tradeDetail.setCreated(new Date());
		tradeDetail.setModified(new Date());
		tradeDetail.setParentId(tradeDetailItem.getId());
		tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
		tradeDetail.setProductName(tradeDetailItem.getProductName());
		tradeDetail.setTradeType(TradeTypeEnum.SEPARATE_SALES.getCode());
		tradeDetail.setStatus(TradeDetailStatusEnum.NONE.getCode());
		tradeDetail.setWeightUnit(tradeDetailItem.getWeightUnit());
		tradeDetail.setTradeRequestId(tradeRequestId);
		this.insertSelective(tradeDetail);
		return tradeDetail;
	}

	@Transactional
	public List<Long> createTradeList(TradeDetailInputWrapperDto input, Long sellerId) {
		if (input == null || sellerId == null || input.getBuyerId() == null) {
			throw new TraceBusinessException("参数错误");
		}
		User seller=this.userService.get(sellerId);
		User buyer=this.userService.get(input.getBuyerId());
		
		Long buyerId = input.getBuyerId();
		logger.info("seller userid={}", sellerId);
		logger.info("buyer userid={}", buyerId);

		return StreamEx.of(input.getTradeDetailInputList()).nonNull().map(record -> {
			// 理货区分销校验
			logger.info("tradedetail id={}", record.getTradeDetailId());
			logger.info("salesWeight={}", record.getTradeWeight());
			if (record.getTradeWeight() == null || BigDecimal.ZERO.compareTo(record.getTradeWeight()) <= 0) {
				throw new TraceBusinessException("分销重量输入错误");
			}
			Long parentTradeId = record.getTradeDetailId();

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

	/**
	 * 申请退货
	 * 
	 * @param input
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<Long> doReturning(TradeDetailInputWrapperDto input, Long userId) {
		if (input == null || userId == null || input.getTradeDetailInputList().isEmpty()) {
			throw new TraceBusinessException("参数错误");
		}
		List<Long> tradeDetailIdList = StreamEx.of(input.getTradeDetailInputList()).nonNull()
				.map(TradeDetailInputDto::getTradeDetailId).nonNull().map(tradeDetailId -> {
					return this.get(tradeDetailId);
				}).filter(item -> {
					return item.getBuyerId().equals(userId);
				}).filter(item -> {
					return TradeDetailStatusEnum.NONE.equalsToCode(item.getStatus());
				}).map(item -> {

					TradeDetail data = new TradeDetail();
					data.setId(item.getId());
					data.setStatus(TradeDetailStatusEnum.RETURNING.getCode());
					this.updateSelective(data);

					return item.getId();
				}).toList();
		if (tradeDetailIdList.isEmpty()) {
			throw new TraceBusinessException("没有可以进行退货的交易单");
		}
		return tradeDetailIdList;
	}

	/**
	 * 完成退货处理
	 * 
	 * @param input
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<Long> handleReturning(TradeDetailInputWrapperDto input, Long userId) {
		if (input == null || userId == null || input.getTradeDetailInputList().isEmpty() || input.getStatus() == null) {
			throw new TraceBusinessException("参数错误");
		}
		TradeDetailStatusEnum statusEnum = TradeDetailStatusEnum.fromCode(input.getStatus()).orElseThrow(() -> {
			return new TraceBusinessException("参数错误");
		});
		if (TradeDetailStatusEnum.REFUSE == statusEnum || TradeDetailStatusEnum.RETURNED == statusEnum) {
			// donothing
		} else {
			throw new TraceBusinessException("参数错误");
		}

		List<Long> tradeDetailIdList = StreamEx.of(input.getTradeDetailInputList()).nonNull()
				.map(TradeDetailInputDto::getTradeDetailId).nonNull().map(tradeDetailId -> {
					return this.get(tradeDetailId);
				}).filter(item -> {
					return item.getSellerId().equals(userId);
				}).filter(item -> {
					return TradeDetailStatusEnum.RETURNING.equalsToCode(item.getStatus());
				}).map(item -> {

					TradeDetail data = new TradeDetail();
					data.setId(item.getId());
					data.setStatus(statusEnum.getCode());
					this.updateSelective(data);

					return item.getId();
				}).toList();
		if (tradeDetailIdList.isEmpty()) {
			throw new TraceBusinessException("没有可以进行处理的退货申请");
		}
		return tradeDetailIdList;
	}

	/**
	 * 查询用户的登记单类型
	 */
	public BasePage<TradeDetailBillOutput> selectTradeDetailAndBill(RegisterBillDto dto) {
		if (dto.getPage() == null || dto.getPage() < 0) {
			dto.setPage(1);
		}
		if (dto.getRows() == null || dto.getRows() <= 0) {
			dto.setRows(10);
		}
		PageHelper.startPage(dto.getPage(), dto.getRows());
		List<TradeDetailBillOutput> list = this.tradeDetailMapper.selectTradeDetailAndBill(dto);
		Page<TradeDetailBillOutput> page = (Page) list;
		StreamEx.of(page).forEach(o -> {
			if (o.getTradeType() == null) {
				o.setTradeType(TradeTypeEnum.NONE.getCode());
			}

		});
		BasePage<TradeDetailBillOutput> result = new BasePage<TradeDetailBillOutput>();
		result.setDatas(list);
		result.setPage(page.getPageNum());
		result.setRows(page.getPageSize());
		result.setTotalItem(Integer.parseInt(String.valueOf(page.getTotal())));
		result.setTotalPage(page.getPages());
		result.setStartIndex(page.getStartRow());
		return result;

	}

}
