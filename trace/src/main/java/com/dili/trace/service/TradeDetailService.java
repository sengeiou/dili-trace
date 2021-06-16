package com.dili.trace.service;

import cn.hutool.core.date.DateUtil;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.dao.TradeDetailMapper;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.TradeDto;
import com.dili.trace.dto.ret.TradeDetailRetDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 交易详情
 */
@Service
public class TradeDetailService extends BaseServiceImpl<TradeDetail, Long> {
    private static final Logger logger = LoggerFactory.getLogger(TradeDetailService.class);

    @Autowired
    RegisterBillService registerBillService;
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
     *
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
     *
     * @param billItem
     * @return
     */
    public String buildParentBatchNo(RegisterBill billItem) {
        return billItem.getName() + " " + DateUtil.format(billItem.getCreated(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 构造上一级批次号
     *
     * @param tradeDetailItem
     * @return
     */
    public String buildParentBatchNo(TradeDetail tradeDetailItem) {
        return tradeDetailItem.getBuyerName() + " " + DateUtil.format(tradeDetailItem.getCreated(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 更新卖家库存及批次信息(交易触发)
     *
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

        ProductStock sellerBatchStock = new ProductStock();
        sellerBatchStock.setId(batchStockId);
        sellerBatchStock.setStockWeight(sellerBatchStockItem.getStockWeight().subtract(tradeWeight));
        if (stockWeight.compareTo(BigDecimal.ZERO) <= 0) {
            sellerBatchStock.setTradeDetailNum(sellerBatchStockItem.getTradeDetailNum() - 1);
            sellerTradeDetail.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
            sellerTradeDetail.setIsBatched(YesOrNoEnum.NO.getCode());
        }
        this.batchStockService.updateSelective(sellerBatchStock);

        sellerTradeDetail.setId(tradeDetailItem.getId());
        sellerTradeDetail.setStockWeight(stockWeight);

        this.updateSelective(sellerTradeDetail);
        return this.get(sellerTradeDetail.getId());

    }



    /**
     * 通过卖家批次构造新的买家交易批次
     *
     * @param tradeDetailItem
     * @param buyer
     * @return
     */
    Long createTradeDetailByTrade(TradeDetail tradeDetailItem, TradeDto.Buyer buyer) {
        TradeDetail buyerTradeDetail = new TradeDetail();
        Date now = new Date();

        buyerTradeDetail.setBatchNo(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
        buyerTradeDetail.setParentBatchNo(this.buildParentBatchNo(tradeDetailItem));

        buyerTradeDetail.setProductStockId(null);
        buyerTradeDetail.setIsBatched(YesOrNoEnum.NO.getCode());
        buyerTradeDetail.setBillId(tradeDetailItem.getBillId());

        buyerTradeDetail.setBuyerId(buyer.getBuyerId());
        buyerTradeDetail.setBuyerName(buyer.getBuyerName());
//		buyerTradeDetail.setBuyerType(buyer.getBuyerType().getCode());

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
        dto.setIsDeleted(YesOrNoEnum.NO.getCode());
        dto.setBillTypes(Arrays.asList(BillTypeEnum.REGISTER_BILL.getCode()));
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

    /**
     * 根据id集合查询
     *
     * @param idList
     * @return
     */
    public List<TradeDetail> findTradeDetailByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(TradeDetail.class);
        e.and().andIn("id", idList);
        return this.getDao().selectByExample(e);

    }

    /**
     * 根据父级id查询
     *
     * @param parentIdList
     * @return
     */
    public List<TradeDetail> findTradeDetailByParentIdList(List<Long> parentIdList) {
        if (parentIdList == null || parentIdList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(TradeDetail.class);
        e.and().andIn("parentId", parentIdList);
        return this.getDao().selectByExample(e);

    }

    /**
     * 分组查询统计重量及检测结果
     *
     * @param productStockId
     * @return
     */
    public List<TradeDetailRetDto> groupSumWeightByProductStockId(Long productStockId) {
        if (productStockId == null) {
            return Lists.newArrayList();
        }
        TradeDetail q = new TradeDetail();
        q.setProductStockId(productStockId);
        return this.tradeDetailMapper.groupSumWeightByProductStockId(q);
    }

}
