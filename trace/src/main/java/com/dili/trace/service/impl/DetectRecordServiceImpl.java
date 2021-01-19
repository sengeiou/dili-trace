package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.DetectRecordMapper;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.DetectRecordInputDto;
import com.dili.trace.enums.DetectRecordStateEnum;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectRequestService;
import com.dili.uap.sdk.domain.UserTicket;
import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class DetectRecordServiceImpl extends BaseServiceImpl<DetectRecord, Long> implements DetectRecordService {
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    BillService billService;

    public DetectRecordMapper getActualDao() {
        return (DetectRecordMapper)getDao();
    }

    @Override
    @Transactional
    public int saveDetectRecord(DetectRecord detectRecord) {
        saveOrUpdate(detectRecord);
        return 1;
    }

    @Override
    public DetectRecord findByRegisterBillCode(String registerBillCode) {
        DetectRecord detectRecord = new DetectRecord();
        detectRecord.setRegisterBillCode(registerBillCode);
        detectRecord.setSort("id");
        detectRecord.setOrder("desc");
        List<DetectRecord> list = this.listByExample(detectRecord);
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

	@Override
	public List<DetectRecord> findTop2AndLatest(String registerBillCode) {
		return this.getActualDao().findTop2AndLatest(registerBillCode);
	}

    @Override
    public  Map<String, DetectRecord>  findMapRegisterBillByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Maps.newHashMap();
        }
        Example example = new Example(DetectRecord.class);
        example.and().andIn("id", ids);
        return StreamEx.of(selectByExample(example)).toMap(DetectRecord::getRegisterBillCode, Function.identity());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveDetectRecordManually(DetectRecordInputDto input, UserTicket userTicket) {
        DetectResultEnum detectResultEnum=DetectResultEnum.fromCode(input.getDetectResult()).orElseThrow(()->{
            return  new TraceBizException("检测结果不正确");
        });

        DetectTypeEnum detectTypeEnum=DetectTypeEnum.fromCode(input.getDetectType()).orElseThrow(()->{
            return  new TraceBizException("检测类型不正确");
        });


        DetectRecord detectRecord=new DetectRecord();
        try {
            BeanUtils.copyProperties(detectRecord,input);
            detectRecord.setDetectState(input.getDetectResult());
            detectRecord.setDetectType(input.getDetectType());
        } catch (Exception e) {
           throw new TraceBizException("程序错误");
        }

        RegisterBill query = new RegisterBill();
        query.setCode(detectRecord.getRegisterBillCode());
        RegisterBill registerBill= StreamEx.of(billService.list(query)).findFirst().orElse(null);
        if (registerBill==null) {
            throw new TraceBizException("上传检测任务结果失败登记单【"+detectRecord.getRegisterBillCode()+"】查询失败");
        }
        detectRecord.setDetectRequestId(registerBill.getDetectRequestId());
        this.saveOrUpdate(detectRecord);
        this.detectRequestService.manualCheck(detectRecord.getId(), registerBill.getBillId(), userTicket,detectTypeEnum,detectResultEnum);

        return 1;
    }
}