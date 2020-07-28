package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.CategoryMapper;
import com.dili.trace.domain.Category;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

@Service
public class CategoryService extends BaseServiceImpl<Category, Long> {
	public List<Category> listCategoryByKeyword(String keyword, Integer level, Long parentId) {
		if (StringUtils.isBlank(keyword) && level == null && parentId == null) {
			return Lists.newArrayList();
		}
		Example e = new Example(Category.class);
		if (StringUtils.isNotBlank(keyword)) {
			e.and().andLike("name", "%" + keyword.trim() + "%");
		}
		if (level != null) {
			e.and().andEqualTo("level", level);
		}
		if (parentId != null) {
			e.and().andEqualTo("parentId", parentId);
		}
		return this.getDao().selectByExample(e);
	}

	public Integer count(Category category) {
		return ((CategoryMapper) (this.getDao())).selectCount(category);
	}
}
