package com.dili.trace.jobs;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.thirdparty.report.CodeCountDto;
import com.dili.trace.dto.thirdparty.report.MarketCountDto;
import com.dili.trace.dto.thirdparty.report.RegionCountDto;
import com.dili.trace.dto.thirdparty.report.RegionCountInfo;
import com.dili.trace.dto.thirdparty.report.ReportCountDto;
import com.dili.trace.dto.thirdparty.report.UnqualifiedPdtInfo;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.service.CategoryService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.ThirdPartyReportService;
import com.dili.trace.service.UserService;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;
import one.util.streamex.StreamEx;

@Component
public class ThirdPartyReportJob implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyReportJob.class);
    @Autowired
    ThirdPartyReportService thirdPartyReportService;

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    RegisterBillMapper registerBillMapper;

    @Autowired
    RegisterBillService registerBillService;


    // 每115分钟执行一次(token是两小时有效时间)
    @Scheduled(fixedRate = 1000L * 60L * 115)
    public void execute() {
        logger.info("开始刷新Token");
        try {
            this.thirdPartyReportService.refreshToken(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void reportData() {
        Optional<OperatorUser>optUser=Optional.empty();
        this.marketCount(optUser);
        this.regionCount(optUser);
        this.reportCount(optUser);
        this.codeCount(optUser);
    }

    public BaseOutput marketCount(Optional<OperatorUser>optUser) {
        User query = DTOUtils.newDTO(User.class);
        query.setYn(YesOrNoEnum.YES.getCode());
        Integer userCount = this.userService.countUser(query);

        Category category = new Category();
        Integer categoryCount = this.categoryService.count(category);

        MarketCountDto marketCountDto = new MarketCountDto();
        marketCountDto.setPdtCount(userCount == null ? 0 : userCount);
        marketCountDto.setSubjectCount(categoryCount == null ? 0 : categoryCount);
        marketCountDto.setUpdateTime(new Date());

        return this.thirdPartyReportService.marketCount(marketCountDto,optUser);

    }
    public BaseOutput reportCount(Optional<OperatorUser>optUser,Integer checkBatch) {
        RegisterBillDto billDto = new RegisterBillDto();
        LocalDateTime now = LocalDateTime.now();
        Date updateTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date start = Date.from(
                now.withHour(0).withMinute(0).withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        billDto.setCreatedStart(DateUtil.format(start, "yyyy-MM-dd HH:mm:ss"));
        billDto.setCreatedEnd(DateUtil.format(end, "yyyy-MM-dd HH:mm:ss"));
        ReportCountDto reportCountDto=StreamEx.ofNullable( this.registerBillMapper.selectReportCountData(billDto)).nonNull().flatCollection(Function.identity()).findFirst().orElse(new ReportCountDto());
        reportCountDto.setUpdateTime(updateTime);


        billDto.setIsCheckin(TFEnum.FALSE.getCode());
        billDto.setVerifyStatus(BillVerifyStatusEnum.NO_PASSED.getCode());

       List<UnqualifiedPdtInfo>unqualifiedPdtInfo= StreamEx.ofNullable(this.registerBillService.listByExample(billDto)).nonNull().flatCollection(Function.identity()).map(rb->{
            UnqualifiedPdtInfo info=new UnqualifiedPdtInfo();
            info.setBatchNo(rb.getCode());
            info.setPdtName(rb.getProductName());
            info.setPdtPlace(rb.getOriginName());
            info.setPdtSpec(rb.getSpecName());
            info.setStallNo(rb.getTallyAreaNo());
            info.setSubjectName(rb.getName());
            info.setUpdateTime(updateTime);
            return info;

        }).toList();

        reportCountDto.setUnqualifiedPdtInfo(unqualifiedPdtInfo);
        if(checkBatch!=null&&checkBatch>0){
            reportCountDto.setCheckBatch(checkBatch);
        }
        
        return   this.thirdPartyReportService.reportCount(reportCountDto,optUser);



    }
    public BaseOutput reportCount(Optional<OperatorUser>optUser) {
        return this.reportCount(optUser, null);

    }

    public BaseOutput regionCount(Optional<OperatorUser>optUser) {

        RegisterBillDto billDto = new RegisterBillDto();
        Date updateTime = new Date();
        List<RegionCountInfo> infoList = StreamEx.ofNullable(this.registerBillMapper.selectRegionCountData(billDto))
                .nonNull().flatCollection(Function.identity()).map(info -> {
                    info.setUpdateTime(updateTime);
                    return info;
                }).toList();

        RegionCountDto regionCountDto = new RegionCountDto();
        regionCountDto.setInfo(infoList);
        return this.thirdPartyReportService.regionCount(regionCountDto,optUser);

    }

    public BaseOutput codeCount(Optional<OperatorUser>optUser) {
        List<UserOutput> userOutputList = this.userMapper.groupByQrStatus(Lists.newArrayList(
                UserQrStatusEnum.GREEN.getCode(), UserQrStatusEnum.YELLOW.getCode(), UserQrStatusEnum.RED.getCode()));
        Map<Integer, Integer> qrStatusMap = StreamEx.of(userOutputList).toMap(UserOutput::getQrStatus,
                UserOutput::getCnt);
        CodeCountDto codeCountDto = new CodeCountDto();
        codeCountDto.setUpdateTime(new Date());
        codeCountDto.setGreenCount(qrStatusMap.getOrDefault(UserQrStatusEnum.GREEN.getCode(), 0));
        codeCountDto.setYellowCount(qrStatusMap.getOrDefault(UserQrStatusEnum.YELLOW.getCode(), 0));
        codeCountDto.setRedCount(qrStatusMap.getOrDefault(UserQrStatusEnum.RED.getCode(), 0));
        codeCountDto.setWaringInfo(Lists.newArrayList());
        return this.thirdPartyReportService.codeCount(codeCountDto,optUser);

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("启动时刷新Token");
        try {
            this.thirdPartyReportService.refreshToken(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    // protected Date start(LocalDateTime now) {
    //     Date start = Date.from(now.minusDays(6).atZone(ZoneId.systemDefault()).toInstant());
    //     return start;
    // }

    // protected Date end(LocalDateTime now) {
    //     Date end = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    //     return end;
    // }

}