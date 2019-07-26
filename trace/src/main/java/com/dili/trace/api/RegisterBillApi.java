package com.dili.trace.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/bill")
public class RegisterBillApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillApi.class);
    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private SeparateSalesRecordService separateSalesRecordService;

    /**
     * 保存登记单
     * @param registerBill
     * @return
     */
    @RequestMapping(value = "/saveBill")
    public BaseOutput<Boolean> saveRegisterBill(@RequestBody RegisterBill registerBill){
        LOGGER.info("保存登记单:"+registerBill.getIdCardNo()+registerBill.toString());
        registerBillService.saveOrUpdate(registerBill);
        return BaseOutput.success().setData(true);
    }
    @RequestMapping(value = "/saveSalesRecord")
    public BaseOutput<Boolean> saveSeparateSalesRecord(@RequestBody SeparateSalesRecord registerBill){
        LOGGER.info("保存分销单:"+registerBill.getRegisterBillCode()+registerBill.toString());
        separateSalesRecordService.saveOrUpdate(registerBill);
        return BaseOutput.success().setData(true);
    }
    @RequestMapping(value = "/getBill/{id}")
    public BaseOutput<RegisterBill> getRegisterBill( @PathVariable("id")Long id){
        LOGGER.info("获取登记单:"+id);
        RegisterBill bill = registerBillService.get(id);
        return BaseOutput.success().setData(bill);
    }
    @RequestMapping(value = "/getSalesRecord/{id}")
    public BaseOutput<SeparateSalesRecord> getSeparateSalesRecord( @PathVariable("id")Long id){
        LOGGER.info("获取分销记录:"+id);
        SeparateSalesRecord record = separateSalesRecordService.get(id);
        return BaseOutput.success().setData(record);
    }
    @RequestMapping(value = "/getBillRecord/{id}")
    public BaseOutput<RegisterBill> getBillRecord( @PathVariable("id")Long id){
        LOGGER.info("获取登记单&分销记录:"+id);
        RegisterBill bill = registerBillService.get(id);
        List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(bill.getCode());
        bill.setSeparateSalesRecords(records);
        return BaseOutput.success().setData(bill);
    }
}
