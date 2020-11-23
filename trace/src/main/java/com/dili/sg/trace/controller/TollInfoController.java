package com.dili.sg.trace.controller;

import java.util.List;
import java.util.Map;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.sg.trace.domain.Category;
import com.dili.sg.trace.service.CategoryService;
import com.dili.sg.trace.service.CityService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 神农系统中相关基础信息
 */
@RestController
@RequestMapping(value = "/toll")
public class TollInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TollInfoController.class);

    //    @Autowired
//    BaseInfoRpc baseInfoRpc;
    @Autowired
    CityService cityService;
    @Autowired
    CategoryService categoryService;

    /**
     * 根据name查询品类
     *
     * @param name
     * @param allFlag
     * @return
     */
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
        Map<String, Object> map = Maps.newHashMap();
        map.put("suggestions", list);
        return map;
    }

    /**
     * 查询产地
     *
     * @param name
     * @return
     */
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

        Map<String, Object> map = Maps.newHashMap();
        map.put("suggestions", list);
        return map;
    }

    /**
     * 查询 产地
     *
     * @param name
     * @return
     */

    private List<CityDto> queryCitys(String name) {
        return this.cityService.listCityByCondition(name);
    }
}
