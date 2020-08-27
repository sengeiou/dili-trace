package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.ThirdPartyPushData;

import java.util.List;

public interface ThirdPartyPushDataService extends BaseService<ThirdPartyPushData,Long> {

    ThirdPartyPushData getThredPartyPushData(String tableName, Long tableId);

    void updatePushTime(List<ThirdPartyPushData> thirdPartyPushData);
}
