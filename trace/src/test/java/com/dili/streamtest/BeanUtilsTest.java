package com.dili.streamtest;

import com.dili.trace.domain.RegisterBill;
import com.dili.trace.util.MergeBeanUtils;

import org.junit.jupiter.api.Test;
import org.nutz.json.Json;

public class BeanUtilsTest {
    @Test
    public void test_merge() {
        RegisterBill item = new RegisterBill();
        item.setId(1L);
        item.setCode("code-abc");
        RegisterBill input = new RegisterBill();
        input.setId(2L);
        MergeBeanUtils.merge(item, input);
        System.out.println("========================");
        System.out.println(Json.toJson(input));

    }

}