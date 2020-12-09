package com.dili.trace.service;

import org.springframework.stereotype.Service;

/**
 * 流程处理
 */
@Service
public class ProcessService {
    /**
     * 创建报备之后
     *
     * @param billId
     * @param marketId
     */
    public void afterCreateBill(Long billId, Long marketId) {

    }
}
