package com.dili.trace.jobs;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.Market;
import com.dili.trace.domain.ThirdPartyPushData;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.thirdparty.report.GoodsDto;
import com.dili.trace.dto.thirdparty.report.ReportInspectionDto;
import com.dili.trace.dto.thirdparty.report.ReportUserDto;
import com.dili.trace.enums.CommodityTypeEnum;
import com.dili.trace.enums.ReportInterfaceEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.service.*;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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


    @Value("${current.baseWebPath}")
    private String baseWebPath;
    @Value("${push.batch.size}")
    private Integer pushBatchSize;

    @Override
    public void run(String... args) throws Exception {
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
                boolean isHangGuo = market.getName().indexOf("杭果") != -1 && appId != null && StringUtils.isNoneBlank(appSecret) && StringUtils.isNoneBlank(contextUrl);
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

    private void pushFruitsUnqualifiedInspectionData(Optional<OperatorUser> optUser, Date endTime, Market market) {

    }

    private void pushFruitsInspectionData(Optional<OperatorUser> optUser, Date endTime, Market market) {
        Date updateTime = null;
        boolean newPushFlag = true;
        Long marketId = market.getId();
        String platformMarketId = String.valueOf(market.getPlatformMarketId());
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

        List<ReportInspectionDto> inspectionDtoList = getReportInspectionList();

    }

    private List<ReportInspectionDto> getReportInspectionList() {
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
        String platformMarketId = String.valueOf(market.getPlatformMarketId());
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
        user.setMarketId(Long.valueOf(platformMarketId));
        if (!newPushFlag) {
            user.mset(IDTO.AND_CONDITION_EXPR, " modified >= '" + DateUtils.format(updateTime) + "'");
        }
        List<User> userList = userService.listByExample(user);
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
        BaseOutput baseOutput = this.dataReportService.reportFruitsUser(reportUserDtoList, optUser, market);
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
        category.setType(CommodityTypeEnum.SUPPLEMENT.getCode());
        if (!newPushFlag) {
            category.setMetadata(IDTO.AND_CONDITION_EXPR, " created >= '" + DateUtils.format(updateTime) + "'");
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
        BaseOutput baseOutput = this.dataReportService.reportFruitsGoods(categoryDtos, optUser, market);
        if (baseOutput.isSuccess()) {
            this.thirdPartyPushDataService.updatePushTime(pushData, endTime);
        } else {
            logger.error("上报:{} 失败，原因:{}", ReportInterfaceEnum.HANGGUO_GOODS.getName(), baseOutput.getMessage());
        }
        return baseOutput;
    }


}