package com.dili.trace.dto;

import com.dili.ss.domain.BaseDomain;

/**
 * 删除检测报告和产地证明
 */
public class ReportAndCertifiyRemoveDto extends BaseDomain{
    /**
     * 单据ID
     */
    private Long id;

    /**
     * 删除类型
     */
    private String deleteType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }
}