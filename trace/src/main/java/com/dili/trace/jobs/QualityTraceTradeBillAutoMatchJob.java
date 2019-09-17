package com.dili.trace.jobs;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.dto.QualityTraceTradeBillDto;
import com.dili.trace.glossary.QualityTraceTradeBillMatchStatusEnum;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.RegisterBillService;

@Component
public class QualityTraceTradeBillAutoMatchJob {
	private static final Logger logger = LoggerFactory.getLogger(QualityTraceTradeBillAutoMatchJob.class);
	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	RegisterBillMapper registerBillMapper;
	@Autowired
	private TaskScheduler taskScheduler;
	
	//每10分钟匹配一次前七天的登记单
	@Value("${qualityTraceTradeBill.match7DaysDelay:10}")
	private Long match7DaysDelay;
	
	//每2分钟匹配一次前七天的登记单
	@Value("${qualityTraceTradeBill.matchTodaysDelay:2}")
	private Long matchTodaysDelay;

	@PostConstruct
	public void init() {

		this.registeTrigger(this::executeMatch7daysRegisterBill, 60 * match7DaysDelay);
		this.registeTrigger(this::executeMatchTodayRegisterBill, 60 * matchTodaysDelay);
		

	}

	
	// 间隔两分钟同步数据
//	@Async
//	@Scheduled(fixedDelay = 1000L * 60L * 30L)
	public void executeMatchTodayRegisterBill() {
		while (true) {
			List<QualityTraceTradeBill> qualityTraceTradeBillList = this
					.queryQualityTraceTradeBill(Arrays.asList(QualityTraceTradeBillMatchStatusEnum.UNMATCHE_7DAYS.getCode(),QualityTraceTradeBillMatchStatusEnum.UNMATCHE_TODAY.getCode()));
			if (qualityTraceTradeBillList.isEmpty()) {
				logger.info("executeMatchTodayRegisterBill no data");
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

	// 间隔两分钟同步数据
//	@Async
//	@Scheduled(fixedDelay = 1000L * 60L * 1L)
	public void executeMatch7daysRegisterBill() {
		while (true) {
			List<QualityTraceTradeBill> qualityTraceTradeBillList = this
					.queryQualityTraceTradeBill(Arrays.asList(QualityTraceTradeBillMatchStatusEnum.INITED.getCode()));
			if (qualityTraceTradeBillList.isEmpty()) {
				logger.info("executeMatch7daysRegisterBill no data");
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

	private List<QualityTraceTradeBill> queryQualityTraceTradeBill(List<Integer> matchStatusList) {

		QualityTraceTradeBillDto queryCondtion = DTOUtils.newDTO(QualityTraceTradeBillDto.class);
		queryCondtion.setMatchStatusList(matchStatusList);
		queryCondtion.setSort("id");
		queryCondtion.setOrder("asc");
		queryCondtion.setPage(1);
		queryCondtion.setRows(100);
		List<QualityTraceTradeBill> qualityTraceTradeBillList = this.qualityTraceTradeBillService
				.listPageByExample(queryCondtion).getDatas();
		return qualityTraceTradeBillList;
	}

	private boolean endMatch(QualityTraceTradeBill qualityTraceTradeBill) {

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime payDate = qualityTraceTradeBill.getOrderPayDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime todayEndOfPayDate = payDate.withHour(23).withMinute(59).withSecond(59);

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

	private boolean match7days(QualityTraceTradeBill qualityTraceTradeBill) {

		LocalDateTime payDate = qualityTraceTradeBill.getOrderPayDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime start = payDate.minusDays(7);
		LocalDateTime end = payDate;

		return this.matchRegisterBill(qualityTraceTradeBill, start, end,QualityTraceTradeBillMatchStatusEnum.UNMATCHE_7DAYS);

	}

	private boolean matchToday(QualityTraceTradeBill qualityTraceTradeBill) {

		LocalDateTime payDate = qualityTraceTradeBill.getOrderPayDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime start = payDate;
		LocalDateTime end = payDate.withHour(23).withMinute(59).withSecond(59);

		return this.matchRegisterBill(qualityTraceTradeBill, start, end,QualityTraceTradeBillMatchStatusEnum.UNMATCHE_TODAY);

	}

	private boolean matchRegisterBill(QualityTraceTradeBill qualityTraceTradeBill, LocalDateTime start,
			LocalDateTime end,QualityTraceTradeBillMatchStatusEnum unMatchStatus) {
		boolean endMatch = this.endMatch(qualityTraceTradeBill);
		if (endMatch) {
			return true;
		}
		MatchDetectParam matchDetectParam = new MatchDetectParam();
		// matchDetectParam.setTradeNo(qualityTraceTradeBill.getOrderId());
		matchDetectParam.setTradeTypeId(qualityTraceTradeBill.getTradetypeId());
		// matchDetectParam.setTradeTypeName(qualityTraceTradeBill.getTradetypeName());
		matchDetectParam.setProductName(qualityTraceTradeBill.getProductName());
		matchDetectParam.setIdCardNo(qualityTraceTradeBill.getSellerIDNo());

		matchDetectParam.setStart(Date.from(start.toInstant(OffsetDateTime.now().getOffset())));
		matchDetectParam.setEnd(Date.from(end.toInstant(OffsetDateTime.now().getOffset())));
		logger.info("进行匹配:{}", matchDetectParam);
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
			return true;
		}else if(unMatchStatus!=null){
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
		return false;

	}


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
