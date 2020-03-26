package com.dili.trace.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.CheckSheetDetail;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.CheckSheetDetailService;
import com.dili.trace.service.CheckSheetService;
import com.dili.trace.service.RegisterBillService;

@Service
public class CheckSheetServiceImpl extends BaseServiceImpl<CheckSheet, Long> implements CheckSheetService {
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	CheckSheetDetailService checkSheetDetailService;

	@Transactional
	@Override
	public int createCheckSheet(CheckSheetInputDto input) {

		
		Map<Long,String>idAndAliasNameMap=CollectionUtils.emptyIfNull(input.getRegisterBillList()).stream().filter(Objects::nonNull).filter(item->{
			
			return item.getId()!=null;
			
		}).collect(Collectors.toMap(RegisterBill::getId,item->item.getAliasName()));
				
		if (idAndAliasNameMap.isEmpty()) {
			return 0;
		}
		
		List<Long> idList =new ArrayList<>(idAndAliasNameMap.keySet());

		RegisterBillDto queryCondition = DTOUtils.newDTO(RegisterBillDto.class);
		queryCondition.setIdList(idList);
		List<RegisterBill> registerBillList = registerBillService.listByExample(queryCondition);

		if (registerBillList.isEmpty()) {
			return 0;
		}

		boolean withoutCheckSheet = registerBillList.stream().allMatch(bill -> bill.getCheckSheetId() == null);
		if (!withoutCheckSheet) {
			throw new BusinessException("已经有登记单创建了检验单");
		}
		boolean allBelongSamePerson = registerBillList.stream().allMatch(bill -> bill.getCheckSheetId() == null);
		if (!allBelongSamePerson) {
			throw new BusinessException("登记单不属于同一个业户");
		}

		this.insertExact(input);
		List<CheckSheetDetail> checkSheetDetailList = registerBillList.stream().map(bill -> {

			CheckSheetDetail detail = DTOUtils.newDTO(CheckSheetDetail.class);
			detail.setCheckSheetId(input.getId());
			detail.setCreated(new Date());
			detail.setModified(new Date());
			detail.setOriginId(bill.getOriginId());
			detail.setOriginName(bill.getOriginName());
			detail.setProductId(bill.getProductId());
			detail.setProductName(bill.getProductName());
			detail.setProductAliasName(idAndAliasNameMap.get(bill.getId()));
			detail.setRegisterBillId(bill.getId());
			
			bill.setCheckSheetId(input.getId());
			return detail;
		}).collect(Collectors.toList());

		List<RegisterBill> updateRegisterBillList = registerBillList.stream().map(bill -> {

			RegisterBill item = DTOUtils.newDTO(RegisterBill.class);

			item.setId(bill.getId());
			item.setCheckSheetId(bill.getCheckSheetId());
			return item;
		}).collect(Collectors.toList());
		this.registerBillService.batchUpdateSelective(updateRegisterBillList);

		return this.checkSheetDetailService.batchInsert(checkSheetDetailList);
	}

}
