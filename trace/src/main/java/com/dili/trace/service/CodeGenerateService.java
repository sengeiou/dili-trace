package com.dili.trace.service;

import com.dili.trace.enums.BillTypeEnum;
import com.dili.ss.base.BaseService;
import com.dili.trace.domain.CodeGenerate;

/**
 * 编号 生成
 */
public interface CodeGenerateService extends BaseService<CodeGenerate, Long> {


    /**
     * 初始化
     */
    public void init();


    /**
     * 生成采样单编号
     *
     * @return
     */
//    public String nextRegisterBillSampleCode();


    /**
     * 生成采样单号
     *
     * @param billTypeEnum
     * @return
     */

    public String nextSampleCode(BillTypeEnum billTypeEnum);

    /**
     * 生成委托单采样单编号
     *
     * @return
     */
//    public String nextCommissionBillSampleCode();

    /**
     * 生成打印报告编号
     *
     * @return
     */

    public String nextCheckSheetCode(BillTypeEnum taskTypeEnum);

    /**
     * I tdut
     * @param taskTypeEnum
     * @return
     */
    public String getMaskCheckSheetCode(BillTypeEnum taskTypeEnum);

    /**
     * 生成委托单编号
     *
     * @return
     */
    public String nextECommerceBillCode();

    /**
     * 生成委托单采样单编号
     *
     * @return
     */
    public String nextECommerceBillSampleCode();

}
