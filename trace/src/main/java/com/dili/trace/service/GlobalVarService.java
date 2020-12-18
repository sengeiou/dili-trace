package com.dili.trace.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 全局变量
 */
@Service
public class GlobalVarService {

    @Value("${diliDfs.image.view.path.prefix:http://gaeway.diligrp.com:8285/dili-dfs/file/view}")
    private String dfsImageViewPathPrefix;

    /**
     * 图片路径前缀
     * @return
     */
    public String getDfsImageViewPathPrefix() {
        return dfsImageViewPathPrefix;
    }


}