package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.RUserUpStreamMapper;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.service.RUserUpStreamService;
import org.springframework.stereotype.Service;

@Service
public class RUserUpStreamServiceImpl extends BaseServiceImpl<RUserUpstream, Long> implements RUserUpStreamService {
    public RUserUpStreamMapper getActualDao() {
        return (RUserUpStreamMapper)getDao();
    }
}
