package com.dili.trace.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.TallyAreaNo;
import com.dili.trace.domain.User;
import com.dili.trace.util.ChineseStringUtil;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

@Service
public class TallyAreaNoService extends BaseServiceImpl<TallyAreaNo, Long> implements CommandLineRunner {
    private Map<String, List<String>> areaAndNumMap=new HashMap<>();

    public TallyAreaNoService() {
        areaAndNumMap.put("A1", Lists.newArrayList("-6-", "-7-", "7街", "-8-", "-9-", "-XQ-", "虾区"));
        areaAndNumMap.put("A2", Lists.newArrayList("-3-", "3街", "-5-", "5街", "-18-", "东大街"));
        areaAndNumMap.put("B1", Lists.newArrayList("-11-", "-12-", "-13-", "HY"));
        areaAndNumMap.put("B2", Lists.newArrayList("-10-", "BX", "SZ"));

    }

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User uq = DTOUtils.newDTO(User.class);
        uq.setPage(1);
        uq.setRows(20);
        // while (true) {
        //     List<User> userList = this.userService.listPageByExample(uq).getDatas();
        //     if (userList.isEmpty()) {
        //         return;
        //     }

        //     StreamEx.of(userList).nonNull().map(User::getTallyAreaNos).filter(StringUtils::isNotBlank).map(String::trim)
        //             .map(ChineseStringUtil::cToe).map(ChineseStringUtil::full2Half)
        //             .flatArray(tallyAreaNos -> tallyAreaNos.split(",")).filter(StringUtils::isNotBlank)
        //             .map(String::trim).forEach(tallyAreaNo -> {
        //                 this.parseAndSave(tallyAreaNo);
        //             });
        //     uq.setPage(uq.getPage() + 1);
        // }
    }

    private TallyAreaNo parseAndSave(String number) {
        String area = EntryStream.of(areaAndNumMap).filterValues(list -> {
            return StreamEx.of(list).anyMatch(flag -> {
                return number.contains(flag);
            });
        }).map(e -> e.getKey()).findFirst().orElse("Others");
        TallyAreaNo query = new TallyAreaNo();
        query.setNumber(number);

        TallyAreaNo item = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            TallyAreaNo obj = new TallyAreaNo();
            obj.setNumber(number);
            obj.setArea(area);
            this.insertSelective(obj);
            return obj;
        });
        return item;
    }

}