package com.dili.trace.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.api.output.SeparateSalesApiListOutput;
import com.dili.trace.api.output.SeparateSalesApiListQueryInput;
import com.dili.trace.dao.SeparateSalesRecordMapper;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import tk.mybatis.mapper.entity.Example;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class SeparateSalesRecordServiceImpl extends BaseServiceImpl<SeparateSalesRecord, Long>
		implements SeparateSalesRecordService {

	private static final Logger logger = LoggerFactory.getLogger(SeparateSalesRecordServiceImpl.class);

	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	UserService userService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	CheckinOutRecordService checkinOutRecordService;

	public SeparateSalesRecordMapper getActualDao() {
		return (SeparateSalesRecordMapper) getDao();
	}

	@Override
	public List<SeparateSalesRecord> findByRegisterBillCode(String registerBillCode) {
		Example example = new Example(SeparateSalesRecord.class);
		example.and().andEqualTo("registerBillCode", registerBillCode).andIn("salesType",
				Arrays.asList(TradeTypeEnum.NONE.getCode(), TradeTypeEnum.SEPARATE_SALES.getCode()));
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

		if (queryInput.getPage() == null || queryInput.getPage() <= 0) {
			queryInput.setPage(1);
		}
		if (queryInput.getRows() == null || queryInput.getRows() <= 0) {
			queryInput.setRows(10);
		}
		queryInput.setOffSet((queryInput.getPage() - 1) * queryInput.getRows());

		List<SeparateSalesApiListOutput> list = this.getActualDao().listSeparateSalesOutput(queryInput);

		List results = ValueProviderUtils.buildDataByProvider(queryInput, list);
		return new EasyuiPageOutput(total, results);
	}

	

	
	@Override
	public int deleteSeparateSalesRecordByBillId(Long billId) {
		if (billId == null) {
			return 0;
		}
		SeparateSalesRecord queryCondition = new SeparateSalesRecord();
		queryCondition.setBillId(billId);
		List<Long> idList = this.listByExample(queryCondition).stream().map(SeparateSalesRecord::getId)
				.collect(Collectors.toList());
		if (!idList.isEmpty()) {
			this.delete(idList);
		}
		return idList.size();
	}


	

}
