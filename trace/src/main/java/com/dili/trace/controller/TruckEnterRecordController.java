package com.dili.trace.controller;

import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.service.TruckEnterRecordService;
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
 * 司机进门信息
 */
@Controller
@RequestMapping("/truckEnterRecord")
public class TruckEnterRecordController {
    @Autowired
    TruckEnterRecordService truckEnterRecordService;

    /**
     * 跳转到TruckEnterRecord页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到TruckEnterRecord页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {

        return "truckEnterRecord/index";
    }

    /**
     * 分页查询TruckEnterRecord
     *
     * @param queryInput
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询TruckEnterRecord", notes = "分页查询TruckEnterRecord，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "TruckEnterRecord", paramType = "form", value = "TruckEnterRecord的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@RequestBody TruckEnterRecord queryInput) throws Exception {
        return this.truckEnterRecordService.listEasyuiPage(queryInput, true).toString();

    }
}
