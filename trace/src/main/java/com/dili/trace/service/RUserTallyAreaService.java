package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RUserTallyArea;

import org.springframework.stereotype.Service;

@Service
public class RUserTallyAreaService extends BaseServiceImpl<RUserTallyArea, Long> {
    public RUserTallyArea saveOrUpdate(Long tallyAreaNoId, Long userId) {

        RUserTallyArea query = new RUserTallyArea();
        query.setUserId(userId);
        query.setTallyAreaNoId(tallyAreaNoId);
        return this.listByExample(query).stream().findFirst().orElseGet(() -> {

            RUserTallyArea item = new RUserTallyArea();
            item.setUserId(userId);
            item.setTallyAreaNoId(tallyAreaNoId);
            this.insertSelective(item);
            return item;

        });

    }


}