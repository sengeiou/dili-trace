package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;

@Table(name = "`process_config`")
public class ProcessConfig extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize =false)
    private Long id;

    @Column(name = "`market_id`")
    private Long marketId;


    @Column(name = "`is_auto`")
    private Integer isAuto1;



    @Column(name = "`is_auto`")
    private Integer isAuto2;


    @Column(name = "`is_auto`")
    private Integer isAuto3;
}
