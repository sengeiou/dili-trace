package com.dili.trace.service;

import com.alibaba.fastjson.JSON;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BillReportService {
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;
    @Autowired
    UserService userService;
    @Autowired
    SysConfigService sysConfigService;

    private static final Logger logger = LoggerFactory.getLogger(BillReportService.class);

    public EasyuiPageOutput listEasyuiPage(BillReportQueryDto query) throws Exception {
        BasePage<BillReportDto> listPageBillReport = this.listPageBillReport(query);
        long total = listPageBillReport.getTotalItem();
        List results = ValueProviderUtils.buildDataByProvider(query, listPageBillReport.getDatas());
        return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);


    }

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
        result.setTotalItem(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPage(page.getPages());
        result.setStartIndex(page.getStartRow());
        return result;

    }

    public List<TradeReportDto> getUserBillReport(int limitDay) {
        String baseDay = "";
        Map<String, Object> map = new HashMap<>(16);
        for (int i = 0; i < limitDay; i++) {
            baseDay += "  UNION ALL   SELECT DATE_SUB(CURDATE(), INTERVAL " + (i + 2) + " DAY) AS reportDate ";
        }
        Date createEnd = new Date();
        Date createStart = DateUtils.addDays(createEnd, 0 - limitDay);
        createStart = DateUtils.formatDate2DateTimeStart(createStart);
        createEnd = DateUtils.formatDate2DateTimeStart(createEnd);
        String createStartStr = DateUtils.format(createStart);
        String createEndStr = DateUtils.format(createEnd);
        map.put("baseDay", baseDay);
        map.put("createdStart", createStartStr);
        map.put("createdEnd", createEndStr);
        List<TradeReportDto> list = checkinOutRecordMapper.getUserBillReport(map);

        String userType = "bill";
        int userCount = getUserCount(userType);
        BigDecimal userDecimal = new BigDecimal(userCount);
        int mult =100;
        StreamEx.of(list).nonNull().forEach(t -> {
            BigDecimal b = new BigDecimal(t.getBillCount());
            BigDecimal result = b.divide(userDecimal, 4, BigDecimal.ROUND_HALF_UP);
            t.setBillRatio(result.multiply(new BigDecimal(mult)));
        });
        return list;
    }

    public List<TradeReportDto> getUserTradeReport(int limitDay) {
        String baseDay = "";
        Map<String, Object> map = new HashMap<>(16);
        //每一天都需要一条数据，查询一个当天的记录
        for (int i = 0; i < limitDay; i++) {
            baseDay += "  UNION ALL   SELECT DATE_SUB(CURDATE(), INTERVAL " + (i + 2) + " DAY) AS reportDate ";
        }
        Date createEnd = new Date();
        Date createStart = DateUtils.addDays(createEnd, 0 - limitDay);
        createStart = DateUtils.formatDate2DateTimeStart(createStart);
        createEnd = DateUtils.formatDate2DateTimeStart(createEnd);
        String createStartStr = DateUtils.format(createStart);
        String createEndStr = DateUtils.format(createEnd);
        map.put("baseDay", baseDay);
        map.put("createdStart", createStartStr);
        map.put("createdEnd", createEndStr);
        List<TradeReportDto> buyList = checkinOutRecordMapper.getUserBuyerTradeReport(map);
        List<TradeReportDto> sellerList = checkinOutRecordMapper.getUserSellerTradeReport(map);

        String userType = "trade";
        int userCount = getUserCount(userType);
        BigDecimal userDecimal = new BigDecimal(userCount);
        if (CollectionUtils.isEmpty(buyList)) {
            buyList = new ArrayList<>(16);
        }
        List<TradeReportDto> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sellerList)) {
            buyList.addAll(sellerList);
            buyList.parallelStream().collect(Collectors.groupingBy(o -> o.getReportDate())).forEach((id, tr) -> {
                tr.stream().reduce((a, b) -> {
                    //同一天，非同一经营户相加
                    int total = 0;
                    if (StringUtils.isNotBlank(a.getUserIds()) && StringUtils.isNotBlank(b.getUserIds())) {
                        List<String> userIdList = Arrays.asList(a.getUserIds().split(","));
                        List<String> bList = Arrays.asList(b.getUserIds().split(","));
                        List<String> collect = Stream.of(userIdList, bList)
                                .flatMap(Collection::stream)
                                .distinct()
                                .collect(Collectors.toList());
                        logger.info("userList1:"+ JSON.toJSONString(userIdList));
                        logger.info("userList2:"+ JSON.toJSONString(bList));
                        logger.info("userList result:"+ JSON.toJSONString(collect));
                        total = collect.size();
                    } else {
                        total = a.getTradeCount() + b.getTradeCount();
                    }
                    TradeReportDto totalItem = new TradeReportDto();
                    totalItem.setReportDate(a.getReportDate());
                    totalItem.setTradeCount(total);
                    return totalItem;
                }).ifPresent(resultList::add);
            });
        }
        int mult =100;
        StreamEx.of(resultList).nonNull().forEach(t -> {
            BigDecimal b = new BigDecimal(t.getTradeCount());
            BigDecimal result = b.divide(userDecimal, 4, BigDecimal.ROUND_HALF_UP);
            t.setTradeRatio(result.multiply(new BigDecimal(mult)));
        });
        resultList.sort(Comparator.comparing(TradeReportDto::getReportDate));
        return resultList;
    }

    private int getUserCount(String userType) {
        int resultCount = 0;
        String optType = "statisticBaseUser";
        String optCategory = userType;
        SysConfig config = new SysConfig();
        config.setOpt_type(optType);
        config.setOpt_category(optCategory);
        List<SysConfig> sysConfigList = sysConfigService.listByExample(config);
        if (CollectionUtils.isNotEmpty(sysConfigList)) {
            String optValue = sysConfigList.get(0).getOpt_value();
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