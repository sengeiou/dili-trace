package com.dili.trace.controller;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.UapRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 品类接口
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    AssetsRpcService assetsRpcService;
    @Autowired
    UapRpcService uapRpcService;

    /**
     * 查询品类信息
     *
     * @param query
     * @return
     */
    @RequestMapping("/listCategories.action")
    @ResponseBody
    public BaseOutput listCategories(@RequestBody CusCategoryQuery query) {
        List<CusCategoryDTO> categories = this.assetsRpcService.listCusCategory(query, uapRpcService.getCurrentFirm().orElse(null).getId());
        return BaseOutput.successData(categories);
//        if (categorys != null && !categorys.isEmpty()) {
//            for (Category c : categorys) {
//                Map<String, Object> obj = Maps.newHashMap();
//                obj.put("id", c.getId());
//                obj.put("data", name);
//                obj.put("value", c.getName());
//                list.add(obj);
//            }
//        }
//        Map map = Maps.newHashMap();
//        map.put("suggestions", list);
//        return map;
//        return null;
    }
}
