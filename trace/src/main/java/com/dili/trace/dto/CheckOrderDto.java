package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.CheckOrder;

import javax.persistence.Column;
import java.util.Date;


/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/10/30
 */
public class CheckOrderDto extends CheckOrder {
    @Column(name = "`check_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date checkTimeStart;

    @Column(name = "`check_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date checkTimeEnd;

    private String url;
    private Long billId;
    private Long checkId;
    private String normalValue;
    private String project;
    private String result;
    private String value;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCheckTimeStart() {
        return checkTimeStart;
    }

    public void setCheckTimeStart(Date checkTimeStart) {
        this.checkTimeStart = checkTimeStart;
    }

    public Date getCheckTimeEnd() {
        return checkTimeEnd;
    }

    public void setCheckTimeEnd(Date checkTimeEnd) {
        this.checkTimeEnd = checkTimeEnd;
    }
}
