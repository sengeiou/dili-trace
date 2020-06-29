package com.dili.trace.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.Category;
import com.google.common.collect.Lists;

import tk.mybatis.mapper.entity.Example;

@Service
public class CategoryService extends BaseServiceImpl<Category, Long> {
	public List<Category> listCategoryByKeyword(String keyword) {
		if (StringUtils.isBlank(keyword)) {
			return Lists.newArrayList();
		}
		Example e = new Example(Category.class);
		e.and().andLike("name", "%" + keyword + "%");
		return this.getDao().selectByExample(e);
	}
}
