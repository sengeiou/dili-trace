package com.dili.trace.service.impl;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.util.MD5Util;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.HangGuoDataMapper;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.hangguo.HangGuoCategory;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.service.CategoryService;
import com.dili.trace.service.SyncRpcService;
import com.dili.trace.service.UserService;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class SyncRpcServiceImpl implements SyncRpcService {

    private static final Logger logger = LoggerFactory.getLogger(SyncRpcServiceImpl.class);

    @Resource
    private CustomerRpc customerRpc;
    @Resource
    private AssetsRpc assetsRpc;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private HangGuoDataMapper hangGuoDataMapper;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    private Integer pushFlag = 1;
    private Integer userYNn = -1;
    /**
     * 溯源系统商品最高级
     */
    private Integer syncLevelTop = 1;
    private Integer syncLevelClass = 2;
    private Integer syncLevelGoods = 3;
    /**
     * uap商品最高级
     */
    Long topParentId = 0L;
    /**
     * 补充商品分类名称
     */
    private String syncGoodsDefaultNames = "其他";


    @Override
    public void syncRpcUserByUserIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        try {
            CustomerQueryInput customerQueryInput = new CustomerQueryInput();
            Set<Long> userSet = new HashSet<>(userIds);
            customerQueryInput.setIdSet(userSet);
            BaseOutput<List<CustomerExtendDto>> rpcUserByIds = customerRpc.list(customerQueryInput);
            List<UserInfo> userList = userService.getUserListByUserIds(userIds);
            if (null == rpcUserByIds) {
                return;
            }
            updateUserByRpcUserList(rpcUserByIds.getData(), userList);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncGoodsToRpcCategory(Long categoryId) {
        //向上同步三级
        Integer syncLevel = syncLevelGoods;
        syncGoodsFromRpcCategory(categoryId, syncLevel);
    }

    /**
     * 同步商品及其上级
     *
     * @param categoryId
     * @param syncLevel
     */
    public void syncGoodsFromRpcCategory(Long categoryId, Integer syncLevel) {
        BaseOutput<CategoryDTO> baseOutput = assetsRpc.get(categoryId);
        if (Objects.nonNull(baseOutput)) {
            CategoryDTO dto = baseOutput.getData();
            if (Objects.isNull(dto)) {
                if (logger.isErrorEnabled()) {
                    logger.error("同步商品id：" + categoryId + "失败，此商品查询失败");
                }
                return;
            }
            //上级为空且同步级别大于0则新增其他类型商品类型作为上级商品
            if (Objects.isNull(dto.getParent()) || topParentId.equals(dto.getParent())) {
                syncDefaultCategory(dto, syncLevel);
            } else {
                //新增商品上级
                if (syncLevelGoods.equals(syncLevel)) {
                    syncLevel--;
                    BaseOutput<CategoryDTO> categoryClass = assetsRpc.get(dto.getParent());
                    if (Objects.nonNull(categoryClass)) {
                        CategoryDTO classData = categoryClass.getData();
                        //二级分类为空，则新增其他分类
                        if (Objects.isNull(classData)) {
                            if (logger.isInfoEnabled()) {
                                logger.info("商品非最高级，但上级为空，新增其他分类作为上级");
                            }
                            Long parentId = addDefaultCategory(dto, syncLevel);
                            //新增商品本体
                            addOrUpdateHangGuoCategory(dto, syncLevelGoods, null, parentId);
                            return;
                        }
                        //新增商品本体
                        addOrUpdateHangGuoCategory(dto, syncLevelGoods, null, null);
                        //新增并获取商品最高级分类
                        BaseOutput<CategoryDTO> topClass = assetsRpc.get(classData.getParent());
                        Long topUapParentId = null;
                        Long topParentId = null;
                        syncLevel--;
                        if (Objects.nonNull(topClass)) {
                            //三级分类为空新增其他分类
                            boolean isTopNull = Objects.isNull(topClass.getData());
                            if (isTopNull) {
                                CategoryDTO other = new CategoryDTO();
                                other.setMarketId(dto.getMarketId());
                                syncLevel++;
                                topParentId = addDefaultCategory(other, syncLevel);
                            } else {
                                if (isNotTopCategory(topClass.getData(), syncLevel)) {
                                    if (logger.isInfoEnabled()) {
                                        logger.info("商品不是最顶级极点，查询上级商品同步");
                                    }
                                    topUapParentId = getTopCategoryFromRpcCategory(topClass.getData().getParent(), syncLevel, classData.getMarketId());
                                } else {
                                    addOrUpdateHangGuoCategory(topClass.getData(), syncLevelTop, topUapParentId, topParentId);
                                }
                            }
                        }
                        addOrUpdateHangGuoCategory(classData, syncLevelClass, topUapParentId, topParentId);
                    }
                }
                //更新category的父节点Id
                updateCategoryParentId();
            }
        }

    }

    /**
     * 新增最高级分类并且返回其uap-id
     *
     * @param parent
     * @param syncLevel
     * @param marketId
     * @return
     */
    private Long getTopCategoryFromRpcCategory(Long parent, Integer syncLevel, Long marketId) {
        BaseOutput<CategoryDTO> baseOutput = assetsRpc.get(parent);
        if (Objects.nonNull(baseOutput)) {
            //不是最高级
            if (isNotTopCategory(baseOutput.getData(), syncLevel)) {
                return getTopCategoryFromRpcCategory(baseOutput.getData().getParent(), syncLevel, baseOutput.getData().getMarketId());
            }
            HangGuoCategory category = addOrUpdateHangGuoCategory(baseOutput.getData(), syncLevel, null, null);
            return null == category ? null : category.getUapId();
        }
        return null;
    }

    /**
     * 更新当天同步的上报商品的父节点
     */
    private void updateCategoryParentId() {
        HangGuoCategory up = new HangGuoCategory();
        up.setModified(DateUtils.getCurrentDate());
        hangGuoDataMapper.updateParentByUapId(up);
    }

    /**
     * 判断当前递归是否是最高级商品
     *
     * @param dto
     * @param syncLevel
     * @return
     */
    private boolean isNotTopCategory(CategoryDTO dto, Integer syncLevel) {

        return null != dto && !topParentId.equals(dto.getParent()) && syncLevelTop.equals(syncLevel);
    }

    private HangGuoCategory addOrUpdateHangGuoCategory(CategoryDTO dto, Integer syncLevel, Long topUapParentId, Long parentId) {
        HangGuoCategory que = new HangGuoCategory();
        que.setUapId(dto.getId());
        List<HangGuoCategory> categoryList = categoryService.listByExample(que);
        if (CollectionUtils.isEmpty(categoryList)) {
            HangGuoCategory syCategory = buildCreatCategory(dto, syncLevel);
            if (Objects.nonNull(topUapParentId)) {
                syCategory.setUapParentId(topUapParentId);
            }
            if (Objects.nonNull(parentId)) {
                syCategory.setParentId(parentId);
            }
            categoryService.insertSelective(syCategory);
            return syCategory;
        } else {
            HangGuoCategory syCategory = buildUpdateCategory(dto, syncLevel);
            if (Objects.nonNull(topUapParentId)) {
                syCategory.setUapParentId(topUapParentId);
            }
            if (Objects.nonNull(parentId)) {
                syCategory.setParentId(parentId);
            }
            HangGuoCategory conCategory = new HangGuoCategory();
            conCategory.setUapId(dto.getId());
            categoryService.updateExactByExample(syCategory, conCategory);
            return syCategory;
        }
    }

    /**
     * 同步没有上级的商品
     *
     * @param dto
     */
    private void syncDefaultCategory(CategoryDTO dto, Integer level) {
        HangGuoCategory category = buildUpdateCategory(dto, level);
        //新增上级商品并将标识符递减
        category.setParentId(addDefaultCategory(dto, level));
        categoryService.insertSelective(category);
    }

    /**
     * 新增其他分类并返回id给下级作为parent-id
     *
     * @param dto
     * @param level
     * @return
     */
    private Long addDefaultCategory(CategoryDTO dto, Integer level) {
        if (level < syncLevelTop) {
            if (logger.isInfoEnabled()) {
                logger.info("新增默认商品分类已到极点");
            }
            return null;
        }
        level--;
        HangGuoCategory topCategory = buildNullParentCategory(dto, level);
        if (!syncLevelTop.equals(level)) {
            topCategory.setParentId(addDefaultCategory(dto, level));
        }
        categoryService.insertSelective(topCategory);
        return null == topCategory ? null : topCategory.getId();
    }

    /**
     * @param dto
     * @return
     */
    private HangGuoCategory buildNullParentCategory(CategoryDTO dto, Integer level) {
        HangGuoCategory parentCategory = new HangGuoCategory();
        parentCategory.setLevel(level);
        if (Objects.nonNull(dto) && Objects.nonNull(dto.getMarketId())) {
            parentCategory.setMarketId(dto.getMarketId());
        }
        parentCategory.setName(syncGoodsDefaultNames);
        parentCategory.setFullName(syncGoodsDefaultNames);
        parentCategory.setModified(DateUtils.getCurrentDate());
        parentCategory.setCreated(DateUtils.getCurrentDate());
        return parentCategory;
    }


    /**
     * 通过rpc构建新增商品
     *
     * @param dto
     * @param syncLevel
     * @return
     */
    private HangGuoCategory buildCreatCategory(CategoryDTO dto, Integer syncLevel) {
        HangGuoCategory category = buildUpdateCategory(dto, syncLevel);
        category.setCreated(DateUtils.getCurrentDate());
        return category;
    }

    /**
     * 通过rpc更新商品
     *
     * @param dto
     * @return
     */
    private HangGuoCategory buildUpdateCategory(CategoryDTO dto, Integer level) {
        HangGuoCategory category = new HangGuoCategory();
        category.setLevel(level);
        category.setUapId(dto.getId());
        category.setUapParentId(dto.getParent());
        category.setOldUapParentId(dto.getParent());
        category.setMarketId(dto.getMarketId());
        category.setName(dto.getName());
        category.setFullName(dto.getName());
        category.setModified(DateUtils.getCurrentDate());
        return category;
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncRpcUserByUserId(Long userId) {
        //rpc用户列表
        BaseOutput<List<CustomerExtendDto>> resultData = getRpcUserById(userId);
        //未获取到rpc用户
        if (null == resultData) {
            return;
        }
        List<CustomerExtendDto> rpcUserList = resultData.getData();
        if (CollectionUtils.isNotEmpty(rpcUserList)) {
            StreamEx.of(rpcUserList).nonNull().forEach(rpcUser -> {
                //用户没有id或者没有市场时不同步
                if (Objects.isNull(rpcUser.getId()) || Objects.isNull(rpcUser.getCustomerMarket())) {
                    return;
                }
                UserInfo user = getNewUserPojo();
                user.setId(rpcUser.getId());
                user.setMarketId(rpcUser.getCustomerMarket().getMarketId());
                List<UserInfo> userList = userService.list(user);
                if (CollectionUtils.isNotEmpty(userList)) {
                    UserInfo upUser = buildUpdateUser(rpcUser);
                    updateUserInfo(upUser);
                } else {
                    UserInfo addUser = buildAddUser(rpcUser);
                    addUserInfoByRpc(addUser);
                }
            });

        }
    }


    /**
     * 同步rpc用户信息到溯源用户信息
     *
     * @param userList
     * @param list
     */
    private void updateUserByRpcUserList(List<CustomerExtendDto> userList, List<UserInfo> list) {
        Map<Long, UserInfo> userMap = StreamEx.of(list).nonNull().collect(Collectors.toMap(UserInfo::getId, Function.identity(), (a, b) -> a));
        //取需要更新的用户信息updateList
        List<UserInfo> updateList = new ArrayList<>();
        List<UserInfo> addList = new ArrayList<>();
        StreamEx.of(userList).nonNull().forEach(rpcUser -> {
            if (userMap.containsKey(rpcUser.getId())) {
                UserInfo upUser = buildUpdateUser(rpcUser);
                updateList.add(upUser);
            } else {
                UserInfo addUser = buildAddUser(rpcUser);
                addList.add(addUser);
            }
        });
        if (CollectionUtils.isNotEmpty(updateList)) {
            userService.batchUpdateSelective(updateList);
        }
        if (CollectionUtils.isNotEmpty(addList)) {
            userService.batchInsert(addList);
        }
    }

    /**
     * 根据rpc用户信息新增用户
     *
     * @param addUser
     */
    private void addUserInfoByRpc(UserInfo addUser) {
        userService.insertSelective(addUser);
    }

    /**
     * 根据rpc用户信息更新用户
     *
     * @param upUser
     */
    private void updateUserInfo(UserInfo upUser) {
        userService.updateSelective(upUser);
    }

    /**
     * 返回prc用户信息
     *
     * @param userId
     * @return
     */
    private BaseOutput<List<CustomerExtendDto>> getRpcUserById(Long userId) {
        CustomerQueryInput customerQueryInput = new CustomerQueryInput();
        customerQueryInput.setId(userId);
        return customerRpc.list(customerQueryInput);
    }


    /**
     * 构建新增用户实体
     *
     * @param rpcUser
     * @return
     */
    private UserInfo buildAddUser(CustomerExtendDto rpcUser) {
        Integer version = 0;
        UserInfo addUser = buildUpdateUser(rpcUser);
        addUser.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
        addUser.setVersion(version);
        addUser.setCreated(DateUtils.getCurrentDate());
        addUser.setModified(DateUtils.getCurrentDate());
        addUser.setValidateState(ValidateStateEnum.PASSED.getCode());
        return addUser;
    }

    /**
     * 构建更新用户实体
     *
     * @param rpcUser
     * @return
     */
    private UserInfo buildUpdateUser(CustomerExtendDto rpcUser) {
        UserInfo upUser = getNewUserPojo();
        upUser.setId(rpcUser.getId());
        upUser.setName(rpcUser.getName());
        upUser.setPhone(null == rpcUser.getContactsPhone() ? "''" : rpcUser.getContactsPhone());
        upUser.setCardNo(null == rpcUser.getCertificateNumber() ? "''" : rpcUser.getCertificateNumber());
        if (null != rpcUser.getIsDelete() && rpcUser.getIsDelete().equals(YesOrNoEnum.YES.getCode())) {
            upUser.setYn(userYNn);
        }
        //YesOrNoEnum.YES
        if (Objects.nonNull(rpcUser.getIsDelete())) {
            upUser.setIsDelete(rpcUser.getIsDelete().longValue());
        }
        //未激活
        if (CustomerEnum.State.NORMAL.getCode().equals(rpcUser.getState())) {
            upUser.setState(EnabledStateEnum.ENABLED.getCode());
        } else {
            upUser.setState(EnabledStateEnum.DISABLED.getCode());
        }
        upUser.setIsPush(pushFlag);
        if (Objects.nonNull(rpcUser.getCustomerMarket())) {
            upUser.setMarketId(rpcUser.getCustomerMarket().getMarketId());
        }
        return upUser;
    }

    /**
     * @return
     */
    private static UserInfo getNewUserPojo() {
        return new UserInfo();
    }
}
