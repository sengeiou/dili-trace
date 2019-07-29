package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.service.RegisterBillService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class RegisterBillServiceImpl extends BaseServiceImpl<RegisterBill, Long> implements RegisterBillService {

    public RegisterBillMapper getActualDao() {
        return (RegisterBillMapper)getDao();
    }

    @Override
    public List<RegisterBill> findByExeMachineNo(String exeMachineNo, int pageSize) {
        return getActualDao().findByExeMachineNo(exeMachineNo,pageSize);
    }

    @Override
    public List<RegisterBill> findByProductName(String productName) {
        RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        registerBill.setProductName(productName);
        return list(registerBill);
    }

    @Override
    public RegisterBill findByCode(Long code) {
        RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        registerBill.setCode(code);
        List<RegisterBill> list = list(registerBill);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public RegisterBill findByTradeNo(Long tradeNo) {
        RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        registerBill.setTradeNo(tradeNo);
        List<RegisterBill> list = list(registerBill);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
    public int matchDetectBind(String tradeNo,String tallyAreaNo,String productName,String idCardNo,Date settlement){
        MatchDetectParam matchDetectParam = new MatchDetectParam();
        matchDetectParam.setTradeNo(tradeNo);
        matchDetectParam.setTallyAreaNo(tallyAreaNo);
        matchDetectParam.setProductName(productName);
        matchDetectParam.setIdCardNo(idCardNo);
        matchDetectParam.setEnd(settlement);
        Date start = new Date(settlement.getTime()-(48*3600000));
        matchDetectParam.setStart(start);
        return getActualDao().matchDetectBind(matchDetectParam);
    }
}