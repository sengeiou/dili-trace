package com.dili.trace.jobs;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
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
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.service.*;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
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
    private CategoryService categoryService;
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


    @Value("${current.baseWebPath}")
    private String baseWebPath;
    @Value("${push.batch.size}")
    private Integer pushBatchSize;
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;

    private String marketId = "330110800";

    @Override
    public void run(String... args) throws Exception {
        //Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        //this.reportRegisterBill(optUser);
        /*Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        this.pushBigCategory(optUser);
        this.pushCategory("category_smallClass", "商品二级类目新增/修改", 1, optUser);
        this.pushCategory("category_goods", "商品新增/修改", 2, optUser);
       *//* Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        this.pushUserQrCode(optUser);
        this.pushUserSaveUpdate(optUser);
        this.pushUserDelete(optUser);*//*
        this.pushStream("upstream_up", "上游新增/修改", 10, optUser);
        this.pushStream("upstream_down", "下游新增/修改", 20, optUser);*/
    }

    // 每五分钟提交一次数据
    @Scheduled(cron = "0 */5 * * * ?")
    public void pushData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            this.pushBigCategory(optUser);
            this.pushCategory("category_smallClass", "商品二级类目新增/修改", 1, optUser);
            this.pushCategory("category_goods", "商品新增/修改", 2, optUser);
            this.pushStream("upstream_up", "上游新增/修改", 10, optUser);
            this.pushStream("upstream_down", "下游新增/修改", 20, optUser);
            this.reportRegisterBill(optUser);// 报备新增/编辑
            this.reportCheckIn(optUser);// 进门
            this.reportOrder("trade_request_delivery", "配送交易", 10, optUser);// 配送交易
            this.reportOrder("trade_request_scan", "扫码交易", 20, optUser);// 扫码交易
            this.pushUserQrCode(optUser);//食安码新增/修改
            this.pushUserSaveUpdate(optUser);//经营户新增/修改
            this.pushUserDelete(optUser);//经营户作废
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void pushBigCategory(Optional<OperatorUser> optUser) {
        String tableName = "category_bigClass";
        String interfaceName = "商品大类新增/修改";
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName);
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
            BaseOutput baseOutput = this.dataReportService.reportCategory(categoryDtos, optUser);
            if (baseOutput.isSuccess()) {
                ThirdPartyPushData pushData = new ThirdPartyPushData();
                pushData.setTableName(tableName);
                pushData.setInterfaceName(interfaceName);
                this.thirdPartyPushDataService.updatePushTime(pushData);
            } else {
                logger.error("上报:商品大类新增/修改 失败，原因:{}", baseOutput.getMessage());
            }
        }
    }

    private void pushCategory(String tableName, String interfaceName,
                              Integer level, Optional<OperatorUser> optUser) {
        Date endTime = new Date();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName);
        Category category = new Category();
        category.setLevel(level);
        List<Category> categories = categoryService.list(category);
        ThirdPartyPushData pushData = new ThirdPartyPushData();
        pushData.setTableName(tableName);
        pushData.setInterfaceName(interfaceName);
        BaseOutput baseOutput = new BaseOutput();
        if (tableName.equals("category_smallClass")) {
            List<CategorySecondDto> categoryDtos = new ArrayList<>();
            int i = 1;
            PreserveTypeEnum[] preserveTypeEnums = PreserveTypeEnum.values();
            for (PreserveTypeEnum type : preserveTypeEnums) {
                for (Category td : categories) {
                    if (thirdPartyPushData == null || (thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0
                            && td.getModified().compareTo(endTime) <= 0)) {
                        CategorySecondDto categoryDto = new CategorySecondDto();
                        categoryDto.setThirdSmallClassId(td.getId().toString());
                        categoryDto.setSmallClassName(td.getName());
                        categoryDto.setThirdBigClassId(String.valueOf(i));
                        categoryDtos.add(categoryDto);
                    }
                }
                ;
                i++;
            }
            if (categoryDtos.size() > 0) {
                baseOutput = this.dataReportService.reportSecondCategory(categoryDtos, optUser);
                if (baseOutput.isSuccess()) {
                    this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
                } else {
                    logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
                }
            }
        } else if (tableName.equals("category_goods")) {
            List<GoodsDto> categoryDtos = new ArrayList<>();
            StreamEx.of(categories).forEach(td -> {
                if (thirdPartyPushData == null || (thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0
                        && td.getModified().compareTo(endTime) <= 0)) {
                    GoodsDto categoryDto = new GoodsDto();
                    categoryDto.setGoodsName(td.getName());
                    categoryDto.setThirdGoodsId(td.getId().toString());
                    categoryDto.setThirdSmallClassId(td.getParentId().toString());
                    categoryDtos.add(categoryDto);
                }
            });
            if (categoryDtos.size() > 0) {
                baseOutput = this.dataReportService.reportGoods(categoryDtos, optUser);
                if (baseOutput.isSuccess()) {
                    this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
                } else {
                    logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
                }
            }
        }


    }

    private BaseOutput pushUserQrCode(Optional<OperatorUser> optUser) {
        Date updateTime = null;
        boolean newPushFlag = true;
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData("user_qr_history");
        if (pushData == null) {
            updateTime = new Date();
            pushData = new ThirdPartyPushData();
            pushData.setTableName("user_qr_history");
            pushData.setInterfaceName("食安码新增/修改");
            pushData.setPushTime(updateTime);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        Date finalUpdateTime = updateTime;
        boolean finalNewPushFlag = newPushFlag;
        List<UserQrHistory> qrHistories = new ArrayList<>();
        //取有效二维码变更记录
        UserQrHistory queryQr = new UserQrHistory();
        queryQr.setIsValid(1);
        StreamEx.ofNullable(userQrHistoryService.list(queryQr)).nonNull().flatCollection(Function.identity()).forEach(q -> {
            if (finalNewPushFlag || finalUpdateTime.compareTo(q.getModified()) < 0) {
                qrHistories.add(q);
            }
        });
        List<ReportQrCodeDto> pushList = thirdDataReportService.reprocessUserQrCode(qrHistories);
        logger.info("ReportQrCodeDto ： " + JSON.toJSONString(pushList));
        // 分批上报--由于数据结构较为庞大与其他分批不同，单独分批
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = 100;
        Integer part = pushList.size() / batchSize; // 分批数
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? pushList.size() : (i + 1) * batchSize;
            List<ReportQrCodeDto> partBills = pushList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportUserQrCode(partBills, optUser);
        }

        //BaseOutput baseOutput = this.dataReportService.reportUserQrCode(pushList, optUser);
        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData);
        }
        return baseOutput;
    }

    private BaseOutput pushUserSaveUpdate(Optional<OperatorUser> optUser) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Integer normalUserType = 1;
        List<ReportUserDto> reportUserDtoList = new ArrayList<>();
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData("user");
        if (pushData == null) {
            updateTime = new Date();
            pushData = new ThirdPartyPushData();
            pushData.setTableName("user");
            pushData.setInterfaceName("经营户新增/编辑");
            pushData.setPushTime(updateTime);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }
        //获取正常经营的经营户列表（排除未实名的用户）
        Date finalUpdateTime = updateTime;
        boolean finalNewPushFlag = newPushFlag;
        User queUser = DTOUtils.newDTO(User.class);
        queUser.setYn(normalUserType);
        queUser.mset(IDTO.AND_CONDITION_EXPR, " validate_state <> 10");
        StreamEx.ofNullable(this.userService.listByExample(queUser))
                .nonNull().flatCollection(Function.identity()).map(info -> {
            //push后修改了用户信息
            if (finalNewPushFlag || finalUpdateTime.compareTo(info.getModified()) < 0) {
                ReportUserDto reportUser = thirdDataReportService.reprocessUser(info);
                reportUserDtoList.add(reportUser);
                return true;
            }
            return false;
        }).toList();

        // 分批上报  由于数据结构较为庞大与其他分批不同，单独分批
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = 70;
        Integer part = reportUserDtoList.size() / batchSize; // 分批数
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? reportUserDtoList.size() : (i + 1) * batchSize;
            List<ReportUserDto> partBills = reportUserDtoList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportUserSaveUpdate(partBills, optUser);
        }
        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData);
        }
        return baseOutput;
    }

    private BaseOutput pushUserDelete(Optional<OperatorUser> optUser) {
        Date updateTime = null;
        boolean newPushFlag = true;
        ThirdPartyPushData pushData = thirdPartyPushDataService.getThredPartyPushData("user_delete");
        if (pushData == null) {
            updateTime = new Date();
            pushData = new ThirdPartyPushData();
            pushData.setTableName("user_delete");
            pushData.setInterfaceName("经营户作废");
            pushData.setPushTime(updateTime);
        } else {
            updateTime = pushData.getPushTime();
            newPushFlag = false;
        }

        boolean finalNewPushFlag = newPushFlag;
        Timestamp sqlPushTime = new Timestamp(updateTime.getTime());
        User queUser = DTOUtils.newDTO(User.class);
        //没有push过则将所有作废记录push
        if (finalNewPushFlag) {
            queUser.mset(IDTO.AND_CONDITION_EXPR, " yn = -1 and validate_state <> 10");
        } else {
            queUser.mset(IDTO.AND_CONDITION_EXPR, " yn = -1 and validate_state <> 10 and modified > '" + sqlPushTime + "'");
        }
        List<User> userList = this.userService.listByExample(queUser);
        ReportUserDeleteDto reportUser = new ReportUserDeleteDto();
        reportUser.setMarketId(marketId);
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
        BaseOutput baseOutput = this.dataReportService.reportUserDelete(reportUser, optUser);
        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData);
        }
        return baseOutput;
    }


    public BaseOutput reportRegisterBill(Optional<OperatorUser> optUser) {

        String tableName = "register_bill";
        String interfaceName = "报备新增/编辑";
        Date endTime = new Date();
        // verify_status "待审核"0, "已退回10, "已通过20, "不通过30
        // approvalStatus 审核状态 0-默认未审核 1-通过 2-退回 3-未通过
        Map<Integer, Integer> statusMap = new HashMap<>();
        statusMap.put(0, 0);
        statusMap.put(20, 1);
        statusMap.put(10, 2);
        statusMap.put(30, 3);

        // 查询待上报的报备单
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName);
        RegisterBillDto billDto = new RegisterBillDto();
        billDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        if (thirdPartyPushData != null) {
            billDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        List<ReportRegisterBillDto> billList = StreamEx.ofNullable(this.registerBillMapper.selectRegisterBillReport(billDto))
                .nonNull().flatCollection(Function.identity()).map(bill -> {
                    return dealReportRegisterBillDto(statusMap, bill);
                }).toList();

        if (CollectionUtils.isEmpty(billList)) {
            return new BaseOutput("200", "没有需要推送的报备单数据");
        }

        // 分批上报
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 500 : pushBatchSize;
        Integer part = billList.size() / batchSize; // 分批数
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? billList.size() : (i + 1) * batchSize;
            List<ReportRegisterBillDto> partBills = billList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportRegisterBill(partBills, optUser);
        }

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData() : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        }
        return baseOutput;
    }

    public BaseOutput reportCheckIn(Optional<OperatorUser> optUser) {

        String tableName = "checkinout_record";
        String interfaceName = "进门";
        Date endTime = new Date();

        // 查询待上报的进门单
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName);
        RegisterBillDto queryDto = new RegisterBillDto();
        queryDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        if (thirdPartyPushData != null) {
            queryDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        List<ReportCheckInDto> checkInList = StreamEx.ofNullable(this.checkinOutRecordMapper.selectCheckInReport(queryDto))
                .nonNull().flatCollection(Function.identity()).map(checkIn -> {
                    checkIn.setMarketId(marketId);
                    return checkIn;
                }).toList();

        if (CollectionUtils.isEmpty(checkInList)) {
            return new BaseOutput("200", "没有需要推送的进门单数据");
        }

        // 分批上报
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 500 : pushBatchSize;
        Integer part = checkInList.size() / batchSize; // 分批数
        // 上报
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? checkInList.size() : (i + 1) * batchSize;
            List<ReportCheckInDto> partBills = checkInList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportCheckIn(partBills, optUser);
        }

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData() : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        }
        return baseOutput;
    }

    private ReportRegisterBillDto dealReportRegisterBillDto(Map<Integer, Integer> statusMap, ReportRegisterBillDto bill) {
        // 状态映射
        bill.setApprovalStatus(statusMap.get(bill.getApprovalStatus()));
        bill.setMarketId(marketId);
        // 照片处理
        List<CredentialInfoDto> pzAddVoList = new ArrayList<>();
        List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(Long.valueOf(bill.getThirdEnterId()));
        if (CollectionUtils.isNotEmpty(imageCerts)) {
            imageCerts.forEach(cert -> {
                CredentialInfoDto credentialInfoDto = new CredentialInfoDto();
                credentialInfoDto.setCredentialName(cert.getCertTypeName());
                credentialInfoDto.setPicUrl(baseWebPath + cert.getUrl());
                pzAddVoList.add(credentialInfoDto);
            });
        }
        bill.setPzAddVoList(pzAddVoList);
        return bill;
    }

    public BaseOutput reportOrder(String tableName, String interfaceName, Integer type, Optional<OperatorUser> optUser) {
        Date endTime = new Date();
        // 查询待上报的交易单
        ThirdPartyPushData thirdPartyPushData = thirdPartyPushDataService.getThredPartyPushData(tableName);
        PushDataQueryDto queryDto = new PushDataQueryDto();
        queryDto.setModifiedEnd(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        queryDto.setOrderType(type);
        if (thirdPartyPushData != null) {
            queryDto.setModifiedStart(DateUtil.format(thirdPartyPushData.getPushTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        BaseOutput baseOutput = reportOrderLogic(type, optUser, queryDto);

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName) : thirdPartyPushData;
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData, endTime);
        }
        return baseOutput;
    }

    private BaseOutput reportOrderLogic(Integer type, Optional<OperatorUser> optUser, PushDataQueryDto queryDto) {
        BaseOutput baseOutput = new BaseOutput("200", "成功");
        // 10-配送交易 20-扫码交易
        if (type == 10) {

            List<ReportDeliveryOrderDto> deliveryOrderList = StreamEx.ofNullable(this.tradeRequestMapper.selectDeliveryOrderReport(queryDto))
                    .nonNull().flatCollection(Function.identity()).map(order -> {
                        order.setMarketId(marketId);
                        order.setThirdQrCode(this.baseWebPath + "/user?userId=" + order.getThirdDsId());
                        PushDataQueryDto queryDetailDto = new PushDataQueryDto();
                        queryDetailDto.setTradeRequestId(order.getThirdOrderId());
                        List<ReportOrderDetailDto> reportOrderDetailDtos = this.tradeRequestMapper.selectOrderDetailReport(queryDetailDto);
                        order.setTradeList(reportOrderDetailDtos);
                        return order;
                    }).toList();


            if (CollectionUtils.isEmpty(deliveryOrderList)) {
                return new BaseOutput("200", "没有需要推送的配送交易单数据");
            }

            // 分批上报
            Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 500 : pushBatchSize;
            Integer part = deliveryOrderList.size() / batchSize; // 分批数
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? deliveryOrderList.size() : (i + 1) * batchSize;
                List<ReportDeliveryOrderDto> partBills = deliveryOrderList.subList(i * batchSize, endPos);
                baseOutput = this.dataReportService.reportDeliveryOrder(partBills, optUser);
            }
        } else {
            List<ReportScanCodeOrderDto> scanOrderList = StreamEx.ofNullable(this.tradeRequestMapper.selectScanOrderReport(queryDto))
                    .nonNull().flatCollection(Function.identity()).map(order -> {
                        order.setMarketId(marketId);
                        order.setThirdQrCode(this.baseWebPath + "/user?userId=" + order.getThirdBuyId());
                        PushDataQueryDto queryDetailDto = new PushDataQueryDto();
                        queryDetailDto.setTradeRequestId(order.getThirdOrderId());
                        List<ReportOrderDetailDto> reportOrderDetailDtos = this.tradeRequestMapper.selectOrderDetailReport(queryDetailDto);
                        order.setTradeList(reportOrderDetailDtos);
                        return order;
                    }).toList();
            if (CollectionUtils.isEmpty(scanOrderList)) {
                return new BaseOutput("200", "没有需要推送的扫码交易单数据");
            }

            // 分批上报
            Integer batchSize = (pushBatchSize == null || pushBatchSize == 0) ? 500 : pushBatchSize;
            Integer part = scanOrderList.size() / batchSize; // 分批数
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? scanOrderList.size() : (i + 1) * batchSize;
                List<ReportScanCodeOrderDto> partBills = scanOrderList.subList(i * batchSize, endPos);

                baseOutput = this.dataReportService.reportScanCodeOrder(partBills, optUser);
            }
        }
        return baseOutput;
    }

    private void pushStream(String tableName, String interfaceName,
                            Integer type, Optional<OperatorUser> optUser) {
        Date endTime = new Date();
        ThirdPartyPushData thirdPartyPushData =
                thirdPartyPushDataService.getThredPartyPushData(tableName);
        UpStream upStream = new UpStream();
        upStream.setUpORdown(type);
        if (thirdPartyPushData != null) {
            upStream.setMetadata(IDTO.AND_CONDITION_EXPR,
                    "modified>'" + DateUtils.format(thirdPartyPushData.getPushTime())
                            + "' and modified<='" + DateUtils.format(endTime) + "'");
        }
        List<UpStream> upStreams = upStreamService.listByExample(upStream);
        if (upStreams == null || upStreams.size() == 0) {
            return;
        }
        BaseOutput baseOutput = new BaseOutput();
        ThirdPartyPushData pushData = new ThirdPartyPushData();
        pushData.setTableName(tableName);
        pushData.setInterfaceName(interfaceName);
        // 上游
        if (type.intValue() == 10) {
            baseOutput = reportUpStream(optUser, upStreams, pushData, endTime);
        } else {
            baseOutput = reportDownStream(optUser, upStreams, pushData, endTime);
        }

    }

    private BaseOutput reportUpStream(Optional<OperatorUser> optUser, List<UpStream> upStreams, ThirdPartyPushData pushData, Date endTime) {
        BaseOutput baseOutput = new BaseOutput();
        List<UpStreamDto> upStreamDtos = new ArrayList<>();
        StreamEx.of(upStreams).forEach(td -> {
            UpStreamDto upStreamDto = new UpStreamDto();
            upStreamDto.setIdCard(td.getIdCard());
            upStreamDto.setLegalPerson(td.getLegalPerson());
            upStreamDto.setLicense(td.getLicense());
            upStreamDto.setQyName(td.getName());
            upStreamDto.setTel(td.getTelphone());
            upStreamDto.setThirdAccountId(td.getSourceUserId() == null ? "" : td.getSourceUserId().toString());
            upStreamDto.setThirdUpId(td.getId().toString());
            int upStreamType = td.getUpstreamType().intValue();
            List<UpStreamDto.PzVo> poVoList = new ArrayList<>();
            upStreamDto.setPzVoList(poVoList);
            // 10 个人
            if (upStreamType == 10) {
                upStreamDto.setType(1);
                UpStreamDto.PzVo pzVoFront = new UpStreamDto.PzVo();
                pzVoFront.setCredentialName("身份证正面");
                pzVoFront.setPicUrl(baseWebPath + td.getCardNoFrontUrl());

                UpStreamDto.PzVo pzVoBack = new UpStreamDto.PzVo();
                pzVoBack.setCredentialName("身份证反面");
                pzVoBack.setPicUrl(baseWebPath + td.getCardNoBackUrl());

                poVoList.add(pzVoFront);
                poVoList.add(pzVoBack);

            } else {
                upStreamDto.setType(0);
                UpStreamDto.PzVo pzVoBusiness = new UpStreamDto.PzVo();
                pzVoBusiness.setCredentialName("营业执照");
                pzVoBusiness.setPicUrl(baseWebPath + td.getBusinessLicenseUrl());

                UpStreamDto.PzVo pzVoManu = new UpStreamDto.PzVo();
                pzVoManu.setCredentialName("生产许可证");
                pzVoManu.setPicUrl(baseWebPath + td.getManufacturingLicenseUrl());

                UpStreamDto.PzVo pzVoOperate = new UpStreamDto.PzVo();
                pzVoOperate.setCredentialName("经营许可证");
                pzVoOperate.setPicUrl(baseWebPath + td.getOperationLicenseUrl());

                poVoList.add(pzVoBusiness);
                poVoList.add(pzVoOperate);
                poVoList.add(pzVoOperate);
            }
            upStreamDtos.add(upStreamDto);
        });
        if (upStreamDtos.size() > 0) {
            baseOutput = dataReportService.reportUpStream(upStreamDtos, optUser);
            if (baseOutput.isSuccess()) {
                this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            } else {
                logger.error("上报:{} 失败，原因:{}", pushData.getInterfaceName(), baseOutput.getMessage());
            }
        }
        return baseOutput;
    }

    private BaseOutput reportDownStream(Optional<OperatorUser> optUser, List<UpStream> upStreams, ThirdPartyPushData pushData, Date endTime) {
        BaseOutput baseOutput = new BaseOutput();
        List<DownStreamDto> downStreamDtos = new ArrayList<>();
        StreamEx.of(upStreams).forEach(td -> {
            DownStreamDto downStreamDto = new DownStreamDto();
            downStreamDto.setIdCard(td.getIdCard());
            downStreamDto.setLegalPerson(td.getLegalPerson());
            downStreamDto.setLicense(td.getLicense());
            downStreamDto.setTel(td.getTelphone());
            downStreamDto.setThirdAccountId(td.getSourceUserId() == null ? "" : td.getSourceUserId().toString());
            downStreamDto.setThirdDsId(td.getId().toString());
            int upStreamType = td.getUpstreamType().intValue();
            List<DownStreamDto.DownStreamImg> downStreamImgs = new ArrayList<>();
            downStreamDto.setDownStreamImgList(downStreamImgs);
            // 10 个人
            if (upStreamType == 10) {
                downStreamDto.setName(td.getName());
                downStreamDto.setType(1);

                DownStreamDto.DownStreamImg downStreamImgFront = new DownStreamDto.DownStreamImg();
                downStreamImgFront.setCredentialName("身份证正面");
                downStreamImgFront.setPicUrl(baseWebPath + td.getCardNoFrontUrl());

                DownStreamDto.DownStreamImg downStreamImgBack = new DownStreamDto.DownStreamImg();
                downStreamImgBack.setCredentialName("身份证反面");
                downStreamImgBack.setPicUrl(baseWebPath + td.getCardNoBackUrl());

                downStreamImgs.add(downStreamImgFront);
                downStreamImgs.add(downStreamImgBack);

            } else {
                downStreamDto.setStreamName(td.getName());
                downStreamDto.setType(0);
                DownStreamDto.DownStreamImg pzVoBusiness = new DownStreamDto.DownStreamImg();
                pzVoBusiness.setCredentialName("营业执照");
                pzVoBusiness.setPicUrl(baseWebPath + td.getBusinessLicenseUrl());

                DownStreamDto.DownStreamImg pzVoManu = new DownStreamDto.DownStreamImg();
                pzVoManu.setCredentialName("生产许可证");
                pzVoManu.setPicUrl(baseWebPath + td.getManufacturingLicenseUrl());

                DownStreamDto.DownStreamImg pzVoOperate = new DownStreamDto.DownStreamImg();
                pzVoOperate.setCredentialName("经营许可证");
                pzVoOperate.setPicUrl(baseWebPath + td.getOperationLicenseUrl());

                downStreamImgs.add(pzVoBusiness);
                downStreamImgs.add(pzVoOperate);
                downStreamImgs.add(pzVoOperate);
            }
            downStreamDtos.add(downStreamDto);
        });
        if (downStreamDtos.size() > 0) {
            baseOutput = dataReportService.reportDownStream(downStreamDtos, optUser);
            if (baseOutput.isSuccess()) {
                this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
            } else {
                logger.error("上报:{} 失败，原因:{}", pushData.getInterfaceName(), baseOutput.getMessage());
            }
        }
        return baseOutput;
    }

}