package com.dili.trace.jobs;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dao.TradeRequestMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.PushDataQueryDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.thirdparty.report.*;
import com.dili.trace.enums.*;
import com.dili.trace.service.*;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对接天下粮仓入口，通过定时任务每5min调用天下粮仓的接口
 *
 * @author asa.lee, alvin, lily
 */
@Component
public class ThirdPartyPushDataJob implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyPushDataJob.class);
    @Autowired
    DataReportService dataReportService;
    @Autowired
    RegisterBillMapper registerBillMapper;
    @Autowired
    TradeRequestMapper tradeRequestMapper;
    @Autowired
    private AssetsRpcService categoryService;
    @Autowired
    private ThirdPartyPushDataService thirdPartyPushDataService;
    @Autowired
    private ImageCertService imageCertService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserQrHistoryService userQrHistoryService;
    @Autowired
    private ThirdDataReportService thirdDataReportService;
    @Autowired
    private UpStreamService upStreamService;

    @Autowired
    private MarketService marketService;

    @Value("${current.baseWebPath}")
    private String baseWebPath;
    @Value("${push.batch.size}")
    private Integer pushBatchSize;
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;

    //private String marketId = "330110800";

    /**
     * marketId统一使用330110800
     */
    @Override
    public void run(String... args) throws Exception {
    }

    /**
     * 每五分钟提交一次数据
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void pushData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            List<Market> marketList = marketService.list(new Market());
            for (Market market : marketList) {
                Long appId = market.getAppId();
                String appSecret = market.getAppSecret();
                String contextUrl = market.getContextUrl();
                Integer marketId = market.getId().intValue();
                if (appId != null && StringUtils.isNoneBlank(appSecret) && StringUtils.isNoneBlank(contextUrl)) {
                    Date endTime = this.registerBillMapper.selectCurrentTime();
                    if (marketId.equals(MarketIdEnum.AQUATIC_TYPE.getCode())) {
                        // 商品大类新增/修改
                        this.pushBigCategory(optUser, market);
                        // 商品二级类目新增/修改
                        // 商品新增/修改
                        this.pushCategory(ReportInterfaceEnum.CATEGORY_SMALL_CLASS.getCode(), ReportInterfaceEnum.CATEGORY_SMALL_CLASS.getName(), 1, optUser, endTime, market);
                        this.pushCategory(ReportInterfaceEnum.CATEGORY_GOODS.getCode(), ReportInterfaceEnum.CATEGORY_GOODS.getName(), 2, optUser, endTime, market);
                    } else if (marketId.equals(MarketIdEnum.FRUIT_TYPE.getCode())) {
                        // 杭果-商品大类新增/修改
                        this.pushFruitsBigCategory(optUser, market);
                        // 杭果-商品二级类目新增/修改
                        // 杭果-商品新增/修改
                        this.pushFruitsCategory(ReportInterfaceEnum.CATEGORY_SMALL_CLASS.getCode(), ReportInterfaceEnum.CATEGORY_SMALL_CLASS.getName(), 2, optUser, endTime, market);
                        this.pushFruitsCategory(ReportInterfaceEnum.CATEGORY_GOODS.getCode(), ReportInterfaceEnum.CATEGORY_GOODS.getName(), 3, optUser, endTime, market);
                    }
                    // 上游新增编辑
                    this.pushStream(ReportInterfaceEnum.UPSTREAM_UP.getCode(), ReportInterfaceEnum.UPSTREAM_UP.getName(), 10, optUser, endTime, market);
                    // 下游新增/编辑
                    this.pushStream(ReportInterfaceEnum.UPSTREAM_DOWN.getCode(), ReportInterfaceEnum.UPSTREAM_DOWN.getName(), 20, optUser, endTime, market);
                    // 进门
                    this.reportCheckIn(optUser, endTime, market);
                    // 配送交易
                    this.reportOrder(ReportInterfaceEnum.TRADE_REQUEST_DELIVERY.getCode(), ReportInterfaceEnum.TRADE_REQUEST_DELIVERY.getName(), 10, optUser, endTime, market);
                    // 配送交易作废
                    this.reportOrderDelete(ReportInterfaceEnum.TRADE_REQUEST_DELIVERY_DELETE.getCode(), ReportInterfaceEnum.TRADE_REQUEST_DELIVERY_DELETE.getName(),
                            10, optUser, endTime, market);
                    // 扫码交易
                    this.reportOrder(ReportInterfaceEnum.TRADE_REQUEST_SCAN.getCode(), ReportInterfaceEnum.TRADE_REQUEST_SCAN.getName(), 20, optUser, endTime, market);
                    // 扫码交易作废
                    this.reportOrderDelete(ReportInterfaceEnum.TRADE_REQUEST_SCAN_DELETE.getCode(), ReportInterfaceEnum.TRADE_REQUEST_SCAN_DELETE.getName(),
                            20, optUser, endTime, market);
                    //食安码新增/修改
                    this.pushUserQrCode(optUser, endTime, market);
                    //经营户新增/修改
                    this.pushUserSaveUpdate(optUser, endTime, market);
                    //经营户作废
                    this.pushUserDelete(optUser, endTime, market);
                    // 报备作废
                    this.reportRegisterBillDelete(optUser, endTime, market);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void pushRegisterBillData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            List<Market> marketList = marketService.list(new Market());
            for (Market market : marketList) {
                Long appId = market.getAppId();
                String appSecret = market.getAppSecret();
                String contextUrl = market.getContextUrl();
                if (appId != null && StringUtils.isNoneBlank(appSecret) && StringUtils.isNoneBlank(contextUrl)) {
                    Date endTime = this.registerBillMapper.selectCurrentTime();
                    // 报备新增/编辑
                    this.reportRegisterBill(optUser, endTime, market);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 上报商品大类
     *
     * @param optUser 操作人信息
     */
    private void pushBigCategory(Optional<OperatorUser> optUser, Market market) {
        String tableName = ReportInterfaceEnum.BIG_CATEGORY.getCode();
        String interfaceName = ReportInterfaceEnum.BIG_CATEGORY.getName();
        Long marketId = market.getId();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        if (thirdPartyPushData == null) {
            PreserveTypeEnum[] preserveTypeEnums = PreserveTypeEnum.values();
            List<CategoryDto> categoryDtos = new ArrayList<>();
            int i = 1;
            for (PreserveTypeEnum td : preserveTypeEnums) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setThirdBigClassName(td.getName());
                categoryDto.setThirdBigClassId(String.valueOf(i));
                categoryDtos.add(categoryDto);
                i++;
            }
            ;
            BaseOutput baseOutput = this.dataReportService.reportCategory(categoryDtos, optUser, market);
            if (baseOutput.isSuccess()) {
                ThirdPartyPushData pushData = new ThirdPartyPushData();
                pushData.setTableName(tableName);
                pushData.setInterfaceName(interfaceName);
                pushData.setMarketId(marketId);
                this.thirdPartyPushDataService.updatePushTime(pushData);
            } else {
                logger.error("上报:商品大类新增/修改 失败，原因:{}", baseOutput.getMessage());
            }
        }
    }

    /**
     * 上报商品大类
     *
     * @param optUser 操作人信息
     */
    private void pushFruitsBigCategory(Optional<OperatorUser> optUser, Market market) {
        String tableName = ReportInterfaceEnum.BIG_CATEGORY.getCode();
        String interfaceName = ReportInterfaceEnum.BIG_CATEGORY.getName();
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        Integer fruitsBigCategory = 1;
        if (thirdPartyPushData == null) {
            CusCategoryDTO category = new CusCategoryDTO();
//TODO
            //            category.setLevel(fruitsBigCategory);
            category.setMarketId(marketId);
            List<CusCategoryDTO> categories = categoryService.listCusCategory(null);

            List<CategoryDto> categoryDtos = StreamEx.of(categories).nonNull().map(c -> {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setThirdBigClassName(c.getName());
                categoryDto.setThirdBigClassId(String.valueOf(c.getId()));
                categoryDto.setMarketId(String.valueOf(platformMarketId));
                return categoryDto;
            }).collect(Collectors.toList());
            BaseOutput baseOutput = this.dataReportService.reportCategory(categoryDtos, optUser, market);
            if (baseOutput.isSuccess()) {
                ThirdPartyPushData pushData = new ThirdPartyPushData();
                pushData.setTableName(tableName);
                pushData.setInterfaceName(interfaceName);
                pushData.setMarketId(marketId);
                this.thirdPartyPushDataService.updatePushTime(pushData);
            } else {
                logger.error("上报:杭果商品大类新增/修改 失败，原因:{}", baseOutput.getMessage());
            }
        }
    }

    /**
     * 上报商品二类/商品新增/修改
     *
     * @param optUser
     */
    private void pushCategory(String tableName, String interfaceName,
                              Integer level, Optional<OperatorUser> optUser, Date endTime, Market market) {
        String categorySmallClass = "category_smallClass";
        String categoryGoods = "category_goods";
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        CusCategoryQuery category = new CusCategoryQuery();
        //TODO
//        category.setLevel(level);
        category.setMarketId(marketId);
        List<CusCategoryDTO> categories = categoryService.listCusCategory(category);
        ThirdPartyPushData pushData = new ThirdPartyPushData();
        pushData.setTableName(tableName);
        pushData.setInterfaceName(interfaceName);
        pushData.setMarketId(marketId);
        BaseOutput baseOutput = new BaseOutput();
        if (categorySmallClass.equals(tableName)) {
            List<CategorySecondDto> categoryDtos = new ArrayList<>();
            int i = 1;
            PreserveTypeEnum[] preserveTypeEnums = PreserveTypeEnum.values();
            for (PreserveTypeEnum type : preserveTypeEnums) {
                for (CusCategoryDTO td : categories) {
                    boolean needPush = true;
                   /* boolean needPush = thirdPartyPushData == null || (thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0
                            && td.getModified().compareTo(endTime) <= 0);*/
                    if (needPush) {
                        CategorySecondDto categoryDto = new CategorySecondDto();
                        categoryDto.setThirdSmallClassId(td.getId().toString());
                        categoryDto.setSmallClassName(td.getName());
                        categoryDto.setThirdBigClassId(String.valueOf(i));
                        categoryDto.setMarketId(String.valueOf(platformMarketId));
                        categoryDtos.add(categoryDto);
                    }
                }
                ;
                i++;
            }
            if (categoryDtos.size() > 0) {
                baseOutput = this.dataReportService.reportSecondCategory(categoryDtos, optUser, market);
                if (baseOutput.isSuccess()) {
                    this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
                } else {
                    logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
                }
            }
        } else if (categoryGoods.equals(tableName)) {
            List<GoodsDto> categoryDtos = new ArrayList<>();
            StreamEx.of(categories).forEach(td -> {
                boolean needPush =true;
 /*               boolean needPush = thirdPartyPushData == null || (thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0
                        && td.getModified().compareTo(endTime) <= 0);*/
                if (needPush) {
                    GoodsDto categoryDto = new GoodsDto();
                    categoryDto.setGoodsName(td.getName());
                    categoryDto.setThirdGoodsId(td.getId().toString());
//                    categoryDto.setThirdSmallClassId(td.getParentId().toString());
                    categoryDto.setMarketId(String.valueOf(platformMarketId));
                    categoryDtos.add(categoryDto);
                }
            });
            if (categoryDtos.size() > 0) {
                baseOutput = this.dataReportService.reportGoods(categoryDtos, optUser, market);
                if (baseOutput.isSuccess()) {
                    this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
                } else {
                    logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
                }
            }
        }
    }

    /**
     * 上报商品二类/商品新增/修改
     *
     * @param optUser
     */
    private void pushFruitsCategory(String tableName, String interfaceName,
                                    Integer level, Optional<OperatorUser> optUser, Date endTime, Market market) {
        String categorySmallClass = "category_smallClass";
        String categoryGoods = "category_goods";
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        CusCategoryQuery category = new CusCategoryQuery();
        //TODO
//        category.setLevel(level);
        category.setMarketId(marketId);
        List<CusCategoryDTO> categories = categoryService.listCusCategory(category);
        ThirdPartyPushData pushData = new ThirdPartyPushData();
        pushData.setTableName(tableName);
        pushData.setInterfaceName(interfaceName);
        pushData.setMarketId(marketId);
        BaseOutput baseOutput = new BaseOutput();
        if (categorySmallClass.equals(tableName)) {
            List<CategorySecondDto> categoryDtos = new ArrayList<>();
            for (CusCategoryDTO td : categories) {
                //TODO
                boolean needPush =true;
/*                boolean needPush = thirdPartyPushData == null
                        || (thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0
                        && td.getModified().compareTo(endTime) <= 0);*/
                if (needPush) {
                    CategorySecondDto categoryDto = new CategorySecondDto();
                    categoryDto.setThirdSmallClassId(td.getId().toString());
                    categoryDto.setSmallClassName(td.getName());
                    //TODO
//                    categoryDto.setThirdBigClassId(String.valueOf(td.getParentId()));
                    categoryDto.setMarketId(String.valueOf(platformMarketId));
                    categoryDtos.add(categoryDto);
                }
            }
            if (categoryDtos.size() > 0) {
                baseOutput = this.dataReportService.reportSecondCategory(categoryDtos, optUser, market);
                if (baseOutput.isSuccess()) {
                    this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
                } else {
                    logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
                }
            }
        } else if (categoryGoods.equals(tableName)) {
            List<GoodsDto> categoryDtos = new ArrayList<>();
            StreamEx.of(categories).forEach(td -> {
                //TODO
                boolean needPush =true;
                /*boolean needPush = thirdPartyPushData == null || (thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0
                        && td.getModified().compareTo(endTime) <= 0);*/
                if (needPush) {
                    GoodsDto categoryDto = new GoodsDto();
                    categoryDto.setGoodsName(td.getName());
                    categoryDto.setThirdGoodsId(td.getId().toString());
//TODO
                    //                    categoryDto.setThirdSmallClassId(td.getParentId().toString());
                    categoryDto.setMarketId(String.valueOf(platformMarketId));
                    categoryDtos.add(categoryDto);
                }
            });
            if (categoryDtos.size() > 0) {
                baseOutput = this.dataReportService.reportGoods(categoryDtos, optUser, market);
                if (baseOutput.isSuccess()) {
                    this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
                } else {
                    logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
                }
            }
        }
    }

    private BaseOutput pushUserQrCode(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Integer isValidate = 1;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.USER_QR_HISTORY.getCode(), marketId);
        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.USER_QR_HISTORY.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.USER_QR_HISTORY.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        Date finalUpdateTime = updateTime;
        boolean finalNewPushFlag = newPushFlag;
        List<UserQrHistory> qrHistories = new ArrayList<>();
        List<UserQrHistory> allQrHistories = new ArrayList<>();
        Map<Long, String> userMap = new HashMap<>(16);
        User user = DTOUtils.newDTO(User.class);
        user.setValidateState(ValidateStateEnum.PASSED.getCode());
        user.setMarketId(marketId);

        allQrHistories = userQrHistoryService.listByExample(null);
        StreamEx.ofNullable(userService.list(user)).nonNull().flatCollection(Function.identity()).forEach(u -> {
            userMap.put(u.getId(), u.getName());
        });
        StreamEx.ofNullable(allQrHistories).nonNull().flatCollection(Function.identity()).forEach(q -> {
            if (finalNewPushFlag || finalUpdateTime.compareTo(q.getModified()) < 0) {
                if (userMap.containsKey(q.getUserId())) {
                    qrHistories.add(q);
                }
            }
        });
        //根据用户分组二维码

        List<UserQrHistory> resultQrHisList = filterQrResultList(qrHistories, isValidate);


        List<ReportQrCodeDto> pushList = thirdDataReportService.reprocessUserQrCode(resultQrHisList, platformMarketId);
        logger.info("ReportQrCodeDto ： " + JSON.toJSONString(pushList));
        // 分批上报--由于数据结构较为庞大与其他分批不同，单独分批
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = 100;
        // 分批数
        Integer part = pushList.size() / batchSize;
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? pushList.size() : (i + 1) * batchSize;
            List<ReportQrCodeDto> partBills = pushList.subList(i * batchSize, endPos);
            if (CollectionUtils.isNotEmpty(partBills)) {
                baseOutput = this.dataReportService.reportUserQrCode(partBills, optUser, market);
            }
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.USER_QR_HISTORY.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    private List<UserQrHistory> filterQrResultList(List<UserQrHistory> qrHistories, Integer isValidate) {
        List<UserQrHistory> resultQrHisList = new ArrayList<>();
        Map<Long, List<UserQrHistory>> qrMap = StreamEx.of(qrHistories).nonNull().groupingBy(UserQrHistory::getUserId);
        qrMap.entrySet().stream().forEach(qList -> {
            //如果最新的一条记录是变更为无效记录（报备单删除），则取当前用户最新有效二维码变更记录
            List<UserQrHistory> userQrHistories = qList.getValue();
            Set<Long> userIdSet = new HashSet<>();
            UserQrHistory latestQr = userQrHistories.stream().max(Comparator.comparing(UserQrHistory::getModified)).orElse(null);
            if (isValidate.equals(latestQr.getIsValid())) {
                resultQrHisList.add(latestQr);
            } else {
                userIdSet.add(latestQr.getUserId());
            }

            //如果最新记录是变更为无效记录（报备单删除），则取当前用户最新有效二维码变更记录
            boolean onlyVoid = CollectionUtils.isNotEmpty(userIdSet) && userIdSet.size() == userQrHistories.size();
            if (onlyVoid) {
                Long userId = userIdSet.iterator().next();
                UserQrHistory iteamQr = new UserQrHistory();
                iteamQr.setIsValid(isValidate);
                iteamQr.setUserId(userIdSet.iterator().next());
                iteamQr.setMetadata(IDTO.AND_CONDITION_EXPR, " modified = (SELECT MAX(modified) FROM user_qr_history WHERE user_id ='"
                        + userId + "' AND is_valid ='" + isValidate + "' )");
                UserQrHistory addItem = userQrHistoryService.listByExample(iteamQr).get(0);
                logger.info("时间段内最新食安码为作废记录，获取回滚记录" + JSON.toJSONString(addItem));
                resultQrHisList.add(addItem);
            }
        });
        return resultQrHisList;
    }

    private BaseOutput pushUserSaveUpdate(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Integer normalUserType = 1;
        Integer isPush = 1;
        Integer pushed = -1;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        List<ReportUserDto> reportUserDtoList = new ArrayList<>();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.USER.getCode(), marketId);
        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.USER.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.USER.getName());
            pushData.setPushTime(endTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }
        //获取正常经营(审核通过)的经营户列表（排除未实名的用户）
        Date finalUpdateTime = updateTime;
        boolean finalNewPushFlag = newPushFlag;
        User queUser = DTOUtils.newDTO(User.class);
        queUser.setYn(normalUserType);
        //1为需要上报
        queUser.setIsPush(isPush);
        queUser.mset(IDTO.AND_CONDITION_EXPR, " validate_state = 40 and phone <> '' and phone <> '''''' ");
        queUser.setMarketId(marketId);
        StreamEx.ofNullable(this.userService.listByExample(queUser))
                .nonNull().flatCollection(Function.identity()).map(info -> {
            //push后修改了用户信息
            if (finalNewPushFlag || finalUpdateTime.compareTo(info.getModified()) < 0) {
                ReportUserDto reportUser = thirdDataReportService.reprocessUser(info, platformMarketId);
                reportUserDtoList.add(reportUser);
                return true;
            }
            return false;
        }).toList();

        // 分批上报  由于数据结构较为庞大与其他分批不同，单独分批
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = 60;
        // 分批数
        Integer part = reportUserDtoList.size() / batchSize;
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? reportUserDtoList.size() : (i + 1) * batchSize;
            List<ReportUserDto> partBills = reportUserDtoList.subList(i * batchSize, endPos);
            if (CollectionUtils.isNotEmpty(partBills)) {
                baseOutput = this.dataReportService.reportUserSaveUpdate(partBills, optUser, market);
                List<Long> userIdList = StreamEx.ofNullable(partBills)
                        .nonNull().flatCollection(Function.identity()).map(userDto -> Long.valueOf(userDto.getThirdAccId())).toList();
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    userService.updateUserIsPushFlag(pushed, userIdList);
                }
            }
        }
        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.USER.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    private BaseOutput pushUserDelete(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.USER_DELETE.getCode(), marketId);
        if (pushData == null) {
            updateTime = endTime;
            pushData = new ThirdPartyPushData();
            pushData.setTableName(ReportInterfaceEnum.USER_DELETE.getCode());
            pushData.setInterfaceName(ReportInterfaceEnum.USER_DELETE.getName());
            pushData.setPushTime(updateTime);
            pushData.setMarketId(marketId);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        boolean finalNewPushFlag = newPushFlag;
        Timestamp sqlPushTime = new Timestamp(updateTime.getTime());
        User queUser = DTOUtils.newDTO(User.class);
        //没有push过则将所有作废记录push
        if (finalNewPushFlag) {
            //首次push不需要将原作废经营户上报
            ThirdPartyPushData pushUser = thirdPartyPushDataService.getThredPartyPushData(ReportInterfaceEnum.USER.getCode(), marketId);
            //未push经营户新增修改数据时不上报
            if (null == pushUser) {
                queUser.mset(IDTO.AND_CONDITION_EXPR, " id = -1 ");
            } else {
                String userQueTime = DateUtils.dateFormat(pushUser.getPushTime().getTime());
                queUser.mset(IDTO.AND_CONDITION_EXPR, " yn = -1 and validate_state <> 10 and modified > '" + userQueTime + "'");
            }

        } else {
            queUser.mset(IDTO.AND_CONDITION_EXPR, " yn = -1 and validate_state <> 10 and modified > '" + sqlPushTime + "'");
        }
        queUser.setMarketId(marketId);
        List<User> userList = this.userService.listByExample(queUser);
        // 分批上报
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        if (CollectionUtils.isNotEmpty(userList)) {
            ReportUserDeleteDto reportUser = new ReportUserDeleteDto();
            reportUser.setMarketId(String.valueOf(platformMarketId));
            List<String> thirdAccIds = new ArrayList<>();
            StreamEx.ofNullable(userList)
                    .nonNull().flatCollection(Function.identity()).map(info -> {
                //push后修改了用户信息
                thirdAccIds.add(String.valueOf(info.getId()));
                return true;
            }).toList();
            logger.info("thirdAccIds:" + JSON.toJSONString(thirdAccIds));
            reportUser.setThirdAccIds(String.join(",", thirdAccIds));
            // 分批上报
            baseOutput = this.dataReportService.reportUserDelete(reportUser, optUser, market);
            if (baseOutput.isSuccess()) {
                this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            } else {
                logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.USER_DELETE.getName(), baseOutput.getMessage());
            }
        } else {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        }
        return baseOutput;
    }


    public BaseOutput reportRegisterBill(Optional<OperatorUser> optUser, Date endTime, Market market) {
        String tableName = ReportInterfaceEnum.REGISTER_BILL.getCode();
        String interfaceName = ReportInterfaceEnum.REGISTER_BILL.getName();
        Integer noDelete = 0;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        // verify_status "待审核"0, "已退回10, "已通过20, "不通过30
        // approvalStatus 审核状态 0-默认未审核 1-通过 2-退回 3-未通过
        Map<Integer, Integer> statusMap = new HashMap<>(16);
        statusMap.put(0, 0);
        statusMap.put(20, 1);
        statusMap.put(10, 2);
        statusMap.put(30, 3);

        // 查询待上报的报备单
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        RegisterBillDto billDto = new RegisterBillDto();
        billDto.setIsDeleted(noDelete);
        billDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        billDto.setMarketId(marketId);
        if (thirdPartyPushData != null) {
            billDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        List<Long> billIdList = new ArrayList<>();
        List<ReportRegisterBillDto> billList = StreamEx.ofNullable(this.registerBillMapper.selectRegisterBillReport(billDto))
                .nonNull().flatCollection(Function.identity()).map(bill -> {
                    // 状态映射
                    bill.setApprovalStatus(statusMap.get(bill.getApprovalStatus()));
                    bill.setMarketId(String.valueOf(platformMarketId));
                    billIdList.add(Long.valueOf(bill.getThirdEnterId()));
                    return bill;
                }).toList();

        if (CollectionUtils.isEmpty(billList)) {
            return new BaseOutput("200", "没有需要推送的报备单数据");
        }

        // 设置证件
        settingImageCerts(billIdList, billList);

        // 分批上报
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = billList.size() / batchSize;
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? billList.size() : (i + 1) * batchSize;
            List<ReportRegisterBillDto> partBills = billList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportRegisterBill(partBills, optUser, market);
        }

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName, marketId) : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.REGISTER_BILL.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    private void settingImageCerts(List<Long> billIdList, List<ReportRegisterBillDto> billList) {
        Map<Long, List<ImageCert>> imageCertMap = this.imageCertService.findImageCertListByBillIdList(billIdList)
                .stream().collect(Collectors.groupingBy(ImageCert::getBillId));
        billList.forEach(bill -> {
            // 照片处理
            List<CredentialInfoDto> pzAddVoList = new ArrayList<>();
            List<ImageCert> imageCerts = imageCertMap.get(Long.valueOf(bill.getThirdEnterId()));
            if (CollectionUtils.isNotEmpty(imageCerts)) {
                imageCerts.forEach(cert -> {
                    CredentialInfoDto credentialInfoDto = new CredentialInfoDto();
                    credentialInfoDto.setCredentialName(cert.getCertTypeName());
                    credentialInfoDto.setPicUrl(baseWebPath + cert.getUrl());
                    pzAddVoList.add(credentialInfoDto);
                });
            }
            bill.setPzAddVoList(pzAddVoList);
        });
    }

    public BaseOutput reportCheckIn(Optional<OperatorUser> optUser, Date endTime, Market market) {
        String tableName = ReportInterfaceEnum.CHECK_INOUT_RECORD.getCode();
        String interfaceName = ReportInterfaceEnum.CHECK_INOUT_RECORD.getName();
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();

        // 查询待上报的进门单
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        RegisterBillDto queryDto = new RegisterBillDto();
        queryDto.setMarketId(marketId);
        queryDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        if (thirdPartyPushData != null) {
            queryDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        List<ReportCheckInDto> checkInList = StreamEx.ofNullable(this.checkinOutRecordMapper.selectCheckInReport(queryDto))
                .nonNull().flatCollection(Function.identity()).map(checkIn -> {
                    checkIn.setMarketId(String.valueOf(platformMarketId));
                    return checkIn;
                }).toList();

        if (CollectionUtils.isEmpty(checkInList)) {
            return new BaseOutput("200", "没有需要推送的进门单数据");
        }

        // 分批上报
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
        // 分批数
        Integer part = checkInList.size() / batchSize;
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? checkInList.size() : (i + 1) * batchSize;
            List<ReportCheckInDto> partBills = checkInList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportCheckIn(partBills, optUser, market);
        }

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName, marketId) : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.CHECK_INOUT_RECORD.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }

    public BaseOutput reportOrder(String tableName, String interfaceName, Integer type, Optional<OperatorUser> optUser, Date endTime, Market market) {
        // 查询待上报的交易单
        Long marketId = market.getId();
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        PushDataQueryDto queryDto = new PushDataQueryDto();
        queryDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        queryDto.setOrderType(type);
        if (thirdPartyPushData != null) {
            queryDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        BaseOutput baseOutput = reportOrderLogic(type, optUser, queryDto, market);

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName, marketId) : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        } else {
            String name = type == 10 ? ReportInterfaceEnum.TRADE_REQUEST_DELIVERY.getName() : ReportInterfaceEnum.TRADE_REQUEST_SCAN.getName();
            logger.error("上报:{} 失败，原因:{}", name, baseOutput.getMessage());
        }
        return baseOutput;
    }

    private BaseOutput reportOrderLogic(Integer type, Optional<OperatorUser> optUser, PushDataQueryDto queryDto, Market market) {
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        List<String> requestIdList = new ArrayList<>();
        queryDto.setMarketId(marketId);
        // 10-配送交易 20-扫码交易
        if (type == 10) {
            List<ReportDeliveryOrderDto> deliveryOrderList = StreamEx.ofNullable(this.tradeRequestMapper.selectDeliveryOrderReport(queryDto))
                    .nonNull().flatCollection(Function.identity()).map(order -> {
                        order.setMarketId(String.valueOf(platformMarketId));
                        order.setThirdQrCode(this.baseWebPath + "/user?userId=" + order.getThirdDsId());
                        requestIdList.add(order.getThirdOrderId());
                        return order;
                    }).toList();

            if (CollectionUtils.isEmpty(deliveryOrderList)) {
                return new BaseOutput("200", "没有需要推送的配送交易单数据");
            }

            // 设置 detail
            Map<String, List<ReportOrderDetailDto>> detailMap = this.tradeRequestMapper.selectOrderDetailReport(requestIdList)
                    .stream().collect(Collectors.groupingBy(ReportOrderDetailDto::getRequestId));
            deliveryOrderList.forEach(order -> {
                order.setTradeList(detailMap.get(order.getThirdOrderId()));
            });

            // 分批上报
            Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
            // 分批数
            Integer part = deliveryOrderList.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? deliveryOrderList.size() : (i + 1) * batchSize;
                List<ReportDeliveryOrderDto> partBills = deliveryOrderList.subList(i * batchSize, endPos);
                baseOutput = this.dataReportService.reportDeliveryOrder(partBills, optUser, market);
            }
        } else {
            List<ReportScanCodeOrderDto> scanOrderList = StreamEx.ofNullable(this.tradeRequestMapper.selectScanOrderReport(queryDto))
                    .nonNull().flatCollection(Function.identity()).map(order -> {
                        order.setMarketId(String.valueOf(platformMarketId));
                        order.setThirdQrCode(this.baseWebPath + "/user?userId=" + order.getThirdBuyId());
                        requestIdList.add(order.getThirdOrderId());
                        return order;
                    }).toList();
            if (CollectionUtils.isEmpty(scanOrderList)) {
                return new BaseOutput("200", "没有需要推送的扫码交易单数据");
            }

            // 设置 detail
            Map<String, List<ReportOrderDetailDto>> detailMap = this.tradeRequestMapper.selectOrderDetailReport(requestIdList)
                    .stream().collect(Collectors.groupingBy(ReportOrderDetailDto::getRequestId));
            scanOrderList.forEach(order -> {
                order.setTradeList(detailMap.get(order.getThirdOrderId()));
            });

            // 分批上报
            Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
            // 分批数
            Integer part = scanOrderList.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? scanOrderList.size() : (i + 1) * batchSize;
                List<ReportScanCodeOrderDto> partBills = scanOrderList.subList(i * batchSize, endPos);

                baseOutput = this.dataReportService.reportScanCodeOrder(partBills, optUser, market);
            }
        }
        return baseOutput;
    }

    public BaseOutput reportOrderDelete(String tableName, String interfaceName, Integer type, Optional<OperatorUser> optUser, Date endTime, Market market) {
        // 查询待上报的交易单(退回单)
        Long marketId = market.getId();
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        PushDataQueryDto queryDto = new PushDataQueryDto();
        queryDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        queryDto.setOrderType(type);
        queryDto.setMarketId(marketId);
        if (thirdPartyPushData != null) {
            queryDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        BaseOutput baseOutput = new BaseOutput("200", "成功");
        // 10-配送交易 20-扫码交易
        if (type == 20) {
            baseOutput = reportDeletedScanCodeOrder(type, optUser, queryDto, market);
        } else {
            baseOutput = reportDeletedDeliveryOrder(type, optUser, queryDto, market);
        }

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName, marketId) : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        } else {
            String name = type == 10 ? ReportInterfaceEnum.TRADE_REQUEST_DELIVERY_DELETE.getName() : ReportInterfaceEnum.TRADE_REQUEST_SCAN_DELETE.getName();
            logger.error("上报:{} 失败，原因:{}", name, baseOutput.getMessage());
        }
        return baseOutput;
    }

    private BaseOutput reportDeletedScanCodeOrder(Integer type, Optional<OperatorUser> optUser, PushDataQueryDto queryDto, Market market) {
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        ReportDeletedOrderDto reportDeletedOrder = this.tradeRequestMapper.selectDeletedScanOrderReport(queryDto);
        if (reportDeletedOrder == null) {
            return baseOutput;
        }
        Long platformMarketId = market.getPlatformMarketId();
        reportDeletedOrder.setMarketId(String.valueOf(platformMarketId));

        return this.dataReportService.reportDeletedScanCodeOrder(reportDeletedOrder, optUser, market);
    }

    private BaseOutput reportDeletedDeliveryOrder(Integer type, Optional<OperatorUser> optUser, PushDataQueryDto queryDto, Market market) {
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        ReportDeletedOrderDto reportDeletedOrder = this.tradeRequestMapper.selectDeletedDeliveryOrderReport(queryDto);
        if (reportDeletedOrder == null) {
            return baseOutput;
        }
        Long platformMarketId = market.getPlatformMarketId();
        reportDeletedOrder.setMarketId(String.valueOf(platformMarketId));

        return this.dataReportService.reportDeletedDeliveryOrder(reportDeletedOrder, optUser, market);
    }


    private void pushStream(String tableName, String interfaceName,
                            Integer type, Optional<OperatorUser> optUser, Date endTime, Market market) {
        Long marketId = market.getId();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        UpStream upStream = new UpStream();
        upStream.setUpORdown(type);
        upStream.setMarketId(marketId);
        upStream.setMetadata(IDTO.AND_CONDITION_EXPR, "source_user_id is not null");
        if (thirdPartyPushData != null) {
            upStream.setMetadata(IDTO.AND_CONDITION_EXPR,
                    "modified>'" + DateUtils.format(thirdPartyPushData.getPushTime())
                            + "' and modified<='" + DateUtils.format(endTime) + "' and source_user_id is not null");
        }
        List<UpStream> upStreams = upStreamService.listByExample(upStream);
        if (upStreams == null || upStreams.size() == 0) {
            return;
        }
        BaseOutput baseOutput = new BaseOutput();
        ThirdPartyPushData pushData = new ThirdPartyPushData();
        pushData.setTableName(tableName);
        pushData.setInterfaceName(interfaceName);
        pushData.setMarketId(marketId);
        // 上游
        if (type.intValue() == 10) {
            baseOutput = reportUpStream(optUser, upStreams, pushData, endTime, market);
        } else {
            baseOutput = reportDownStream(optUser, upStreams, pushData, endTime, market);
        }

    }

    /**
     * 上报上游新增/修改
     *
     * @param optUser
     * @param upStreams
     * @param pushData
     * @param endTime
     * @return
     */
    private BaseOutput reportUpStream(Optional<OperatorUser> optUser, List<UpStream> upStreams, ThirdPartyPushData pushData, Date endTime, Market market) {
        BaseOutput baseOutput = new BaseOutput();
        List<UpStreamDto> upStreamDtos = new ArrayList<>();
        StreamEx.of(upStreams).forEach(td -> {
            UpStreamDto upStreamDto = new UpStreamDto();
            upStreamDto.setMarketId(String.valueOf(market.getPlatformMarketId()));
            upStreamDto.setIdCard(td.getIdCard());
            upStreamDto.setLegalPerson(td.getLegalPerson());
            upStreamDto.setLicense(td.getLicense());
            upStreamDto.setQyName(td.getName());
            upStreamDto.setTel(td.getTelphone());
            upStreamDto.setThirdAccountId(td.getSourceUserId() == null ? "" : td.getSourceUserId().toString());
            upStreamDto.setThirdUpId(td.getId().toString());
            int upStreamType = td.getUpstreamType().intValue();
            List<PzVo> poVoList = new ArrayList<>();
            upStreamDto.setPzVoList(poVoList);
            // 10 个人
            if (upStreamType == 10) {
                upStreamDto.setType(1);
                if (StringUtils.isNotBlank(td.getCardNoFrontUrl())) {
                    PzVo pzVoFront = new PzVo();
                    pzVoFront.setCredentialName(ReportInterfacePicEnum.ID_CARD_FRONT.getName());
                    pzVoFront.setPicUrl(baseWebPath + td.getCardNoFrontUrl());
                    poVoList.add(pzVoFront);
                }

                if (StringUtils.isNotBlank(td.getCardNoBackUrl())) {
                    PzVo pzVoBack = new PzVo();
                    pzVoBack.setCredentialName(ReportInterfacePicEnum.ID_CARD_REVERSE.getName());
                    pzVoBack.setPicUrl(baseWebPath + td.getCardNoBackUrl());
                    poVoList.add(pzVoBack);
                }

            } else {
                upStreamDto.setType(0);
                if (StringUtils.isNotBlank(td.getBusinessLicenseUrl())) {
                    PzVo pzVoBusiness = new PzVo();
                    pzVoBusiness.setCredentialName(ReportInterfacePicEnum.BUSINESS_LICENSE.getName());
                    pzVoBusiness.setPicUrl(baseWebPath + td.getBusinessLicenseUrl());
                    poVoList.add(pzVoBusiness);

                }
                if (StringUtils.isNotBlank(td.getManufacturingLicenseUrl())) {
                    PzVo pzVoManu = new PzVo();
                    pzVoManu.setCredentialName(ReportInterfacePicEnum.PRODUCTION_LICENSE.getName());
                    pzVoManu.setPicUrl(baseWebPath + td.getManufacturingLicenseUrl());
                    poVoList.add(pzVoManu);
                }
                if (StringUtils.isNotBlank(td.getOperationLicenseUrl())) {
                    PzVo pzVoOperate = new PzVo();
                    pzVoOperate.setCredentialName(ReportInterfacePicEnum.OPERATING_LICENSE.getName());
                    pzVoOperate.setPicUrl(baseWebPath + td.getOperationLicenseUrl());
                    poVoList.add(pzVoOperate);
                }

            }
            upStreamDtos.add(upStreamDto);
        });
        if (upStreamDtos.size() > 0) {

            // 分批上报
            Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
            // 分批数
            Integer part = upStreamDtos.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? upStreamDtos.size() : (i + 1) * batchSize;
                List<UpStreamDto> partBills = upStreamDtos.subList(i * batchSize, endPos);
                baseOutput = this.dataReportService.reportUpStream(partBills, optUser, market);
            }

            if (baseOutput.isSuccess()) {
                this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            } else {
                logger.error("上报:{} 失败，原因:{}", pushData.getInterfaceName(), baseOutput.getMessage());
            }
        }
        return baseOutput;
    }

    /**
     * 上报下游新增/修改
     *
     * @param optUser
     * @param upStreams
     * @param pushData
     * @param endTime
     * @return
     */
    private BaseOutput reportDownStream(Optional<OperatorUser> optUser, List<UpStream> upStreams, ThirdPartyPushData pushData, Date endTime, Market market) {
        BaseOutput baseOutput = new BaseOutput();
        List<DownStreamDto> downStreamDtos = new ArrayList<>();
        StreamEx.of(upStreams).forEach(td -> {
            DownStreamDto downStreamDto = new DownStreamDto();
            downStreamDto.setMarketId(String.valueOf(market.getPlatformMarketId()));
            downStreamDto.setIdCard(td.getIdCard());
            downStreamDto.setLegalPerson(td.getLegalPerson());
            downStreamDto.setLicense(td.getLicense());
            downStreamDto.setTel(td.getTelphone());
            downStreamDto.setThirdAccountId(td.getSourceUserId() == null ? "" : td.getSourceUserId().toString());
            downStreamDto.setThirdDsId(td.getId().toString());
            int upStreamType = td.getUpstreamType().intValue();
            List<DownStreamImg> downStreamImgs = new ArrayList<>();
            downStreamDto.setDownStreamImgList(downStreamImgs);
            // 10 个人
            if (upStreamType == 10) {
                downStreamDto.setName(td.getName());
                downStreamDto.setType(1);
                if (StringUtils.isNotBlank(td.getCardNoFrontUrl())) {
                    DownStreamImg downStreamImgFront = new DownStreamImg();
                    downStreamImgFront.setCredentialName(ReportInterfacePicEnum.ID_CARD_FRONT.getName());
                    downStreamImgFront.setPicUrl(baseWebPath + td.getCardNoFrontUrl());
                    downStreamImgs.add(downStreamImgFront);
                }
                if (StringUtils.isNotBlank(td.getCardNoBackUrl())) {
                    DownStreamImg downStreamImgBack = new DownStreamImg();
                    downStreamImgBack.setCredentialName(ReportInterfacePicEnum.ID_CARD_REVERSE.getName());
                    downStreamImgBack.setPicUrl(baseWebPath + td.getCardNoBackUrl());
                    downStreamImgs.add(downStreamImgBack);
                }

            } else {
                downStreamDto.setStreamName(td.getName());
                downStreamDto.setType(0);
                if (StringUtils.isNotBlank(td.getBusinessLicenseUrl())) {
                    DownStreamImg pzVoBusiness = new DownStreamImg();
                    pzVoBusiness.setCredentialName(ReportInterfacePicEnum.BUSINESS_LICENSE.getName());
                    pzVoBusiness.setPicUrl(baseWebPath + td.getBusinessLicenseUrl());
                    downStreamImgs.add(pzVoBusiness);
                }
                if (StringUtils.isNotBlank(td.getManufacturingLicenseUrl())) {
                    DownStreamImg pzVoManu = new DownStreamImg();
                    pzVoManu.setCredentialName(ReportInterfacePicEnum.PRODUCTION_LICENSE.getName());
                    pzVoManu.setPicUrl(baseWebPath + td.getManufacturingLicenseUrl());
                    downStreamImgs.add(pzVoManu);
                }
                if (StringUtils.isNotBlank(td.getOperationLicenseUrl())) {
                    DownStreamImg pzVoOperate = new DownStreamImg();
                    pzVoOperate.setCredentialName(ReportInterfacePicEnum.OPERATING_LICENSE.getName());
                    pzVoOperate.setPicUrl(baseWebPath + td.getOperationLicenseUrl());
                    downStreamImgs.add(pzVoOperate);
                }
            }
            downStreamDtos.add(downStreamDto);
        });
        if (downStreamDtos.size() > 0) {

            // 分批上报
            Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 64 : pushBatchSize;
            // 分批数
            Integer part = downStreamDtos.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? downStreamDtos.size() : (i + 1) * batchSize;
                List<DownStreamDto> partBills = downStreamDtos.subList(i * batchSize, endPos);
                baseOutput = this.dataReportService.reportDownStream(partBills, optUser, market);
            }

            if (baseOutput.isSuccess()) {
                this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            } else {
                logger.error("上报:{} 失败，原因:{}", pushData.getInterfaceName(), baseOutput.getMessage());
            }
        }
        return baseOutput;
    }

    public BaseOutput reportRegisterBillDelete(Optional<OperatorUser> optUser, Date endTime, Market market) {
        String tableName = ReportInterfaceEnum.REGISTER_BILL_DELETE.getCode();
        String interfaceName = ReportInterfaceEnum.REGISTER_BILL_DELETE.getName();
        Integer isDelete = 1;
        Long marketId = market.getId();
        Long platformMarketId = market.getPlatformMarketId();
        // 查询待上报的报备单
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName, marketId);
        RegisterBillDto billDto = new RegisterBillDto();
        billDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        if (thirdPartyPushData != null) {
            billDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        billDto.setIsDeleted(isDelete);
        billDto.setMarketId(marketId);
        Set<String> billIdSet = new HashSet<>();
        StreamEx.ofNullable(this.registerBillMapper.selectRegisterBillReport(billDto))
                .nonNull().flatCollection(Function.identity()).forEach(bill -> {
            // 状态映射
            billIdSet.add(bill.getThirdEnterId());
        });
        if (CollectionUtils.isEmpty(billIdSet)) {
            return new BaseOutput("200", "没有需要推送的报备单数据");
        }
        String billIds = String.join(",", billIdSet);
        ReportRegisterBillDeleteDto deleteDto = new ReportRegisterBillDeleteDto();
        deleteDto.setMarketId(String.valueOf(platformMarketId));
        deleteDto.setThirdEnterIds(billIds);
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        baseOutput = this.dataReportService.reportRegisterBillDelete(deleteDto, optUser, market);
        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName, marketId) : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.REGISTER_BILL_DELETE.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }
}