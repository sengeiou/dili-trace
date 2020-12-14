package com.dili.trace.api;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.GlobalVarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局变量参数接口
 */
@RestController
@RequestMapping(value = "/api/globalConfigApi")
@AppAccess(role = Role.ANY)
public class GlobalConfigApi {
    private static Logger logger= LoggerFactory.getLogger(GlobalConfigApi.class);
    @Autowired
    GlobalVarService globalVarService;
    /**
     * 查看图片前缀路径
     *
     * @return
     */
    @RequestMapping(value = "/getViewImageContextPath.api", method = RequestMethod.GET)
    public BaseOutput<String> getViewImageContextPath() {
        try {
            return BaseOutput.successData(this.globalVarService.getDfsImageViewPathPrefix());
        } catch (Exception e) {
            logger.error("getViewImageContextPath", e);
            return BaseOutput.failure();
        }
    }

}
