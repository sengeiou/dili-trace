package com.dili.common.annotation;

public enum Role {
    /**
     * 客户（经营户或司机）
     */
    Client,
    /**
     * 管理员
     */
    Manager,
    /**
     * 客户或管理员
     */
    ANY,
    /**
     * 没有角色（机器调用接口时使用）
     */
    NONE
}
