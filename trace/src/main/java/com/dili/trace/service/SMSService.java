package com.dili.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;

public interface SMSService {
    public BaseOutput sendVerificationCodeMsg( JSONObject params,String phone,String verificationCode);

}