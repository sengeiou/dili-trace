package com.dili.trace.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.Category;
import com.dili.trace.dto.CategoryListInput;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryApiTest extends AutoWiredBaseTest {
    @Autowired
    CategoryApi categoryApi;

    @Test
    public void test() {
        CategoryListInput input = new CategoryListInput();
        input.setLevel(1);
        BaseOutput<List<Category>> out = this.categoryApi.listCategoryByCondition(input);
        assertNotNull(out);
        assertTrue(out.isSuccess());
        List<Category>list=  out.getData();
        assertTrue(list.size()>0);
    }

}