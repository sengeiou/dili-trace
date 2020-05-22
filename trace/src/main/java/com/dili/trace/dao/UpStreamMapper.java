package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UpStream;

import java.util.List;
import java.util.Map;

public interface UpStreamMapper extends MyMapper<UpStream> {
    /**
     * 批量查询上游对应的业户集合
     * @param upstreamIds
     * @return Map｛upstreamId，userNames｝
     */
    List<Map<String,Object>> queryUsersByUpstreamIds(List<Long> upstreamIds);
}