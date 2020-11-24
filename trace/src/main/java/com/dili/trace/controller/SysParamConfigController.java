package com.dili.trace.controller;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.SysConfig;
import com.dili.trace.enums.MarketIdEnum;
import com.dili.trace.enums.SysConfigTypeEnum;
import com.dili.trace.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author asa.lee
 */
@Api("/sysParamConfig")
@Controller
@RequestMapping("/sysParamConfig")
public class SysParamConfigController {
    private static final Logger logger = LoggerFactory.getLogger(SysParamConfigController.class);

    @Autowired
    SysConfigService sysConfigService;


    /**
     *  跳转到页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "sysParamConfig/index";
    }

    /**
     * 跳转到内嵌页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到内嵌页面")
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String viewTraceReport(ModelMap modelMap) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setOptType(SysConfigTypeEnum.OPERATION_LIMIT_DAY.getCode());
        sysConfig.setOptCategory(SysConfigTypeEnum.OPERATION_LIMIT_DAY.getCode());
        sysConfig.setMarketId(Long.valueOf(MarketIdEnum.AQUATIC_TYPE.getCode()));
        List<SysConfig> sysConfigList = this.sysConfigService.listByExample(sysConfig);
        if (CollectionUtils.isNotEmpty(sysConfigList)) {
            sysConfig = sysConfigList.get(0);
        }
        modelMap.put("sysConfigItem", sysConfig);
        return "sysParamConfig/view";
    }

    /**
     * 查询
     * @param query
     * @return
     */
    @ApiOperation("查询")
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<SysConfig> list(SysConfig query) {
        try {
            SysConfig queSysConfig = new SysConfig();
            if (StringUtils.isNotBlank(query.getInstructions())) {
                String queParam = " instructions LIKE '%" + query.getInstructions() + "%' ";
                queSysConfig.setMetadata(IDTO.AND_CONDITION_EXPR, queParam);
            }
            if (StringUtils.isNotBlank(query.getOptType())) {
                queSysConfig.setOptType(query.getOptType());
            }
            if (StringUtils.isNotBlank(query.getOptCategory())) {
                queSysConfig.setOptCategory(query.getOptCategory());
            }
            queSysConfig.setMarketId(Long.valueOf(MarketIdEnum.AQUATIC_TYPE.getCode()));
            List<SysConfig> list = sysConfigService.listByExample(queSysConfig);
            return list;
        } catch (TraceBizException e) {
            logger.error(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 修改
     * @param query
     * @return
     */
    @ApiOperation("修改")
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(SysConfig query) {
        try {
            logger.info("update SysConfig:" + JSON.toJSONString(query));
            if (null == query.getId()) {
                return BaseOutput.failure("ID IS NULL");
            }
            sysConfigService.updateSelective(query);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            logger.error(e.getMessage());
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 修改运营报表天数
     * @param query
     * @return
     */
    @ApiOperation("修改运营报表天数")
    @RequestMapping(value = "/updateTraceReportLimitDay.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput updateTraceReportLimitDay(SysConfig query) {
        try {
            logger.info("update SysConfig:" + JSON.toJSONString(query));
            if (null == query.getId()) {
                return BaseOutput.failure("ID IS NULL");
            }
            sysConfigService.updateTraceReportLimitDay(query);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            logger.error(e.getMessage());
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }
    }
}
