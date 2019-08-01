package com.dili.trace.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.QualityTraceTradeBillSyncPointMapper;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.QualityTraceTradeBillSyncPoint;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;
import com.dili.trace.dto.QualityTraceTradeBillSyncPointDto;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.QualityTraceTradeBillSyncPointService;
@EnableAsync
@Service
public class QualityTraceTradeBillSyncPointServiceImpl extends BaseServiceImpl<QualityTraceTradeBillSyncPoint, Long>
		implements QualityTraceTradeBillSyncPointService {

	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;

	public QualityTraceTradeBillSyncPointMapper getActualDao() {
		return (QualityTraceTradeBillSyncPointMapper) getDao();
	}

	@Transactional
	@Override
	public QualityTraceTradeBill syncData(Long localMaxBillId, QualityTraceTradeBill bill) {
		QualityTraceTradeBillSyncPoint point = DTOUtils.newDTO(QualityTraceTradeBillSyncPoint.class);
		point.setBillId(bill.getBillId());
		point.setOrderId(bill.getOrderId());
		this.insertSelective(point);
		this.qualityTraceTradeBillService.insertSelective(bill);
		// 删除多余同步点
		if (localMaxBillId != null) {
			QualityTraceTradeBillSyncPointDto example = DTOUtils.newDTO(QualityTraceTradeBillSyncPointDto.class);
			example.setMinBillId(bill.getBillId());
			this.deleteByExample(example);
		}

		return bill;
	}

	// 对重复数据进行处理
	@Async
	public void fixData() {
		// 删除冲正数据和重复数据
		List<QualityTraceTradeBillRepeatDto> repeatList = this.qualityTraceTradeBillService.selectRepeatedOrderId();
		for (QualityTraceTradeBillRepeatDto dto : repeatList) {
			QualityTraceTradeBill example = DTOUtils.newDTO(QualityTraceTradeBill.class);
			example.setOrderId(dto.getOrderId());
			example.setSort("id");
			example.setOrder("desc");
			List<QualityTraceTradeBill> list = this.qualityTraceTradeBillService.listByExample(example);
			// 充正
			boolean deleteAll = list.stream().map(QualityTraceTradeBill::getBillId).distinct().count() > 1;
			if (deleteAll) {
				this.qualityTraceTradeBillService
						.delete(list.stream().map(QualityTraceTradeBill::getId).collect(Collectors.toList()));
			} else {
				// 数据重复
				long maxId = list.get(list.size() - 1).getId();
				// 保留id最大,删除其他数据
				List<Long> idList = list.stream().map(QualityTraceTradeBill::getId).filter(id -> !id.equals(maxId))
						.collect(Collectors.toList());
				if (!idList.isEmpty()) {
					this.qualityTraceTradeBillService.delete(idList);
				}

			}
		}
	}

}
