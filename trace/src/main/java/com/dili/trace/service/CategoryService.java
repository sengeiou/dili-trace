package com.dili.trace.service;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.dao.HangGuoDataMapper;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.hangguo.HangGuoCategory;
import com.dili.trace.dto.query.CategoryQueryDto;
import com.dili.trace.glossary.UserQrStatusEnum;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author asa.lee
 */
@Service
public class CategoryService extends BaseServiceImpl<HangGuoCategory, Long> {
    @Autowired
    HangGuoDataMapper hangGuoDataMapper;

    /**
     * 保存category
     *
     * @param categoryId
     * @param marektId
     * @return
     */
    public HangGuoCategory saveCategory(Long categoryId, Long marektId) {
        LOGGER.debug("saveCategory categoryId={},marketId={}", categoryId, marektId);
        if (categoryId == null || marektId == null) {
            return null;
        }
        try {
            HangGuoCategory category = new HangGuoCategory();
            category.setCategoryId(categoryId);
            category.setMarketId(marektId);
            category.setLastSyncSuccess(YesOrNoEnum.NO.getCode());
            this.hangGuoDataMapper.insertIgnoreCategory(category);

            CategoryQueryDto query = new CategoryQueryDto();
            query.setCategoryId(categoryId);
            query.setMarketId(marektId);

            return StreamEx.of(this.listByExample(query)).findFirst().orElse(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;


    }

    /**
     * 更新品类数据
     *
     * @param id
     * @param categoryDTO
     */
    public void updateCategory(Long id, CusCategoryDTO categoryDTO) {
        LOGGER.debug("updateCategory id={},categoryDTO={}", id, categoryDTO);
        if (id == null || categoryDTO == null) {
            return;
        }

        try {
            HangGuoCategory updatableCategory = new HangGuoCategory();
            updatableCategory.setId(id);
            updatableCategory.setCreated(categoryDTO.getCreateTime());
            updatableCategory.setModified(categoryDTO.getModifyTime());
            updatableCategory.setLastSyncSuccess(YesOrNoEnum.YES.getCode());
            updatableCategory.setLastSyncTime(new Date());
            updatableCategory.setLastSyncSuccess(YesOrNoEnum.YES.getCode());
            updatableCategory.setName(categoryDTO.getName());
            updatableCategory.setFullName(categoryDTO.getName());
            updatableCategory.setParentId(categoryDTO.getParent());
            this.updateSelective(updatableCategory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
