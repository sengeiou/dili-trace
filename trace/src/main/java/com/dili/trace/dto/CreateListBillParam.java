package com.dili.trace.dto;

import java.io.Serializable;
import java.util.List;

import com.dili.trace.api.dto.CreateRegisterBillInputDto;

/**
 * Created by laikui on 2019/7/26.
 */
public class CreateListBillParam implements Serializable{
    private List<CreateRegisterBillInputDto> registerBills;

    public List<CreateRegisterBillInputDto> getRegisterBills() {
        return registerBills;
    }

    public void setRegisterBills(List<CreateRegisterBillInputDto> registerBills) {
        this.registerBills = registerBills;
    }
}
