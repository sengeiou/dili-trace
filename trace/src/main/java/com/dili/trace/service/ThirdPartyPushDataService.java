package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.ThirdPartyPushData;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author l
 */
@Service
public interface ThirdPartyPushDataService extends BaseService<ThirdPartyPushData, Long> {

    /**
     * 获取第三方数据push记录
     *
     * @param tableName
     * @param marketId
     * @return
     */
    ThirdPartyPushData getThredPartyPushData(String tableName, Long marketId);

    /**
     * 更新push时间
     *
     * @param thirdPartyPushData
     */
    void updatePushTime(ThirdPartyPushData thirdPartyPushData);

    /**
     * 更新push时间
     *
     * @param thirdPartyPushData
     */
    void updatePushTime(ThirdPartyPushData thirdPartyPushData, Date pushDate);
}
