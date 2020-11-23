package com.dili.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.trace.domain.Department;

import org.springframework.stereotype.Component;

/**
 *
 * @Author guzman.liu
 * @Description
 * @Date 2020/11/23 10:22
 * @return
 */
@Component
public class BaseInfoRpcService {

    public Optional<Department> queryDepartmentById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        List<Department> departmentList = this.queryDepartmentByIdList(Arrays.asList(id));
        return departmentList.stream().findFirst();

    }

    private List<Department> queryDepartmentByIdList(List<Long> idList) {
        //TODO
/*
		JSONObject obj = this.cityService.queryDepartmentsByIds(idList.toArray(new Long[idList.size()]));
		if (obj.containsKey("code") && obj.getString("code").equalsIgnoreCase("success")) {
			JSONObject data = obj.getJSONObject("data");
			if (data.containsKey("items")) {
				JSONArray items = data.getJSONArray("items");
				if (items != null) {
					List<Department> departmentList = items.toJavaList(Department.class);
					return departmentList;
				}
			}
		}*/
        return new ArrayList<>();

    }


}
