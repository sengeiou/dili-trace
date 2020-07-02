package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.RegisterBill;

public interface RegisterBillMapper extends MyMapper<RegisterBill> {
    public List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBill query);

}