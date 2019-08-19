package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/detect")
@Api(value ="/api/detect", description = "检测任务相关接口")
@InterceptConfiguration(loginRequired=false)
public class DetectRecordApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(DetectRecordApi.class);
    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private DetectRecordService detectRecordService;

    /**
     * 保存检查单
     * @param detectRecord
     * @return
     */
    @ApiOperation("上传检测记录")
    @RequestMapping(value = "/saveRecord",method = RequestMethod.POST)
    public BaseOutput<Boolean> saveDetectRecord(DetectRecordParam detectRecord){
        LOGGER.info("保存检查单:"+ JSON.toJSONString(detectRecord));
        if(StringUtils.isBlank(detectRecord.getRegisterBillCode())){
            LOGGER.error("上传检测任务结果失败无单号");
            return BaseOutput.failure("没有对应的登记单");
        }
        if(StringUtils.isBlank(detectRecord.getDetectOperator())){
            LOGGER.error("上传检测任务结果失败无检测人员");
            return BaseOutput.failure("没有对应的检测人员");
        }
        if(detectRecord.getDetectState()==null){
            LOGGER.error("上传检测任务结果失败无检测状态");
            return BaseOutput.failure("没有对应的检测状态");
        }
        if(detectRecord.getDetectTime()==null){
            LOGGER.error("上传检测任务结果失败无检测时间");
            return BaseOutput.failure("没有对应的检测时间");
        }
        if(StringUtils.isBlank(detectRecord.getPdResult())){
            LOGGER.error("上传检测任务结果失败无检测值");
            return BaseOutput.failure("没有对应的检测值");
        }
        RegisterBill registerBill = registerBillService.findByCode(detectRecord.getRegisterBillCode());
        if(registerBill== null){
            LOGGER.error("上传检测任务结果失败该单号无登记单");
            return BaseOutput.failure("没有对应的登记单");
        }
        if(!registerBill.getExeMachineNo().equals(detectRecord.getExeMachineNo())){
            LOGGER.error("上传检测任务结果失败，该仪器没有获取该登记单");
            return BaseOutput.failure("该仪器无权操作该单据");
        }
        saveRecordAndUpdateBill(detectRecord,registerBill);
        return BaseOutput.success().setData(true);
    }

    @Transactional(rollbackFor=Exception.class)
    private void saveRecordAndUpdateBill(DetectRecordParam detectRecord,RegisterBill registerBill) {

        if(registerBill.getLatestDetectRecordId()!=null){
            detectRecord.setDetectType(2);
            registerBill.setDetectState(detectRecord.getDetectState()+2);
        }else {
            detectRecord.setDetectType(1);
            registerBill.setDetectState(detectRecord.getDetectState());
        }
        detectRecordService.saveDetectRecord(detectRecord);
        registerBill.setLatestDetectRecordId(detectRecord.getId());
        registerBill.setLatestDetectTime(detectRecord.getDetectTime());
        registerBillService.update(registerBill);
    }


    /**
     * 获取检查任务
     * @param exeMachineNo
     * @return
     */
    @ApiOperation("获取检测任务")
    @RequestMapping(value = "/getDetectTask/{exeMachineNo}/{taskCount}",method = RequestMethod.POST)
    public BaseOutput<List<RegisterBill>> getDetectTask( @PathVariable String exeMachineNo,  @PathVariable Integer taskCount){
        TaskGetParam taskGetParam = new TaskGetParam();
        taskGetParam.setExeMachineNo(exeMachineNo);
        if(taskCount>95){
            taskCount=95;
        }
        taskGetParam.setPageSize(taskCount);
        LOGGER.info("获取检查任务:" + JSON.toJSONString(taskGetParam));
        List<RegisterBill> registerBills=registerBillService.findByExeMachineNo(taskGetParam.getExeMachineNo(), taskGetParam.getPageSize());
        return BaseOutput.success().setData(registerBills);
    }

}
