package com.dili.trace.rpc.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.api.UidRestfulRpc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SB
 *
 */
@Service
public class UidRestfulRpcService {
    @Autowired(required = false)
    UidRestfulRpc uidRestfulRpc;

    /**
     * sb
     * @param type
     * @return
     */
    public String bizNumber(String type) {
        BaseOutput<String> out = this.uidRestfulRpc.bizNumber(type);
        if(out!=null&&out.isSuccess()&& StringUtils.isNotBlank(out.getData())){
            return out.getData().trim();
        }
        throw new TraceBizException("生成编号出错");
    }
}
