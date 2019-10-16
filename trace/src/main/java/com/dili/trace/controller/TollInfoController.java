package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.City;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.dto.CityListInput;
import com.dili.trace.rpc.BaseInfoRpc;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 神农系统中相关基础信息
 */
@RestController
@RequestMapping(value = "/toll")
public class TollInfoController {
    private static final Logger LOGGER= LoggerFactory.getLogger(TollInfoController.class);

    @Autowired
    BaseInfoRpc baseInfoRpc;


    @RequestMapping("/category")
    @ResponseBody
    public Map<String, ?> listByName(String name, boolean allFlag) {
        List<Category> categorys = queryCategorys(name);

        List<Map<String, Object>> list = Lists.newArrayList();
        if (categorys != null && categorys.size() > 0) {
            for (Category c : categorys) {
                /*String[] split = c.getPath().split(",");
                Long parentId = c.getParent();
                String parentName = c.getName();*/
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
    
    private int checkPriority(City city) {
    	List<String>prirityCitys=Arrays.asList("青州市","寿光市","辽宁省","河北省","吉林省");
    	for(int i=0;i<prirityCitys.size();i++) {
    		if(city.getMergerName().contains(prirityCitys.get(i))) {
    			return i;
    		}
    	}
    	return -1;
    }
    @RequestMapping(value="/city",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, ?> queryCity(String name) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            try{
                List<City> citys =queryCitys(name);
                if(citys!=null&&!citys.isEmpty()) {
                	
                	Collections.sort(citys, (o1, o2) -> {
						int index1=checkPriority(o1);
						int index2=checkPriority(o2);
						if(index1!=-1&&index2!=-1) {
							return index1-index2;
						}else if(index1!=-1){
							return -1;
						}else if(index2!=-1){
							return 1;
						}else {
							return 0;
						}
					});
                	
                }
                
                
                for (City city : citys) {
                    Map<String, Object> obj = Maps.newHashMap();
                    obj.put("id", city.getId());
                    obj.put("data", name);
                    obj.put("name", city.getName());
                    obj.put("value", city.getMergerName());
                    obj.put("customCode", city.getCustomCode());
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

    private List<Category> queryCategorys(String name) {
        CategoryListInput query = new CategoryListInput();
        query.setKeyword(name);
        BaseOutput<List<Category>> result = baseInfoRpc.listCategoryByCondition(query);
        if(result.isSuccess()){
            return result.getData();
        }
        List<Category> citys = new ArrayList<>();
        /*Category city = new Category();
        city.setName("苹果");
        city.setId(1L);
        city.setParent(0L);
        citys.add(city);
        Category city1 = new Category();
        city1.setName("苹果2");
        city1.setId(2L);
        city1.setParent(1L);
        citys.add(city1);*/
        return citys;
    }
    private List<City> queryCitys(String name) {
        CityListInput query = new CityListInput();
        query.setKeyword(name);
        BaseOutput<List<City>> result = baseInfoRpc.listCityByCondition(query);
        if(result.isSuccess()){
            return result.getData();
        }
        List<City> citys = new ArrayList<>();
        /*City city = new City();
        city.setName("成都");
        city.setMergerName("四川成都");
        city.setId(1L);
        city.setParentId(0L);
        citys.add(city);
        City city1 = new City();
        city1.setName("成南");
        city1.setMergerName("四川成南");
        city1.setId(2L);
        city1.setParentId(1L);
        citys.add(city1);*/
        return citys;
    }
}
