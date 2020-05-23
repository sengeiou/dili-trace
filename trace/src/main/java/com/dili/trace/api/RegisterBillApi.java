package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserTallyArea;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.QualityTraceTradeBillOutDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.SeparateSalesRecordDTO;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/bill")
@Api(value = "/api/bill", description = "登记单相关接口")
@InterceptConfiguration
public class RegisterBillApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Autowired
	private SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	private QualityTraceTradeBillService qualityTraceTradeBillService;

	@Autowired
	DetectRecordService detectRecordService;
	@Resource
	private SessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
	UserTallyAreaService userTallyAreaService;
	@Autowired
	CustomerService customerService;
	@Autowired
	UpStreamService upStreamService;

	@ApiOperation("保存多个登记单")
	@RequestMapping(value = "/createList", method = RequestMethod.POST)
	public BaseOutput createList(@RequestBody CreateListBillParam createListBillParam) {
		LOGGER.info("保存多个登记单:");
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		List<RegisterBill> registerBills = createListBillParam.getRegisterBills();
		if (registerBills == null) {
			return BaseOutput.failure("没有登记单");
		}
		LOGGER.info("保存多个登记单 操作用户:" + JSON.toJSONString(user));
		for (RegisterBill registerBill : registerBills) {
			LOGGER.info("循环保存登记单:" + JSON.toJSONString(registerBill));
			registerBill.setOperatorName(user.getName());
			registerBill.setOperatorId(user.getId());
			registerBill.setUserId(user.getId());
			registerBill.setName(user.getName());
			registerBill.setAddr(user.getAddr());
			registerBill.setIdCardNo(user.getCardNo());
			if (registerBill.getRegisterSource() == null) {
				// 小程序默认理货区
				registerBill.setRegisterSource(RegisterSourceEnum.TALLY_AREA.getCode());
			}
			if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
				registerBill.setTallyAreaNo(user.getTallyAreaNos());
			}

			BaseOutput result = registerBillService.createRegisterBill(registerBill);
			if (!result.isSuccess()) {
				return result;
			}
		}
		return BaseOutput.success();
	}

	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	// @InterceptConfiguration(loginRequired=false)
	public BaseOutput<EasyuiPageOutput> list(RegisterBillDto registerBill) throws Exception {
		LOGGER.info("获取登记单列表:" + JSON.toJSON(registerBill).toString());
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		LOGGER.info("获取登记单列表 操作用户:" + JSON.toJSONString(user));
		registerBill.setUserId(user.getId());
		if (StringUtils.isBlank(registerBill.getOrder())) {
			registerBill.setOrder("desc");
			registerBill.setSort("id");
		}
		EasyuiPageOutput easyuiPageOutput = registerBillService.listEasyuiPageByExample(registerBill, true);
		return BaseOutput.success().setData(easyuiPageOutput);
	}

	@ApiOperation("保存分销单&全销总量与登记单相等")
	@ApiImplicitParam(paramType = "body", name = "SeparateSalesRecord", dataType = "SeparateSalesRecord", value = "分销单保存入参")
	@RequestMapping(value = "/createSalesRecord", method = RequestMethod.POST)
	public BaseOutput<Long> saveSeparateSalesRecord(SeparateSalesRecordDTO input) {
		LOGGER.info("保存分销单:" + JSON.toJSONString(input));
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		LOGGER.info("保存分销单操作用户:" + JSON.toJSONString(user));

		if (RegisterSourceEnum.TRADE_AREA.getCode() == input.getRegisterSource()
				&& (input.getForceSeprate() == null || !input.getForceSeprate())) {
			// 校验买家身份证
			QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService
					.findByTradeNo(input.getTradeNo());
			if (qualityTraceTradeBill == null) {
				return BaseOutput.failure("没有查找到交易单");
			}
			if (!StringUtils.lowerCase(qualityTraceTradeBill.getBuyerIDNo())
					.equals(StringUtils.lowerCase(user.getCardNo()))) {
				LOGGER.info("买家身份证" + qualityTraceTradeBill.getBuyerIDNo() + "用户身份证:" + user.getCardNo());
				return BaseOutput.failure("没有权限分销");
			}
			Integer alreadyWeight = separateSalesRecordService
					.getAlreadySeparateSalesWeightByTradeNo(input.getTradeNo());

			if ((input.getSalesWeight().intValue() + alreadyWeight) > qualityTraceTradeBill.getNetWeight()) {
				LOGGER.error("分销重量超过可分销重量SalesWeight：" + input.getSalesWeight() + ",alreadyWeight:" + alreadyWeight
						+ ",BillWeight：" + qualityTraceTradeBill.getNetWeight());
				return BaseOutput.failure("分销重量超过可分销重量").setData(false);
			}
			if (qualityTraceTradeBill.getNetWeight().intValue() == input.getSalesWeight().intValue()) {
				if (alreadyWeight != 0) {// 未分销过
					LOGGER.error("判断有问题？SalesWeight：" + input.getSalesWeight() + ",alreadyWeight:" + alreadyWeight
							+ ",BillWeight：" + qualityTraceTradeBill.getNetWeight());
					return BaseOutput.failure("已有分销，重量不能全销").setData(false);
				}
			}

			separateSalesRecordService.saveOrUpdate(input);

			qualityTraceTradeBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());

			// qualityTraceTradeBill.setOperatorName(user.getName());
			// qualityTraceTradeBill.setOperatorId(user.getId());
			this.qualityTraceTradeBillService.update(qualityTraceTradeBill);
			return BaseOutput.success().setData(input.getId());

		} else if (RegisterSourceEnum.TALLY_AREA.getCode() == input.getRegisterSource()) {// 理货区分销校验

			Long separateSalesRecordId = input.getParentId();

			if (separateSalesRecordId == null) {
				return BaseOutput.failure("没有需要分销的登记单");
			}

			SeparateSalesRecord separateSalesRecord = this.separateSalesRecordService.get(separateSalesRecordId);
			if (separateSalesRecord == null) {
				return BaseOutput.failure("没有需要分销的数据");
			}

			RegisterBill registerBill = this.registerBillService.get(separateSalesRecord.getBillId());

			if (registerBill == null) {
				return BaseOutput.failure("没有查到需要分销的登记单");
			}
			if (registerBill.getState() == null) {
				return BaseOutput.failure("登记单状态错误");
			}
			List<Integer> stateList = Arrays.asList(RegisterBillStateEnum.ALREADY_CHECK.getCode(),
					RegisterBillStateEnum.ALREADY_AUDIT.getCode());
			if (!stateList.contains(registerBill.getState())) {
				return BaseOutput.failure("当前状态登记单不能分销");
			}
			if(BillDetectStateEnum.PASS.getCode().equals(registerBill.getDetectState())&&BillDetectStateEnum.REVIEW_PASS.getCode().equals(registerBill.getDetectState())) {
				return BaseOutput.failure("当前登记单检测不合格不能分销");
			}
			if (separateSalesRecord.getSalesUserId().longValue() != user.getId().longValue()) {
				LOGGER.info("业户ID" + separateSalesRecord.getSalesUserId() + "用户ID:" + user.getId());
				return BaseOutput.failure("没有权限分销");
			}
			BigDecimal salesWeight = BigDecimal.valueOf(input.getSalesWeight());
			BigDecimal storeWeight = separateSalesRecord.getStoreWeight();

			if (separateSalesRecord.getParentId() == null
					&& !registerBill.getWeight().equals(separateSalesRecord.getSalesWeight())) {
				// 当前分销记录是初始用于分销的记录
				// 更新重量(登记单重量可能被更新过)
				SeparateSalesRecord record = DTOUtils.newDTO(SeparateSalesRecord.class);
				record.setStoreWeight(BigDecimal.valueOf(registerBill.getWeight()));
				record.setSalesWeight(registerBill.getWeight());
				record.setId(separateSalesRecord.getId());
				this.separateSalesRecordService.updateSelective(record);
				storeWeight = record.getStoreWeight();

			}
			Long salesUserId = input.getSalesUserId();
			User salesUser = this.userService.get(salesUserId);
			boolean hasUpStream = this.upStreamService.queryUpStreamByUserId(salesUserId).stream()
					.anyMatch(up ->user.getId().equals(up.getSourceUserId()));
			
			if (storeWeight.compareTo(salesWeight) < 0) {
				return BaseOutput.failure("分销重量超过可分销重量").setData(false);
			}
			if (!hasUpStream) {
				UpStream upStream=this.upStreamService.queryUpStreamBySourceUserId(user.getId());
				UpStreamDto upStreamDto = new UpStreamDto();
				if(upStream==null) {
					if(UserTypeEnum.USER.getCode().equals(user.getUserType())) {
						upStreamDto.setUpstreamType(UpStreamTypeEnum.USER.getCode());	
					}else {
						upStreamDto.setUpstreamType(UpStreamTypeEnum.CORPORATE.getCode());
					}
					
					upStreamDto.setIdCard(user.getCardNo());
					upStreamDto.setManufacturingLicenseUrl(user.getManufacturingLicenseUrl());
					upStreamDto.setOperationLicenseUrl(user.getOperationLicenseUrl());
					upStreamDto.setCardNoFrontUrl(user.getCardNoFrontUrl());
					upStreamDto.setCardNoBackUrl(user.getCardNoBackUrl());
					upStreamDto.setName(user.getName());
					upStreamDto.setSourceUserId(user.getId());
					upStreamDto.setLicense(user.getLicense());
					upStreamDto.setTelphone(user.getPhone());
					upStreamDto.setBusinessLicenseUrl(user.getBusinessLicenseUrl());
					upStreamDto.setUserIds(Arrays.asList(input.getSalesUserId()));
					upStreamDto.setCreated(new Date());
					upStreamDto.setModified(new Date());
					this.upStreamService.addUpstream(upStreamDto, new OperatorUser(user.getId(), user.getName()));
				}else {
					upStreamDto.setUserIds(Arrays.asList(input.getSalesUserId()));
					upStreamDto.setId(upStream.getId());
					this.upStreamService.addUpstreamUsers(upStreamDto, new OperatorUser(user.getId(), user.getName()));
				}
				


			}

			// 更新被分销记录的剩余重量
			SeparateSalesRecord record = DTOUtils.newDTO(SeparateSalesRecord.class);
			record.setStoreWeight(storeWeight.subtract(salesWeight));
			record.setModified(new Date());
			record.setId(separateSalesRecordId);
			this.separateSalesRecordService.updateSelective(record);

			input.setBillId(registerBill.getId());
			input.setRegisterBillCode(registerBill.getCode());
			input.setCreated(new Date());
			input.setModified(new Date());
			input.setSalesCityId(user.getSalesCityId());
			input.setSalesCityName(user.getSalesCityName());
			input.setStoreWeight(salesWeight);
			registerBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());
			separateSalesRecordService.saveOrUpdate(input);

			registerBill.setOperatorName(user.getName());
			registerBill.setOperatorId(user.getId());
			registerBillService.update(registerBill);
			return BaseOutput.success().setData(input.getId());
		} else {
			return BaseOutput.success().setData(0L);
		}

		// if(StringUtils.isBlank(salesRecord.getRegisterBillCode())){
		// return BaseOutput.failure("没有需要分销的登记单");
		// }
		// RegisterBill registerBill =
		// registerBillService.findByCode(salesRecord.getRegisterBillCode());
		// if(registerBill == null){
		// return BaseOutput.failure("没有查到需要分销的登记单");
		// }
		// if(registerBill.getRegisterSource().intValue()==
		// RegisterSourceEnum.TRADE_AREA.getCode()){//交易区分销校验
		// //校验买家身份证
		// QualityTraceTradeBill qualityTraceTradeBill =
		// qualityTraceTradeBillService.findByTradeNo(registerBill.getTradeNo());
		// if(!StringUtils.lowerCase(qualityTraceTradeBill.getBuyerIDNo()).equals(StringUtils.lowerCase(user.getCardNo()))){
		// LOGGER.info("买家身份证"+qualityTraceTradeBill.getBuyerIDNo()+"用户身份证:"+user.getCardNo());
		// return BaseOutput.failure("没有权限分销");
		// }
		// }else {//理货区分销校验
		// if(registerBill.getUserId().longValue()!=user.getId().longValue()){
		// LOGGER.info("业户ID"+registerBill.getUserId()+"用户ID:"+user.getId());
		// return BaseOutput.failure("没有权限分销");
		// }
		// }
		// Integer alreadyWeight
		// =separateSalesRecordService.alreadySeparateSalesWeight(salesRecord.getRegisterBillCode());
		//
		// if((salesRecord.getSalesWeight().intValue()+alreadyWeight)>registerBill.getWeight().intValue()){
		// LOGGER.error("分销重量超过可分销重量SalesWeight："+salesRecord.getSalesWeight()+",alreadyWeight:"+alreadyWeight+",BillWeight："+registerBill.getWeight());
		// return BaseOutput.failure("分销重量超过可分销重量").setData(false);
		// }
		// if(registerBill.getWeight().intValue() ==
		// salesRecord.getSalesWeight().intValue()){
		// if(alreadyWeight !=0){//未分销过
		// LOGGER.error("判断有问题？SalesWeight："+salesRecord.getSalesWeight()+",alreadyWeight:"+alreadyWeight+",BillWeight："+registerBill.getWeight());
		// return BaseOutput.failure("已有分销，重量不能全销").setData(false);
		// }
		// }
		//
		// registerBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());
		// separateSalesRecordService.saveOrUpdate(salesRecord);
		// registerBill.setOperatorName(user.getName());
		// registerBill.setOperatorId(user.getId());
		// registerBillService.update(registerBill);
		// return BaseOutput.success().setData(salesRecord.getId());
	}

	@ApiOperation("处理不分销单")
	@RequestMapping(value = "/doNoSalesRecord/{id}/{registerSource}", method = { RequestMethod.POST,
			RequestMethod.GET })

	public BaseOutput doNoSalesRecord(@PathVariable Long id, @PathVariable Integer registerSource) {
		LOGGER.info("不分销销:" + id);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (registerSource == null) {
			return BaseOutput.failure("登记单类型参数错误");
		}
		if (RegisterSourceEnum.TRADE_AREA.getCode() == registerSource) {
			// 校验买家身份证
			QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.get(id);

			if (qualityTraceTradeBill == null) {
				return BaseOutput.failure("没有查到需要分销的交易单");
			}
			if (!StringUtils.lowerCase(qualityTraceTradeBill.getBuyerIDNo())
					.equals(StringUtils.lowerCase(user.getCardNo()))) {
				LOGGER.info("买家身份证" + qualityTraceTradeBill.getBuyerIDNo() + "用户身份证:" + user.getCardNo());
				return BaseOutput.failure("没有权限处理");
			}
			Integer alreadyWeight = separateSalesRecordService
					.getAlreadySeparateSalesWeightByTradeNo(qualityTraceTradeBill.getOrderId());

			if (alreadyWeight > 0) {
				LOGGER.error(
						"已经有,alreadyWeight:" + alreadyWeight + ",BillWeight：" + qualityTraceTradeBill.getNetWeight());
				return BaseOutput.failure("已经有分销记录,不能处理").setData(false);
			}

			qualityTraceTradeBill.setSalesType(SalesTypeEnum.ONE_SALES.getCode());
			this.qualityTraceTradeBillService.update(qualityTraceTradeBill);
			return BaseOutput.success();

		} else {
			RegisterBill registerBill = registerBillService.get(id);
			if (registerBill == null) {
				return BaseOutput.failure("没有查到需要分销的登记单");
			}
			if (registerBill.getUserId().longValue() != user.getId().longValue()) {
				LOGGER.info("业户ID" + registerBill.getUserId() + "用户ID:" + user.getId());
				return BaseOutput.failure("没有权限处理");
			}
			Integer alreadyWeight = separateSalesRecordService.alreadySeparateSalesWeight(registerBill.getCode());

			if (alreadyWeight > 0) {
				LOGGER.error("已经有,alreadyWeight:" + alreadyWeight + ",BillWeight：" + registerBill.getWeight());
				return BaseOutput.failure("已经有分销记录,不能处理").setData(false);
			}

			registerBill.setSalesType(SalesTypeEnum.ONE_SALES.getCode());
			registerBillService.update(registerBill);
			return BaseOutput.success();
		}

		//
		//
		// RegisterBill registerBill = registerBillService.get(id);
		// if(registerBill == null){
		// return BaseOutput.failure("没有查到需要分销的登记单");
		// }
		//
		// if(registerBill.getRegisterSource().intValue()==
		// RegisterSourceEnum.TRADE_AREA.getCode()){//交易区分销校验
		// //校验买家身份证
		// QualityTraceTradeBill qualityTraceTradeBill =
		// qualityTraceTradeBillService.findByTradeNo(registerBill.getTradeNo());
		//
		// if(!StringUtils.lowerCase(qualityTraceTradeBill.getBuyerIDNo()).equals(StringUtils.lowerCase(user.getCardNo()))){
		// LOGGER.info("买家身份证"+qualityTraceTradeBill.getBuyerIDNo()+"用户身份证:"+user.getCardNo());
		// return BaseOutput.failure("没有权限处理");
		// }
		// }else {//理货区分销校验
		// if(registerBill.getUserId().longValue()!=user.getId().longValue()){
		// LOGGER.info("业户ID"+registerBill.getUserId()+"用户ID:"+user.getId());
		// return BaseOutput.failure("没有权限处理");
		// }
		// }
		// Integer alreadyWeight
		// =separateSalesRecordService.alreadySeparateSalesWeight(registerBill.getCode());
		//
		// if(alreadyWeight>0){
		// LOGGER.error("已经有,alreadyWeight:"+alreadyWeight+",BillWeight："+registerBill.getWeight());
		// return BaseOutput.failure("已经有分销记录,不能处理").setData(false);
		// }
		//
		// registerBill.setSalesType(SalesTypeEnum.ONE_SALES.getCode());
		// registerBillService.update(registerBill);
		// return BaseOutput.success();
	}

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "id/{id}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBill(@PathVariable Long id) {
		LOGGER.info("获取登记单:" + id);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败id:" + id);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);

		return BaseOutput.success().setData(bill);
	}

	@ApiOperation(value = "通过登记单编号获取登记单详细信息")
	@RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBillByCode(@PathVariable String code) {
		LOGGER.info("获取登记单:" + code);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = registerBillService.findByCode(code);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败code:" + code);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);
		return BaseOutput.success().setData(bill);
	}

	@ApiOperation(value = "通过交易区的交易号获取登记单详细信息")
	@RequestMapping(value = "/tradeNo/{tradeNo}", method = RequestMethod.GET)
	public BaseOutput<QualityTraceTradeBillOutDto> getBillByTradeNo(@PathVariable String tradeNo) {
		LOGGER.info("getBillByTradeNo获取登记单:" + tradeNo);
		// User user=userService.get(sessionContext.getAccountId());
		// if(user==null){
		// return BaseOutput.failure("未登陆用户");
		// }
		QualityTraceTradeBillOutDto bill = registerBillService.findQualityTraceTradeBill(tradeNo);
		return BaseOutput.success().setData(bill);
	}

	@ApiOperation(value = "通过分销记录ID获取分销单")
	@RequestMapping(value = "/salesRecordId/{salesRecordId}", method = RequestMethod.GET)
	public BaseOutput<SeparateSalesRecord> getSeparateSalesRecord(@PathVariable Long salesRecordId) {
		LOGGER.info("获取分销记录:" + salesRecordId);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		SeparateSalesRecord record = separateSalesRecordService.get(salesRecordId);
		return BaseOutput.success().setData(record);
	}

	@ApiOperation(value = "通过登记单商品名获取登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/productName/{productName}", method = RequestMethod.GET)
	public BaseOutput<List<RegisterBill>> getBillByProductName(@PathVariable String productName) {
		LOGGER.info("获取登记单&分销记录:" + productName);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		List<RegisterBill> bills = registerBillService.findByProductName(productName);
		return BaseOutput.success().setData(bills);
	}

	@ApiOperation(value = "通过code删除登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/deleteRegisterBillByCode/{registerBillCode}", method = RequestMethod.GET)
	public BaseOutput<Object> deleteRegisterBillByCode(@PathVariable String registerBillCode) {
		LOGGER.info("通过code删除登记单:{}", registerBillCode);
		User user = userService.get(sessionContext.getAccountId());
		if (StringUtils.isBlank(registerBillCode)) {
			return BaseOutput.failure("参数错误");
		}
		RegisterBill registerBill = this.registerBillService.findByCode(registerBillCode);
		if (registerBill == null) {
			return BaseOutput.failure("登记单不存在");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(registerBill.getState())) {
			return BaseOutput.failure("不能删除当前状态登记单");
		}
		this.registerBillService.delete(registerBill.getId());
		return BaseOutput.success().setData(true);

	}

}
