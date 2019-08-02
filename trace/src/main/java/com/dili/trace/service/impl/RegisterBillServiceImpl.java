package com.dili.trace.service.impl;

import com.dili.common.service.BizNumberFunction;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.glossary.*;
import com.dili.trace.service.RegisterBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class RegisterBillServiceImpl extends BaseServiceImpl<RegisterBill, Long> implements RegisterBillService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillServiceImpl.class);
    @Autowired
    BizNumberFunction bizNumberFunction;
    public RegisterBillMapper getActualDao() {
        return (RegisterBillMapper)getDao();
    }

    @Override
    public int createRegisterBill(RegisterBill registerBill) {
        registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
        registerBill.setVersion(1);
        if(registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TRADE_AREA.getCode().intValue()){
            //交易区没有理货区号
            registerBill.setTallyAreaNo(null);
        }
        return saveOrUpdate(registerBill);
    }

    @Override
    public List<RegisterBill> findByExeMachineNo(String exeMachineNo, int taskCount) {
        List<RegisterBill> exist = getActualDao().findByExeMachineNo(exeMachineNo);
        int existCount = exist.size();
        if(existCount==taskCount){
            LOGGER.info("获取的任务已经有相应的数量了" + taskCount);
            return exist;
        }else if(existCount<taskCount){
            //还需要再拿多少个。
            taskCount=taskCount-existCount;
            LOGGER.info("还需要再拿多少个："+taskCount);

        }
        List<Long> ids = getActualDao().findIdsByExeMachineNo(taskCount);
        StringBuilder sb = new StringBuilder();
        sb.append(0);
        for(Long id :ids){
            sb.append(",").append(id);
        }
        getActualDao().taskByExeMachineNo(exeMachineNo,sb.toString());
        return getActualDao().findByExeMachineNo(exeMachineNo);
    }

    @Override
    public List<RegisterBill> findByProductName(String productName) {
        RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        registerBill.setProductName(productName);
        return list(registerBill);
    }

    @Override
    public RegisterBill findByCode(String code) {
        RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        registerBill.setCode(code);
        List<RegisterBill> list = list(registerBill);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public RegisterBill findByTradeNo(String tradeNo) {
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
        Long id = getActualDao().findMatchDetectBind(matchDetectParam);
        return getActualDao().matchDetectBind(tradeNo,id);
    }

    @Override
    public int auditRegisterBill(Long id,Boolean pass) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()){
            if(pass){
                registerBill.setState(RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue());
            }else {
                registerBill.setState(RegisterBillStateEnum.NO_PASS.getCode().intValue());
            }
            return update(registerBill);
        }
        return 0;
    }

    @Override
    public int undoRegisterBill(Long id) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()){
            registerBill.setState(RegisterBillStateEnum.UNDO.getCode().intValue());
            return update(registerBill);
        }
        return 0;
    }

    @Override
    public int autoCheckRegisterBill(Long id) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()){
            registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            registerBill.setSampleSource(SampleSourceEnum.AUTO_CHECK.getCode().intValue());
            return update(registerBill);
        }
        return 0;
    }

    @Override
    public int samplingCheckRegisterBill(Long id) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()){
            registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            return update(registerBill);
        }
        return 0;
    }

    @Override
    public int reviewCheckRegisterBill(Long id) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.ALREADY_CHECK.getCode().intValue()
                && registerBill.getDetectState().intValue() == BillDetectStateEnum.NO_PASS.getCode().intValue()){
            registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            registerBill.setExeMachineNo(null);
            return update(registerBill);
        }
        return 0;
    }
}