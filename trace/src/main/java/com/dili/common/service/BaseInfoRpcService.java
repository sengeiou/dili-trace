package com.dili.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.City;
import com.dili.trace.domain.Department;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.dto.CityListInput;
import com.dili.trace.rpc.BaseInfoRpc;

@Component
public class BaseInfoRpcService {
	@Autowired
	BaseInfoRpc baseInfoRpc;

	public Optional<Department> queryDepartmentById(Long id) {
		if (id == null) {
			return Optional.empty();
		}
		List<Department> departmentList = this.queryDepartmentByIdList(Arrays.asList(id));
		return departmentList.stream().findFirst();

	}

	private List<Department> queryDepartmentByIdList(List<Long> idList) {

		JSONObject obj = this.baseInfoRpc.queryDepartmentsByIds(idList.toArray(new Long[idList.size()]));
		if (obj.containsKey("code") && obj.getString("code").equalsIgnoreCase("success")) {
			JSONObject data = obj.getJSONObject("data");
			if (data.containsKey("items")) {
				JSONArray items = data.getJSONArray("items");
				if (items != null) {
					List<Department> departmentList = items.toJavaList(Department.class);
					return departmentList;
				}
			}
		}
		return new ArrayList<>();

	}

	public List<Category> listCategoryByCondition(CategoryListInput query) {
		BaseOutput<List<Category>> out = baseInfoRpc.listCategoryByCondition(query);
		if (out.isSuccess()) {
			List<Category> list = CollectionUtils.emptyIfNull(out.getData()).stream().filter(c -> {
				return !c.getName().contains("蔬菜") && !c.getName().equals("椒类");
			}).collect(Collectors.toList());
			return list;
		}
		return new ArrayList<>();

	}

	public List<Category> listCategoryByCondition(String keyword) {
		CategoryListInput query = new CategoryListInput();
		query.setKeyword(keyword);
		List<Category> list = this.listCategoryByCondition(query);
		return list;

	}

	public List<City> listCityByCondition(String keyword) {
		CityListInput query = new CityListInput();
		query.setKeyword(keyword);
		BaseOutput<List<City>> result = baseInfoRpc.listCityByCondition(query);
		if (result.isSuccess()) {
			return result.getData();
		}
		return new ArrayList<>();

	}
}
