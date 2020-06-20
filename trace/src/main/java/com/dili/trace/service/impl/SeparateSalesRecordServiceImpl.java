package com.dili.trace.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.api.dto.SeparateSalesApiListOutput;
import com.dili.trace.api.dto.SeparateSalesApiListQueryInput;
import com.dili.trace.dao.SeparateSalesRecordMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.SeparateSalesInputDTO;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import one.util.streamex.StreamEx;
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

		if (queryInput.getPage() == null || queryInput.getPage() <= 0) {
			queryInput.setPage(1);
		}
		if (queryInput.getRows() == null || queryInput.getRows() <= 0) {
			queryInput.setRows(10);
		}
		queryInput.setOffSet((queryInput.getPage() - 1) * queryInput.getRows());

		List<SeparateSalesApiListOutput> list = this.getActualDao().listSeparateSalesOutput(queryInput);

		List results = ValueProviderUtils.buildDataByProvider(queryInput, list);
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
	}

	

	@Transactional
	@Override
	public SeparateSalesRecord createOwnedSeparateSales(RegisterBill registerBill) {
		SeparateSalesRecord queryCondition = new SeparateSalesRecord();
		queryCondition.setBillId(registerBill.getId());
		queryCondition.setSalesType(SalesTypeEnum.OWNED.getCode());

		SeparateSalesRecord item = this.listByExample(queryCondition).stream().findFirst()
				.orElse(new SeparateSalesRecord());
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
			item.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
			item.setSaleStatus(SaleStatusEnum.NONE.getCode());
			this.insertSelective(item);
		} else {
			item.setSalesWeight(registerBill.getWeight());
			item.setStoreWeight(BigDecimal.valueOf(registerBill.getWeight()));
			item.setSalesPlate(registerBill.getPlate());
			item.setModified(new Date());
			this.updateSelective(item);
		}
		return item;
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


	@Transactional
	@Override
	public List<Long> createSeparateSalesRecordList(SeparateSalesInputDTO input, User sellerUser) {
//		if (sellerUser.getId().equals(input.getSalesUserId())) {
//			throw new BusinessException("买卖家不能相同");
//		}
		logger.info("seller userid={}", sellerUser.getId());
		logger.info("buyer userid={}", input.getSalesUserId());
		
		return StreamEx.of(input.getSeparateList()).nonNull().map( record->{
			// 理货区分销校验
			logger.info("parentId={}", record.getParentId());
			logger.info("salesWeight={}", record.getSalesWeight());
			if (record.getSalesWeight() == null || record.getSalesWeight() <= 0) {
				throw new BusinessException("分销重量输入错误");
			}
			Long separateSalesRecordId = record.getParentId();

			if (separateSalesRecordId == null) {
				throw new BusinessException("没有需要分销的登记单");
			}

			SeparateSalesRecord separateSalesRecord = this.get(separateSalesRecordId);
			if (separateSalesRecord == null) {
				throw new BusinessException("没有需要分销的数据");
			}

			RegisterBill registerBill = this.registerBillService.get(separateSalesRecord.getBillId());

			if (registerBill == null) {
				throw new BusinessException("没有查到需要分销的登记单");
			}
			if (registerBill.getState() == null) {
				throw new BusinessException("登记单状态错误");
			}

			if (RegisterBillStateEnum.ALREADY_AUDIT.getCode().equals(registerBill.getState())) {
				// do nothing
			} else if (RegisterBillStateEnum.ALREADY_CHECK.getCode().equals(registerBill.getState())
					&& (BillDetectStateEnum.PASS.getCode().equals(registerBill.getDetectState())
							|| BillDetectStateEnum.REVIEW_PASS.getCode().equals(registerBill.getDetectState()))) {
				// do nothing
			} else {
				throw new BusinessException("当前状态登记单不能分销");
			}

			if (BillDetectStateEnum.PASS.getCode().equals(registerBill.getDetectState())
					&& BillDetectStateEnum.REVIEW_PASS.getCode().equals(registerBill.getDetectState())) {
				throw new BusinessException("当前登记单检测不合格不能分销");
			}
			if (separateSalesRecord.getSalesUserId().longValue() != sellerUser.getId().longValue()) {
				LOGGER.info("业户ID" + separateSalesRecord.getSalesUserId() + "用户ID:" + sellerUser.getId());
				throw new BusinessException("没有权限分销");
			}
			BigDecimal salesWeight = BigDecimal.valueOf(record.getSalesWeight());
			BigDecimal totalWeight = BigDecimal.valueOf(separateSalesRecord.getSalesWeight());
			logger.info(">>>totalWeight={},salesWeight={}", totalWeight, record.getSalesWeight());
			if (totalWeight.compareTo(salesWeight) < 0) {
				throw new BusinessException("分销重量超过可分销重量");
			}
			if (input.getSalesUserId() != null) {
				logger.info("扫码分销，判断是否增加上游：salesUserId:{}", input.getSalesUserId());
				Long salesUserId = input.getSalesUserId();
				User salesUser = this.userService.get(salesUserId);
				if (salesUser == null) {
					throw new BusinessException("买家用户不存在");
				}
				boolean hasUpStream = this.upStreamService.queryUpStreamByUserId(salesUserId).stream()
						.anyMatch(up -> sellerUser.getId().equals(up.getSourceUserId()));

				// 扫码分销
				if (!hasUpStream) {
					UpStream upStream = this.upStreamService.queryUpStreamBySourceUserId(sellerUser.getId());
					UpStreamDto upStreamDto = new UpStreamDto();
					if (upStream == null) {
						if (UserTypeEnum.USER.getCode().equals(sellerUser.getUserType())) {
							upStreamDto.setUpstreamType(UpStreamTypeEnum.USER.getCode());
						} else {
							upStreamDto.setUpstreamType(UpStreamTypeEnum.CORPORATE.getCode());
						}

						upStreamDto.setIdCard(sellerUser.getCardNo());
						upStreamDto.setManufacturingLicenseUrl(sellerUser.getManufacturingLicenseUrl());
						upStreamDto.setOperationLicenseUrl(sellerUser.getOperationLicenseUrl());
						upStreamDto.setCardNoFrontUrl(sellerUser.getCardNoFrontUrl());
						upStreamDto.setCardNoBackUrl(sellerUser.getCardNoBackUrl());
						upStreamDto.setName(sellerUser.getName());
						upStreamDto.setSourceUserId(sellerUser.getId());
						upStreamDto.setLicense(sellerUser.getLicense());
						upStreamDto.setTelphone(sellerUser.getPhone());
						upStreamDto.setBusinessLicenseUrl(sellerUser.getBusinessLicenseUrl());
						upStreamDto.setUserIds(Arrays.asList(input.getSalesUserId()));
						upStreamDto.setCreated(new Date());
						upStreamDto.setModified(new Date());
						this.upStreamService.addUpstream(upStreamDto,
								new OperatorUser(sellerUser.getId(), sellerUser.getName()));
					} else {
						upStreamDto.setUserIds(Arrays.asList(input.getSalesUserId()));
						upStreamDto.setId(upStream.getId());
						this.upStreamService.addUpstreamUsers(upStreamDto,
								new OperatorUser(sellerUser.getId(), sellerUser.getName()));
					}

				}


			} else {
				// 手动分销
				// do nothing
			}
			
			logger.info(">>>change SeparateSalesRecord:{} salesWeiht:{} as: {}", separateSalesRecordId, totalWeight,
					totalWeight.subtract(salesWeight).intValue());
			
			// 更新被分销记录的剩余重量
			SeparateSalesRecord updatableRecord = new SeparateSalesRecord();
			updatableRecord.setSalesWeight(totalWeight.subtract(salesWeight).intValue());
			updatableRecord.setModified(new Date());
			updatableRecord.setId(separateSalesRecordId);
			this.updateSelective(updatableRecord);
			
			SeparateSalesRecord insertRecord=new SeparateSalesRecord();
			insertRecord.setSalesCityId(sellerUser.getSalesCityId());
			insertRecord.setSalesCityName(sellerUser.getSalesCityName());



			insertRecord.setBillId(registerBill.getId());
			insertRecord.setRegisterBillCode(registerBill.getCode());
			insertRecord.setCreated(new Date());
			insertRecord.setModified(new Date());
			insertRecord.setSalesWeight(salesWeight.intValue());
			insertRecord.setStoreWeight(salesWeight);
			insertRecord.setCheckinRecordId(separateSalesRecord.getCheckinRecordId());
			insertRecord.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());
			this.saveOrUpdate(insertRecord);

			registerBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());
			registerBill.setOperatorName(sellerUser.getName());
			registerBill.setOperatorId(sellerUser.getId());
			this.registerBillService.update(registerBill);
			return insertRecord.getId();
		}).toList();
	}

}
