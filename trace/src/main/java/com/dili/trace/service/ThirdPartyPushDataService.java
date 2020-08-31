package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.ThirdPartyPushData;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface ThirdPartyPushDataService extends BaseService<ThirdPartyPushData,Long> {

    ThirdPartyPushData getThredPartyPushData(String tableName);

    void updatePushTime(ThirdPartyPushData thirdPartyPushData);

    void updatePushTime(ThirdPartyPushData thirdPartyPushData, Date pushDate);
}
