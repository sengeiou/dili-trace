package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dili.common.annotation.RegisterBillMessageEvent;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.dto.BatchAuditDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillStaticsDto;
import com.dili.trace.dto.QualityTraceTradeBillOutDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.enums.ImageCertTypeEnum;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface SgRegisterBillService {

    /**
     * 查询第一条需要被高亮显示的登记单
     */
    public RegisterBill findHighLightBill(RegisterBillDto dto) throws Exception;

    /**
     * 分页查询数据
     */
    public String listPage(RegisterBillDto dto) throws Exception;

    /**
     * 查询统计页面分页数据
     */
    public String listStaticsPage(RegisterBillDto dto) throws Exception;

    // /**
    // * 查找任务
    // *
    // * @param exeMachineNo
    // * @param taskCount
    // * @return
    // */
    // List<RegisterBill> findByExeMachineNo(String exeMachineNo, int taskCount);

    /**
     * 通过商品查找
     *
     * @param productName
     * @return
     */
    List<RegisterBill> findByProductName(String productName);


    /**
     * 通过交易区交易单查询
     *
     * @param tradeNo
     * @return
     */
    RegisterBillOutputDto findByTradeNo(String tradeNo);

    /**
     * 创建登记单
     *
     * @param registerBill
     * @return
     */
    int createRegisterBill(RegisterBill registerBill);

    /**
     * 审核登记单
     *
     * @param id
     * @param pass
     * @return
     */
    int auditRegisterBill(Long id, Boolean pass);

    /**
     * 撤销交易单
     *
     * @param id
     * @return
     */
    int undoRegisterBill(Long id);

    /**
     * 自动送检标记
     *
     * @param id
     * @return
     */
    int autoCheckRegisterBill(Long id);

    /**
     * 采样检测标记
     *
     * @param id
     * @return
     */
    int samplingCheckRegisterBill(Long id);

    /**
     * 复检标记
     *
     * @param id
     * @return
     */
    int reviewCheckRegisterBill(Long id);

    /**
     * 通过交易单查询，未绑定就绑定
     *
     * @param tradeNo
     * @return
     */
    public QualityTraceTradeBillOutDto findQualityTraceTradeBill(String tradeNo);

    /**
     * 根据状态统计数据
     *
     * @param dto
     * @return
     */
    public RegisterBillStaticsDto groupByState(RegisterBillDto dto);

    /**
     * 通过登记单，获取详情
     *
     * @param registerBill
     * @return
     */
    public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill);

    /**
     * 检测记录匹配
     *
     * @param qualityTraceTradeBill
     * @return
     */
//	int matchDetectBind(QualityTraceTradeBill qualityTraceTradeBill);

    /**
     * 保存处理结果
     *
     * @param input
     * @return
     */
    public Long saveHandleResult(RegisterBill input);

    /**
     * 保存修改数据
     *
     * @param input
     * @return
     */
    public Long doUploadDetectReport(RegisterBill input);

    /**
     * 保存修改数据
     *
     * @param input
     * @return
     */
    public Long doUploadOrigincertifiy(RegisterBill input);

    /**
     * 直接审核通过不需要检测
     *
     * @param input
     * @return
     */
    public Long doAuditWithoutDetect(RegisterBill input);

    /**
     * 修改登记单
     *
     * @param input
     * @return
     */
    public Long doEdit(RegisterBill input);

    /**
     * 批量主动送检
     *
     * @param idList
     * @return
     */
    public BaseOutput doBatchAutoCheck(List<Long> idList);

    /**
     * 批量撤销
     *
     * @param idList
     * @return
     */
    public BaseOutput doBatchUndo(List<Long> idList);

    /**
     * 批量采样检测
     *
     * @param idList
     * @return
     */
    public BaseOutput doBatchSamplingCheck(List<Long> idList);

    /**
     * 批量审核
     *
     * @param batchAuditDto
     * @return
     */
    public BaseOutput doBatchAudit(BatchAuditDto batchAuditDto);

    /**
     * 删除产地证明及检测报告
     *
     * @param id
     * @param deleteType
     * @return
     */

    public BaseOutput doRemoveReportAndCertifiy(Long id, String deleteType);

    /**
     * 通过采样单编号 查询登记单
     *
     * @param sampleCodeList
     * @return
     */
    public List<RegisterBill> findBySampleCodeList(List<String> sampleCodeList);

    /**
     * 通过ID查询并锁定registerbill
     */
    RegisterBill selectByIdForUpdate(Long id);



    /**
     * 创建登记单
     *
     * @param registerBillList
     * @return
     */
    int createRegisterBillList(List<RegisterBill> registerBillList);


    /**
     * 构造图片列表
     * @param detectReportUrl
     * @param handleResultUrl
     * @param originCertifiyUrl
     * @return
     */
    public List<ImageCert>buildImageCertList(String detectReportUrl,String handleResultUrl,String originCertifiyUrl);


    /**
     * 查询图片列表
     * @param billId
     * @return
     */
    public List<ImageCert>findImageCertListByBillId(Long billId);

    /**
     * 查询图片列表
     * @param billId
     * @return
     */
    public Map<ImageCertTypeEnum,List<ImageCert>> findImageCertMapListByBillId(Long billId);

    /**
     * 查询当前用户可用事件
     * @param billId
     * @return
     */

    public List<RegisterBillMessageEvent>queryEvents(Long billId);


    /**
     * 检查当前事件
     * @param billId
     * @param messageEvent
     */
    public Optional<RegisterBill> checkEvent(Long billId, RegisterBillMessageEvent messageEvent);
}