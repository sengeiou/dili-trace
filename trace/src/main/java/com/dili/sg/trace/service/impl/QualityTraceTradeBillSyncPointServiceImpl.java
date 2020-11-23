package com.dili.sg.trace.service.impl;

import java.util.List;

import com.dili.sg.trace.glossary.OrderVersionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.sg.trace.dao.QualityTraceTradeBillSyncPointMapper;
import com.dili.sg.trace.domain.QualityTraceTradeBill;
import com.dili.sg.trace.domain.QualityTraceTradeBillSyncPoint;
import com.dili.sg.trace.dto.QualityTraceTradeBillSyncPointDto;
import com.dili.sg.trace.exception.TraceBizException;
import com.dili.sg.trace.service.QualityTraceTradeBillService;
import com.dili.sg.trace.service.QualityTraceTradeBillSyncPointService;

@EnableAsync
@Service
public class QualityTraceTradeBillSyncPointServiceImpl extends BaseServiceImpl<QualityTraceTradeBillSyncPoint, Long>
		implements QualityTraceTradeBillSyncPointService, CommandLineRunner {

	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;

	public QualityTraceTradeBillSyncPointMapper getActualDao() {
		return (QualityTraceTradeBillSyncPointMapper) getDao();
	}

	@Transactional
	@Override
	public List<QualityTraceTradeBill> syncData(QualityTraceTradeBillSyncPoint pointItem,
			List<QualityTraceTradeBill> billList) {
		if (pointItem == null || pointItem.getId() == null || billList.isEmpty()) {
			throw new TraceBizException("参数为空");

		}
		QualityTraceTradeBillSyncPoint item = this.selectByIdForUpdate(pointItem.getId());
		if (item == null) {
			throw new TraceBizException("同步点不存在");
		}

//		if (!Objects.equals(pointItem.getBillId(), item.getBillId())) {
//			throw new TraceBizException("同步已经发生变化");
//
//		}
		for (QualityTraceTradeBill bill : billList) {
			if(item.getBillId()!=null){
				if(item.getBillId().compareTo(bill.getBillId())>=0){
					continue;
				}
			}
			QualityTraceTradeBillSyncPoint point = DTOUtils.newDTO(QualityTraceTradeBillSyncPoint.class);
			point.setId(item.getId());
			point.setBillId(bill.getBillId());
			point.setOrderId(bill.getOrderId());
			this.updateSelective(point);
			bill.setOrderVersion(item.getOrderVersion());
			this.qualityTraceTradeBillService.insertSelective(bill);
		}

		return billList;
	}

	@Override
	public void run(String... args) throws Exception {
		QualityTraceTradeBillSyncPointDto example = DTOUtils.newDTO(QualityTraceTradeBillSyncPointDto.class);
		int count = 	this.listByExample(example).size();
		if (count == 0) {
			QualityTraceTradeBillSyncPoint item = DTOUtils.newDTO(QualityTraceTradeBillSyncPoint.class);
			item.setOrderVersion(OrderVersionEnum.VERSION.getCode());
			this.insertSelective(item);

		}

	}

	@Transactional
	@Override
	public QualityTraceTradeBillSyncPoint selectByIdForUpdate(Long id) {
		return this.getActualDao().selectByIdForUpdate(id);
	}

}
