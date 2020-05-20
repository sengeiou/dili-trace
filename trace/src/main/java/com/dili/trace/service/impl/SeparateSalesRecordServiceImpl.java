package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.api.dto.SeparateSalesApiListOutput;
import com.dili.trace.api.dto.SeparateSalesApiListQueryInput;
import com.dili.trace.dao.SeparateSalesRecordMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.service.SeparateSalesRecordService;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class SeparateSalesRecordServiceImpl extends BaseServiceImpl<SeparateSalesRecord, Long>
        implements SeparateSalesRecordService {

    public SeparateSalesRecordMapper getActualDao() {
        return (SeparateSalesRecordMapper) getDao();
    }

    @Override
    public List<SeparateSalesRecord> findByRegisterBillCode(String registerBillCode) {
        Example example = new Example(SeparateSalesRecord.class);
        example.and().andEqualTo("registerBillCode", registerBillCode).andIn("salesType",
                Arrays.asList(SalesTypeEnum.ONE_SALES.getCode(), SalesTypeEnum.SEPARATE_SALES.getCode()));
        return this.getActualDao().selectByExample(example);
    }

    @Override
    public Integer alreadySeparateSalesWeight(String registerBillCode) {
        Integer alreadyWeight = getActualDao().alreadySeparateSalesWeight(registerBillCode);
        if (alreadyWeight == null) {
            alreadyWeight = 0;
        }
        return alreadyWeight;
    }

    @Override
    public Integer getAlreadySeparateSalesWeightByTradeNo(String tradeNo) {
        Integer alreadyWeight = getActualDao().getAlreadySeparateSalesWeightByTradeNo(tradeNo);
        if (alreadyWeight == null) {
            alreadyWeight = 0;
        }
        return alreadyWeight;
    }

    @Override
    public List<SeparateSalesApiListOutput> listByQueryInput(SeparateSalesApiListQueryInput queryInput) {
        List<SeparateSalesApiListOutput> list = this.getActualDao().listSeparateSalesOutput(queryInput);
        return list;
    }

    @Override
    public EasyuiPageOutput listPageByQueryInput(SeparateSalesApiListQueryInput queryInput) throws Exception {
        Long total = this.getActualDao().countSeparateSalesOutput(queryInput);
        List<SeparateSalesApiListOutput> list = this.getActualDao().listSeparateSalesOutput(queryInput);

        List results = ValueProviderUtils.buildDataByProvider(queryInput, list);
        return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
    }

    @Override
    public void checkInSeparateSalesRecord(Long checkinRecordId, Long billId) {
        SeparateSalesRecord queryCondition = DTOUtils.newDTO(SeparateSalesRecord.class);
        queryCondition.setBillId(billId);
        queryCondition.setSalesType(SalesTypeEnum.OWNED.getCode());
        SeparateSalesRecord item = this.listByExample(queryCondition).stream().findFirst()
                .orElse(DTOUtils.newDTO(SeparateSalesRecord.class));
        if (item != null) {
            SeparateSalesRecord updatable = DTOUtils.newDTO(SeparateSalesRecord.class);
            updatable.setCheckinRecordId(checkinRecordId);
            updatable.setId(item.getId());
            this.updateSelective(updatable);
        }
    }

    @Override
    public void createOwnedSeparateSales(RegisterBill registerBill) {
        SeparateSalesRecord queryCondition = DTOUtils.newDTO(SeparateSalesRecord.class);
        queryCondition.setBillId(registerBill.getId());
        queryCondition.setSalesType(SalesTypeEnum.OWNED.getCode());

        SeparateSalesRecord item = this.listByExample(queryCondition).stream().findFirst()
                .orElse(DTOUtils.newDTO(SeparateSalesRecord.class));
        if (item.getId() == null) {
            item.setBillId(registerBill.getId());
            item.setRegisterBillCode(registerBill.getCode());
            item.setSalesWeight(registerBill.getWeight());
            item.setStoreWeight(BigDecimal.valueOf(registerBill.getWeight()));
            item.setSalesUserId(registerBill.getUserId());
            item.setSalesUserName(registerBill.getName());

            item.setParentId(null);
            item.setSalesPlate(registerBill.getPlate());
            item.setCreated(new Date());
            item.setModified(new Date());
            item.setSalesType(SalesTypeEnum.OWNED.getCode());
            item.setSalesCityId(0L);
            item.setSalesCityName("");
            this.insertSelective(item);
        } else {
            item.setSalesWeight(registerBill.getWeight());
            item.setStoreWeight(BigDecimal.valueOf(registerBill.getWeight()));
            item.setSalesPlate(registerBill.getPlate());
            item.setModified(new Date());
            this.updateSelective(item);
        }

    }
}