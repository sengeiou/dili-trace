package com.dili.trace.jobs;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.OperatorUser;
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
        this.pushCategory("category_goods", "商品新增/修改", 2, optUser);*/
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        this.pushUserQrCode(optUser);
        this.pushUserSaveUpdate(optUser);
    }

    // 每五分钟提交一次数据
    @Scheduled(cron = "0 */5 * * * ?")
    public void pushData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            this.pushBigCategory(optUser);
            this.pushCategory("category_smallClass", "商品二级类目新增/修改", 1, optUser);
            this.pushCategory("category_goods", "商品新增/修改", 2, optUser);
            this.reportRegisterBill(optUser);
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
                    if (thirdPartyPushData == null || thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0) {
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
            baseOutput = this.dataReportService.reportSecondCategory(categoryDtos, optUser);
        } else if (tableName.equals("category_goods")) {
            List<GoodsDto> categoryDtos = new ArrayList<>();
            StreamEx.of(categories).forEach(td -> {
                if (thirdPartyPushData == null || thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0) {
                    GoodsDto categoryDto = new GoodsDto();
                    categoryDto.setGoodsName(td.getName());
                    categoryDto.setThirdGoodsId(td.getId().toString());
                    categoryDto.setThirdSmallClassId(td.getParentId().toString());
                    categoryDtos.add(categoryDto);
                }
            });
            baseOutput = this.dataReportService.reportGoods(categoryDtos, optUser);
        }

        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData);
        } else {
            logger.error("上报:{} 失败，原因:{}", interfaceName, baseOutput.getMessage());
        }

    }

    private BaseOutput pushUserQrCode(Optional<OperatorUser> optUser) {
        Date updateTime = null;
        boolean newPushFlag = true;
        List<ReportQrCodeDto> reportUserDtoList = new ArrayList<>();
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

        StreamEx.ofNullable(userQrHistoryService.list(null)).nonNull().flatCollection(Function.identity()).forEach(q -> {
            if (finalNewPushFlag || finalUpdateTime.compareTo(q.getModified()) < 0) {
                qrHistories.add(q);
            }
        });
        List<ReportQrCodeDto> pushList = thirdDataReportService.reprocessUserQrCode(qrHistories);
        System.out.print("ReportQrCodeDto:" + JSON.toJSONString(pushList));
        BaseOutput baseOutput = this.dataReportService.reportUserQrCode(pushList, optUser);
        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData);
        }
        return baseOutput;
    }

    private BaseOutput pushUserSaveUpdate(Optional<OperatorUser> optUser) {
        Date updateTime = null;
        boolean newPushFlag = true;
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

        Date finalUpdateTime = updateTime;
        boolean finalNewPushFlag = newPushFlag;
        StreamEx.ofNullable(this.userService.list(null))
                .nonNull().flatCollection(Function.identity()).map(info -> {
            //push后修改了用户信息
            if (finalNewPushFlag || finalUpdateTime.compareTo(info.getModified()) < 0) {
                ReportUserDto reportUser = thirdDataReportService.reprocessUser(info);
                reportUserDtoList.add(reportUser);
                return true;
            }
            return false;
        }).toList();

        System.out.print("pushUserSaveUpdate:" + JSON.toJSONString(reportUserDtoList));
        BaseOutput baseOutput =this.dataReportService.reportUserSaveUpdate(reportUserDtoList, optUser);
        if(baseOutput.isSuccess()){
            this.thirdPartyPushDataService.updatePushTime(pushData);
        }
        return baseOutput;
    }

    private BaseOutput pushUserDelete(Optional<OperatorUser> optUser) {
        Date updateTime = null;
        boolean newPushFlag = true;
        List<ReportUserDto> reportUserDtoList = new ArrayList<>();
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

        Date finalUpdateTime = updateTime;
        boolean finalNewPushFlag = newPushFlag;
        Timestamp sqlPushTime = new Timestamp(updateTime.getTime());
        User queUser = DTOUtils.newDTO(User.class);
        queUser.setMetadata(IDTO.AND_CONDITION_EXPR," yn = -1 and modified > '"+sqlPushTime+"'");
        StreamEx.ofNullable(this.userService.list(queUser))
                .nonNull().flatCollection(Function.identity()).map(info -> {
            //push后修改了用户信息
            if (finalNewPushFlag || finalUpdateTime.compareTo(info.getModified()) < 0) {
                ReportUserDto reportUser = thirdDataReportService.reprocessUser(info);
                reportUserDtoList.add(reportUser);
                return true;
            }
            return false;
        }).toList();

        this.thirdPartyPushDataService.updatePushTime(pushData);
        System.out.print("pushUserSaveUpdate:" + JSON.toJSONString(reportUserDtoList));
        return this.dataReportService.reportUserSaveUpdate(reportUserDtoList, optUser);
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
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName) : thirdPartyPushData;
            // TODO:endTime应该传入进去
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData);
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
            Integer endPos = i==part ? checkInList.size() : (i + 1) * batchSize;
            List<ReportCheckInDto> partBills = checkInList.subList(i * batchSize, endPos);
            baseOutput = this.dataReportService.reportCheckIn(partBills, optUser);
        }

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData(interfaceName, tableName) : thirdPartyPushData;
            // TODO:endTime应该传入进去
            this.thirdPartyPushDataService.updatePushTime(thirdPartyPushData);
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

}