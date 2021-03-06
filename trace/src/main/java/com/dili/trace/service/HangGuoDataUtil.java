package com.dili.trace.service;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.hangguo.*;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.hanguo.HangGuoGoodsLevelEnum;
import com.dili.trace.jobs.HangGuoTraceabilityDataJob;
import com.dili.trace.rpc.service.UidRestfulRpcService;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class HangGuoDataUtil {
    private static final Logger logger = LoggerFactory.getLogger(HangGuoDataUtil.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtService userExtService;
    @Autowired
    private HangGuoDataService hangGuoDataService;
    @Autowired
    private UidRestfulRpcService uidRestfulRpcService;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private TradeRequestService tradeRequestService;
    @Autowired
    private TradeDetailService tradeDetailService;
    @Autowired
    private ProductStockService productStockService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    @Value("${thrid.insert.batch.size}")
    private Integer insertBatchSize;
    //private static final String TRADE_REQUEST_CODE_TYPE = "TRADE_REQUEST_CODE";
    /**
     * ??????????????????
     */
    private Integer reportMaxAmountInt = 300000;
    /**
     * ????????????code
     */
    private String HGMarketCode = MarketEnum.HZSG.getCode();
    /**
     * ????????????????????????id????????????????????????????????????
     */
    private Long HGMarketId = HangGuoTraceabilityDataJob.HGMarketId;

    /**
     * ????????????
     */
    public void createCommodity(List<HangGuoCommodity> commodityList, Date createTime) {
        if (CollectionUtils.isEmpty(commodityList)) {
            logger.info("??????????????????????????????");
            return;
        }
        StreamEx.of(commodityList).nonNull().forEach(c -> {
            c.setFirstCateg(c.getFirstCateg().trim());
            c.setSecondCateg(c.getSecondCateg().trim());
            c.setItemName(c.getItemName().trim());
            c.setItemNumber(c.getItemNumber().trim());
            c.setItemUnitName(c.getItemUnitName() == null ? null : c.getItemUnitName().trim());
        });
        List<String> list = StreamEx.of(commodityList).nonNull().map(c -> c.getItemNumber()).collect(Collectors.toList());
        List<Category> existsCateGoryList = getCategoryListByThirdCode(list);

        Set<String> existsCodeSet = new HashSet<>();
        StreamEx.of(existsCateGoryList).nonNull().forEach(u -> {
            existsCodeSet.add(u.getCode());
        });
        List<HangGuoCommodity> updateHangGuoCommodityList = new ArrayList<>();
        if (!existsCodeSet.isEmpty()) {
            updateHangGuoCommodityList = StreamEx.of(commodityList).nonNull().filter(trade -> existsCodeSet.contains(trade.getItemNumber())).collect(Collectors.toList());
        }

        if (CollectionUtils.isNotEmpty(updateHangGuoCommodityList)) {
            commodityList.removeAll(updateHangGuoCommodityList);
        }
        if (CollectionUtils.isNotEmpty(commodityList)) {
            addCateGory(commodityList, createTime);
        }
        if (CollectionUtils.isNotEmpty(updateHangGuoCommodityList)) {
            updateCateGoryByThirdCode(updateHangGuoCommodityList, createTime);
        }
    }

    /**
     * @return
     */
    private Map<String, UserInfo> getUserMap() {
        UserInfo u = new UserInfo();
        u.setMarketId(HGMarketId);
        u.setYn(YesOrNoEnum.YES.getCode());
        List<UserInfo> userList = userService.listByExample(u);
        return StreamEx.of(userList).nonNull().collect(Collectors.toMap(us -> us.getThirdPartyCode(), user -> user, (a, b) -> a));
    }


    /**
     * ????????????
     *
     * @param tradeList
     * @param createTime
     */
    public void createTrade(List<HangGuoTrade> tradeList, Date createTime) {
        if (CollectionUtils.isEmpty(tradeList)) {
            logger.info("??????????????????????????????");
            return;
        }

        //?????????????????????
        Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
        // ?????????
        Integer part = tradeList.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? tradeList.size() : (i + 1) * batchSize;
            List<HangGuoTrade> partBills = tradeList.subList(i * batchSize, endPos);

            StreamEx.of(partBills).nonNull().forEach(p -> {
                p.setRegisterNo(p.getRegisterNo() == null ? null : p.getRegisterNo().trim());
            });
            hangGuoDataService.batchInsertCacheTradeList(partBills);
        }
        //patch?????????
        updateCacheTradeReportFlag(createTime);
        //????????????
        addTradeList(createTime);
        logger.info("============>>> ???????????????????????????" + (DateUtils.getCurrentDate().getTime() - createTime.getTime()));
    }

    /**
     * ???????????????
     *
     * @param createTime
     */
    private void addTradeList(Date createTime) {

        List<HangGuoTrade> tradeList = getPendingHandleTradeList();
        if (CollectionUtils.isEmpty(tradeList)) {
            logger.info("???????????????????????????");
            return;
        }
        //?????????????????????
        Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
        // ?????????
        Integer part = tradeList.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? tradeList.size() : (i + 1) * batchSize;
            List<HangGuoTrade> partBills = tradeList.subList(i * batchSize, endPos);
            doPartTrade(partBills, createTime);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param tradeList
     * @param createTime
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void doPartTrade(List<HangGuoTrade> tradeList, Date createTime) {
        List<String> userCode = StreamEx.of(tradeList).map(t -> t.getSupplierNo()).collect(Collectors.toList());
        List<String> commodityCode = new ArrayList<>();
        List<String> billNos = new ArrayList<>();
        for (HangGuoTrade t : tradeList) {
            if (t != null) {
                userCode.add(t.getMemberNo());
                commodityCode.add(t.getItemNumber());
                if (StringUtils.isNotBlank(t.getRegisterNo())) {
                    billNos.add(t.getRegisterNo());
                }
            }
        }
        //???????????????
        userCode = StreamEx.of(userCode).nonNull().distinct().collect(Collectors.toList());
        commodityCode = StreamEx.of(commodityCode).nonNull().distinct().collect(Collectors.toList());
        billNos = StreamEx.of(billNos).nonNull().distinct().collect(Collectors.toList());
        List<UserInfo> userList = getUserListByThirdPartyCode(userCode);
        List<Category> categoryList = getCategoryListByThirdCode(commodityCode);
        if (CollectionUtils.isEmpty(userList) || CollectionUtils.isEmpty(categoryList)) {
            logger.info("????????????????????????????????????????????????,????????????,????????????????????????");
            return;
        }
        Map<String, UserInfo> userMap = StreamEx.of(userList).nonNull().collect(Collectors.toMap(UserInfo::getThirdPartyCode, user -> user, (key1, key2) -> key1));
        Map<String, Category> categoryMap = StreamEx.of(categoryList).nonNull().collect(Collectors.toMap(Category::getCode, category -> category, (key1, key2) -> key1));

        //???????????????map
        Map<Long, TradeDetail> billMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(billNos)) {
            billMap = getTradeBillMap(billNos);
        }
        //????????????
        List<TradeOrder> addTradeList = getTradeOrderList(tradeList, userMap, createTime);
        if (CollectionUtils.isNotEmpty(addTradeList)) {
            tradeOrderService.batchInsert(addTradeList);
        }
        Map<String, TradeOrder> orderMap = StreamEx.of(addTradeList).nonNull().collect(Collectors.toMap(TradeOrder::getTradeNo, Function.identity(), (key1, key2) -> key1));

        //??????request
        List<TradeRequest> addTradeRequestList = getTradeOrderRequestList(tradeList, userMap, categoryMap, orderMap, billMap, createTime);
        if (CollectionUtils.isNotEmpty(addTradeRequestList)) {
            tradeRequestService.batchInsert(addTradeRequestList);
        }
        Map<String, TradeRequest> tradeRequestMap = StreamEx.of(addTradeRequestList).nonNull().collect(Collectors.toMap(TradeRequest::getThirdTradeNo, Function.identity(), (a, b) -> a));
        //??????detail
        List<TradeDetail> addDetailList = getTradeOrderDetailRequestList(tradeList, userMap, categoryMap, billMap, tradeRequestMap, createTime);
        if (CollectionUtils.isNotEmpty(addDetailList)) {
            hangGuoDataService.batchInsertTradeDetail(addDetailList);
        }

        //???????????????
        updateCacheTradeHandleFlag(DataHandleFlagEnum.PROCESSED.getCode(), tradeList);
    }

    /**
     * @param handleFlag
     * @param tradeList
     */
    private void updateCacheTradeHandleFlag(Integer handleFlag, List<HangGuoTrade> tradeList) {
        if (CollectionUtils.isNotEmpty(tradeList)) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", tradeList);
            map.put("handleFlag", handleFlag);
            hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
        }
    }


    /**
     * ?????????????????????
     *
     * @param tradeList
     * @param userMap
     * @param categoryMap
     * @param billMap
     * @param tradeRequestMap
     * @param createTime
     * @return
     */
    private List<TradeDetail> getTradeOrderDetailRequestList(List<HangGuoTrade> tradeList, Map<String, UserInfo> userMap, Map<String, Category> categoryMap,
                                                             Map<Long, TradeDetail> billMap, Map<String, TradeRequest> tradeRequestMap, Date createTime) {
        List<TradeDetail> detailList = new ArrayList<>();
        Integer isBatched = 1;
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            Long parentId = null;
            Long checkInId = null;
            Long billId = Long.valueOf(-1);
            if (null != billMap && !billMap.isEmpty()) {
                TradeDetail detail = billMap.get(t.getRegisterId());
                if (null != detail) {
                    parentId = detail.getId();
                    checkInId = detail.getCheckinRecordId();
                    billId = detail.getBillId();
                }
            }

            Long buyId = userMap.get(t.getMemberNo().trim()).getId();
            String buyName = userMap.get(t.getMemberNo().trim()).getName();
            Long sellerId = userMap.get(t.getSupplierNo().trim()).getId();
            String sellerName = userMap.get(t.getSupplierNo().trim()).getName();
            String productName = categoryMap.get(t.getItemNumber().trim()).getName();
            BigDecimal weight = new BigDecimal(t.getWeight());
            String nowDate = DateUtils.format(DateUtils.getCurrentDate());
            Long tradeRequestId = tradeRequestMap.get(t.getTradeNo().trim()).getId();

            TradeDetail tradeDetail = new TradeDetail();
            tradeDetail.setCheckinRecordId(checkInId);

            tradeDetail.setBillId(billId);

            tradeDetail.setParentId(parentId);
            tradeDetail.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
            tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
            tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            tradeDetail.setTradeType(TradeTypeEnum.SEPARATE_SALES.getCode());
            tradeDetail.setBuyerId(buyId);
            tradeDetail.setBuyerName(buyName);
            tradeDetail.setSellerId(sellerId);
            tradeDetail.setSellerName(sellerName);
            tradeDetail.setProductName(productName);
            tradeDetail.setStockWeight(weight);
            tradeDetail.setTotalWeight(weight);
            tradeDetail.setWeightUnit(WeightUnitEnum.KILO.getCode());

            tradeDetail.setTradeRequestId(tradeRequestId);

            tradeDetail.setCreated(createTime);
            tradeDetail.setModified(createTime);
            tradeDetail.setIsBatched(isBatched);
            tradeDetail.setBatchNo(nowDate);
            tradeDetail.setParentBatchNo(nowDate);

            detailList.add(tradeDetail);
        });
        return detailList;
    }

    /**
     * @param addSize
     * @param now
     * @return
     */
    private Map<String, String> generNextCode(int addSize, LocalDateTime now) {
        int tradeRequestSize = 5;
        String preKey = "prefix";
        String seqKey = "seq";
        CodeGenerate codeGenerate = new CodeGenerate();
        //codeGenerate.setType(TRADE_REQUEST_CODE_TYPE);
        //List<CodeGenerate> codeGenerateList = codeGenerateService.listByExample(codeGenerate);
        List<CodeGenerate> codeGenerateList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(codeGenerateList)) {
            codeGenerate = codeGenerateList.get(0);
        }
        if (codeGenerate == null) {
            throw new TraceBizException("??????????????????");
        }
        String nextSegment = now.format(DateTimeFormatter.ofPattern(codeGenerate.getPattern()));
        if (!nextSegment.equals(codeGenerate.getSegment())) {
            codeGenerate.setSeq(1L);
            codeGenerate.setSegment(nextSegment);
            codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            codeGenerate.setSeq(codeGenerate.getSeq() + addSize);
            codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        }
        //codeGenerateService.updateSelective(codeGenerate);
        Map<String, String> map = new HashMap<>(16);
        map.put(preKey, StringUtils.trimToEmpty(codeGenerate.getPrefix()).concat(nextSegment));
        map.put(seqKey, String.valueOf(codeGenerate.getSeq()));
        return map;
    }

    /**
     * ??????????????????
     *
     * @param tradeList
     * @param userMap
     * @param categoryMap
     * @param orderMap
     * @param billMap
     * @param createTime
     * @return
     */
    private List<TradeRequest> getTradeOrderRequestList(List<HangGuoTrade> tradeList, Map<String, UserInfo> userMap, Map<String, Category> categoryMap, Map<String, TradeOrder> orderMap, Map<Long, TradeDetail> billMap, Date createTime) {
        List<TradeRequest> requestList = new ArrayList<>();
        BigDecimal reportMaxAmount = new BigDecimal(reportMaxAmountInt);
        //createTime
        LocalDateTime dateTime = Instant.ofEpochMilli(createTime.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        String preKey = "prefix";
        String seqKey = "seq";
        Map<String, String> map = generNextCode(tradeList.size(), dateTime);
        String codePrefix = map.get(preKey);
        String codeSeq = map.get(seqKey);
        AtomicInteger index = new AtomicInteger(0);
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            int currentIndex = index.getAndIncrement();
            String code = codePrefix;
            Integer codeSeqInt = Integer.valueOf(codeSeq) + currentIndex;
            code = code.concat(String.valueOf(codeSeqInt));
            logger.info("===>??????????????????" + code);
            Long buyId = userMap.get(t.getMemberNo().trim()).getId();
            String buyName = userMap.get(t.getMemberNo().trim()).getName();
            Long sellerId = userMap.get(t.getSupplierNo().trim()).getId();
            String sellerName = userMap.get(t.getSupplierNo().trim()).getName();
            Long orderId = orderMap.get(t.getTradeNo().trim()).getId();
            Long buyMarketId = userMap.get(t.getMemberNo().trim()).getMarketId();
            Long sellerMarketId = userMap.get(t.getSupplierNo().trim()).getMarketId();
            Long productStockId = null;
            if (null != billMap && !billMap.isEmpty() && StringUtils.isNotBlank(t.getRegisterId())) {
                if (null != billMap.get(t.getRegisterId())) {
                    productStockId = billMap.get(t.getRegisterId()).getProductStockId();
                }
            }
            String productName = categoryMap.get(t.getItemNumber().trim()).getName();
            TradeRequest request = new TradeRequest();
            request.setBuyerId(buyId);
            request.setBuyerName(buyName);
            request.setSellerId(sellerId);
            request.setSellerName(sellerName);
            request.setTradeWeight(new BigDecimal(t.getWeight()));
            request.setTradeOrderId(orderId);
            request.setProductStockId(productStockId);
            request.setCreated(createTime);
            request.setModified(createTime);
            request.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());

            request.setWeightUnit(WeightUnitEnum.KILO.getCode());
            request.setProductName(productName);
            request.setHandleTime(createTime);
            request.setThirdTradeNo(t.getTradeNo());
            request.setBatchNo(t.getBatchNo());
            request.setOriginName(t.getOriginName());
            request.setPositionNo(t.getPositionNo());
            request.setPositionName(t.getPositionName());
            request.setPrice(t.getPrice());
            request.setPackageNumber(t.getPackageNumber());
            request.setNumber(t.getNumber());
            request.setAmount(t.getAmount());
            request.setPosNo(t.getPosNo());
            request.setPayWay(t.getPayWay());
            request.setTotalAmount(t.getTotalAmount());
            request.setOperator(t.getOperator());
            request.setPayer(t.getPayer());
            request.setPayNo(t.getPayNo());
            request.setSourceType(TradeRequestSourceTypeEnum.THIRD_HANGGUO.getCode());
            if (null != t.getAmount() && t.getAmount().compareTo(reportMaxAmount) >= 0) {
                request.setReportFlag(CheckOrderReportFlagEnum.UNTREATED.getCode());
            } else {
                request.setReportFlag(CheckOrderReportFlagEnum.PROCESSED.getCode());
            }
            request.setCode(code);
            requestList.add(request);
        });
        return requestList;
    }


    /**
     * ??????????????????
     *
     * @param tradeList
     * @param userMap
     * @param createTime
     * @return
     */
    private List<TradeOrder> getTradeOrderList(List<HangGuoTrade> tradeList, Map<String, UserInfo> userMap, Date createTime) {
        List<TradeOrder> orderList = new ArrayList<>();
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            Long buyId = userMap.get(t.getMemberNo().trim()).getId();
            String buyName = userMap.get(t.getMemberNo().trim()).getName();
            Long buyMarketId = userMap.get(t.getMemberNo().trim()).getMarketId();
            Long sellerId = userMap.get(t.getSupplierNo().trim()).getId();
            String sellerName = userMap.get(t.getSupplierNo().trim()).getName();
            Long sellerMarketId = userMap.get(t.getSupplierNo().trim()).getMarketId();
            TradeOrder order = new TradeOrder();
            order.setBuyerId(buyId);
            order.setBuyerName(buyName);
            order.setSellerId(sellerId);
            order.setSellerName(sellerName);
//            order.setOrderStatus(TradeOrderStatusEnum.FINISHED.getCode());
            order.setOrderType(TradeOrderTypeEnum.BUY.getCode());
            order.setCreated(createTime);
            order.setModified(createTime);
            order.setTradeNo(t.getTradeNo());
            orderList.add(order);
        });
        return orderList;
    }

    /**
     * @param billNos
     * @return
     */
    private Map<Long, TradeDetail> getTradeBillMap(List<String> billNos) {
        List<RegisterBill> billList = hangGuoDataService.getRegisterBillByIds(billNos);
        List<String> billIds = StreamEx.of(billList).nonNull().map(b -> b.getCode()).collect(Collectors.toList());
        List<TradeDetail> orderDetailList = hangGuoDataService.getBillTradeDetailListByBillIds(billIds);
        return StreamEx.of(orderDetailList).nonNull().collect(Collectors.toMap(TradeDetail::getBillId, Function.identity(), (a, b) -> a));
    }

    /**
     * @param userCode
     * @return
     */
    private List<UserInfo> getUserListByThirdPartyCode(List<String> userCode) {
        if (CollectionUtils.isEmpty(userCode)) {
            return null;
        }
        return hangGuoDataService.getUserListByThirdPartyCode(userCode);
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    private List<HangGuoTrade> getPendingHandleTradeList() {
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.PENDING_HANDLE.getCode());
        return hangGuoDataService.selectTradeReportListByHandleFlag(trade);
    }

    /**
     * @param createTime
     */
    private void updateCacheTradeReportFlag(Date createTime) {

        //???????????????????????????
        Category category = new Category();
        category.setMarketId(HGMarketId);
        List<Category> categoryList = categoryService.list(category);
        Map<String, UserInfo> userMap = getUserMap();
        Map<String, Category> categoryMap = StreamEx.of(categoryList).nonNull().collect(Collectors.toMap(Category::getCode, c -> c, (a, b) -> a));

        //patch?????????????????????????????????????????????????????????????????????
        updateCacheTradeReportFlagToTrue(userMap, categoryMap, createTime);
        //??????50W???????????????patch???????????????,?????????????????????????????????????????????????????????
        updateCacheTradeReportFlagToFalse(userMap, categoryMap, createTime);

    }

    /**
     * @param userMap
     * @param categoryMap
     * @param createTime
     */
    private void updateCacheTradeReportFlagToFalse(Map<String, UserInfo> userMap, Map<String, Category> categoryMap, Date createTime) {
        //???????????????????????????patch???????????????
        hangGuoDataService.updateTradeReportListByBeyondAmount(reportMaxAmountInt);
        //??????????????????????????????????????????
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.PENDING_HANDLE.getCode());
        List<HangGuoTrade> tradeList = hangGuoDataService.selectTradeReportListByHandleFlag(trade);
        List<HangGuoTrade> collect = StreamEx.of(tradeList).nonNull().filter(t -> !userMap.containsKey(t.getMemberNo().trim())
                || !userMap.containsKey(t.getSupplierNo().trim()) || !categoryMap.containsKey(t.getItemNumber().trim())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            String handleRemark = "??????????????????????????????????????????????????????????????????";
            //????????????
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // ?????????
            Integer part = collect.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? collect.size() : (i + 1) * batchSize;
                List<HangGuoTrade> partBills = collect.subList(i * batchSize, endPos);

                Map<String, Object> map = new HashMap<>(16);
                map.put("list", partBills);
                map.put("handleRemark", handleRemark);
                map.put("handleFlag", DataHandleFlagEnum.UN_NEED_HANDLE.getCode());
                hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
            }


        }
    }

    /**
     * ???????????????????????????patch???????????????
     *
     * @param createTime
     */
    private void updateCacheTradeReportFlagToTrue(Map<String, UserInfo> userMap, Map<String, Category> categoryMap, Date createTime) {
        //?????????????????????????????????????????????????????????-??????
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.UN_NEED_HANDLE.getCode());
        List<HangGuoTrade> tradeList = hangGuoDataService.selectTradeReportListByHandleFlag(trade);
        if (CollectionUtils.isEmpty(tradeList)) {
            logger.info("==>??????????????? ?????????????????? ?????????");
            return;
        }
        // ??????????????????????????????????????????????????????????????????????????????
        List<HangGuoTrade> trades = new ArrayList<>();
        StreamEx.of(tradeList).nonNull().forEach(c -> {
            boolean needUpdate = userMap.containsKey(c.getMemberNo().trim()) && userMap.containsKey(c.getSupplierNo().trim()) && categoryMap.containsKey(c.getItemNumber().trim());
            if (needUpdate) {
                trades.add(c);
            }
        });
        //????????????????????????patch????????????
        if (CollectionUtils.isNotEmpty(trades)) {
            //????????????
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // ?????????
            Integer part = trades.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? trades.size() : (i + 1) * batchSize;
                List<HangGuoTrade> partBills = trades.subList(i * batchSize, endPos);

                Map<String, Object> map = new HashMap<>(16);
                map.put("list", partBills);
                map.put("handleFlag", DataHandleFlagEnum.PENDING_HANDLE.getCode());
                hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param commodityList
     * @param createTime
     */
    private void addCateGory(List<HangGuoCommodity> commodityList, Date createTime) {

        List<Category> categoryList = conversionCommodityList(commodityList, createTime);

        //???????????????????????????????????????
        hangGuoDataService.bachInsertCommodityList(categoryList);
        //patch?????????????????????parent_id
        Category category = new Category();
        category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
        category.setCreated(createTime);
        category.setMarketId(HGMarketId);
        hangGuoDataService.updateHangGuoCommodityParent(category);

        hangGuoCategoryLevelFaultTolerant();
    }

    /**
     * ????????????????????????,???????????????????????????????????????????????????,?????????????????????patch
     */
    private void hangGuoCategoryLevelFaultTolerant() {
        Category category = new Category();
        category.setMarketId(HGMarketId);
        List<Category> categoryFaultList = hangGuoDataService.getCategoryFaultList(category);
        StreamEx.of(categoryFaultList).nonNull().forEach(c -> {
            if (StringUtils.isNotBlank(c.getParentCode())) {
                String parentCode = c.getParentCode();
                if (StringUtils.isNotBlank(parentCode)) {
                    recursionCategoty(c, c.getParentCode(), 3);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(categoryFaultList)) {
            logger.info("??????????????????,???????????????:" + categoryFaultList.size());
            categoryService.batchUpdate(categoryFaultList);
        }
    }

    /**
     * ????????????
     *
     * @param c
     * @param count
     */
    private void recursionCategoty(Category c, String parentCode, int count) {
        if (count <= 0) {
            logger.info("????????????");
            return;
        }
        logger.info("???????????????????????????,parentCode:" + parentCode + " |count:" + count);
        parentCode = turnSubStr(parentCode);
        Category parentCategory = hangGuoDataService.getCategoryByThirdCode(parentCode);
        if (null != parentCategory) {
            c.setParentCode(parentCategory.getCode());
            c.setParentId(parentCategory.getId());
        } else {
            count--;
            recursionCategoty(c, parentCode, count);
        }
    }

    /**
     * @param parentCode
     * @return
     */
    private String turnSubStr(String parentCode) {
        if (StringUtils.isNotBlank(parentCode)) {
            return parentCode.substring(0, parentCode.length() - 1);
        }
        return null;
    }


    /**
     * ??????????????????
     *
     * @param updateHangGuoCommodityList
     * @param createTime
     */
    private void updateCateGoryByThirdCode(List<HangGuoCommodity> updateHangGuoCommodityList, Date createTime) {
        List<Category> categoryList = conversionCommodityList(updateHangGuoCommodityList, createTime);
        //??????????????????
        hangGuoDataService.batchUpdateCategoryByThirdCode(categoryList);
    }

    /**
     * ?????????????????????????????????
     *
     * @param commodityList
     * @param createTime
     * @return
     */
    private List<Category> conversionCommodityList(List<HangGuoCommodity> commodityList, Date createTime) {
        List<Category> categoryList = new ArrayList();
        //????????????
        CopyOnWriteArraySet<String> varietySet = new CopyOnWriteArraySet<>();
        StreamEx.of(commodityList).nonNull().forEach(c -> {
            Category category = new Category();
            category.setFullName(c.getItemName() == null ? null : c.getItemName().trim());
            category.setName(c.getItemName() == null ? null : c.getItemName().trim());
            category.setSpecification(c.getItemUnitName() == null ? null : c.getItemUnitName().trim());
            category.setCode(c.getItemNumber() == null ? null : c.getItemNumber().trim());
            category.setCreated(createTime);
            category.setModified(createTime);
            category.setMarketId(HGMarketId);
            category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
            setCommodityLevel(category, c);

            varietySet.add(c.getCategoryNumber());
            categoryList.add(category);
        });
        //???5??????????????????
        StreamEx.of(categoryList).nonNull().forEach(category -> {
            Integer categoryLevel = category.getLevel();
            if (categoryLevel.equals(HangGuoGoodsLevelEnum.GOODS_FIVE.getCode())) {
                String goodsCode = category.getCode().trim();
                String parentGoodsCode = goodsCode.substring(0, goodsCode.length() - 1);
                //??????????????????????????????????????????????????????????????????
                if (varietySet.contains(parentGoodsCode)) {
                    category.setLevel(HangGuoGoodsLevelEnum.GOODS_FOUR.getCode());
                } else {
                    category.setParentCode(parentGoodsCode);
                }
            }
        });
        return categoryList;
    }

    /**
     * ??????????????????
     *
     * @param category
     * @param commodity
     * @return
     */
    private void setCommodityLevel(Category category, HangGuoCommodity commodity) {
        String goodsCode = commodity.getItemNumber().trim();
        String first = commodity.getFirstCateg().trim();
        String second = commodity.getSecondCateg().trim();
        String categoryCode = commodity.getCategoryNumber().trim();
        category.setIsShow(CategoryIsShowEnum.IS_SHOW.getCode());
        //??????????????????????????????????????????????????????.???parentId
        if (goodsCode.equals(first)) {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_ONE.getCode());
        }
        //??????????????????????????????????????????????????????
        else if (goodsCode.equals(second)) {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_TWO.getCode());
            category.setParentCode(first);
        }
        //??????????????????????????????????????????????????????
        else if (goodsCode.equals(categoryCode)) {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_THREE.getCode());
            category.setParentCode(second);
        } else {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_FIVE.getCode());
            Integer psize = 5;
            String parentGoodsCode = categoryCode;
            if (categoryCode.length() > psize) {
                parentGoodsCode = categoryCode.substring(0, categoryCode.length() - 1);
            }
            category.setParentCode(parentGoodsCode);
            category.setIsShow(CategoryIsShowEnum.NOT_SHOW.getCode());
        }
    }

    /**
     * @param commodityCode
     * @return
     */
    private List<Category> getCategoryListByThirdCode(List<String> commodityCode) {
        if (CollectionUtils.isEmpty(commodityCode)) {
            return null;
        }
        return hangGuoDataService.getCategoryListByThirdCode(commodityCode);
    }


}
