package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ThirdPartyPushData;
import com.dili.trace.service.ThirdPartyPushDataService;
import com.dili.trace.util.MarketUtil;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ThirdPartyPushDataServiceImpl extends BaseServiceImpl<ThirdPartyPushData, Long> implements ThirdPartyPushDataService {

    @Override
    public ThirdPartyPushData getThredPartyPushData(String tableName, Long marketId) {
        ThirdPartyPushData d = new ThirdPartyPushData();
        d.setTableName(tableName);
        d.setMarketId(marketId);
        d = StreamEx.of(this.list(d)).nonNull().findFirst().orElse(null);
        return d;
    }

    @Override
    public void updatePushTime(ThirdPartyPushData thirdPartyPushData) {
        Date currentDate = new Date();
        ThirdPartyPushData param = new ThirdPartyPushData();
        param.setTableName(thirdPartyPushData.getTableName());
        param.setInterfaceName(thirdPartyPushData.getInterfaceName());
        param.setMarketId(thirdPartyPushData.getMarketId());
        List<ThirdPartyPushData> thirdPartyPushDataList = this.list(param);
        thirdPartyPushData.setPushTime(currentDate);
        if(thirdPartyPushDataList == null || thirdPartyPushDataList.size() == 0)
        {
            thirdPartyPushData.setCreated(currentDate);
            this.insertSelective(thirdPartyPushData);
        }
        else
        {
            thirdPartyPushData.setId(thirdPartyPushDataList.get(0).getId());
            this.updateSelective(thirdPartyPushData);
        }
    }

    @Override
    public void updatePushTime(ThirdPartyPushData thirdPartyPushData, Date pushDate) {
        ThirdPartyPushData param = new ThirdPartyPushData();
        param.setTableName(thirdPartyPushData.getTableName());
        param.setInterfaceName(thirdPartyPushData.getInterfaceName());
        param.setMarketId(thirdPartyPushData.getMarketId());
        List<ThirdPartyPushData> thirdPartyPushDataList = this.list(param);
        thirdPartyPushData.setPushTime(pushDate);
        if(thirdPartyPushDataList == null || thirdPartyPushDataList.size() == 0)
        {
            thirdPartyPushData.setCreated(pushDate);
            this.insertSelective(thirdPartyPushData);
        }
        else
        {
            thirdPartyPushData.setId(thirdPartyPushDataList.get(0).getId());
            this.updateSelective(thirdPartyPushData);
        }
    }
}
