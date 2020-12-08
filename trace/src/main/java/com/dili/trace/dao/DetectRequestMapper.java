package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.enums.DetectStatusEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DetectRequestMapper extends MyMapper<DetectRequest> {

    public List<DetectRequest> selectRequestForDetect(@Param("marketId") Long marketId, @Param("detectorName") String detectorName, @Param("maxCount") Integer maxCount);

    public List<DetectRequest> selectDetectRequest(@Param("marketId") Long marketId, @Param("detectorName") String detectorName, @Param("detectStatus") DetectStatusEnum detectStatus);



}
