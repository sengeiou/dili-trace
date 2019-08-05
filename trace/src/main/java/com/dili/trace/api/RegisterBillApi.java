package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.service.BizNumberFunction;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/bill")
@Api(value ="/api/bill", description = "登记单相关接口")
public class RegisterBillApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillApi.class);
    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private SeparateSalesRecordService separateSalesRecordService;

    @Autowired
    BizNumberFunction bizNumberFunction;

    /**
     * 测试登记单编号生成器
     * @param
     * @return
     */
    //@RequestMapping(value = "/testGenId")
    public BaseOutput<String> testGenId(){
        String code = bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL);
        return BaseOutput.success().setData(code);
    }

    /**
     * 保存登记单
     * @param registerBill
     * @return
     */
    @ApiOperation("保存登记单")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "登记单保存入参")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public BaseOutput saveRegisterBill(RegisterBill registerBill){
        LOGGER.info("保存登记单:"+ JSON.toJSONString(registerBill));
        int result =registerBillService.createRegisterBill(registerBill);
        if(result==0){
            return BaseOutput.failure().setMessage("请检测相关参数完整性");
        }
        return BaseOutput.success();
    }
    @ApiOperation(value = "获取登记单列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public BaseOutput<EasyuiPageOutput> list(RegisterBillDto registerBill) throws Exception {
        LOGGER.info("获取登记单列表:"+JSON.toJSON(registerBill).toString());
        EasyuiPageOutput easyuiPageOutput = registerBillService.listEasyuiPageByExample(registerBill, true);
        return BaseOutput.success().setData(easyuiPageOutput);
    }
    @ApiOperation("保存分销单")
    @ApiImplicitParam(paramType = "body", name = "SeparateSalesRecord", dataType = "SeparateSalesRecord", value = "分销单保存入参")
    @RequestMapping(value = "/createSalesRecord",method = RequestMethod.POST)
    public BaseOutput<Boolean> saveSeparateSalesRecord(SeparateSalesRecord salesRecord){
        LOGGER.info("保存分销单:"+JSON.toJSONString(salesRecord));
        if(StringUtils.isBlank(salesRecord.getRegisterBillCode())){
            return BaseOutput.failure("没有分销的登记单");
        }
        RegisterBill registerBill =  registerBillService.findByCode(salesRecord.getRegisterBillCode());
        if(registerBill.getWeight().intValue() == salesRecord.getSalesWeight().intValue()){
            registerBill.setSalesType(2);
        }else {
            registerBill.setSalesType(1);
        }
        registerBillService.update(registerBill);
        separateSalesRecordService.saveOrUpdate(salesRecord);
        return BaseOutput.success().setData(true);
    }
    @ApiOperation(value = "通过ID获取登记单和分销单")
    @RequestMapping(value = "id/{id}",method = RequestMethod.GET)
    public BaseOutput<RegisterBill> getRegisterBill( @PathVariable Long id){
        LOGGER.info("获取登记单:"+id);
        RegisterBill bill = registerBillService.get(id);
        List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(bill.getCode());
        bill.setSeparateSalesRecords(records);
        return BaseOutput.success().setData(bill);
    }
    @ApiOperation(value = "通过登记单编号获取登记单和分销单")
    @RequestMapping(value = "/code/{code}",method = RequestMethod.GET)
    public BaseOutput<RegisterBill> getRegisterBillByCode( @PathVariable String code){
        LOGGER.info("获取登记单:"+code);
        RegisterBill bill = registerBillService.findByCode(code);
        List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(bill.getCode());
        bill.setSeparateSalesRecords(records);
        return BaseOutput.success().setData(bill);
    }
    @ApiOperation(value = "通过交易区的交易号获取登记单和分销单")
    @RequestMapping(value = "/tradeNo/{tradeNo}",method = RequestMethod.GET)
    public BaseOutput<RegisterBill> getBillByTradeNo( @PathVariable String tradeNo){
        LOGGER.info("getBillByTradeNo获取登记单:"+tradeNo);
        RegisterBill bill = registerBillService.findByTradeNo(tradeNo);
        List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(bill.getCode());
        bill.setSeparateSalesRecords(records);
        return BaseOutput.success().setData(bill);
    }
    @ApiOperation(value = "通过分销记录ID获取分销单")
    @RequestMapping(value = "/salesRecordId/{salesRecordId}",method = RequestMethod.GET)
    public BaseOutput<SeparateSalesRecord> getSeparateSalesRecord( @PathVariable Long salesRecordId){
        LOGGER.info("获取分销记录:"+salesRecordId);
        SeparateSalesRecord record = separateSalesRecordService.get(salesRecordId);
        return BaseOutput.success().setData(record);
    }
    @ApiOperation(value = "通过登记单ID获取登记单和分销单")
    @RequestMapping(value = "/billSalesRecord/{id}",method = RequestMethod.GET)
    public BaseOutput<RegisterBill> getBillSalesRecord( @PathVariable Long id){
        LOGGER.info("获取登记单&分销记录:"+id);
        RegisterBill bill = registerBillService.get(id);
        List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(bill.getCode());
        bill.setSeparateSalesRecords(records);
        return BaseOutput.success().setData(bill);
    }
    @ApiOperation(value = "通过登记单商品名获取登记单",httpMethod = "GET", notes="productName=?")
    @RequestMapping(value = "/productName/{productName}",method = RequestMethod.GET)
    public BaseOutput<List<RegisterBill>> getBillByProductName( @PathVariable String productName){
        LOGGER.info("获取登记单&分销记录:"+productName);
        List<RegisterBill> bills = registerBillService.findByProductName(productName);
        return BaseOutput.success().setData(bills);
    }


}
