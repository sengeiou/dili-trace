package com.dili.common.config;

import org.springframework.stereotype.Component;

/**
 * 整个应用全局变量
 */
@Component
public class WebVars {
    private String imageserverCtx="http://dfs.diligrp.com";

    /**
     * 返回变量
     * @return
     */
    public String getImageserverCtx() {
        return imageserverCtx;
    }

    /**
     * 设置变量
     * @param imageserverCtx
     */
    public void setImageserverCtx(String imageserverCtx) {
        this.imageserverCtx = imageserverCtx;
    }


}
