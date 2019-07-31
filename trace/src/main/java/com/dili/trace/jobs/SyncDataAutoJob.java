package com.dili.trace.jobs;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dili.ss.domain.BasePage;
import com.dili.trace.etrade.domain.VTradeBill;
import com.dili.trace.etrade.service.VTradeBillService;

@Component
public class SyncDataAutoJob {
	private static final Logger logger=LoggerFactory.getLogger(SyncDataAutoJob.class);
	@Autowired
	VTradeBillService vTradeBillService;

	//间隔两分钟同步数据
//	@Scheduled(fixedDelay = 1000L*60L*2L)
	public void execute() {

		logger.info("===sync data===");
		//List<VTradeBill>list=this.vTradeBillService.listByExample(new VTradeBill());
		//System.out.println(list);
		while(true) {
			//查询同步点
			Long maxBillId=this.getLocalMaxBillID();
			if(maxBillId!=null) {
				//根据同步点查询并同步数据
				BasePage<VTradeBill>page=this.findRemoteData(maxBillId);
				if(page.getTotalItem()==0) {
					break;
				}else {
					this.syncData(page);
				}
			}else {
				//获得最新的同步点
				VTradeBill vTradeBill=	getRemoteLatestData();
				if(vTradeBill==null) {
					break;
				}else {
					//将同步点的数据进行处理
					this.syncLatestData(vTradeBill);
				}
			}
		}

	}
	/**
	 * 同步数据到mysql数据库
	 * @param page
	 * @return
	 */
	private boolean syncData(BasePage<VTradeBill>page) {
		return true;
	}
	/**
	 * 同步第一条数据到mysql数据库
	 * @param page
	 * @return
	 */
	private boolean syncLatestData(VTradeBill vTradeBill) {
		return true;
	}
	/**
	 * 基于maxBillId查找增量数据
	 * @param maxBillId
	 * @return
	 */
	private BasePage<VTradeBill>findRemoteData(Long maxBillId){
		
		VTradeBill example=new VTradeBill();
		example.setPage(1);
		example.setRows(100);
		example.setSort("billID");
		example.setOrder("asc");
		return this.vTradeBillService.listPageByExample(example);
		
	}
	/**
	 * 基于maxBillId查找增量数据
	 * @param maxBillId
	 * @return
	 */
	private VTradeBill getRemoteLatestData(){
		
		VTradeBill example=new VTradeBill();
		example.setPage(1);
		example.setRows(0);
		example.setSort("billID");
		example.setOrder("asc");
		List<VTradeBill>list=this.vTradeBillService.listPageByExample(example).getDatas();
		return CollectionUtils.emptyIfNull(list).stream().findFirst().orElse(null);
	}
	private Long getLocalMaxBillID() {
		return null;
	}

}
