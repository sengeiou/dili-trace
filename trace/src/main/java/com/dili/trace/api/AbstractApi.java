package com.dili.trace.api;

import com.dili.trace.util.JSON;

public class AbstractApi {

    public String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }
}
