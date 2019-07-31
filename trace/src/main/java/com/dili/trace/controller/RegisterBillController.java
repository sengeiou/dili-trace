package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.BaseBillParam;
import com.dili.trace.dto.ProductParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBill")
@Controller
@RequestMapping("/registerBill")
public class RegisterBillController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillController.class);
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    DetectRecordService detectRecordService;

    private static HashMap NAME_RESUBMIT=new HashMap();

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
    @RequestMapping(value="/insert.action", method = RequestMethod.POST)
    public @ResponseBody BaseOutput insert(@RequestBody List<RegisterBill> registerBills) {
        LOGGER.info("保存登记单数据:"+registerBills.size());
        String customerName = registerBills.get(0).getName();
        if(NAME_RESUBMIT.get(customerName)!=null){
            Long time = (Long) NAME_RESUBMIT.get(customerName);
            if(System.currentTimeMillis()-time<3000){
                LOGGER.error("有重复提交的数据" + customerName);
                return BaseOutput.failure("请勿重复提交");
            }
        }
        NAME_RESUBMIT.put(customerName,System.currentTimeMillis());
        //RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        for(RegisterBill registerBill :registerBills){
            registerBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
            registerBillService.createRegisterBill(registerBill);
        }
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
    /**
     * 登记单录入页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value="/view/{id}", method = RequestMethod.GET)
    public String create(ModelMap modelMap,@PathVariable Long id) {
        RegisterBill registerBill= registerBillService.get(id);
        List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(registerBill.getCode());
        registerBill.setSeparateSalesRecords(records);
        DetectRecord detectRecord =detectRecordService.findByRegisterBillCode(registerBill.getCode());
        if(detectRecord==null){
            detectRecord = DTOUtils.newDTO(DetectRecord.class);
            detectRecord.setPdResult("");
            detectRecord.setDetectOperator("");
        }
        registerBill.setDetectRecord(detectRecord);
        modelMap.put("registerBill",registerBill);
        return "registerBill/view";
    }
}