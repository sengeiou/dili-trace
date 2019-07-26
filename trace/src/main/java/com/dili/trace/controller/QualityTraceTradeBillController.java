package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.service.QualityTraceTradeBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Api("/qualityTraceTradeBill")
@Controller
@RequestMapping("/qualityTraceTradeBill")
public class QualityTraceTradeBillController {
    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;

    @ApiOperation("跳转到QualityTraceTradeBill页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "qualityTraceTradeBill/index";
    }

    @ApiOperation(value="查询QualityTraceTradeBill", notes = "查询QualityTraceTradeBill，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="QualityTraceTradeBill", paramType="form", value = "QualityTraceTradeBill的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<QualityTraceTradeBill> list(QualityTraceTradeBill qualityTraceTradeBill) {
        return qualityTraceTradeBillService.list(qualityTraceTradeBill);
    }

    @ApiOperation(value="分页查询QualityTraceTradeBill", notes = "分页查询QualityTraceTradeBill，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="QualityTraceTradeBill", paramType="form", value = "QualityTraceTradeBill的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(QualityTraceTradeBill qualityTraceTradeBill) throws Exception {
        return qualityTraceTradeBillService.listEasyuiPageByExample(qualityTraceTradeBill, true).toString();
    }

    @ApiOperation("新增QualityTraceTradeBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="QualityTraceTradeBill", paramType="form", value = "QualityTraceTradeBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(QualityTraceTradeBill qualityTraceTradeBill) {
        qualityTraceTradeBillService.insertSelective(qualityTraceTradeBill);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改QualityTraceTradeBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="QualityTraceTradeBill", paramType="form", value = "QualityTraceTradeBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(QualityTraceTradeBill qualityTraceTradeBill) {
        qualityTraceTradeBillService.updateSelective(qualityTraceTradeBill);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除QualityTraceTradeBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "QualityTraceTradeBill的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        qualityTraceTradeBillService.delete(id);
        return BaseOutput.success("删除成功");
    }
}