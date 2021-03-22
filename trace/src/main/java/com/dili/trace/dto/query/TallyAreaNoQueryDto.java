package com.dili.trace.dto.query;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RegisterTallyAreaNo;
import com.dili.trace.domain.TallyAreaNo;

import javax.persistence.Column;
import java.util.List;

public class TallyAreaNoQueryDto extends RegisterTallyAreaNo {
    @Column(name = "`id`")
    @Operator(Operator.IN)
    private List<Long> idList;

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }
}
