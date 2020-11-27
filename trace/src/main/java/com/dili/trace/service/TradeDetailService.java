package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.dao.TradeDetailMapper;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.TFEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;
import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

/**
 * 交易详情
 */
@Service
public class TradeDetailService extends BaseServiceImpl<TradeDetail, Long> {
	private static final Logger logger = LoggerFactory.getLogger(TradeDetailService.class);

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
	@Autowired
	UserQrHistoryService userQrHistoryService;

	/**
	 * 查询详情信息
	 * @param billId
	 * @return
	 */
	public Optional<TradeDetail> findBilledTradeDetailByBillId(Long billId) {

		// 查找报备相关的TradeDetail信息
		TradeDetail query = new TradeDetail();

		query.setBillId(billId);
		query.setTradeType(TradeTypeEnum.NONE.getCode());

		return StreamEx.of(this.listByExample(query)).findFirst();

	}
	/**
	 * 构造上一级批次号
	 * @param billItem
	 * @return
	 */
	private String buildParentBatchNo(RegisterBill billItem){
		return billItem.getName()+" "+DateUtil.format(billItem.getCreated(), "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 构造上一级批次号
	 * @param tradeDetailItem
	 * @return
	 */
	private String buildParentBatchNo(TradeDetail tradeDetailItem){
		return tradeDetailItem.getBuyerName()+" "+DateUtil.format(tradeDetailItem.getCreated(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 通过报备单创建批次
	 * @param billItem
	 * @return
	 */
	public TradeDetail createTradeDetailForCheckInBill(RegisterBill billItem) {
		TradeDetail item = new TradeDetail();
		Date now=new Date();

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
		item.setBatchNo(this.buildParentBatchNo(billItem));
		item.setParentBatchNo(this.buildParentBatchNo(billItem));
		item.setModified(now);
		item.setCreated(now);
		this.insertSelective(item);
		return item;

	}

	// 1->2 苹果 100 （卖:1->）
	/**
	 * 通过交易行为创建批次，同时更新相应的库存信息
	 */
	public TradeDetail createTradeDetail(Long tradeRequestId, TradeDetail tradeDetailItem, BigDecimal tradeWeight,
			Long sellerId, User buyer, TradeOrderTypeEnum tradeOrderTypeEnum) {
		if (tradeDetailItem == null) {
			throw new TraceBizException("数据不存在");
		}
		RegisterBill billItem = this.registerBillService.get(tradeDetailItem.getBillId());

		logger.info("sellerId:{},buyerId:{},tradeDetail.Id:{},stockweight:{},tradeWeight:{}", sellerId, buyer.getId(),
				tradeDetailItem.getId(), tradeDetailItem.getStockWeight(), tradeWeight);

		if (!tradeDetailItem.getBuyerId().equals(sellerId)) {
			throw new TraceBizException("没有权限销售");
		}
		if (!SaleStatusEnum.FOR_SALE.equalsToCode(tradeDetailItem.getSaleStatus())) {
			throw new TraceBizException("当前状态不能销售");
		}
		if (tradeDetailItem.getStockWeight().compareTo(tradeWeight) < 0) {
			throw new TraceBizException("库存不足不能销售");
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
				tradeRequestId, tradeOrderTypeEnum);

		return buyerTradeDetail;
	}

	/**
	 * 更新卖家库存及批次信息(交易触发)
	 * @param billItem
	 * @param tradeDetailItem
	 * @param tradeWeight
	 * @return
	 */
	TradeDetail updateSellerTradeDetail(RegisterBill billItem, TradeDetail tradeDetailItem, BigDecimal tradeWeight) {
		Long sellerId = tradeDetailItem.getBuyerId();
		BigDecimal stockWeight = tradeDetailItem.getStockWeight().subtract(tradeWeight);
		Long batchStockId = this.batchStockService.findOrCreateBatchStock(sellerId, billItem).getId();
		ProductStock sellerBatchStockItem = this.batchStockService.selectByIdForUpdate(batchStockId).orElseThrow(() -> {
			return new TraceBizException("操作库存失败");
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
	/**
	 * 创建买家的交易批次并(创建)更新相应的库存信息
	 * @param billItem
	 * @param tradeDetailItem
	 * @param tradeWeight
	 * @param buyer
	 * @param tradeRequestId
	 * @return
	 */

	TradeDetail updateBuyerTradeDetail(RegisterBill billItem, TradeDetail tradeDetailItem, BigDecimal tradeWeight,
			User buyer, Long tradeRequestId, TradeOrderTypeEnum tradeOrderTypeEnum) {
		Long buyerBatchStockId = this.batchStockService.findOrCreateBatchStock(buyer.getId(), billItem).getId();
		ProductStock buyerBatchStockItem = this.batchStockService.selectByIdForUpdate(buyerBatchStockId).orElseThrow(() -> {
			return new TraceBizException("操作库存失败");
		});

		Long buyerTradeDetailId = this.createTradeDetailByTrade(tradeDetailItem, buyer);
		TradeDetail buyerTradeDetail = new TradeDetail();
		buyerTradeDetail.setId(buyerTradeDetailId);
		// 卖家下单
		ProductStock buyerBatchStock=new ProductStock();
		buyerBatchStock.setId(buyerBatchStockItem.getId());
		buyerBatchStock.setTotalWeight(buyerBatchStockItem.getTotalWeight().add(tradeWeight));
		if(tradeOrderTypeEnum.getCode().equals(TradeOrderTypeEnum.BUY.getCode()))
		{
			buyerBatchStock.setTradeDetailNum(buyerBatchStockItem.getTradeDetailNum() + 1);
			buyerBatchStock.setStockWeight(buyerBatchStockItem.getStockWeight().add(tradeWeight));
			buyerTradeDetail.setStockWeight(tradeWeight);
			buyerTradeDetail.setIsBatched(TFEnum.TRUE.getCode());
		}
		else
		{
			buyerTradeDetail.setSoftWeight(tradeWeight);
			buyerTradeDetail.setStockWeight(BigDecimal.ZERO);
			buyerTradeDetail.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
			buyerTradeDetail.setIsBatched(TFEnum.FALSE.getCode());
		}

		buyerTradeDetail.setTotalWeight(tradeWeight);
		buyerTradeDetail.setTradeRequestId(tradeRequestId);
		buyerTradeDetail.setProductStockId(buyerBatchStockItem.getId());
		buyerTradeDetail.setIsBatched(TFEnum.TRUE.getCode());

		this.batchStockService.updateSelective(buyerBatchStock);
		this.updateSelective(buyerTradeDetail);
		//更新买家二维码颜色
		// this.userQrHistoryService.createUserQrHistoryForVerifyBill(billItem, buyer.getId());

		return this.get(buyerTradeDetailId);

	}


	/**
	 * 通过卖家批次构造新的买家交易批次
	 * @param tradeDetailItem
	 * @param buyer
	 * @return
	 */
	Long createTradeDetailByTrade(TradeDetail tradeDetailItem, User buyer) {
		TradeDetail buyerTradeDetail = new TradeDetail();
		Date now=new Date();

		buyerTradeDetail.setBatchNo(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
		buyerTradeDetail.setParentBatchNo(this.buildParentBatchNo(tradeDetailItem));

		buyerTradeDetail.setProductStockId(null);
		buyerTradeDetail.setIsBatched(TFEnum.FALSE.getCode());
		buyerTradeDetail.setBillId(tradeDetailItem.getBillId());

		buyerTradeDetail.setBuyerId(buyer.getId());
		buyerTradeDetail.setBuyerName(buyer.getName());

		buyerTradeDetail.setSellerId(tradeDetailItem.getBuyerId());
		buyerTradeDetail.setSellerName(tradeDetailItem.getBuyerName());
		buyerTradeDetail.setSoftWeight(BigDecimal.ZERO);
		buyerTradeDetail.setStockWeight(BigDecimal.ZERO);
		buyerTradeDetail.setTotalWeight(BigDecimal.ZERO);
		buyerTradeDetail.setCheckinRecordId(tradeDetailItem.getCheckinRecordId());
		buyerTradeDetail.setCheckinStatus(tradeDetailItem.getCheckinStatus());
		buyerTradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
		buyerTradeDetail.setCheckoutRecordId(null);
		buyerTradeDetail.setCreated(now);
		buyerTradeDetail.setModified(now);
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
	 * 查询用户的报备单
	 */
	public BasePage<TradeDetailBillOutput> selectTradeDetailAndBill(RegisterBillDto dto) {
		if (dto.getPage() == null || dto.getPage() < 0) {
			dto.setPage(1);
		}
		if (dto.getRows() == null || dto.getRows() <= 0) {
			dto.setRows(10);
		}
		dto.setIsDeleted(TFEnum.FALSE.getCode());
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
		result.setTotalItem(page.getTotal());
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
