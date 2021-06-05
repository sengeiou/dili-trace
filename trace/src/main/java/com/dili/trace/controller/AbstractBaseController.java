package com.dili.trace.controller;

import com.dili.trace.util.JSON;

public abstract class AbstractBaseController {

    public String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }
}
