package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.dao.TradeDetailMapper;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.service.impl.SeparateSalesRecordServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

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
	@Autowired
	ProductStockService batchStockService;

	public Optional<TradeDetail> findBilledTradeDetailByBillId(Long billId) {

		// 查找报备相关的TradeDetail信息
		TradeDetail query = new TradeDetail();
		query.setBillId(billId);
		query.setTradeType(TradeTypeEnum.NONE.getCode());

		return StreamEx.of(this.listByExample(query)).findFirst();

	}

	public TradeDetail createTradeDetailForCheckInBill(RegisterBill billItem) {
		TradeDetail item = new TradeDetail();
		item.setParentId(null);
		item.setIsBatched(TFEnum.FALSE.getCode());
		item.setBillId(billItem.getId());
		item.setTradeType(TradeTypeEnum.NONE.getCode());
		item.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
		item.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
		item.setSaleStatus(SaleStatusEnum.NONE.getCode());
		item.setStockWeight(billItem.getWeight());
		item.setTotalWeight(billItem.getWeight());
		item.setWeightUnit(billItem.getWeightUnit());
		item.setBuyerId(billItem.getUserId());
		item.setBuyerName(billItem.getName());
		item.setProductName(billItem.getProductName());
		item.setBatchNo(billItem.getCode());
		item.setModified(new Date());
		item.setCreated(new Date());
		this.insertSelective(item);
		return item;

	}

	// 1->2 苹果 100 （卖:1->）
	/**
	 * 创建单个交易信息
	 */
	public TradeDetail createTradeDetail(Long tradeRequestId, TradeDetail tradeDetailItem, BigDecimal tradeWeight,
			Long sellerId, User buyer) {
		if (tradeDetailItem == null) {
			throw new TraceBusinessException("数据不存在");
		}
		RegisterBill billItem = this.registerBillService.get(tradeDetailItem.getBillId());

		logger.info("sellerId:{},buyerId:{},tradeDetail.Id:{},stockweight:{},tradeWeight:{}", sellerId, buyer.getId(),
				tradeDetailItem.getId(), tradeDetailItem.getStockWeight(), tradeWeight);

		if (!tradeDetailItem.getBuyerId().equals(sellerId)) {
			throw new TraceBusinessException("没有权限销售");
		}
		if (!SaleStatusEnum.FOR_SALE.equalsToCode(tradeDetailItem.getSaleStatus())) {
			throw new TraceBusinessException("当前状态不能销售");
		}
		if (tradeDetailItem.getStockWeight().compareTo(tradeWeight) < 0) {
			throw new TraceBusinessException("库存不足不能销售");
		}

		// BigDecimal stockWeight =
		// tradeDetailItem.getStockWeight().subtract(tradeWeight);

		// BatchStock sellerBatchStock =
		// this.batchStockService.findOrCreateBatchStock(sellerId, billItem);
		// sellerBatchStock.setStockWeight(sellerBatchStock.getStockWeight().subtract(tradeWeight));
		// this.batchStockService.updateSelective(sellerBatchStock);

		// TradeDetail sellerTradeDetail = new TradeDetail();
		// sellerTradeDetail.setId(tradeDetailItem.getId());
		// sellerTradeDetail.setStockWeight(stockWeight);
		// this.updateSelective(sellerTradeDetail);
		this.updateSellerTradeDetail(billItem, tradeDetailItem, tradeWeight);

		// BatchStock buyerBatchStock =
		// this.batchStockService.findOrCreateBatchStock(buyer.getId(), billItem);
		// buyerBatchStock.setStockWeight(buyerBatchStock.getStockWeight().add(tradeWeight));
		// this.batchStockService.updateSelective(buyerBatchStock);

		// Long buyerTradeDetailId = this.createTradeDetailByTrade(tradeDetailItem,
		// buyer);
		// TradeDetail buyerTradeDetail = new TradeDetail();
		// buyerTradeDetail.setId(buyerTradeDetailId);
		// buyerTradeDetail.setStockWeight(tradeWeight);
		// buyerTradeDetail.setTotalWeight(tradeWeight);
		// buyerTradeDetail.setTradeRequestId(tradeRequestId);

		// buyerTradeDetail.setBatchStockId(buyerBatchStock.getId());
		// buyerTradeDetail.setIsBatched(TFEnum.TRUE.getCode());
		// this.updateSelective(buyerTradeDetail);
		TradeDetail buyerTradeDetail = this.updateBuyerTradeDetail(billItem, tradeDetailItem, tradeWeight, buyer,
				tradeRequestId);

		return buyerTradeDetail;
	}

	TradeDetail updateSellerTradeDetail(RegisterBill billItem, TradeDetail tradeDetailItem, BigDecimal tradeWeight) {
		Long sellerId = tradeDetailItem.getBuyerId();
		BigDecimal stockWeight = tradeDetailItem.getStockWeight().subtract(tradeWeight);
		Long batchStockId = this.batchStockService.findOrCreateBatchStock(sellerId, billItem).getId();
		ProductStock sellerBatchStockItem = this.batchStockService.selectByIdForUpdate(batchStockId).orElseThrow(() -> {
			return new TraceBusinessException("操作库存失败");
		});
		TradeDetail sellerTradeDetail = new TradeDetail();

		ProductStock sellerBatchStock=new ProductStock();
		sellerBatchStock.setId(batchStockId);
		sellerBatchStock.setStockWeight(sellerBatchStockItem.getStockWeight().subtract(tradeWeight));
		if (stockWeight.compareTo(BigDecimal.ZERO) <= 0) {
			sellerBatchStock.setTradeDetailNum(sellerBatchStockItem.getTradeDetailNum() - 1);
			sellerTradeDetail.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
			sellerTradeDetail.setIsBatched(TFEnum.FALSE.getCode());
		}
		this.batchStockService.updateSelective(sellerBatchStock);

		sellerTradeDetail.setId(tradeDetailItem.getId());
		sellerTradeDetail.setStockWeight(stockWeight);

		this.updateSelective(sellerTradeDetail);
		return this.get(sellerTradeDetail.getId());

	}

	TradeDetail updateBuyerTradeDetail(RegisterBill billItem, TradeDetail tradeDetailItem, BigDecimal tradeWeight,
			User buyer, Long tradeRequestId) {
		Long buyerBatchStockId = this.batchStockService.findOrCreateBatchStock(buyer.getId(), billItem).getId();
		ProductStock buyerBatchStock = this.batchStockService.selectByIdForUpdate(buyerBatchStockId).orElseThrow(() -> {
			return new TraceBusinessException("操作库存失败");
		});
		buyerBatchStock.setStockWeight(buyerBatchStock.getStockWeight().add(tradeWeight));
		buyerBatchStock.setTradeDetailNum(buyerBatchStock.getTradeDetailNum() + 1);
		this.batchStockService.updateSelective(buyerBatchStock);

		Long buyerTradeDetailId = this.createTradeDetailByTrade(tradeDetailItem, buyer);
		TradeDetail buyerTradeDetail = new TradeDetail();
		buyerTradeDetail.setId(buyerTradeDetailId);
		buyerTradeDetail.setStockWeight(tradeWeight);
		buyerTradeDetail.setTotalWeight(tradeWeight);
		buyerTradeDetail.setTradeRequestId(tradeRequestId);

		buyerTradeDetail.setProductStockId(buyerBatchStock.getId());
		buyerTradeDetail.setIsBatched(TFEnum.TRUE.getCode());
		this.updateSelective(buyerTradeDetail);
		return this.get(buyerTradeDetailId);

	}

	Long createTradeDetailByTrade(TradeDetail tradeDetailItem, User buyer) {
		TradeDetail buyerTradeDetail = new TradeDetail();
		buyerTradeDetail.setBatchNo(tradeDetailItem.getBatchNo());
		buyerTradeDetail.setProductStockId(null);
		buyerTradeDetail.setIsBatched(TFEnum.FALSE.getCode());
		buyerTradeDetail.setBillId(tradeDetailItem.getBillId());

		buyerTradeDetail.setBuyerId(buyer.getId());
		buyerTradeDetail.setBuyerName(buyer.getName());

		buyerTradeDetail.setSellerId(tradeDetailItem.getBuyerId());
		buyerTradeDetail.setSellerName(tradeDetailItem.getBuyerName());

		buyerTradeDetail.setStockWeight(BigDecimal.ZERO);
		buyerTradeDetail.setTotalWeight(BigDecimal.ZERO);
		buyerTradeDetail.setCheckinRecordId(tradeDetailItem.getCheckinRecordId());
		buyerTradeDetail.setCheckinStatus(tradeDetailItem.getCheckinStatus());
		buyerTradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
		buyerTradeDetail.setCheckoutRecordId(null);
		buyerTradeDetail.setCreated(new Date());
		buyerTradeDetail.setModified(new Date());
		buyerTradeDetail.setParentId(tradeDetailItem.getId());
		buyerTradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
		buyerTradeDetail.setProductName(tradeDetailItem.getProductName());
		buyerTradeDetail.setTradeType(TradeTypeEnum.SEPARATE_SALES.getCode());
		// tradeDetail.setStatus(TradeDetailStatusEnum.NONE.getCode());
		buyerTradeDetail.setWeightUnit(tradeDetailItem.getWeightUnit());
		buyerTradeDetail.setTradeRequestId(null);
		this.insertSelective(buyerTradeDetail);

		return buyerTradeDetail.getId();
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

	public List<TradeDetail> findTradeDetailByIdList(List<Long> idList) {
		if (idList == null || idList.isEmpty()) {
			return Lists.newArrayList();
		}
		Example e = new Example(TradeDetail.class);
		e.and().andIn("id", idList);
		return this.getDao().selectByExample(e);

	}

	public List<TradeDetail> findTradeDetailByParentIdList(List<Long> parentIdList) {
		if (parentIdList == null || parentIdList.isEmpty()) {
			return Lists.newArrayList();
		}
		Example e = new Example(TradeDetail.class);
		e.and().andIn("parentId", parentIdList);
		return this.getDao().selectByExample(e);

	}

}
