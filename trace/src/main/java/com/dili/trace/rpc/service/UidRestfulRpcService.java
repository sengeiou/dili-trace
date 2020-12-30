package com.dili.trace.rpc.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.api.UidRestfulRpc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生成编号服务
 *
 */
@Service
public class UidRestfulRpcService {

    private static final Logger logger = LoggerFactory.getLogger(UidRestfulRpcService.class);

    @Autowired(required = false)
    UidRestfulRpc uidRestfulRpc;

    /**
     * 生成编号
     * @param type
     * @return
     */
    public String bizNumber(String type) {
        BaseOutput<String> out = this.uidRestfulRpc.bizNumber(type);
        if(out!=null&&out.isSuccess()&& StringUtils.isNotBlank(out.getData())){
            return out.getData().trim();
        } else {
            if (out != null) {
                logger.error("生成编号出错：" + out.getCode()+"--"+out.getMessage()+"--"+out.getErrorData());
            }
        }
        throw new TraceBizException("生成编号出错");
    }
}
