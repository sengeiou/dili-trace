package com.dili.trace.service;

import java.util.ArrayList;
import java.util.List;

import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.dto.BillReportDto;
import com.dili.trace.dto.BillReportQueryDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillReportService {
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;


    public EasyuiPageOutput listEasyuiPage(BillReportQueryDto query) throws Exception {
		BasePage<BillReportDto> listPageBillReport=this.listPageBillReport(query);
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
}