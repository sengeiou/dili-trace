package com.dili.trace.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.Base64Signature;
import com.dili.trace.service.Base64SignatureService;

/**
 * base64签名信息查询
 */
@Service
public class Base64SignatureService extends BaseServiceImpl<Base64Signature, Long> {
    /**
     * 通过签名人id查询 签名
     *
     * @param approverInfoId
     * @return
     */
    public String findBase64SignatureByApproverInfoId(Long approverInfoId) {
        if (approverInfoId == null) {
            return "";
        }
        Base64Signature condition = new Base64Signature();
        condition.setApproverInfoId(approverInfoId);
        condition.setSort("order_num");
        condition.setOrder("asc");
        return this.listByExample(condition).stream().map(Base64Signature::getBase64).collect(Collectors.joining());
    }

}
