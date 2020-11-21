package com.dili.trace.controller;

import java.util.List;
import java.util.Map;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.trace.service.CityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.trace.domain.Category;
import com.dili.trace.service.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 神农系统中相关基础信息
 */
@RestController
@RequestMapping(value = "/toll")
public class TollInfoController {
    private static final Logger logger = LoggerFactory.getLogger(TollInfoController.class);

    @Autowired
    CityService cityService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/category")
    @ResponseBody
    public Map<String, ?> listByName(String name, boolean allFlag) {
        List<Category> categorys = this.categoryService.listCategoryByCondition(name);

        List<Map<String, Object>> list = Lists.newArrayList();
        if (categorys != null && !categorys.isEmpty()) {
            for (Category c : categorys) {
                Map<String, Object> obj = Maps.newHashMap();
                obj.put("id", c.getId());
                obj.put("data", name);
                obj.put("value", c.getName());
                list.add(obj);
            }
        }
        Map map = Maps.newHashMap();
        map.put("suggestions", list);
        return map;
    }


    @RequestMapping(value = "/city", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, ?> queryCity(String name) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(name)) {
            try {
                List<CityDto> citys = queryCitys(name);
                for (CityDto city : citys) {
                    Map<String, Object> obj = Maps.newHashMap();
                    obj.put("id", city.getId());
                    obj.put("data", name);
                    obj.put("name", city.getName());
                    obj.put("value", city.getMergerName());
//                    obj.put("customCode", city.getCustomCode());
                    list.add(obj);
                }
            } catch (Exception e) {
//青州、寿光、辽宁、河北,吉林

            }
        }

        Map map = Maps.newHashMap();
        map.put("suggestions", list);
        return map;
    }

    private List<CityDto> queryCitys(String name) {
        return this.cityService.listCityByCondition(name);
    }
}
