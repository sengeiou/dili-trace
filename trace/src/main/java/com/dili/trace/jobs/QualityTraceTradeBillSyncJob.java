package com.dili.trace.jobs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dili.ss.datasource.SwitchDataSource;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.QualityTraceTradeBillSyncPoint;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;
import com.dili.trace.etrade.domain.VTradeBill;
import com.dili.trace.etrade.domain.dto.VTradeBillQueryDTO;
import com.dili.trace.etrade.service.VTradeBillService;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.QualityTraceTradeBillSyncPointService;
@EnableAsync
@Component
public class QualityTraceTradeBillSyncJob {
	private static final Logger logger = LoggerFactory.getLogger(QualityTraceTradeBillSyncJob.class);
	@Autowired
	VTradeBillService vTradeBillService;
	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
	@Autowired
	QualityTraceTradeBillSyncPointService qualityTraceTradeBillSyncPointService;


	@Value("${schedule.enable:true}")
	private boolean scheduleEnable;
	
	// 间隔两分钟同步数据
	@Scheduled(fixedDelay = 1000L * 60L * 2L)
	public void execute() {
		if(!scheduleEnable) {
			return;
		}
		logger.info("===sync data===");
		// List<VTradeBill>list=this.vTradeBillService.listByExample(new VTradeBill());
		// System.out.println(list);
		while (true) {
			// 查询同步点
			VTradeBill vTradeBill = this.vTradeBillService.selectRemoteLatestData();
			if (vTradeBill == null || vTradeBill.getBillID() == null) {
				logger.info("电子结算无数据或数据出错");
				break;
			}
			Long remoteMaxBillId = vTradeBill.getBillID();

			Long localMaxBillId = this.getLocalMaxBillID();

			if (localMaxBillId != null) {
				if (localMaxBillId >= remoteMaxBillId) {
					logger.info("数据无需同步 localMaxBillId: {},remoteMaxBillId: {}", localMaxBillId, remoteMaxBillId);
					break;
				} else {
					// 根据同步点查询并同步数据
					List<VTradeBill> list = this.selectTopRemoteData(localMaxBillId);
					if (list.isEmpty()) {
						break;
					} else {
						this.syncVTradeBillList(localMaxBillId, list);
					}

				}
			} else {
				Long count=this.vTradeBillService.selectRemoteRepeatData(remoteMaxBillId);
				if(count!=null&&count>1) {
					this.qualityTraceTradeBillSyncPointService.syncPoint(vTradeBill.buildQualityTraceTradeBillSyncPoint());
				}else {
					this.syncVTradeBillList(localMaxBillId, Arrays.asList(vTradeBill));					
				}

			}

		}

	}

	

	/**
	 * 同步数据到mysql数据库
	 * 
	 * @param page
	 * @return
	 */
	private boolean syncVTradeBillList(Long localMaxBillId, List<VTradeBill> list) {
		CollectionUtils.emptyIfNull(list).stream().sorted(Comparator.comparing(VTradeBill::getBillID))
				.forEach(vTradeBill -> {

					this.syncVTradeBill(localMaxBillId, vTradeBill);

				});
		return true;
	}

	/**
	 * 同步第一条数据到mysql数据库
	 * 
	 * 
	 * @param page
	 * @return
	 */
	private boolean syncVTradeBill(Long localMaxBillId, VTradeBill vTradeBill) {

		QualityTraceTradeBill bill = vTradeBill.buildQualityTraceTradeBill();
		this.qualityTraceTradeBillSyncPointService.syncData(localMaxBillId, bill);
		this.qualityTraceTradeBillSyncPointService.fixData();
		return true;
	}

	/**
	 * 基于maxBillId查找增量数据
	 * 
	 * @param maxBillId
	 * @return
	 */
	private List<VTradeBill> selectTopRemoteData(Long startBillId) {

		VTradeBillQueryDTO example = new VTradeBillQueryDTO();
		example.setPage(1);
		example.setRows(100);
		example.setMinBillID(startBillId);
		return this.vTradeBillService.selectTopRemoteData(example);

	}

	@SwitchDataSource()
	private Long getLocalMaxBillID() {

		QualityTraceTradeBillSyncPoint domain = DTOUtils.newDTO(QualityTraceTradeBillSyncPoint.class);
		domain.setSort("bill_id");
		domain.setOrder("desc");
		domain.setPage(1);
		domain.setRows(1);
		List<QualityTraceTradeBillSyncPoint> list = this.qualityTraceTradeBillSyncPointService.listPageByExample(domain)
				.getDatas();
		return CollectionUtils.isEmpty(list) ? null : list.get(0).getBillId();
	}

}
