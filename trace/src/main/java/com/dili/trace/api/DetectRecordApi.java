package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/no/detect")
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
    @RequestMapping(value = "/saveRecord")
    public BaseOutput<Boolean> saveDetectRecord(@RequestBody DetectRecord detectRecord){
        LOGGER.info("保存检查单:"+ JSON.toJSONString(detectRecord));
        detectRecordService.saveDetectRecord(detectRecord);
        return BaseOutput.success().setData(true);
    }


    /**
     * 获取检查任务
     * @param taskGetParam
     * @return
     */
    @RequestMapping(value = "/getDetectTask")
    public BaseOutput<List<RegisterBill>> getDetectTask(@RequestBody TaskGetParam taskGetParam){
        LOGGER.info("获取检查任务:" + JSON.toJSONString(taskGetParam));
        List<RegisterBill> registerBills=registerBillService.findByExeMachineNo(taskGetParam.getExeMachineNo(), taskGetParam.getPageSize());
        return BaseOutput.success().setData(registerBills);
    }

}
