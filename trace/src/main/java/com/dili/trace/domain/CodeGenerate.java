package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

/**
 * 由MyBatis Generator工具自动生成
 *
 * This file was generated on 2019-10-11 10:05:11.
 */
@Table(name = "`code_generate`")
public class CodeGenerate extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;


    @Column(name = "`type`")
    private String type;


    @Column(name = "`prefix`")
    private String prefix;



    @Column(name = "`segment`")
    private String segment;



    @Column(name = "`seq`")
    private Long seq;


    @Column(name = "`pattern`")
    private String pattern;


    @Column(name = "`created`")
    private Date created;


    @Column(name = "`modified`")
    private Date modified;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getPrefix() {
        return prefix;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    public String getSegment() {
        return segment;
    }


    public void setSegment(String segment) {
        this.segment = segment;
    }


    public Long getSeq() {
        return seq;
    }


    public void setSeq(Long seq) {
        this.seq = seq;
    }


    public String getPattern() {
        return pattern;
    }


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public Date getCreated() {
        return created;
    }


    public void setCreated(Date created) {
        this.created = created;
    }


    public Date getModified() {
        return modified;
    }


    public void setModified(Date modified) {
        this.modified = modified;
    }

}