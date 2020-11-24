package com.dili.trace.service;

import com.dili.sg.trace.dto.DetectTaskApiOutputDto;
import com.dili.sg.trace.dto.TaskGetParam;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.DetectRecordParam;

import java.util.List;

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