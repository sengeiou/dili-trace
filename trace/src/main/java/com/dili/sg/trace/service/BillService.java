package com.dili.sg.trace.service;

import cn.hutool.core.bean.BeanUtil;
import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.RegisterBillStateEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.*;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class BillService extends BaseServiceImpl<RegisterBill, Long> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BillService.class);

	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	DetectRecordService detectRecordService;
	@Autowired
	UserPlateService userPlateService;
	@Autowired
	CodeGenerateService codeGenerateService;
	@Autowired
	UsualAddressService usualAddressService;
	@Autowired
	DetectTaskService detectTaskService;

	/**
	 * 返回mapper
	 * @return
	 */
	public RegisterBillMapper getActualDao() {
		return (RegisterBillMapper) getDao();
	}


	/**
	 * 通过code查询登记单
	 * @param code
	 * @return
	 */
	public RegisterBill findByCode(String code) {
		RegisterBill registerBill = new RegisterBill();
		registerBill.setCode(code);
		List<RegisterBill> list = list(registerBill);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 通过idlist查询登记单
	 * @param idList
	 * @return
	 */
	public List<RegisterBill> findByIdList(Collection<Long> idList) {
		  RegisterBillDto dto = new RegisterBillDto();
	        dto.setIdList(CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull).collect(Collectors.toList()));
	        if (dto.getIdList().isEmpty()) {
	            return Lists.newArrayList();
	        }
	        return this.listByExample(dto);
	}

	/**
	 * 通过sampleCode查询登记单
	 *
	 * @param sampleCode
	 * @return
	 */
	public RegisterBill findBySampleCode(String sampleCode) {
		if (StringUtils.isBlank(sampleCode)) {
			return null;
		}
		RegisterBill registerBill = new RegisterBill();
		registerBill.setSampleCode(sampleCode.trim());
		List<RegisterBill> list = list(registerBill);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	/**
	 * 查询登记单信息
	 * @param tradeNo
	 * @return
	 */
	public RegisterBillOutputDto findByTradeNo(String tradeNo) {
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(tradeNo);
		if (qualityTraceTradeBill != null && StringUtils.isNotBlank(qualityTraceTradeBill.getRegisterBillCode())) {
			RegisterBill registerBill = new RegisterBill();
			registerBill.setCode(qualityTraceTradeBill.getRegisterBillCode());
			List<RegisterBill> list = this.listByExample(registerBill);
			if (list != null && list.size() > 0) {
				RegisterBillOutputDto target=new RegisterBillOutputDto();
				BeanUtil.copyProperties(list.get(0), target);
				return target;
			}
		}
		return null;
	}


	/**
	 * 获得当前登录用户信息
	 * @return
	 */

	private UserTicket getOptUser() {
		return SessionContext.getSessionContext().getUserTicket();
	}

	/**
	 * 保存处理结果
	 * @param input
	 * @return
	 */
	public Long saveHandleResult(RegisterBill input) {
		if (input == null || input.getId() == null
				|| StringUtils.isAnyBlank(input.getHandleResult(), input.getHandleResultUrl())) {
			throw new TraceBizException("参数错误");
		}
		if (input.getHandleResult().trim().length() > 1000) {
			throw new TraceBizException("处理结果不能超过1000");
		}
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new TraceBizException("数据错误");
		}

		RegisterBill example =  new RegisterBill();
		example.setId(item.getId());
		example.setHandleResult(input.getHandleResult());
		example.setHandleResultUrl(input.getHandleResultUrl());
		this.updateSelective(example);

		return example.getId();

	}


	/**
	 * 上传检测报告
	 * @param input
	 * @return
	 */
	public Long doUploadDetectReport(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new TraceBizException("参数错误");
		}
		if (StringUtils.isBlank(input.getOriginCertifiyUrl()) && StringUtils.isBlank(input.getDetectReportUrl())) {
			throw new TraceBizException("请上传报告");
		}
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new TraceBizException("数据错误");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
			throw new TraceBizException("状态错误,不能上传检测报告");
		}

		RegisterBill example =  new RegisterBill();
		example.setId(item.getId());
		example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
		example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
		this.updateSelective(example);

		return example.getId();
	}

	/**
	 * 上传产地证明
	 * @param input
	 * @return
	 */
	public Long doUploadOrigincertifiy(RegisterBill input) {
		if (input == null || input.getId() == null) {
			throw new TraceBizException("参数错误");
		}
		if (StringUtils.isBlank(input.getOriginCertifiyUrl()) && StringUtils.isBlank(input.getDetectReportUrl())) {
			throw new TraceBizException("请上传报告");
		}
		RegisterBill item = this.get(input.getId());
		if (item == null) {
			throw new TraceBizException("数据错误");
		}
		// if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
		// throw new TraceBizException("状态错误,不能上传产地证明");
		// }
		RegisterBill example =  new RegisterBill();
		example.setId(item.getId());
		example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
		// example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
		this.updateSelective(example);

		return example.getId();
	}

	/**
	 * 查询登记单
	 * @param sampleCodeList
	 * @return
	 */
	public List<RegisterBill> findBySampleCodeList(List<String> sampleCodeList) {

		if (sampleCodeList == null || sampleCodeList.size() == 0) {
			return Collections.emptyList();
		}
		Example example = new Example(RegisterBill.class);
		example.createCriteria().andIn("sampleCode", sampleCodeList);
		return this.getDao().selectByExample(example);

	}

	/**
	 * 查询并锁定登记单
	 * @param id
	 * @return
	 */

	public RegisterBill selectByIdForUpdate(Long id) {
		return this.getActualDao().selectByIdForUpdate(id);
	}

}