package com.dili.trace.service;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.UpStreamDto;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UpStreamService extends BaseServiceImpl<UpStream, Long> {
    @Autowired
    private RUserUpStreamService rUserUpStreamService;

    /**
     * 新增上游
     * @param upStreamDto
     * @return
     */
    @Transactional
    public BaseOutput addUpstream(UpStreamDto upStreamDto){
        insertSelective(upStreamDto);
        addUpstreamUsers(upStreamDto);
        return BaseOutput.success();
    }

    /**
     * 添加上游的所有业户信息（绑定）
     * @param upStreamDto
     */
    public void addUpstreamUsers(UpStreamDto upStreamDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            throw new BusinessException("未登录或登录过期");
        }
        List<RUserUpstream> rUserUpstreams = new ArrayList<>();
        upStreamDto.getUserIds().forEach(o->{
            RUserUpstream rUserUpstream = new RUserUpstream();
            rUserUpstream.setUpstreamId(upStreamDto.getId());
            rUserUpstream.setUserId(o);
            rUserUpstream.setOperatorId(userTicket.getId());
            rUserUpstream.setOperatorName(userTicket.getRealName());
            rUserUpstream.setCreated(new Date());
            rUserUpstream.setModified(new Date());
            rUserUpstreams.add(rUserUpstream);
        });
        rUserUpStreamService.batchInsert(rUserUpstreams);
    }

    /**
     * 修改上游
     * @param upStreamDto
     * @return
     */
    @Transactional
    public BaseOutput updateUpstream(UpStreamDto upStreamDto){
        updateSelective(upStreamDto);

        RUserUpstream rUserUpstreamDelCondition = new RUserUpstream();
        rUserUpstreamDelCondition.setUpstreamId(upStreamDto.getId());
        rUserUpStreamService.deleteByExample(rUserUpstreamDelCondition);
        addUpstreamUsers(upStreamDto);
        return BaseOutput.success();
    }

}