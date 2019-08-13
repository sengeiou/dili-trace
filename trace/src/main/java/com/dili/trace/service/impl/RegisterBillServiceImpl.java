package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.service.BizNumberFunction;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.RegisterBillStaticsDto;
import com.dili.trace.glossary.*;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class RegisterBillServiceImpl extends BaseServiceImpl<RegisterBill, Long> implements RegisterBillService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillServiceImpl.class);
    @Autowired
    BizNumberFunction bizNumberFunction;
    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    DetectRecordService detectRecordService;
    public RegisterBillMapper getActualDao() {
        return (RegisterBillMapper)getDao();
    }

    @Override
    public int createRegisterBill(RegisterBill registerBill) {
        if (checkBill(registerBill)) return 0;
        registerBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
        registerBill.setVersion(1);
        if(registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TRADE_AREA.getCode().intValue()){
            //交易区没有理货区号
            registerBill.setTallyAreaNo(null);
        }
        if(StringUtils.isBlank(registerBill.getOperatorName())){
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
        }
        return saveOrUpdate(registerBill);
    }

    private boolean checkBill(RegisterBill registerBill) {
        if(registerBill.getRegisterSource()==null || registerBill.getRegisterSource().intValue()==0){
            LOGGER.error("登记来源不能为空");
            return true;
        }
        if(StringUtils.isBlank(registerBill.getName())){
            LOGGER.error("业户姓名不能为空");
            return true;
        }
        if(StringUtils.isBlank(registerBill.getIdCardNo())){
            LOGGER.error("业户身份证号不能为空");
            return true;
        }
        if(StringUtils.isBlank(registerBill.getAddr())){
            LOGGER.error("业户身份证地址不能为空");
            return true;
        }
        if(StringUtils.isBlank(registerBill.getProductName())){
            LOGGER.error("商品名称不能为空");
            return true;
        }
        if(StringUtils.isBlank(registerBill.getOriginName())){
            LOGGER.error("商品产地不能为空");
            return true;
        }
        if(registerBill.getWeight()==null || registerBill.getWeight().longValue()==0L){
            LOGGER.error("商品重量不能为空");
            return true;
        }
        return false;
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
    public RegisterBillOutputDto findByTradeNo(String tradeNo) {
        RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
        registerBill.setTradeNo(tradeNo);
        List<RegisterBill> list = list(registerBill);
        if(list!=null && list.size()>0){
            return DTOUtils.as(list.get(0), RegisterBillOutputDto.class);
        }
        return null;
    }
    public int matchDetectBind(QualityTraceTradeBill qualityTraceTradeBill){

        MatchDetectParam matchDetectParam = new MatchDetectParam();
        matchDetectParam.setTradeNo(qualityTraceTradeBill.getOrderId());
        matchDetectParam.setTallyAreaNo(qualityTraceTradeBill.getTradetypeName());
        matchDetectParam.setProductName(qualityTraceTradeBill.getProductName());
        matchDetectParam.setIdCardNo(qualityTraceTradeBill.getSellerIDNo());
        matchDetectParam.setEnd(qualityTraceTradeBill.getOrderPayDate());
        Date start = new Date(qualityTraceTradeBill.getOrderPayDate().getTime()-(48*3600000));
        matchDetectParam.setStart(start);
        LOGGER.info("进行匹配:"+matchDetectParam.toString());
        Long id = getActualDao().findMatchDetectBind(matchDetectParam);
        return getActualDao().matchDetectBind(qualityTraceTradeBill.getOrderId(),qualityTraceTradeBill.getNetWeight(),id);
    }

    @Override
    public int auditRegisterBill(Long id,Boolean pass) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()){
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
            if(pass){
                registerBill.setState(RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue());
                if(StringUtils.isNotBlank(registerBill.getDetectReportUrl())){
                    //有检测报告，直接通过检测
                    registerBill.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
                }
            }else {
                registerBill.setState(-1);
            }
            return update(registerBill);
        }
        return 0;
    }

    @Override
    public int undoRegisterBill(Long id) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()){
            UserTicket userTicket = getOptUser();
            LOGGER.info(userTicket.getDepName()+":"+userTicket.getRealName()+"删除登记单"+ JSON.toJSON(registerBill).toString());
            delete(id);
            //return update(registerBill);
        }
        return 0;
    }

    @Override
    public int autoCheckRegisterBill(Long id) {
        RegisterBill registerBill =get(id);
        if(registerBill.getState().intValue()== RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()){
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
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
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
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
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
            registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            registerBill.setExeMachineNo(null);
            return update(registerBill);
        }
        return 0;
    }
    UserTicket getOptUser(){
        return SessionContext.getSessionContext().getUserTicket();
    }

    @Override
    public RegisterBillOutputDto findAndBind(String tradeNo) {
        if(StringUtils.isBlank(tradeNo)){
            return null;
        }
        RegisterBillOutputDto registerBill = findByTradeNo(tradeNo);
        QualityTraceTradeBill qualityTraceTradeBill =qualityTraceTradeBillService.findByTradeNo(tradeNo);
        if(registerBill == null){
            int result = matchDetectBind(qualityTraceTradeBill);
            if(result==1){
                registerBill=findByTradeNo(tradeNo);
            }
        }
        if(registerBill!=null){
            List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(registerBill.getCode());
            registerBill.setSeparateSalesRecords(records);
            registerBill.setDetectRecord(detectRecordService.findByRegisterBillCode(registerBill.getCode()));
        }
        registerBill.setQualityTraceTradeBill(qualityTraceTradeBill);
        return registerBill;
    }
    @Override
	public RegisterBillStaticsDto groupByState(RegisterBillDto dto) {
    	return		 this.getActualDao().groupByState(dto);
		
	}
}