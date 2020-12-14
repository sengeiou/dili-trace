package com.dili.trace.api.input;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RegisterBill;

import javax.persistence.Column;
import java.util.List;

public class RegisterBillQueryDto extends RegisterBill {
    @Column(name = "`verify_status`")
    @Operator(Operator.IN)
    private List<Integer> verifyStatueList;

    public List<Integer> getVerifyStatueList() {
        return verifyStatueList;
    }

    public void setVerifyStatueList(List<Integer> verifyStatueList) {
        this.verifyStatueList = verifyStatueList;
    }
}
