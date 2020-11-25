package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.sg.trace.glossary.*;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.SgRegisterBillService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.User;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.QualityTraceTradeBillOutDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.SeparateSalesRecordDTO;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/bill")
@Api(value = "/api/bill", description = "登记单相关接口")
@InterceptConfiguration
public class SgRegisterBillApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(SgRegisterBillApi.class);
	@Autowired
	private BillService billService;
	
	@Autowired
	private SgRegisterBillService registerBillService;
	@Autowired
	private SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	private QualityTraceTradeBillService qualityTraceTradeBillService;

	@Autowired
	DetectRecordService detectRecordService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
    UserTallyAreaService userTallyAreaService;

	/**
	 * 保存多个登记单
	 * @param createListBillParam
	 * @return
	 */
	@ApiOperation("保存多个登记单")
	@RequestMapping(value = "/createList", method = RequestMethod.POST)
	public BaseOutput createList(@RequestBody CreateListBillParam createListBillParam) {
		LOGGER.info("保存多个登记单:");
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		List<CreateRegisterBillInputDto> registerBills = createListBillParam.getRegisterBills();
		if (registerBills == null) {
			return BaseOutput.failure("没有登记单");
		}
		LOGGER.info("保存多个登记单 操作用户:" + JSON.toJSONString(user));
		for(CreateRegisterBillInputDto inputDto:registerBills){
			LOGGER.info("循环保存登记单:" + JSON.toJSONString(inputDto));
			RegisterBill registerBill=new RegisterBill();
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
			if(registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
				registerBill.setTallyAreaNo(user.getTallyAreaNos());
			}
			registerBill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());
			BaseOutput result = registerBillService.createRegisterBill(registerBill);
			if (!result.isSuccess()) {
				return result;
			}

		}
		/*for (RegisterBill registerBill : registerBills) {
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
			if(registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
				registerBill.setTallyAreaNo(user.getTallyAreaNos());
			}
			registerBill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());
			BaseOutput result = registerBillService.createRegisterBill(registerBill);
			if (!result.isSuccess()) {
				return result;
			}
		}*/
		return BaseOutput.success();
	}

	/**
	 * 获取登记单列表
	 * @param registerBill
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	// @InterceptConfiguration(loginRequired=false)
	public BaseOutput<EasyuiPageOutput> list(@RequestBody RegisterBillDto registerBill) throws Exception {
		LOGGER.info("获取登记单列表:" + JSON.toJSON(registerBill).toString());
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		LOGGER.info("获取登记单列表 操作用户:" + JSON.toJSONString(user));
		registerBill.setUserId(user.getId());
		registerBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
		if (StringUtils.isBlank(registerBill.getOrder())) {
			registerBill.setOrder("desc");
			registerBill.setSort("id");
		}
		EasyuiPageOutput easyuiPageOutput = billService.listEasyuiPageByExample(registerBill, true);
		return BaseOutput.success().setData(easyuiPageOutput);
	}

	/**
	 * 保存分销单&全销总量与登记单相等
	 * @param salesRecord
	 * @return
	 */
	@ApiOperation("保存分销单&全销总量与登记单相等")
	@ApiImplicitParam(paramType = "body", name = "SeparateSalesRecord", dataType = "SeparateSalesRecord", value = "分销单保存入参")
	@RequestMapping(value = "/createSalesRecord", method = RequestMethod.POST)
	public BaseOutput<Long> saveSeparateSalesRecord(@RequestBody SeparateSalesRecordDTO salesRecord) {
		LOGGER.info("保存分销单:" + JSON.toJSONString(salesRecord));
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		LOGGER.info("保存分销单操作用户:" + JSON.toJSONString(user));

		if (RegisterSourceEnum.TRADE_AREA.getCode() == salesRecord.getRegisterSource()) {
			// 校验买家身份证
			QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService
					.findByTradeNo(salesRecord.getTradeNo());
			if(qualityTraceTradeBill==null) {
				return BaseOutput.failure("没有查找到交易单");
			}
			if (!StringUtils.lowerCase(qualityTraceTradeBill.getBuyerIDNo())
					.equals(StringUtils.lowerCase(user.getCardNo()))) {
				LOGGER.info("买家身份证" + qualityTraceTradeBill.getBuyerIDNo() + "用户身份证:" + user.getCardNo());
				return BaseOutput.failure("没有权限分销");
			}
			Integer alreadyWeight = separateSalesRecordService
					.getAlreadySeparateSalesWeightByTradeNo(salesRecord.getTradeNo());

			if ((salesRecord.getSalesWeight().intValue() + alreadyWeight) > qualityTraceTradeBill.getNetWeight()) {
				LOGGER.error("分销重量超过可分销重量SalesWeight：" + salesRecord.getSalesWeight() + ",alreadyWeight:"
						+ alreadyWeight + ",BillWeight：" + qualityTraceTradeBill.getNetWeight());
				return BaseOutput.failure("分销重量超过可分销重量").setData(false);
			}
			if (qualityTraceTradeBill.getNetWeight().intValue() == salesRecord.getSalesWeight().intValue()) {
				if (alreadyWeight != 0) {// 未分销过
					LOGGER.error("判断有问题？SalesWeight：" + salesRecord.getSalesWeight() + ",alreadyWeight:" + alreadyWeight
							+ ",BillWeight：" + qualityTraceTradeBill.getNetWeight());
					return BaseOutput.failure("已有分销，重量不能全销").setData(false);
				}
			}

			separateSalesRecordService.saveOrUpdate(salesRecord);

			qualityTraceTradeBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());

//            qualityTraceTradeBill.setOperatorName(user.getName());
//            qualityTraceTradeBill.setOperatorId(user.getId());
			qualityTraceTradeBill.setOrderVersion(OrderVersionEnum.VERSION.getCode());
			this.qualityTraceTradeBillService.update(qualityTraceTradeBill);
			return BaseOutput.success().setData(salesRecord.getId());

		} else {// 理货区分销校验
			if (StringUtils.isBlank(salesRecord.getRegisterBillCode())) {
				return BaseOutput.failure("没有需要分销的登记单");
			}
			RegisterBill registerBill = billService.findByCode(salesRecord.getRegisterBillCode());
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

			if (registerBill.getUserId().longValue() != user.getId().longValue()) {
				LOGGER.info("业户ID" + registerBill.getUserId() + "用户ID:" + user.getId());
				return BaseOutput.failure("没有权限分销");
			}
			Integer alreadyWeight = separateSalesRecordService
					.alreadySeparateSalesWeight(salesRecord.getRegisterBillCode());

			if ((salesRecord.getSalesWeight().intValue() + alreadyWeight) > registerBill.getWeight().intValue()) {
				LOGGER.error("分销重量超过可分销重量SalesWeight：" + salesRecord.getSalesWeight() + ",alreadyWeight:"
						+ alreadyWeight + ",BillWeight：" + registerBill.getWeight());
				return BaseOutput.failure("分销重量超过可分销重量").setData(false);
			}
			if (registerBill.getWeight().intValue() == salesRecord.getSalesWeight().intValue()) {
				if (alreadyWeight != 0) {// 未分销过
					LOGGER.error("判断有问题？SalesWeight：" + salesRecord.getSalesWeight() + ",alreadyWeight:" + alreadyWeight
							+ ",BillWeight：" + registerBill.getWeight());
					return BaseOutput.failure("已有分销，重量不能全销").setData(false);
				}
			}

			registerBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());
			separateSalesRecordService.saveOrUpdate(salesRecord);
			registerBill.setOperatorName(user.getName());
			registerBill.setOperatorId(user.getId());
			billService.update(registerBill);
			return BaseOutput.success().setData(salesRecord.getId());
		}

//        if(StringUtils.isBlank(salesRecord.getRegisterBillCode())){
//            return BaseOutput.failure("没有需要分销的登记单");
//        }
//        RegisterBill registerBill =  registerBillService.findByCode(salesRecord.getRegisterBillCode());
//        if(registerBill == null){
//            return BaseOutput.failure("没有查到需要分销的登记单");
//        }
//        if(registerBill.getRegisterSource().intValue()== RegisterSourceEnum.TRADE_AREA.getCode()){//交易区分销校验
//            //校验买家身份证
//            QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(registerBill.getTradeNo());
//            if(!StringUtils.lowerCase(qualityTraceTradeBill.getBuyerIDNo()).equals(StringUtils.lowerCase(user.getCardNo()))){
//                LOGGER.info("买家身份证"+qualityTraceTradeBill.getBuyerIDNo()+"用户身份证:"+user.getCardNo());
//                return BaseOutput.failure("没有权限分销");
//            }
//        }else {//理货区分销校验
//            if(registerBill.getUserId().longValue()!=user.getId().longValue()){
//                LOGGER.info("业户ID"+registerBill.getUserId()+"用户ID:"+user.getId());
//                return BaseOutput.failure("没有权限分销");
//            }
//        }
//        Integer alreadyWeight =separateSalesRecordService.alreadySeparateSalesWeight(salesRecord.getRegisterBillCode());
//
//        if((salesRecord.getSalesWeight().intValue()+alreadyWeight)>registerBill.getWeight().intValue()){
//            LOGGER.error("分销重量超过可分销重量SalesWeight："+salesRecord.getSalesWeight()+",alreadyWeight:"+alreadyWeight+",BillWeight："+registerBill.getWeight());
//            return BaseOutput.failure("分销重量超过可分销重量").setData(false);
//        }
//        if(registerBill.getWeight().intValue() == salesRecord.getSalesWeight().intValue()){
//            if(alreadyWeight !=0){//未分销过
//                LOGGER.error("判断有问题？SalesWeight："+salesRecord.getSalesWeight()+",alreadyWeight:"+alreadyWeight+",BillWeight："+registerBill.getWeight());
//                return BaseOutput.failure("已有分销，重量不能全销").setData(false);
//            }
//        }
//
//        registerBill.setSalesType(SalesTypeEnum.SEPARATE_SALES.getCode());
//        separateSalesRecordService.saveOrUpdate(salesRecord);
//        registerBill.setOperatorName(user.getName());
//        registerBill.setOperatorId(user.getId());
//        registerBillService.update(registerBill);
//        return BaseOutput.success().setData(salesRecord.getId());
	}

	/**
	 * 处理不分销单
	 * @param id
	 * @param registerSource
	 * @return
	 */
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
			RegisterBill registerBill = billService.get(id);
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
			billService.update(registerBill);
			return BaseOutput.success();
		}

	}

	/**
	 * 通过登记单ID获取登记单详细信息
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "id/{id}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBill(@PathVariable Long id) {
		LOGGER.info("获取登记单:" + id);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = billService.get(id);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败id:" + id);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);

		return BaseOutput.success().setData(bill);
	}

	/**
	 * 通过登记单编号获取登记单详细信息
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "通过登记单编号获取登记单详细信息")
	@RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBillByCode(@PathVariable String code) {
		LOGGER.info("获取登记单:" + code);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = billService.findByCode(code);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败code:" + code);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);
		return BaseOutput.success().setData(bill);
	}

	/**
	 * 通过交易区的交易号获取登记单详细信息
	 * @param tradeNo
	 * @return
	 */

	@ApiOperation(value = "通过交易区的交易号获取登记单详细信息")
	@RequestMapping(value = "/tradeNo/{tradeNo}", method = RequestMethod.GET)
	public BaseOutput<QualityTraceTradeBillOutDto> getBillByTradeNo(@PathVariable String tradeNo) {
		LOGGER.info("getBillByTradeNo获取登记单:" + tradeNo);
//        User user=userService.get(sessionContext.getAccountId());
//        if(user==null){
//            return BaseOutput.failure("未登陆用户");
//        }
		QualityTraceTradeBillOutDto bill = registerBillService.findQualityTraceTradeBill(tradeNo);
		return BaseOutput.success().setData(bill);
	}

	/**
	 * 通过分销记录ID获取分销单
	 * @param salesRecordId
	 * @return
	 */
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

	/**
	 * 通过登记单商品名获取登记单
	 * @param productName
	 * @return
	 */
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

	/**
	 * 通过code删除登记单
	 * @param registerBillCode
	 * @return
	 */
	@ApiOperation(value = "通过code删除登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/deleteRegisterBillByCode/{registerBillCode}", method = RequestMethod.GET)
	public BaseOutput<Object> deleteRegisterBillByCode(@PathVariable String registerBillCode) {
		LOGGER.info("通过code删除登记单:{}", registerBillCode);
		User user = userService.get(sessionContext.getAccountId());
		if (StringUtils.isBlank(registerBillCode)) {
			return BaseOutput.failure("参数错误");
		}
		RegisterBill registerBill = this.billService.findByCode(registerBillCode);
		if (registerBill == null) {
			return BaseOutput.failure("登记单不存在");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(registerBill.getState())) {
			return BaseOutput.failure("不能删除当前状态登记单");
		}
		this.billService.delete(registerBill.getId());
		return BaseOutput.success().setData(true);

	}


}
