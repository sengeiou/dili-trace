package com.dili.trace.rpc.service;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.rpc.api.UidRestfulRpc;
import com.dili.trace.util.ChineseStringUtil;
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

    private static final String DETECT_CODE_PREFIX = "Bd";

    @Autowired(required = false)
    UidRestfulRpc uidRestfulRpc;
    @Autowired
    private LoginSessionContext sessionContext;

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

    /**
     * 生成检测单编号
     * @return
     */
    public String detectRequestBizNumber(String marketName) {

        String detectCode = this.bizNumber(BizNumberType.DETECT_REQUEST.getType());
        StringBuilder returnCode = new StringBuilder(detectCode);

        if (!detectCode.contains(DETECT_CODE_PREFIX)) {
            returnCode.insert(0, DETECT_CODE_PREFIX);
        }

        if (StringUtils.isNotEmpty(marketName)) {
            // 市场首字母
            String marketFirstSpell = ChineseStringUtil.getFirstSpell(marketName.substring(0, 1));
            returnCode.insert(2, marketFirstSpell);
        } else {
            logger.error("生成检测编号出错：获取市场失败");
        }

        return returnCode.toString();
    }
}
