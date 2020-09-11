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
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.glossary.YnEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BillReportService {
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;
    @Autowired
    UserService userService;
    @Autowired
    SysConfigService sysConfigService;


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
            baseDay += "  UNION ALL   SELECT DATE_SUB(CURDATE(), INTERVAL " + (i + 1) + " DAY) AS reportDate ";
        }
        Date createEnd = new Date();
        Date createStart = DateUtils.addDays(createEnd, 0 - limitDay);
        createStart = DateUtils.formatDate2DateTimeStart(createStart);
        createEnd = DateUtils.formatDate2DateTimeEnd(createEnd);
        String createStartStr = DateUtils.format(createStart);
        String createEndStr = DateUtils.format(createEnd);
        map.put("baseDay", baseDay);
        map.put("createdStart", createStartStr);
        map.put("createdEnd", createEndStr);
        List<TradeReportDto> list = checkinOutRecordMapper.getUserBillReport(map);

        String userType="bill";
        int userCount = getUserCount(userType);
        BigDecimal userDecimal = new BigDecimal(userCount);
        StreamEx.of(list).nonNull().forEach(t -> {
            BigDecimal b = new BigDecimal(t.getBillCount());
            BigDecimal result = b.divide(userDecimal,2, BigDecimal.ROUND_HALF_UP);
            t.setBillRatio(result);
        });
        return list;
    }

    public List<TradeReportDto> getUserTradeReport(int limitDay) {
        String baseDay = "";
        Map<String, Object> map = new HashMap<>(16);
        for (int i = 0; i < limitDay; i++) {
            baseDay += "  UNION ALL   SELECT DATE_SUB(CURDATE(), INTERVAL " + (i + 1) + " DAY) AS reportDate ";
        }
        Date createEnd = new Date();
        Date createStart = DateUtils.addDays(createEnd, 0 - limitDay);
        createStart = DateUtils.formatDate2DateTimeStart(createStart);
        createEnd = DateUtils.formatDate2DateTimeEnd(createEnd);
        String createStartStr = DateUtils.format(createStart);
        String createEndStr = DateUtils.format(createEnd);
        map.put("baseDay", baseDay);
        map.put("createdStart", createStartStr);
        map.put("createdEnd", createEndStr);
        List<TradeReportDto> buyList = checkinOutRecordMapper.getUserBuyerTradeReport(map);
        List<TradeReportDto> sellerList = checkinOutRecordMapper.getUserSellerTradeReport(map);

        String userType="trade";
        int userCount = getUserCount(userType);
        BigDecimal userDecimal = new BigDecimal(userCount);
        if (CollectionUtils.isEmpty(buyList)) {
            buyList = new ArrayList<>(16);
        }
        /*if (CollectionUtils.isNotEmpty(sellerList)) {
            Map<Date,List<TradeReportDto>> buyMap=buyList.stream().collect(Collectors.groupingBy(TradeReportDto::getReportDate));
            for(TradeReportDto sd:sellerList){
                if(buyMap.containsKey(sd.getReportDate())){
                    TradeReportDto buIt= buyMap.get(sd.getReportDate()).get(0);
                    int resultCount=sd.getTradeCount()+buIt.getTradeCount();
                    buIt.setTradeCount(resultCount);
                    buyMap.put(sd.getReportDate(),Arrays.asList(buIt));
                }
            }
            buyList.stream().forEach(b->{

            });
            List<TradeReportDto> listAllDistinct = buyList.stream().distinct().collect(toList());
            System.out.println("---得到去重并集 listAllDistinct---");
            listAllDistinct.parallelStream().forEachOrdered(System.out :: println);

        }*/
        StreamEx.of(buyList).nonNull().forEach(t -> {
            BigDecimal b = new BigDecimal(t.getTradeCount());
            BigDecimal result = b.divide(userDecimal,2, BigDecimal.ROUND_HALF_UP);
            t.setTradeRatio(result);
        });
        return buyList;
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