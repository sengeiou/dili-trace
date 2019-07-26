package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.service.DetectRecordService;
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
@Api("/detectRecord")
@Controller
@RequestMapping("/detectRecord")
public class DetectRecordController {
    @Autowired
    DetectRecordService detectRecordService;

    @ApiOperation("跳转到DetectRecord页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "detectRecord/index";
    }

    @ApiOperation(value="查询DetectRecord", notes = "查询DetectRecord，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DetectRecord", paramType="form", value = "DetectRecord的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<DetectRecord> list(DetectRecord detectRecord) {
        return detectRecordService.list(detectRecord);
    }

    @ApiOperation(value="分页查询DetectRecord", notes = "分页查询DetectRecord，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DetectRecord", paramType="form", value = "DetectRecord的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(DetectRecord detectRecord) throws Exception {
        return detectRecordService.listEasyuiPageByExample(detectRecord, true).toString();
    }

    @ApiOperation("新增DetectRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DetectRecord", paramType="form", value = "DetectRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(DetectRecord detectRecord) {
        detectRecordService.insertSelective(detectRecord);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改DetectRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DetectRecord", paramType="form", value = "DetectRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(DetectRecord detectRecord) {
        detectRecordService.updateSelective(detectRecord);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除DetectRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "DetectRecord的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        detectRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}