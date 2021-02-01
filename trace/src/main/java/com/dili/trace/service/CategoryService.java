package com.dili.trace.service;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.hangguo.HangGuoCategory;
import com.dili.trace.dto.query.CategoryQueryDto;
import com.dili.trace.glossary.UserQrStatusEnum;
import one.util.streamex.StreamEx;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author asa.lee
 */
@Service
public class CategoryService extends BaseServiceImpl<HangGuoCategory, Long> {
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
        CategoryQueryDto query = new CategoryQueryDto();
        query.setCategoryId(categoryId);
        query.setMarketId(marektId);
        HangGuoCategory category = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {

            HangGuoCategory newDomain = new HangGuoCategory();
            newDomain.setCategoryId(categoryId);
            newDomain.setMarketId(marektId);
            newDomain.setLastSyncSuccess(YesOrNoEnum.NO.getCode());
            try {
                this.insertSelective(newDomain);
                return newDomain;
            } catch (Exception e) {
                return StreamEx.of(this.listByExample(query)).findFirst().orElse(null);
            }

        });
        return category;
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
        try {
            this.updateSelective(updatableCategory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
