package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.RegisterBillService;
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
 * This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBill")
@Controller
@RequestMapping("/registerBill")
public class RegisterBillController {
    @Autowired
    RegisterBillService registerBillService;

    @ApiOperation("跳转到RegisterBill页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "registerBill/index";
    }

    @ApiOperation(value="查询RegisterBill", notes = "查询RegisterBill，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RegisterBill", paramType="form", value = "RegisterBill的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<RegisterBill> list(RegisterBillDto registerBill) {
        return registerBillService.list(registerBill);
    }

    @ApiOperation(value="分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RegisterBill", paramType="form", value = "RegisterBill的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(RegisterBillDto registerBill) throws Exception {
        return registerBillService.listEasyuiPageByExample(registerBill, true).toString();
    }

    @ApiOperation("新增RegisterBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RegisterBill", paramType="form", value = "RegisterBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(RegisterBill registerBill) {
        registerBillService.insertSelective(registerBill);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改RegisterBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RegisterBill", paramType="form", value = "RegisterBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(RegisterBill registerBill) {
        registerBillService.updateSelective(registerBill);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除RegisterBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "RegisterBill的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        registerBillService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 登记单录入页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value="/create.html")
    public String create(ModelMap modelMap) {
        return "registerBill/create";
    }
}