package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

//@Table(name = "`buyer_info`")
public class BuyerInfo{// extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize = false)
    private Long id;
    @ApiModelProperty(value = "用户iD")
    @Column(name = "`buyer_id`")
    private Long buyerId;
    @Column(name = "`buyer_name`")
    private String buyer_name;

    /**
     * 买家类型
     * @link{BuyerTypeEnum}
     */
    @Column(name = "`buyer_type`")
    private Integer buyerType;
//    @Column(name = "`user_type`")
//    private Integer userType;


//    @Override
    public Long getId() {
        return id;
    }

//    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public Integer getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(Integer buyerType) {
        this.buyerType = buyerType;
    }
}
