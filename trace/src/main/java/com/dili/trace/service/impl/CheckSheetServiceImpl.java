package com.dili.trace.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.CheckSheetDetail;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.CheckSheetInputDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.Base64SignatureService;
import com.dili.trace.service.CheckSheetDetailService;
import com.dili.trace.service.CheckSheetService;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.RegisterBillService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Service
public class CheckSheetServiceImpl extends BaseServiceImpl<CheckSheet, Long> implements CheckSheetService {
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	CheckSheetDetailService checkSheetDetailService;
	@Autowired
	CodeGenerateService codeGenerateService;
	@Autowired
	ApproverInfoService approverInfoService;
	@Autowired
	Base64SignatureService base64SignatureService;
	@Value("${current.baseWebPath}")
	private String baseWebPath;

	@Transactional
	@Override
	public Map createCheckSheet(CheckSheetInputDto input) {

		Triple<CheckSheet, List<CheckSheetDetail>, List<RegisterBill>> triple = this.buildCheckSheet(input);
		CheckSheet checkSheet = triple.getLeft();

		// 生成编号，插入数据库
		String checkSheetCode = this.codeGenerateService.nextCheckSheetCode();
		checkSheet.setCode(checkSheetCode);
		checkSheet.setQrcodeUrl(this.baseWebPath + "/checkSheet/detail/" + checkSheetCode);

		this.insertExact(checkSheet);

		// 生成详情并插入数据库
		List<CheckSheetDetail> checkSheetDetailList = triple.getMiddle().stream().map(detail -> {

			detail.setCheckSheetId(checkSheet.getId());
			return detail;
		}).collect(Collectors.toList());

		List<RegisterBill> updateRegisterBillList = triple.getMiddle().stream().map(detail -> {

			RegisterBill item = DTOUtils.newDTO(RegisterBill.class);
			item.setId(detail.getRegisterBillId());

			item.setCheckSheetId(checkSheet.getId());
			return item;
		}).collect(Collectors.toList());

		this.checkSheetDetailService.batchInsert(checkSheetDetailList);
		// 更新登记单信息
		this.registerBillService.batchUpdateSelective(updateRegisterBillList);
		return this.buildPrintDTOMap(checkSheet, checkSheetDetailList);
	}

	private UserTicket getOptUser() {
		return SessionContext.getSessionContext().getUserTicket();
	}

	private static BufferedImage getBufferImage(String content, int qrWidth, int qrHeight) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		return image;
	}

	private static String getBase64(String content, int qrWidth, int qrHeight) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			BufferedImage image = getBufferImage(content, qrWidth, qrHeight);
			// 转换成png格式的IO流
			ImageIO.write(image, "jpeg", byteArrayOutputStream);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}
		byte[] bytes = byteArrayOutputStream.toByteArray();
		String base64 = Base64.getEncoder().encodeToString(bytes).trim();
		base64 = "data:image/jpeg;base64," + base64;
		return base64;
	}

	private Map buildPrintDTOMap(CheckSheet checkSheet, List<CheckSheetDetail> checkSheetDetailList) {
		checkSheet.setQrcodeUrl(this.baseWebPath + "/checkSheet/detail/" + checkSheet.getCode());// 空请求路径

		checkSheet.setBase64Qrcode(this.getBase64(checkSheet.getQrcodeUrl(), 200, 200));
		Date created=checkSheet.getCreated();

		Map checkSheetMap = JSONObject.parseObject(checkSheet.toString());
		checkSheetMap.put("qrcodeUrl", "sss");

		checkSheetMap.put("showProductAlias", false);
		if (checkSheetDetailList != null && !checkSheetDetailList.isEmpty()) {
			for (int i = 0; i < checkSheetDetailList.size(); i++) {
				Integer detectState = checkSheetDetailList.get(i).getDetectState();
				if (BillDetectStateEnum.PASS.getCode().equals(detectState)
						|| BillDetectStateEnum.REVIEW_PASS.getCode().equals(detectState)) {
					checkSheetDetailList.get(i).setDetectStateView("合格");
				} else {
					checkSheetDetailList.get(i).setDetectStateView("未知");
				}

			}
			boolean showProductAlias = checkSheetDetailList.stream().filter(Objects::nonNull).anyMatch(detail -> {
				return detail.getProductAliasName() != null && detail.getProductAliasName().trim().length() > 0;
			});
			checkSheetMap.put("showProductAlias", showProductAlias);

		}
		checkSheetMap.put("checkSheetDetailList", checkSheetDetailList);
		checkSheetMap.put("created", created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
		return checkSheetMap;

	}

	@Override
	public Map prePrint(CheckSheetInputDto input) {
		Triple<CheckSheet, List<CheckSheetDetail>, List<RegisterBill>> triple = this.buildCheckSheet(input);
		Map checkSheetMap = this.buildPrintDTOMap(triple.getLeft(), triple.getMiddle());

		input.setRegisterBillList(Collections.emptyList());

		return checkSheetMap;
	}

	public static void main(String[] args) {
		CheckSheet obj = DTOUtils.newDTO(CheckSheet.class);
		obj.setId(22L);
		Object dd = DTOUtils.go(obj);
		System.out.println(dd);
	}

	private Triple<CheckSheet, List<CheckSheetDetail>, List<RegisterBill>> buildCheckSheet(CheckSheetInputDto input) {
		Pair<List<RegisterBill>, Map<Long, String>> pair = this.checkInputDto(input);
		Map<Long, String> idAndAliasNameMap = pair.getValue();

		List<RegisterBill> registerBillList = pair.getKey();
		// 生成编号，插入数据库
		input.setDetectOperatorId(0L);
		UserTicket ut = this.getOptUser();
		input.setOperatorId(ut.getId());
		input.setOperatorName(ut.getRealName());
		input.setCode("SGJC000000");
		input.setCreated(new Date());
		input.setModified(new Date());
		
		List<CheckSheetDetail> checkSheetDetailList = new ArrayList<CheckSheetDetail>();
		// 生成详情
		for (int i = 0; i < registerBillList.size(); i++) {
			RegisterBill bill = registerBillList.get(i);
			CheckSheetDetail detail = DTOUtils.newDTO(CheckSheetDetail.class);
			detail.setCheckSheetId(input.getId());
			detail.setCreated(new Date());
			detail.setModified(new Date());
			detail.setOriginId(bill.getOriginId());
			detail.setOriginName(bill.getOriginName());
			detail.setProductId(bill.getProductId());
			detail.setProductName(bill.getProductName());
			detail.setProductAliasName(idAndAliasNameMap.get(bill.getId()));
			detail.setRegisterBillId(bill.getId());
			detail.setDetectState(bill.getDetectState());
			detail.setOrderNumber(i + 1);
			detail.setLatestPdResult(bill.getLatestPdResult());
			checkSheetDetailList.add(detail);

		}

		List<RegisterBill> updateRegisterBillList = registerBillList.stream().map(bill -> {

			RegisterBill item = DTOUtils.newDTO(RegisterBill.class);
			item.setId(bill.getId());
			item.setCheckSheetId(bill.getCheckSheetId());
			return item;
		}).collect(Collectors.toList());
		return MutableTriple.of(input, checkSheetDetailList, updateRegisterBillList);

	}

	/**
	 * 检查输入的数据及状态
	 * 
	 * @param input
	 * @return
	 */
	private Pair<List<RegisterBill>, Map<Long, String>> checkInputDto(CheckSheetInputDto input) {
		Map<Long, String> idAndAliasNameMap = CollectionUtils.emptyIfNull(input.getRegisterBillList()).stream()
				.filter(Objects::nonNull).filter(item -> {

					return item.getId() != null;

				}).collect(Collectors.toMap(RegisterBill::getId, item -> item.getAliasName()));

		if (idAndAliasNameMap.isEmpty()) {
			throw new BusinessException("提交的数据错误");
		}
		ApproverInfo approverInfo = this.approverInfoService.get(input.getApproverInfoId());
		if (approverInfo == null) {
			throw new BusinessException("提交的数据错误");
		}
		String base64Sign = this.base64SignatureService.findBase64SignatureByApproverInfoId(approverInfo.getId());
		input.setApproverBase64Sign(base64Sign);
		List<Long> idList = new ArrayList<>(idAndAliasNameMap.keySet());

		RegisterBillDto queryCondition = DTOUtils.newDTO(RegisterBillDto.class);
		queryCondition.setIdList(idList);
		queryCondition.setSort("id");
		queryCondition.setOrder("DESC");
		List<RegisterBill> registerBillList = registerBillService.listByExample(queryCondition);

		if (registerBillList.isEmpty()) {
			throw new BusinessException("提交的数据错误");
		}

		boolean withoutCheckSheet = registerBillList.stream().allMatch(bill -> bill.getCheckSheetId() == null);
		if (!withoutCheckSheet) {
			throw new BusinessException("已经有登记单创建了检验单");
		}
		boolean allBelongSamePerson = registerBillList.stream().map(RegisterBill::getIdCardNo).distinct().count() == 1;
		if (!allBelongSamePerson) {
			throw new BusinessException("登记单不属于同一个业户");
		}

		boolean allChecked = registerBillList.stream()
				.allMatch(bill -> BillDetectStateEnum.PASS.getCode().equals(bill.getDetectState())
						|| BillDetectStateEnum.REVIEW_PASS.getCode().equals(bill.getDetectState()));
		if (!allChecked) {
			throw new BusinessException("登记单状态错误");
		}
		return MutablePair.of(registerBillList, idAndAliasNameMap);
	}

	@Override
	public Optional<CheckSheet> findCheckSheetByCode(String code) {
		if (StringUtils.isBlank(code)) {
			return Optional.empty();
		}
		CheckSheet query = DTOUtils.newDTO(CheckSheet.class);
		query.setCode(code.trim());
		return this.listByExample(query).stream().findFirst();
	}

}
