package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.DetectRequestQueryDto;
import com.dili.trace.api.output.CountDetectStatusDto;
import com.dili.trace.api.output.SampleSourceCountOutputDto;
import com.dili.trace.api.output.SampleSourceListOutputDto;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.dto.DetectRequestDto;
import com.dili.trace.dto.DetectRequestWithBillDto;
import com.dili.trace.enums.DetectStatusEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DetectRequestMapper extends MyMapper<DetectRequest> {

    public List<DetectRequest> selectRequestForDetect(@Param("marketId") Long marketId, @Param("detectorName") String detectorName, @Param("maxCount") Integer maxCount);

    public List<DetectRequest> selectDetectRequest(@Param("marketId") Long marketId, @Param("detectorName") String detectorName, @Param("detectStatus") DetectStatusEnum detectStatus);

    /**
     * 查询检测请求页面api
     *
     * @param domain
     * @return
     */
    List<DetectRequestDto> selectListPageByUserCategory(DetectRequestDto domain);

    /**
     * 查询检测请求详情页面api
     *
     * @param detectRequestDto
     * @return
     */
    DetectRequestDto getDetectRequestDetail(DetectRequestDto detectRequestDto);

    /**
     * 采样检测-统计查询
     *
     * @param query
     * @return
     */
    List<SampleSourceCountOutputDto> countBySampleSource(DetectRequestQueryDto query);

    /**
     * 采样检测-列表查询
     *
     * @param query
     * @return
     */
    List<SampleSourceListOutputDto> queryListBySampleSource(DetectRequestQueryDto query);

    /**
     * PC端检测请求-列表查询
     *
     * @param query
     * @return
     */
    List<DetectRequestWithBillDto> queryListByExample(DetectRequestWithBillDto query);


    /**
     * 根据检测状态统计
     * @param query
     * @return
     */
    List<CountDetectStatusDto> countByDetectStatus(DetectRequestQueryDto query);
}
