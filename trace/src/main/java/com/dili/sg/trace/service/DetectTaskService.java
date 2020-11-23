package com.dili.trace.service;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.DetectTaskApiOutputDto;
import com.dili.trace.dto.TaskGetParam;

public interface DetectTaskService  {

    /**
     * 查询检测任务
     */
    public List<DetectTaskApiOutputDto> findByExeMachineNo(TaskGetParam taskGetParam);


    /**
     * 更新检测状态
     * 
     * @param detectTask
     * @return
     */
    public BaseOutput<Boolean> updateDetectTask(DetectRecordParam detectRecord);


    

}