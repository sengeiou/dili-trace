package com.dili.trace.controller;

import com.dili.sg.trace.glossary.SalesTypeEnum;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.query.PurchaseIntentionRecordQueryDto;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.PurchaseIntentionRecordService;
import com.dili.trace.service.UapRpcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 买家意向单信息
 */
@Controller
@RequestMapping("/purchaseIntentionRecord")
public class PurchaseIntentionRecordController {
    @Autowired
    PurchaseIntentionRecordService purchaseIntentionRecordService;
    @Autowired
    UapRpcService uapRpcService;
    /**
     * 跳转到PurchaseIntentionRecord页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到PurchaseIntentionRecord页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {

        return "purchaseIntentionRecord/index";
    }

    /**
     * 分页查询PurchaseIntentionRecord
     *
     * @param queryInput
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询PurchaseIntentionRecord", notes = "分页查询PurchaseIntentionRecord，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PurchaseIntentionRecord", paramType = "form", value = "PurchaseIntentionRecord的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@RequestBody PurchaseIntentionRecordQueryDto queryInput) throws Exception {
        queryInput.setMarketId(this.uapRpcService.getCurrentFirm().get().getId());
        return this.purchaseIntentionRecordService.listEasyuiPageByExample(queryInput, true).toString();

    }

    /**
     * 买家报备查看页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @RequestParam(required = true, name = "id") Long id) {
        PurchaseIntentionRecord purchaseIntentionRecord = purchaseIntentionRecordService.get(id);
        modelMap.put("purchaseIntentionRecord",purchaseIntentionRecord);
        return "purchaseIntentionRecord/view";
    }
}
