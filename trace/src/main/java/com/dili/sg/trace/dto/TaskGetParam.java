package com.dili.sg.trace.dto;

public class TaskGetParam{
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
