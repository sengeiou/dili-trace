package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dili.trace.events.RegisterBillMessageEvent;
import com.dili.common.entity.SessionData;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.dto.*;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.ImageCertTypeEnum;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface SgRegisterBillService {



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
    Long createRegisterBill(RegisterBill registerBill, OperatorUser operatorUser);



    /**
     * 撤销交易单
     *
     * @param id
     * @return
     */
    int undoRegisterBill(Long id, OperatorUser operatorUser);

    /**
     * 复检标记
     *
     * @param id
     * @return
     */
    int reviewCheckRegisterBill(Long id, OperatorUser operatorUser);


    /**
     * 保存处理结果
     *
     * @param input
     * @return
     */
    public Long doUploadHandleResult(RegisterBill input);


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
     * 批量撤销
     *
     * @param idList
     * @return
     */
    public BaseOutput doBatchUndo(List<Long> idList, OperatorUser operatorUser);







    /**
     * 创建登记单
     *
     * @param registerBillList
     * @return
     */
    int createRegisterBillList(List<RegisterBill> registerBillList, OperatorUser operatorUser);



    /**
     * 查询图片列表
     * @param billId
     * @return
     */
    public List<ImageCert>findImageCertListByBillId(Long billId);



    /**
     * 查询当前用户可用事件
     * @param billId
     * @return
     */

    public List<RegisterBillMessageEvent>queryEvents(Long billId);




}