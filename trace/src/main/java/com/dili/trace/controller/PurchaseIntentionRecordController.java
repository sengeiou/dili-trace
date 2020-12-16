package com.dili.trace.controller;

import com.dili.trace.domain.PurchaseIntentionRecord;
import com.dili.trace.service.PurchaseIntentionRecordService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 买家意向单信息
 */
@Controller
@RequestMapping("/purchaseIntentionRecord")
public class PurchaseIntentionRecordController {
    @Autowired
    PurchaseIntentionRecordService purchaseIntentionRecordService;

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
    String listPage(@RequestBody PurchaseIntentionRecord queryInput) throws Exception {
        return this.purchaseIntentionRecordService.listEasyuiPage(queryInput, true).toString();

    }
}