package com.dili.trace.service;

import com.dili.trace.domain.DefaultFieldDetail;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultFieldDetailService extends TraceBaseService<DefaultFieldDetail, Long> {
    public List<DefaultFieldDetail> findByModuleType(Integer moduleType) {
        if(moduleType==null){
            return Lists.newArrayList();
        }
        DefaultFieldDetail q=new DefaultFieldDetail();
        q.setModuleType(moduleType);
        return this.listByExample(q);
    }

}
