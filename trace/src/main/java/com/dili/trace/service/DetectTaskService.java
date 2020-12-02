package com.dili.trace.service;

import com.dili.trace.dto.DetectTaskApiOutputDto;
import com.dili.trace.dto.TaskGetParam;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.DetectRecordParam;

import java.util.List;

/**
 * 检测任务接口
 */
public interface DetectTaskService {

    /**
     * 查询检测任务
     */
    public List<DetectTaskApiOutputDto> findByExeMachineNo(TaskGetParam taskGetParam);


    /**
     * 更新检测状态
     * 
     * @param detectRecord
     * @return
     */
    public BaseOutput<Boolean> updateDetectTask(DetectRecordParam detectRecord);


    

}