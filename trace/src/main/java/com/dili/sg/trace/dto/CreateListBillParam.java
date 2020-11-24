package com.dili.sg.trace.dto;

import com.dili.trace.domain.RegisterBill;

import java.io.Serializable;
import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
public class CreateListBillParam implements Serializable{
    private List<RegisterBill> registerBills;

    public List<RegisterBill> getRegisterBills() {
        return registerBills;
    }

    public void setRegisterBills(List<RegisterBill> registerBills) {
        this.registerBills = registerBills;
    }
}
