package com.dili.trace.jobs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import com.dili.trace.dto.ThirdPartyReportDataQueryDto;
import com.dili.trace.dto.thirdparty.report.CodeCountDto;
import com.dili.trace.dto.thirdparty.report.MarketCountDto;
import com.dili.trace.dto.thirdparty.report.RegionCountDto;
import com.dili.trace.dto.thirdparty.report.RegionCountInfo;
import com.dili.trace.dto.thirdparty.report.ReportCountDto;
import com.dili.trace.dto.thirdparty.report.UnqualifiedPdtInfo;
import com.dili.trace.dto.thirdparty.report.WaringInfoDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.service.CategoryService;
import com.dili.trace.service.DataReportService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.ThirdPartyReportDataService;
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
    DataReportService dataReportService;
    @Autowired
    ThirdPartyReportDataService thirdPartyReportDataService;

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

    @Override
    public void run(String... args) throws Exception {
        logger.info("===启动时刷新Token===");
        this.execute();
    }

    // 每115分钟执行一次(token是两小时有效时间)
    @Scheduled(fixedRate = 1000L * 60L * 115)
    public void execute() {
        logger.info("---开始刷新Token---");
        try {
            this.dataReportService.refreshToken(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("---结束刷新Token---");
    }

    // 每五分钟提交一次数据
    @Scheduled(cron = "0 */5 * * * ?")
    public void reportData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            this.marketCount(optUser);
            this.regionCount(optUser);
            this.reportCount(optUser);
            this.codeCount(optUser);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    // 每天凌晨清理数据，防止历史数据太多
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanReportData() {
        logger.info("---开始清理数据---");
        LocalDateTime end = LocalDateTime.now().minusDays(11).withHour(23).withMinute(59).withSecond(59);
        String createdEnd = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ThirdPartyReportDataQueryDto condition = new ThirdPartyReportDataQueryDto();
        condition.setCreatedEnd(createdEnd);
        try {
            this.thirdPartyReportDataService.deleteByExample(condition);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("---结束清理数据---");
    }

    public BaseOutput marketCount(Optional<OperatorUser> optUser) {
        User query = DTOUtils.newDTO(User.class);
        query.setYn(YesOrNoEnum.YES.getCode());
        Integer userCount = this.userService.countUser(query);

        Category category = new Category();
        Integer categoryCount = this.categoryService.count(category);

        MarketCountDto marketCountDto = new MarketCountDto();
        marketCountDto.setPdtCount(userCount == null ? 0 : userCount);
        marketCountDto.setSubjectCount(categoryCount == null ? 0 : categoryCount);
        marketCountDto.setUpdateTime(new Date());

        return this.dataReportService.marketCount(marketCountDto, optUser);

    }

    public BaseOutput reportCount(Optional<OperatorUser> optUser, Integer checkBatch) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime= now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDateTime = now;
        return this.dataReportService.reportCount(optUser,startDateTime,endDateTime, 0);

    }

    public BaseOutput reportCount(Optional<OperatorUser> optUser) {
        return this.reportCount(optUser, null);

    }

    public BaseOutput regionCount(Optional<OperatorUser> optUser) {

        RegisterBillDto billDto = new RegisterBillDto();
        Date updateTime = new Date();
        List<RegionCountInfo> infoList = StreamEx.ofNullable(this.registerBillMapper.selectRegionCountData(billDto))
                .nonNull().flatCollection(Function.identity()).map(info -> {
                    info.setUpdateTime(updateTime);
                    return info;
                }).toList();

        RegionCountDto regionCountDto = new RegionCountDto();
        regionCountDto.setInfo(infoList);
        return this.dataReportService.regionCount(regionCountDto, optUser);

    }

    public BaseOutput codeCount(Optional<OperatorUser> optUser) {
        List<UserOutput> userOutputList = this.userMapper.groupByQrStatus(Lists.newArrayList(
                UserQrStatusEnum.GREEN.getCode(), UserQrStatusEnum.YELLOW.getCode(), UserQrStatusEnum.RED.getCode()));
        Map<Integer, Integer> qrStatusMap = StreamEx.of(userOutputList).toMap(UserOutput::getQrStatus,
                UserOutput::getCnt);
        Date updateTime = new Date();
        CodeCountDto codeCountDto = new CodeCountDto();
        codeCountDto.setUpdateTime(updateTime);
        codeCountDto.setGreenCount(qrStatusMap.getOrDefault(UserQrStatusEnum.GREEN.getCode(), 0));
        codeCountDto.setYellowCount(qrStatusMap.getOrDefault(UserQrStatusEnum.YELLOW.getCode(), 0));
        codeCountDto.setRedCount(qrStatusMap.getOrDefault(UserQrStatusEnum.RED.getCode(), 0));

        User yellowQuery = DTOUtils.newDTO(User.class);
        yellowQuery.setYn(YesOrNoEnum.YES.getCode());
        yellowQuery.setQrStatus(UserQrStatusEnum.YELLOW.getCode());

        User redQuery = DTOUtils.newDTO(User.class);
        redQuery.setYn(YesOrNoEnum.YES.getCode());
        redQuery.setQrStatus(UserQrStatusEnum.RED.getCode());

        List<WaringInfoDto> warninfoList = StreamEx.of(this.userService.listByExample(yellowQuery))
                .append(this.userService.listByExample(redQuery)).map(u -> {

                    WaringInfoDto warn = new WaringInfoDto();
                    warn.setCodeStatus(
                            UserQrStatusEnum.fromCode(u.getQrStatus()).map(UserQrStatusEnum::getDesc).orElse(""));
                    warn.setStallNo(u.getTallyAreaNos());
                    warn.setSubjectName(u.getName());
                    warn.setUpdateTime(updateTime);
                    warn.setMarketId(u.getMarketId() == null ? "" : String.valueOf(u.getMarketId()));
                    warn.setMarketName(u.getMarketName());
                    return warn;
                }).toList();

        codeCountDto.setWarningInfo(warninfoList);
        return this.dataReportService.codeCount(codeCountDto, optUser);

    }

    // protected Date start(LocalDateTime now) {
    // Date start =
    // Date.from(now.minusDays(6).atZone(ZoneId.systemDefault()).toInstant());
    // return start;
    // }

    // protected Date end(LocalDateTime now) {
    // Date end = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    // return end;
    // }

}