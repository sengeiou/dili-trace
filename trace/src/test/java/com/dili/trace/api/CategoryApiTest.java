package com.dili.trace.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.client.ClientUserCategoryApi;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.enums.PreserveTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@Rollback(false)
public class CategoryApiTest extends AutoWiredBaseTest {
    @Autowired
    ClientUserCategoryApi categoryApi;



    @Test
    public void add(){
        List<RUserCategory> categories = new ArrayList<>();
        RUserCategory rUserCategory1 = new RUserCategory();
        rUserCategory1.setCategoryId(82l);
        rUserCategory1.setCategoryName("鲜活-白条");
        rUserCategory1.setCategoryType(PreserveTypeEnum.FRESH.getCode());
        categories.add(rUserCategory1);

        RUserCategory rUserCategory2 = new RUserCategory();
        rUserCategory2.setCategoryId(79l);
        rUserCategory2.setCategoryName("鲜活-鲈鱼");
        rUserCategory2.setCategoryType(PreserveTypeEnum.FRESH.getCode());
        categories.add(rUserCategory2);

        System.out.println(JSONObject.toJSONString(categories));

        BaseOutput out =  categoryApi.addUserCategory(categories);
        System.out.println(JSONObject.toJSONString(out));
    }


    @Test
    public void listTest(){
        RUserCategory rUserCategory = new RUserCategory();
        rUserCategory.setPage(1);
        BaseOutput<BasePage<RUserCategory>> out = categoryApi.listUserCategory(rUserCategory);
        System.out.println(JSONObject.toJSONString(out));
    }

    @Test
    public void delTest(){
        List<Long> ids = new ArrayList<>();
        ids.add(78l);
        ids.add(82l);
        System.out.println(ids);
        BaseOutput<BasePage<RUserCategory>> out = categoryApi.delUserCategory(ids);
        System.out.println(JSONObject.toJSONString(out));
    }

}