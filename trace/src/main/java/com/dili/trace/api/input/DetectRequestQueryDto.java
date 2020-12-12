package com.dili.trace.api.input;

import com.dili.trace.domain.DetectRequest;

/**
 * 检测请求查询实体
 */
public class DetectRequestQueryDto extends DetectRequest {

    /**
     * 商品名称/商户名称LIKE
     */
    private String likeProductNameOrUserName;

    /**
     * 市场过滤
     */
    private Long marketId;

    /**
     * 报备单是否删除过滤
     */
    private Integer isDeleted;

    public String getLikeProductNameOrUserName() {
        return likeProductNameOrUserName;
    }

    public void setLikeProductNameOrUserName(String likeProductNameOrUserName) {
        this.likeProductNameOrUserName = likeProductNameOrUserName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}