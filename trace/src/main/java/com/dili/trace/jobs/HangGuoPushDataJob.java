package com.dili.trace.jobs;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.PushDataQueryDto;
import com.dili.trace.dto.thirdparty.report.*;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.*;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 对接天下粮仓入口，通过定时任务每5min调用天下粮仓的接口
 *
 * @author asa.lee
 */
@Component
public class HangGuoPushDataJob implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(HangGuoPushDataJob.class);
    @Autowired
    DataReportService dataReportService;
    @Autowired
    RegisterBillMapper registerBillMapper;
    @Autowired
    private MarketService marketService;
    @Autowired
    private ThirdPartyPushDataService thirdPartyPushDataService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private HangGuoDataService hangGuoDataService;
    @Autowired
    private ImageCertService imageCertService;

    @Value("${current.baseWebPath}")
    private String baseWebPath;
    @Value("${push.batch.size}")
    private Integer pushBatchSize;

    @Override
    public void run(String... args) throws Exception {
        //pushHangGuoTradeData();
        //pushData();
    }

    /**
     * 每五分钟提交一次数据
     */
    //@Scheduled(cron = "0 */5 * * * ?")
    public void pushData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            List<Market> marketList = marketService.list(new Market());
            for (Market market : marketList) {
                Long appId = market.getAppId();
                String appSecret = market.getAppSecret();
                String contextUrl = market.getContextUrl();
                Integer marketId = market.getId().intValue();
                boolean isHangGuo = marketId.equals(MarketIdEnum.FRUIT_TYPE.getCode()) && appId != null && StringUtils.isNoneBlank(appSecret) && StringUtils.isNoneBlank(contextUrl);
                if (isHangGuo) {
                    Date endTime = this.registerBillMapper.selectCurrentTime();
                    // 商品上报
                    this.pushFruitsCategory(optUser, endTime, market);
                    // 经营户上报
                    this.pushFruitsUser(optUser, endTime, market);
                    //检测数据上报
                    this.pushFruitsInspectionData(optUser, endTime, market);
                    //不合格检测上报
                    this.pushFruitsUnqualifiedInspectionData(optUser, endTime, market);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    //@Scheduled(cron = "0 */5 * * * ?")
    public void pushHangGuoTradeData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            List<Market> marketList = marketService.list(new Market());
            for (Market market : marketList) {
                Long appId = market.getAppId();
                String appSecret = market.getAppSecret();
                String contextUrl = market.getContextUrl();
                Integer marketId = market.getId().intValue();
                boolean isHangGuo = marketId.equals(MarketIdEnum.FRUIT_TYPE.getCode()) && appId != null && StringUtils.isNoneBlank(appSecret) && StringUtils.isNoneBlank(contextUrl);
                if (isHangGuo) {
                    Date endTime = this.registerBillMapper.selectCurrentTime();
                    //交易单
                    this.pushFruitsTradeData(optUser, endTime, market);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private BaseOutput pushFruitsTradeData(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        Date startTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.HANGGUO_TRADE.getCode(), marketId);

        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.HANGGUO_TRADE.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.HANGGUO_TRADE.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            startTime = pushData.getPushTime();
            newPushFlag = false;
        }

        List<ReportScanCodeOrderDto> partTrade = getFruitsTradeData(platformMarketId, startTime, endTime);
        if (CollectionUtils.isEmpty(partTrade)) {
            logger.info("Report Hangguo Unqualified Disposal IS NULL");
            return BaseOutput.success("Report Hangguo Unqualified Disposal IS NULL");
        }

        BaseOutput baseOutput = new BaseOutput();
        // 分批上报
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = partTrade.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? partTrade.size() : (i + 1) * batchSize;
            List<ReportScanCodeOrderDto> partBills = partTrade.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportScanCodeOrder(partBills, optUser, market);
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.HANGGUO_TRADE.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    private List<ReportScanCodeOrderDto> getFruitsTradeData(Long platformMarketId, Date startdTime, Date endTime) {
        PushDataQueryDto queryDto = new PushDataQueryDto();
        queryDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        if (startdTime != null) {
            queryDto.setModifiedStart(DateUtil.format(startdTime, "yyyy-MM-dd HH:mm:ss"));
        }
        List<ReportScanCodeOrderDto> scanCodeOrderDtoList = StreamEx.of(hangGuoDataService.getHangGuoScanOrderReport(queryDto)).
                nonNull().map(order -> {
            order.setMarketId(String.valueOf(platformMarketId));
            order.setThirdQrCode(this.baseWebPath + "/user?userId=" + order.getThirdBuyId());
            return order;
        }).collect(Collectors.toList());
        return scanCodeOrderDtoList;
    }


    private BaseOutput pushFruitsUnqualifiedInspectionData(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.HANGGUO_DISPOSE.getCode(), marketId);

        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.HANGGUO_DISPOSE.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.HANGGUO_DISPOSE.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }
        List<ReportUnqualifiedDisposalDto> disposalDtos = getReportDisposeList(platformMarketId);
        logger.info("Report Hangguo Unqualified Disposal Dto ： " + JSON.toJSONString(disposalDtos));
        if (CollectionUtils.isEmpty(disposalDtos)) {
            logger.info("Report Hangguo Unqualified Disposal IS NULL");
            return BaseOutput.success("Report Hangguo Unqualified Disposal IS NULL");
        }
        //BaseOutput baseOutput = this.dataReportService.reportFruitsUnqualifiedDisposal(disposalDtos, optUser, market);
        BaseOutput baseOutput = new BaseOutput();
        // 分批上报
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = disposalDtos.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? disposalDtos.size() : (i + 1) * batchSize;
            List<ReportUnqualifiedDisposalDto> partBills = disposalDtos.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportFruitsUnqualifiedDisposal(partBills, optUser, market);
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            hangGuoDataService.updateCheckOrderDisposeReportFlag(disposalDtos);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.HANGGUO_DISPOSE.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    private List<ReportUnqualifiedDisposalDto> getReportDisposeList(Long platformMarketId) {

        List<CheckOrderDispose> disposeList = getReportCheckOrderDisposeList();
        if (CollectionUtils.isEmpty(disposeList)) {
            return null;
        }
        List<ReportUnqualifiedDisposalDto> reportList = transformationDisposeExample(disposeList, platformMarketId);
        String ids = getCheckDisposeIds(disposeList);
        Map<Long, List<ImageCert>> imageCertMap = getCheckOrderImgMap(ImageCertBillTypeEnum.DISPOSE_TYPE.getCode(), ids);

        StreamEx.of(reportList).nonNull().forEach(h -> {
            List<ImageCert> imgList = imageCertMap.get(h.getId());
            List<ReportInspectionImgDto> reportImgList = new ArrayList<>();
            List<ReportInspectionItemDto> itemDtoList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(imgList)) {
                reportImgList = StreamEx.of(imgList).nonNull().map(m -> {
                    ReportInspectionImgDto reportImg = new ReportInspectionImgDto();
                    reportImg.setCredentialName(ImageCertBillTypeEnum.INSPECTION_TYPE.getName());
                    if (StringUtils.isNotBlank(m.getUrl())) {
                        reportImg.setPicUrl(baseWebPath + m.getUrl());
                    }
                    return reportImg;
                }).collect(Collectors.toList());
                h.setCheckFailImgList(reportImgList);
            }
        });
        return reportList;
    }

    private String getCheckDisposeIds(List<CheckOrderDispose> disposeList) {
        List<String> idList = StreamEx.of(disposeList).nonNull().map(h -> String.valueOf(h.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(idList)) {
            return idList.stream().collect(Collectors.joining("','"));
        }
        return null;
    }

    private List<ReportUnqualifiedDisposalDto> transformationDisposeExample(List<CheckOrderDispose> disposeList, Long platformMarketId) {
        return StreamEx.of(disposeList).nonNull().map(m -> {
            ReportUnqualifiedDisposalDto r = new ReportUnqualifiedDisposalDto();
            r.setId(m.getId());
            r.setMarketId(platformMarketId);
            r.setChuZhiType(m.getDisposeType());
            r.setCheckNo(m.getCheckNo());
            r.setChuZhiDate(m.getDisposeDate());
            r.setChuZhiDesc(m.getDisposeDesc());
            r.setChuZhier(m.getDisposer());
            r.setChuZhiNum(m.getDisposeNum());
            r.setChuZhiResult(m.getDisposeResult());
            return r;
        }).collect(Collectors.toList());
    }

    private List<CheckOrderDispose> getReportCheckOrderDisposeList() {
        CheckOrderDispose dispose = new CheckOrderDispose();
        dispose.setReportFlag(CheckOrderReportFlagEnum.PROCESSED.getCode());
        return hangGuoDataService.getReportCheckOrderDisposeList(dispose);
    }

    private BaseOutput pushFruitsInspectionData(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.HANGGUO_INSPECTION.getCode(), marketId);

        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.HANGGUO_INSPECTION.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.HANGGUO_INSPECTION.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        List<ReportInspectionDto> inspectionDtoList = getReportInspectionList(platformMarketId);
        logger.info("Report Hangguo checkOrder Dto ： " + JSON.toJSONString(inspectionDtoList));
        if (CollectionUtils.isEmpty(inspectionDtoList)) {
            logger.info("report checkOrder is null");
            return BaseOutput.success("report checkOrder is null");
        }
        //BaseOutput baseOutput = this.dataReportService.reportFruitsInspection(inspectionDtoList, optUser, market);

        BaseOutput baseOutput = new BaseOutput();
        // 分批上报
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = inspectionDtoList.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? inspectionDtoList.size() : (i + 1) * batchSize;
            List<ReportInspectionDto> partBills = inspectionDtoList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportFruitsInspection(partBills, optUser, market);
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            hangGuoDataService.updateCheckOrderReportFlag(inspectionDtoList);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.HANGGUO_INSPECTION.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    private List<ReportInspectionDto> getReportInspectionList(Long platformMarketId) {
        List<CheckOrder> checkOrderList = getReportCheckOrderList();
        if (CollectionUtils.isEmpty(checkOrderList)) {
            return null;
        }
        List<ReportInspectionDto> headList = transformationExample(checkOrderList, platformMarketId);
        String ids = getCheckIds(headList);
        Map<Long, List<ImageCert>> imageCertMap = getCheckOrderImgMap(ImageCertBillTypeEnum.INSPECTION_TYPE.getCode(), ids);

        Map<Long, List<CheckOrderData>> valueMap = getCheckOrderDataMap(checkOrderList);
        StreamEx.of(headList).nonNull().forEach(h -> {
            List<ImageCert> imgList = imageCertMap.get(h.getId());
            List<CheckOrderData> dataList = valueMap.get(h.getId());
            List<ReportInspectionImgDto> reportImgList = new ArrayList<>();
            List<ReportInspectionItemDto> itemDtoList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(imgList)) {
                reportImgList = StreamEx.of(imgList).nonNull().map(m -> {
                    ReportInspectionImgDto reportImg = new ReportInspectionImgDto();
                    reportImg.setCredentialName(ImageCertBillTypeEnum.INSPECTION_TYPE.getName());
                    if (StringUtils.isNotBlank(m.getUrl())) {
                        reportImg.setPicUrl(baseWebPath + m.getUrl());
                    }
                    return reportImg;
                }).collect(Collectors.toList());
                h.setCheckImgList(reportImgList);
            }
            if (CollectionUtils.isNotEmpty(dataList)) {
                itemDtoList = StreamEx.of(dataList).nonNull().map(m -> {
                    ReportInspectionItemDto reportvalue = new ReportInspectionItemDto();
                    reportvalue.setNormalValue(m.getNormalValue());
                    reportvalue.setProject(m.getProject());
                    reportvalue.setResult(m.getResult());
                    reportvalue.setValue(m.getValue());
                    return reportvalue;
                }).collect(Collectors.toList());
                h.setCheckItem(itemDtoList);
            }
        });
        return headList;
    }

    private List<ReportInspectionDto> transformationExample(List<CheckOrder> checkOrderList, Long platformMarketId) {
        return StreamEx.of(checkOrderList).nonNull().map(c -> {
            ReportInspectionDto re = new ReportInspectionDto();
            re.setId(c.getId());
            re.setIdCard(c.getIdCard());
            re.setChecker(c.getChecker());
            re.setCheckImgList(c.getCheckImgList());
            re.setCheckItem(c.getCheckItem());
            re.setCheckNo(c.getCheckNo());
            re.setCheckOrgName(c.getCheckOrgName());
            re.setCheckResult(c.getCheckResult());
            re.setCheckTime(c.getCheckTime());
            re.setCheckType(c.getCheckType());
            re.setGoodsName(c.getGoodsName());
            re.setGoodsCode(c.getGoodsCode());
            re.setAccountName(c.getUserName());
            re.setBoothNo(c.getTallyAreaNos());
            re.setMarketId(platformMarketId);
            return re;
        }).collect(Collectors.toList());
    }

    private List<CheckOrder> getReportCheckOrderList() {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setReportFlag(CheckOrderReportFlagEnum.PROCESSED.getCode());
        return hangGuoDataService.getReportCheckOrderList(checkOrder);
    }

    private Map<Long, List<CheckOrderData>> getCheckOrderDataMap(List<CheckOrder> headList) {
        CheckOrderData da = new CheckOrderData();
        if (CollectionUtils.isEmpty(headList)) {
            return null;
        }
        List<CheckOrderData> dataList = hangGuoDataService.getCheckOrderDataList(headList);
        return StreamEx.of(dataList).nonNull().collect(Collectors.groupingBy(CheckOrderData::getCheckId));
    }

    private Map<Long, List<ImageCert>> getCheckOrderImgMap(Integer billType, String ids) {
        ImageCert imageCert = new ImageCert();
        imageCert.setBillType(billType);
        if (StringUtils.isNotBlank(ids)) {
            imageCert.setMetadata(IDTO.AND_CONDITION_EXPR, " bill_id in ('" + ids + "')");
        }
        List<ImageCert> imageCerts = imageCertService.listByExample(imageCert);
        return StreamEx.of(imageCerts).nonNull().collect(Collectors.groupingBy(ImageCert::getBillId));
    }


    private String getCheckIds(List<ReportInspectionDto> headList) {
        List<String> idList = StreamEx.of(headList).nonNull().map(h -> String.valueOf(h.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(idList)) {
            return idList.stream().collect(Collectors.joining("','"));
        }
        return null;
    }

    /**
     * 上报杭果用户
     *
     * @param optUser
     * @param endTime
     * @param market
     * @return
     */
    private BaseOutput pushFruitsUser(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.HANGGUO_USER.getCode(), marketId);

        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.HANGGUO_USER.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.HANGGUO_USER.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        User user = DTOUtils.newDTO(User.class);
        user.setMarketId(market.getId());
        user.setState(EnabledStateEnum.ENABLED.getCode());
        user.setYn(YnEnum.YES.getCode());
        user.setValidateState(ValidateStateEnum.PASSED.getCode());
        if (!newPushFlag) {
            user.mset(IDTO.AND_CONDITION_EXPR, " modified >= '" + DateUtils.format(updateTime) + "' and phone <> '' and phone <> '''''' ");
        }
        List<User> userList = userService.listByExample(user);
        if (CollectionUtils.isEmpty(userList)) {
            return BaseOutput.success("NULL USER NEED PUSH");
        }
        List<ReportUserDto> reportUserDtoList = new ArrayList<>();
        StreamEx.of(userList).nonNull().forEach(u -> {
            ReportUserDto userDto = new ReportUserDto();
            userDto.setAccountName(u.getName());
            userDto.setTel(u.getPhone());
            userDto.setBoothNo(u.getTallyAreaNos());
            if (UserTypeEnum.CORPORATE.getCode().equals(u.getUserType())) {
                userDto.setIdCard(u.getLicense());
            } else {
                userDto.setIdCard(u.getCardNo());
            }
            userDto.setMarketId(platformMarketId);
            reportUserDtoList.add(userDto);
        });
        logger.info("Report Hangguo User Dto ： " + JSON.toJSONString(reportUserDtoList));
        if (CollectionUtils.isEmpty(reportUserDtoList)) {
            logger.info("Report Hangguo User ISNULL");
            return BaseOutput.success("Report Hangguo User ISNULL");
        }
        //BaseOutput baseOutput = this.dataReportService.reportFruitsUser(reportUserDtoList, optUser, market);

        BaseOutput baseOutput = new BaseOutput();
        // 分批上报
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = reportUserDtoList.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? reportUserDtoList.size() : (i + 1) * batchSize;
            List<ReportUserDto> partBills = reportUserDtoList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportFruitsUser(partBills, optUser, market);
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.HANGGUO_USER.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    /**
     * 上报杭果商品
     *
     * @param optUser
     * @param endTime
     * @param market
     * @return
     */
    private BaseOutput pushFruitsCategory(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.HANGGUO_GOODS.getCode(), marketId);

        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.HANGGUO_GOODS.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.HANGGUO_GOODS.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        Category category = new Category();
        category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
        category.setIsShow(CategoryIsShowEnum.IS_SHOW.getCode());
        if (!newPushFlag) {
            category.setMetadata(IDTO.AND_CONDITION_EXPR, " modified >= '" + DateUtils.format(updateTime) + "'");
        }
        List<Category> allQrHistories = categoryService.listByExample(category);
        List<GoodsDto> categoryDtos = new ArrayList<>();
        StreamEx.of(allQrHistories).nonNull().forEach(c -> {
            GoodsDto categoryDto = new GoodsDto();
            categoryDto.setGoodsName(c.getName());
            categoryDto.setGoodsCode(c.getCode());
            categoryDto.setMarketId(String.valueOf(platformMarketId));
            categoryDtos.add(categoryDto);
        });
        logger.info("Report Hangguo Category Dto ： " + JSON.toJSONString(categoryDtos));
        if (CollectionUtils.isEmpty(categoryDtos)) {
            logger.info("report Category ISNULL");
            return BaseOutput.success("report Category ISNULL");
        }
        //BaseOutput baseOutput = this.dataReportService.reportFruitsGoods(categoryDtos, optUser, market);

        BaseOutput baseOutput = new BaseOutput();
        // 分批上报
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = categoryDtos.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? categoryDtos.size() : (i + 1) * batchSize;
            List<GoodsDto> partBills = categoryDtos.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportFruitsGoods(partBills, optUser, market);
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.HANGGUO_GOODS.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }


}