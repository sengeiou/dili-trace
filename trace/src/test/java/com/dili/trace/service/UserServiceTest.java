package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.output.UserQrOutput;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends AutoWiredBaseTest {
    @Autowired
    UserService userService;

    @Test
    public void getUserQrCode() throws Exception {
        Long userId = 445L;
        UserQrOutput out = this.userService.getUserQrCode(userId);
        System.out.println(out.getBase64QRImg());
    }

    @Test
    public void dd() {

        Integer i = 0xFF000000; //-16777216
        System.out.println(i);
        Integer.parseInt("000000", 16);
        System.out.println(Integer.parseInt("000000", 16));
    }

}