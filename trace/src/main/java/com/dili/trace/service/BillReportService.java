package com.dili.trace.service;

import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.input.TradeReportDto;
import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.domain.SysConfig;
import com.dili.trace.domain.User;
import com.dili.trace.dto.BillReportDto;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.enums.OrderTypeEnum;
import com.dili.trace.enums.SysConfigTypeEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.glossary.YnEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 报备单统一服务
 */
@Service
public class BillReportService {
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;
    @Autowired
    UserService userService;
    @Autowired
    SysConfigService sysConfigService;

    private static final Logger logger = LoggerFactory.getLogger(BillReportService.class);

    /**
     * 页面查询
     * @param query
     * @return
     * @throws Exception
     */
    public EasyuiPageOutput listEasyuiPage(BillReportQueryDto query) throws Exception {
        // query.setOrderType(OrderTypeEnum.REGISTER_BILL.getCode());
        BasePage<BillReportDto> listPageBillReport = this.listPageBillReport(query);
        long total = listPageBillReport.getTotalItem();
        List results = ValueProviderUtils.buildDataByProvider(query, listPageBillReport.getDatas());
        return new EasyuiPageOutput(total, results);


    }

    /**
     * 查询报表
     * @param query
     * @return
     */
    public BasePage<BillReportDto> listPageBillReport(BillReportQueryDto query) {
        if (query.getPage() == null || query.getPage() <= 0) {
            query.setPage(1);
        }
        if (query.getRows() == null || query.getRows() <= 0) {
            query.setRows(10);
        }
        PageHelper.startPage(query.getPage(), query.getRows());
        // List<String> sqlList = new ArrayList<>();
        // if (query.getCheckinCreatedStart() != null) {
        //     sqlList.add("'"+query.getCheckinCreatedStart() + "'<=checkinout_record.created");
        // }
        // if (query.getCheckinCreatedEnd() != null) {
        //     sqlList.add("checkinout_record.created<='" + query.getCheckinCreatedEnd()+"'");
        // }
        // StringBuilder dynamicSql = new StringBuilder(String.join(" AND ", sqlList));
        // if (dynamicSql.length() > 0) {
        //     dynamicSql.insert(0, "(").append(")");
        // }
        // if (query.isIncludeUnCheckin() != null && query.isIncludeUnCheckin()) {
        //     if (dynamicSql.length() > 0) {
        //         dynamicSql.append(" OR ");
        //     }
        //     dynamicSql.append("(checkinout_record.created is null)");
        //     if (dynamicSql.length() > 0) {
        //         dynamicSql.insert(0, "(").append(")");
        //     }
        // }else{
        //     if (dynamicSql.length() > 0) {
        //         dynamicSql.append(" AND ");
        //     }
        //     dynamicSql.append("(checkinout_record.created is null)");
        //     if (dynamicSql.length() > 0) {
        //         dynamicSql.insert(0, "(").append(")");
        //     }
        // }

        // if(dynamicSql.length()>0){
        //     query.setDynamicSql(dynamicSql.toString());
        // }


        List<BillReportDto> list = this.checkinOutRecordMapper.queryBillReport(query);

        Page<BillReportDto> page = (Page) list;
        BasePage<BillReportDto> result = new BasePage<BillReportDto>();
        result.setDatas(list);
        result.setPage(page.getPageNum());
        result.setRows(page.getPageSize());
        result.setTotalItem(page.getTotal());
        result.setTotalPage(page.getPages());
        result.setStartIndex(page.getStartRow());
        return result;

    }

    /**
     * 查询报表
     * @param limitDay
     * @return
     */
    public List<TradeReportDto> getUserBillReport(int limitDay) {
        String baseDay = "";
        Map<String, Object> map = new HashMap<>(16);
        limitDay=limitDay-1;
        for (int i = 0; i < limitDay; i++) {
            baseDay += "  UNION ALL   SELECT DATE_SUB(CURDATE(), INTERVAL " + (i + 2) + " DAY) AS reportDate ";
        }
        Date createEnd = new Date();
        int startDay = -1-limitDay;
        Date createStart = DateUtils.addDays(createEnd, startDay);
        createStart = DateUtils.formatDate2DateTimeStart(createStart);
        createEnd = DateUtils.formatDate2DateTimeStart(createEnd);
        String createStartStr = DateUtils.format(createStart);
        String createEndStr = DateUtils.format(createEnd);
        map.put("baseDay", baseDay);
        map.put("createdStart", createStartStr);
        map.put("createdEnd", createEndStr);
        List<TradeReportDto> list = checkinOutRecordMapper.getUserBillReport(map);

        String userType = SysConfigTypeEnum.CATEGORY_BILL.getCode();
        int userCount = getUserCount(userType);
        BigDecimal userDecimal = new BigDecimal(userCount);
        int mult =100;
        StreamEx.of(list).nonNull().forEach(t -> {
            BigDecimal b = new BigDecimal(t.getBillCount());
            BigDecimal result = b.divide(userDecimal, 4, BigDecimal.ROUND_HALF_UP);
            if(BigDecimal.ONE.compareTo(result)<0){
                result=BigDecimal.ONE;
            }
            t.setBillRatio(result.multiply(new BigDecimal(mult)));
        });
        return list;
    }

    /**
     * 查询报表
     * @param limitDay
     * @return
     */
    public List<TradeReportDto> getUserTradeReport(int limitDay) {
        String baseDay = "";
        Map<String, Object> map = new HashMap<>(16);
        //每一天都需要一条数据，查询一个当天的记录
        limitDay=limitDay-1;
        for (int i = 0; i < limitDay; i++) {
            baseDay += "  UNION ALL   SELECT DATE_SUB(CURDATE(), INTERVAL " + (i + 2) + " DAY) AS reportDate ";
        }
        Date createEnd = new Date();
        int startDay = -1-limitDay;
        Date createStart = DateUtils.addDays(createEnd, startDay);
        createStart = DateUtils.formatDate2DateTimeStart(createStart);
        createEnd = DateUtils.formatDate2DateTimeStart(createEnd);
        String createStartStr = DateUtils.format(createStart);
        String createEndStr = DateUtils.format(createEnd);
        String userType = SysConfigTypeEnum.CATEGORY_TRADE.getCode();
        int userCount = getUserCount(userType);
        BigDecimal userDecimal = new BigDecimal(userCount);
        map.put("baseDay", baseDay);
        map.put("createdStart", createStartStr);
        map.put("createdEnd", createEndStr);
        List<TradeReportDto> resultList = checkinOutRecordMapper.getUserSellerTradeReport(map);

        int mult =100;
        StreamEx.of(resultList).nonNull().forEach(t -> {
            BigDecimal b = new BigDecimal(t.getTradeCount());
            BigDecimal result = b.divide(userDecimal, 4, BigDecimal.ROUND_HALF_UP);
            if(BigDecimal.ONE.compareTo(result)<0){
                result=BigDecimal.ONE;
            }
            t.setTradeRatio(result.multiply(new BigDecimal(mult)));
        });
        resultList.sort(Comparator.comparing(TradeReportDto::getReportDate));
        return resultList;
    }

    /**
     * 统计
     * @param userType
     * @return
     */
    private int getUserCount(String userType) {
        int resultCount = 0;
        String optType = SysConfigTypeEnum.STATISTIC_BASE_USER.getCode();
        String optCategory = userType;
        SysConfig config = new SysConfig();
        config.setOptType(optType);
        config.setOptCategory(optCategory);
        List<SysConfig> sysConfigList = sysConfigService.listByExample(config);
        if (CollectionUtils.isNotEmpty(sysConfigList)) {
            String optValue = sysConfigList.get(0).getOptValue();
            if (StringUtils.isNotBlank(optValue)) {
                resultCount = Integer.valueOf(optValue);
            }
        }
        if (resultCount == 0) {
            User user = DTOUtils.newDTO(User.class);
            Integer normal = 1;
            Long noDelte = new Long(0);
            user.setValidateState(ValidateStateEnum.PASSED.getCode());
            user.setIsDelete(noDelte);
            user.setYn(YnEnum.YES.getCode());
            user.setState(normal);
            List<User> userList = userService.listByExample(user);
            resultCount = userList.size();
        }
        return resultCount;
    }
}