package com.dili.trace.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.domain.Base64Signature;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.Base64SignatureService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 签名人信息接口
 */
@Service
public class ApproverInfoService extends BaseServiceImpl<ApproverInfo, Long> {
    private static final int MAX_LENGTH = 1000;

    @Autowired
    Base64SignatureService base64SignatureService;

    /**
     * 录入签名人信息
     *
     * @param input
     * @return
     */
    @Transactional
    public int insertApproverInfo(ApproverInfo input) {

        String signBase64 = input.getSignBase64();
        if (StringUtils.isBlank(signBase64)) {
            throw new TraceBizException("签名不能为空");
        }
        if (StringUtils.isBlank(input.getUserName())) {
            throw new TraceBizException("用户名不能为空");
        }
        boolean existSameUserName = this.findByUserName(input.getUserName()).map((item) -> {
            return true;
        }).orElse(false);
        if (existSameUserName) {
            throw new TraceBizException("用户名已存在");
        }
        this.insertSelective(input);

        List<Base64Signature> base64SignatureList = this.buildBase64SignatureList(signBase64, input.getId());

        this.base64SignatureService.batchInsert(base64SignatureList);
        return 1;
    }

    /**
     * 更新签名人信息
     *
     * @param input
     * @return
     */
    @Transactional
    public int updateApproverInfo(ApproverInfo input) {

        String signBase64 = input.getSignBase64();
        if (StringUtils.isBlank(signBase64)) {
            throw new TraceBizException("签名不能为空");
        }
        if (StringUtils.isBlank(input.getUserName())) {
            throw new TraceBizException("用户名不能为空");
        }
        ApproverInfo approverInfo = this.get(input.getId());
        boolean existSameUserName = this.findByUserName(input.getUserName()).map((item) -> {
            return (item.getUserName().equals(input.getUserName()) && !item.getId().equals(input.getId()));
        }).orElse(false);
        if (existSameUserName) {
            throw new TraceBizException("用户名已存在");
        }

        approverInfo.setUserName(input.getUserName());
        approverInfo.setUserId(input.getUserId());
        approverInfo.setPhone(input.getPhone());

        this.updateSelective(approverInfo);
        String oldSignBase64 = this.base64SignatureService.findBase64SignatureByApproverInfoId(approverInfo.getId());

        if (!signBase64.equals(oldSignBase64)) {
            Base64Signature condition = DTOUtils.newDTO(Base64Signature.class);
            condition.setApproverInfoId(approverInfo.getId());
            this.base64SignatureService.deleteByExample(condition);

            List<Base64Signature> base64SignatureList = this.buildBase64SignatureList(signBase64, approverInfo.getId());

            this.base64SignatureService.batchInsert(base64SignatureList);
        }

        return 1;
    }

    /**
     * 通过姓名查询签名人信息
     * @param userName
     * @return
     */
    private Optional<ApproverInfo> findByUserName(String userName) {
        ApproverInfo approverInfo = new ApproverInfo();
        approverInfo.setUserName(userName);
        return this.listByExample(approverInfo).stream().findFirst();

    }

    /**
     * 通过id查询签名信息
     * @param signBase64
     * @param approverInfoId
     * @return
     */
    private List<Base64Signature> buildBase64SignatureList(String signBase64, Long approverInfoId) {

        List<Base64Signature> base64SignatureList = new ArrayList<>();

        for (int start = 0; start < signBase64.length(); start += MAX_LENGTH) {
            String base64 = signBase64.substring(start, Math.min(signBase64.length(), start + MAX_LENGTH));
            Base64Signature base64Signature = DTOUtils.newDTO(Base64Signature.class);
            base64Signature.setBase64(base64);
            base64Signature.setApproverInfoId(approverInfoId);
            base64Signature.setOrderNum(base64SignatureList.size());
            base64Signature.setCreated(new Date());
            base64Signature.setModified(new Date());
            base64SignatureList.add(base64Signature);
        }
        return base64SignatureList;
    }

}
