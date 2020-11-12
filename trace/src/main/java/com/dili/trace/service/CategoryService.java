package com.dili.trace.service;

import java.util.List;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.POJOUtils;
import com.dili.trace.dao.CategoryMapper;
import com.dili.trace.domain.Category;
import com.dili.trace.enums.CategoryIsShowEnum;
import com.dili.trace.glossary.EnabledStateEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

@Service
public class CategoryService extends BaseServiceImpl<Category, Long> {
	public List<Category> listCategoryByKeyword(String keyword, Integer level, Long parentId, Long marketId) {
		if (StringUtils.isBlank(keyword) && level == null && parentId == null && marketId == null) {
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
		if (marketId != null) {
			e.and().andEqualTo("marketId", marketId);
		}
		e.and().andNotEqualTo("isShow", CategoryIsShowEnum.NOT_SHOW.getCode());
		return this.getDao().selectByExample(e);
	}

	public Integer count(Category category) {
		return ((CategoryMapper) (this.getDao())).selectCount(category);
	}

	/**
	 * 根据用户ID，操作启禁用
	 *
	 * @param id
	 * @param enable 是否启用(true-启用，false-禁用)
	 * @return
	 */
	public BaseOutput updateEnable(Long id, Boolean enable){
		Category category = get(id);
		if (category == null) {
			return BaseOutput.failure("数据不存在");
		}
		if (enable) {
			if (EnabledStateEnum.ENABLED.getCode().equals(category.getState())){
				return BaseOutput.failure("该商品已启用");
			}
			category.setState(EnabledStateEnum.ENABLED.getCode());
			this.updateSelective(category);
		} else {
			if (EnabledStateEnum.DISABLED.getCode().equals(category.getState())){
				System.out.println("该商品已禁用");
				return BaseOutput.failure("该商品已禁用");
			}
			category.setState(EnabledStateEnum.DISABLED.getCode());
			this.updateSelective(category);
		}
		return BaseOutput.success("操作成功");
	}

	public CategoryMapper getActualDao() {
		return (CategoryMapper) getDao();
	}

	public EasyuiPageOutput selectForEasyuiPage(Category domain, boolean useProvider) throws Exception {
		if (domain.getRows() != null && domain.getRows() >= 1) {
			PageHelper.startPage(domain.getPage(), domain.getRows());
		}
		if (StringUtils.isNotBlank(domain.getSort())) {
			domain.setSort(POJOUtils.humpToLineFast(domain.getSort()));
		}
		List<Category> categorie = getActualDao().selectForPage(domain);
		long total = categorie instanceof Page ? ((Page) categorie).getTotal() : (long) categorie.size();
		List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, categorie) : categorie;
		return new EasyuiPageOutput((int) total, results);
	}
}
