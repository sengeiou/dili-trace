package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ThirdPartyPushData;
import com.dili.trace.service.ThirdPartyPushDataService;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThirdPartyPushDataServiceImpl extends BaseServiceImpl<ThirdPartyPushData, Long> implements ThirdPartyPushDataService {
    @Override
    public void updatePushTime(List<ThirdPartyPushData> thirdPartyPushData) {
        List<ThirdPartyPushData> updateThirtDataList = new ArrayList<>();
        List<ThirdPartyPushData> insertThirtDataList = new ArrayList<>();
        Date currentDate = new Date();
        StreamEx.of(thirdPartyPushData).forEach(td ->{
            td.setPushTime(currentDate);
            List<ThirdPartyPushData> thirdPartyPushDataList = this.list(td);
            if(thirdPartyPushDataList == null || thirdPartyPushData.size() == 0)
            {
                insertThirtDataList.add(td);
                td.setCreated(currentDate);
            }
            else
            {
                updateThirtDataList.add(thirdPartyPushDataList.get(0));
            }
        });
        this.batchInsert(insertThirtDataList);
        this.batchUpdateSelective(updateThirtDataList);
    }
}
