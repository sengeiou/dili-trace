package com.dili.trace.jobs;

import cn.hutool.core.date.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.ThirdPartyPushData;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.thirdparty.report.CredentialInfoDto;
import com.dili.trace.dto.thirdparty.report.ReportRegisterBillDto;
import com.dili.trace.service.CategoryService;
import com.dili.trace.service.DataReportService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.ThirdPartyPushDataService;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    @Value("${current.baseWebPath}")
    private String baseWebPath;

    private String marketId = "330110800";

    @Override
    public void run(String... args) throws Exception {
         Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
         this.reportRegisterBill(optUser);
    }

    // 每五分钟提交一次数据
    // @Scheduled(cron = "0 */5 * * * ?")
    public void pushData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            this.pushCategory(1);
            this.reportRegisterBill(optUser);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void pushCategory(Integer level)
    {
        String tableName = "category";
        String interfaceName = "商品大类新增/修改";
        Category category = new Category();
        category.setLevel(level);
        List<Category> categories = categoryService.list(category);
        List<ThirdPartyPushData> pushDataList = new ArrayList<>();
        List<Category> pushCategories = StreamEx.of(categories).filter(td -> {
            ThirdPartyPushData thirdPartyPushData =
                    thirdPartyPushDataService.getThredPartyPushData(tableName, td.getId());
            if (thirdPartyPushData == null || (thirdPartyPushData != null
                    && thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0))
            {
                ThirdPartyPushData pushData = new ThirdPartyPushData();
                pushData.setTableName(tableName);
//                pushData.setTableId(td.getId());
                pushData.setInterfaceName(interfaceName);
                pushDataList.add(pushData);
                return true;
            }
            return false;
        }).toList();


        this.thirdPartyPushDataService.updatePushTime(pushDataList);
    }

    public BaseOutput reportRegisterBill(Optional<OperatorUser> optUser) {

        String tableName = "register_bill";
        String interfaceName = "报备新增/编辑";
        Date endTime = new Date();
        // verify_status "待审核"0, "已退回10, "已通过20, "不通过30
        // approvalStatus 审核状态 0-默认未审核 1-通过 2-退回 3-未通过
        Map<Integer, Integer> statusMap = new HashMap<>();
        statusMap.put(0,0);
        statusMap.put(20,1);
        statusMap.put(10,2);
        statusMap.put(30,3);

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

        // TODO:分批上报

        // 上报
        BaseOutput baseOutput = this.dataReportService.reportRegisterBill(billList, optUser);

        // 更新 pushtime
        if (baseOutput.isSuccess()) {
            thirdPartyPushData = thirdPartyPushData == null ? new ThirdPartyPushData() : thirdPartyPushData;
            // TODO:endTime应该传入进去
            this.thirdPartyPushDataService.updatePushTime(Arrays.asList(thirdPartyPushData));
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