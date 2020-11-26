package com.dili.sg.trace.jobs;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.dili.sg.trace.glossary.QualityTraceTradeBillMatchStatusEnum;
import com.dili.trace.service.SgRegisterBillService;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.domain.sg.QualityTraceTradeBillDto;
import com.dili.trace.service.QualityTraceTradeBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dto.MatchDetectParam;

/**
 * 自动匹配登记单与订单信息
 */
@Component
public class QualityTraceTradeBillAutoMatchJob {
	private static final Logger logger = LoggerFactory.getLogger(QualityTraceTradeBillAutoMatchJob.class);
	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
	@Autowired
    SgRegisterBillService registerBillService;
	@Autowired
	RegisterBillMapper registerBillMapper;
	@Autowired
	private TaskScheduler taskScheduler;

	@Value("${schedule.enable:true}")
	private boolean scheduleEnable;
	// 每10分钟匹配一次前七天的登记单
	@Value("${qualityTraceTradeBill.match7DaysDelay:10}")
	private Long match7DaysDelay;

	// 每2分钟匹配一次当天的登记单
	@Value("${qualityTraceTradeBill.matchTodaysDelay:2}")
	private Long matchTodaysDelay;

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		if (scheduleEnable) {
			this.registeTrigger(this::executeMatch7daysRegisterBill, 60 * match7DaysDelay);
			this.registeTrigger(this::executeMatchTodayRegisterBill, 60 * matchTodaysDelay);
		}

	}

	/**
	 * 同步数据
	 */
//	@Async
//	@Scheduled(fixedDelay = 1000L * 60L * 30L)
	public void executeMatchTodayRegisterBill() {
		int page=1;
		while (true) {
			List<QualityTraceTradeBill> qualityTraceTradeBillList = this.queryQualityTraceTradeBill(
					Arrays.asList(QualityTraceTradeBillMatchStatusEnum.UNMATCHE_7DAYS.getCode()),page++);
			if (qualityTraceTradeBillList.isEmpty()) {
				logger.info("没有数据可以当天登记单匹配");
				break;
			}
			qualityTraceTradeBillList.stream().filter(Objects::nonNull).filter(qb -> qb.getOrderPayDate() != null)
					.forEach(qualityTraceTradeBill -> {
						try {
							this.matchToday(qualityTraceTradeBill);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					});
		}
	}

	/**
	 * 间隔两分钟同步数据
	 */
	// 间隔两分钟同步数据
//	@Async
//	@Scheduled(fixedDelay = 1000L * 60L * 1L)
	public void executeMatch7daysRegisterBill() {
		int page=1;
		while (true) {
			List<QualityTraceTradeBill> qualityTraceTradeBillList = this
					.queryQualityTraceTradeBill(Arrays.asList(QualityTraceTradeBillMatchStatusEnum.INITED.getCode()),page++);
			if (qualityTraceTradeBillList.isEmpty()) {
				logger.info("没有数据可以进行前七天登记单匹配");
				break;
			}
			qualityTraceTradeBillList.stream().filter(Objects::nonNull).filter(qb -> qb.getOrderPayDate() != null)
					.forEach(qualityTraceTradeBill -> {
						try {

							this.match7days(qualityTraceTradeBill);

						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					});
		}
	}

	/**
	 * 查询 数据
	 * @param matchStatusList
	 * @param page
	 * @return
	 */
	private List<QualityTraceTradeBill> queryQualityTraceTradeBill(List<Integer> matchStatusList,int page) {

		QualityTraceTradeBillDto queryCondtion = DTOUtils.newDTO(QualityTraceTradeBillDto.class);
		queryCondtion.setMatchStatusList(matchStatusList);
		queryCondtion.setSort("id");
		queryCondtion.setOrder("asc");
		queryCondtion.setPage(page);
		queryCondtion.setRows(100);
		List<QualityTraceTradeBill> qualityTraceTradeBillList = this.qualityTraceTradeBillService
				.listPageByExample(queryCondtion).getDatas();
		return qualityTraceTradeBillList;
	}

	/**
	 * 结束时间匹配
	 * @param qualityTraceTradeBill
	 * @return
	 */
	private boolean endMatch(QualityTraceTradeBill qualityTraceTradeBill) {

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime payDate = qualityTraceTradeBill.getOrderPayDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime todayEndOfPayDate = payDate.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

		if (now.isAfter(todayEndOfPayDate)) {
			logger.info("超时,未能匹配:{}", qualityTraceTradeBill.getId());
			QualityTraceTradeBill domain = DTOUtils.newDTO(QualityTraceTradeBill.class);
			domain.setId(qualityTraceTradeBill.getId());
			domain.setMatchStatus(QualityTraceTradeBillMatchStatusEnum.UNMATCHED.getCode());
			// 更新条件对象
			QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
			condition.setId(qualityTraceTradeBill.getId());
			condition.setMatchStatus(qualityTraceTradeBill.getMatchStatus());
			this.qualityTraceTradeBillService.updateSelectiveByExample(domain, condition);
			return true;
		}
		return false;
	}

	/**
	 * 前7天匹配
	 * @param qualityTraceTradeBill
	 * @return
	 */
	private boolean match7days(QualityTraceTradeBill qualityTraceTradeBill) {

		LocalDateTime payDate = qualityTraceTradeBill.getOrderPayDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime start = payDate.minusDays(7);
		LocalDateTime end = payDate;
		logger.info("交易单:{} 进行前七天{}-{}匹配",qualityTraceTradeBill.getId(),start,end);
		return this.matchRegisterBill(qualityTraceTradeBill, start, end,
				QualityTraceTradeBillMatchStatusEnum.UNMATCHE_7DAYS);

	}

	/**
	 * 当天匹配
	 * @param qualityTraceTradeBill
	 * @return
	 */
	private boolean matchToday(QualityTraceTradeBill qualityTraceTradeBill) {

		LocalDateTime payDate = qualityTraceTradeBill.getOrderPayDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime start = payDate;
		LocalDateTime end = payDate.withHour(23).withMinute(59).withSecond(59);
		logger.info("交易单:{} 进行当天{}-{}匹配",qualityTraceTradeBill.getId(),start,end);
		boolean matched= this.matchRegisterBill(qualityTraceTradeBill, start, end,null);
		
		//如果没有匹配到交易日当天,则进行时间判断,决定是否终止匹配
		if(!matched) {
			boolean endMatch = this.endMatch(qualityTraceTradeBill);
			if (endMatch) {
				logger.info("交易单:{} 已过今天,不再匹配",qualityTraceTradeBill.getId());
				return true;
			}
		}
		
		return false;

	}

	/**
	 * 匹配数据
	 * @param qualityTraceTradeBill
	 * @param start
	 * @param end
	 * @param unMatchStatus
	 * @return
	 */
	private boolean matchRegisterBill(QualityTraceTradeBill qualityTraceTradeBill, LocalDateTime start,
			LocalDateTime end, QualityTraceTradeBillMatchStatusEnum unMatchStatus) {

		MatchDetectParam matchDetectParam = new MatchDetectParam();
		// matchDetectParam.setTradeNo(qualityTraceTradeBill.getOrderId());
//		matchDetectParam.setTradeTypeId(qualityTraceTradeBill.getTradetypeId());
		// matchDetectParam.setTradeTypeName(qualityTraceTradeBill.getTradetypeName());
		matchDetectParam.setProductName(qualityTraceTradeBill.getProductName());
		matchDetectParam.setIdCardNo(qualityTraceTradeBill.getSellerIDNo());

		matchDetectParam.setStart(Date.from(start.toInstant(OffsetDateTime.now().getOffset())));
		matchDetectParam.setEnd(Date.from(end.toInstant(OffsetDateTime.now().getOffset())));
		logger.info("进行条件匹配:{}", matchDetectParam);
		List<RegisterBill> registerBillList = this.registerBillMapper.findUnMatchedRegisterBill(matchDetectParam);
		RegisterBill registerBill = registerBillList.stream().findFirst().orElse(null);
		if (registerBill != null) {
			
			QualityTraceTradeBill domain = DTOUtils.newDTO(QualityTraceTradeBill.class);
			domain.setId(qualityTraceTradeBill.getId());
			domain.setMatchStatus(QualityTraceTradeBillMatchStatusEnum.MATCHED.getCode());
			domain.setRegisterBillCode(registerBill.getCode());
			// 更新条件对象
			QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
			condition.setId(qualityTraceTradeBill.getId());
			condition.setMatchStatus(qualityTraceTradeBill.getMatchStatus());
			this.qualityTraceTradeBillService.updateSelectiveByExample(domain, condition);
			logger.info("交易单:{} 匹配到登记单:{},更新为新匹配状态:{}",qualityTraceTradeBill.getId(),registerBill.getId(),QualityTraceTradeBillMatchStatusEnum.MATCHED);
			return true;
		} else if (unMatchStatus != null) {
			logger.info("交易单:{} 没有匹配到登记单,更新为新匹配状态:{}",qualityTraceTradeBill.getId(),unMatchStatus);
			QualityTraceTradeBill domain = DTOUtils.newDTO(QualityTraceTradeBill.class);
			domain.setId(qualityTraceTradeBill.getId());
			domain.setMatchStatus(unMatchStatus.getCode());
			// 更新条件对象
			QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
			condition.setId(qualityTraceTradeBill.getId());
			condition.setMatchStatus(qualityTraceTradeBill.getMatchStatus());
			this.qualityTraceTradeBillService.updateSelectiveByExample(domain, condition);
			return true;

		}
		logger.info("交易单:{} 没有匹配到登记单,保持原有匹配状态:{}",qualityTraceTradeBill.getId(),qualityTraceTradeBill.getMatchStatus());
		return false;

	}

	/**
	 * 注册定时触发
	 * @param task
	 * @param delaySeconds
	 * @return
	 */
	private Trigger registeTrigger(Runnable task, final Long delaySeconds) {

		Trigger trigger = new PeriodicTrigger(0) {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				Date lastCompletionTime = triggerContext.lastCompletionTime();
				if (lastCompletionTime != null) {
					LocalDateTime next = lastCompletionTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
							.plusSeconds(delaySeconds);
					return Date.from(next.toInstant(OffsetDateTime.now().getOffset()));
				}
				Date lastExecution = triggerContext.lastActualExecutionTime();
				if (lastExecution == null) {
					LocalDateTime next = LocalDateTime.now().plusSeconds(delaySeconds);
					return Date.from(next.toInstant(OffsetDateTime.now().getOffset()));
				}
				return null;
			}
		};

		taskScheduler.schedule(task, trigger);
		return trigger;

	}
}