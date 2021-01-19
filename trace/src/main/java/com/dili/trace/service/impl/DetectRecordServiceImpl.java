package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.DetectRecordMapper;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.DetectRecordStateEnum;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectRequestService;
import com.dili.uap.sdk.domain.UserTicket;
import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
        DetectRecord detectRecord = DTOUtils.newDTO(DetectRecord.class);
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
    public int saveDetectRecordManually(DetectRecord detectRecord, UserTicket userTicket) {
        this.saveOrUpdate(detectRecord);

        RegisterBill query = new RegisterBill();
        query.setCode(detectRecord.getRegisterBillCode());
        RegisterBill registerBill= StreamEx.of(billService.list(query)).findFirst().orElse(null);
        if (registerBill==null) {
            throw new TraceBizException("上传检测任务结果失败登记单【"+detectRecord.getRegisterBillCode()+"】查询失败");
        }

        DetectResultEnum detectResultEnum=StreamEx.ofNullable(detectRecord.getDetectState()).map(dt->{
            if(Objects.equals(1,dt)){
                return DetectResultEnum.PASSED;
            }
            if(Objects.equals(2,dt)){
                return DetectResultEnum.FAILED;
            }
            return null;
        }).nonNull().findFirst().orElseThrow(()->{
            return  new TraceBizException("检测结果不正确");
        });

        DetectTypeEnum detectTypeEnum=StreamEx.ofNullable(detectRecord.getDetectType()).map(dt->{
            if(Objects.equals(1,dt)){
                return DetectTypeEnum.INITIAL_CHECK;
            }
            if(Objects.equals(2,dt)){
                return DetectTypeEnum.RECHECK;
            }
            return null;
        }).nonNull().findFirst().orElseThrow(()->{
            return  new TraceBizException("检测类型不正确");
        });

        this.detectRequestService.manualCheck(registerBill.getBillId(), userTicket,detectTypeEnum,detectResultEnum);

        return 1;
    }
}