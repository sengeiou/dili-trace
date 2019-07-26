package com.dili.trace.dto;

import java.io.Serializable;

/**
 * Created by laikui on 2019/7/26.
 */
public class TaskGetParam implements Serializable{
    private String exeMachineNo;
    private int pageSize;

    public String getExeMachineNo() {
        return exeMachineNo;
    }

    public void setExeMachineNo(String exeMachineNo) {
        this.exeMachineNo = exeMachineNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
