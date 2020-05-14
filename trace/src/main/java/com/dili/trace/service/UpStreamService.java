package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.UpStream;

import org.springframework.stereotype.Service;

@Service
public class UpStreamService extends BaseServiceImpl<UpStream, Long> {

    @Override
    public EasyuiPageOutput listEasyuiPageByExample(UpStream domain, boolean useProvider) throws Exception {

        return super.listEasyuiPageByExample(domain, useProvider);
    }

    public int createUpstream(UpStream input) {
        return 0;
    }

}