package com.dili.trace.service;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.util.MD5Util;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.hangguo.HangGuoCommodity;
import com.dili.trace.domain.hangguo.HangGuoPic;
import com.dili.trace.domain.hangguo.HangGuoTrade;
import com.dili.trace.domain.hangguo.HangGuoUser;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.glossary.hanguo.HangGuoGoodsLevelEnum;
import com.dili.trace.glossary.hanguo.HangGuoVocationTypeEnum;
import one.util.streamex.StreamEx;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class HangGuoDataUtil {
    private static final Logger logger = LoggerFactory.getLogger(HangGuoDataUtil.class);

    @Value("${current.baseWebPath}")
    private String baseWebPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HangGuoDataService hangGuoDataService;

    @Autowired
    private CodeGenerateService codeGenerateService;

    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private TradeRequestService tradeRequestService;
    @Autowired
    private TradeDetailService tradeDetailService;
    @Autowired
    private ProductStockService productStockService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    /**
     * 会员信息
     *
     * @param infoList
     */
    public void createHangGuoMember(List<HangGuoUser> infoList, Date createTime) {
        if (CollectionUtils.isEmpty(infoList)) {
            logger.info("获取杭果供应商数据为空");
            return;
        }
        try {
            List<String> codeList = StreamEx.of(infoList).nonNull().map(c -> c.getMemberNo()).collect(Collectors.toList());
            //已存在用户list
            List<User> updateList = getUserListByThirdPartyCode(codeList);
            Set<String> existsUserSet = new HashSet<>();
            StreamEx.of(updateList).nonNull().forEach(u -> {
                existsUserSet.add(u.getThirdPartyCode());
            });

            List<HangGuoUser> updateHangGuoUserList = new ArrayList<>();
            if (!existsUserSet.isEmpty()) {
                updateHangGuoUserList = StreamEx.of(infoList).nonNull().filter(user -> existsUserSet.contains(user.getSupplierNo())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(updateHangGuoUserList)) {
                infoList.removeAll(updateHangGuoUserList);
            }

            //新增到经营户正式表
            List<User> userList = getHangGuoMemberList(infoList, createTime);
            if (CollectionUtils.isNotEmpty(userList)) {
                addUser(userList);
            }

            //更新经营户正式表
            List<User> updateUserList = getHangGuoMemberList(updateHangGuoUserList, createTime);
            if (CollectionUtils.isNotEmpty(updateUserList)) {
                updateUserByThirdCode(updateUserList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 供应商信息
     *
     * @param list
     */
    public void createHangGuoSupplier(List<HangGuoUser> list, Date createTime) {
        if (CollectionUtils.isEmpty(list)) {
            logger.error("获取杭果供应商数据为空");
            return;
        }
        try {
            //已存在用户list
            List<String> codeList = StreamEx.of(list).nonNull().map(c -> c.getSupplierNo()).collect(Collectors.toList());
            List<User> updateList = getUserListByThirdPartyCode(codeList);
            Set<String> existsUserSet = new HashSet<>();
            StreamEx.of(updateList).nonNull().forEach(u -> {
                existsUserSet.add(u.getThirdPartyCode());
            });
            List<HangGuoUser> updateHangGuoUserList = new ArrayList<>();

            if (!existsUserSet.isEmpty()) {
                updateHangGuoUserList = StreamEx.of(list).nonNull().filter(user -> existsUserSet.contains(user.getSupplierNo())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(updateHangGuoUserList)) {
                list.removeAll(updateHangGuoUserList);
            }
            //新增到经营户正式表
            List<User> userList = getHangGuoSupplierList(list, createTime);
            addUser(userList);
            //更新经营户正式表
            List<User> updateUserList = getHangGuoSupplierList(updateHangGuoUserList, createTime);
            updateUserByThirdCode(updateUserList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    /**
     * 商品信息
     */
    public void createCommodity(List<HangGuoCommodity> commodityList, Date createTime) {
        if (CollectionUtils.isEmpty(commodityList)) {
            logger.info("获取杭果商品数据为空");
            return;
        }
        List<String> list = StreamEx.of(commodityList).nonNull().map(c -> c.getItemNumber()).collect(Collectors.toList());
        List<Category> existsCateGoryList = getCategoryListByThirdCode(list);

        Set<String> existsCodeSet = new HashSet<>();
        StreamEx.of(existsCateGoryList).nonNull().forEach(u -> {
            existsCodeSet.add(u.getCode());
        });
        List<HangGuoCommodity> updateHangGuoCommodityList = new ArrayList<>();
        if (!existsCodeSet.isEmpty()) {
            updateHangGuoCommodityList = StreamEx.of(commodityList).nonNull().filter(trade -> existsCodeSet.contains(trade.getItemNumber())).collect(Collectors.toList());
        }

        if (CollectionUtils.isNotEmpty(updateHangGuoCommodityList)) {
            commodityList.removeAll(updateHangGuoCommodityList);
        }
        if (CollectionUtils.isNotEmpty(commodityList)) {
            addCateGory(commodityList, createTime);
        }
        if (CollectionUtils.isNotEmpty(updateHangGuoCommodityList)) {
            updateCateGoryByThirdCode(updateHangGuoCommodityList, createTime);
        }
    }

    /**
     * 交易信息
     *
     * @param tradeList
     * @param createTime
     */
    public void createTrade(List<HangGuoTrade> tradeList, Date createTime) {
        if (CollectionUtils.isEmpty(tradeList)) {
            logger.info("获取杭果交易数据为空");
            return;
        }

        //插入交易缓存表
        hangGuoDataService.batchInsertCacheTradeList(tradeList);
        //patch标志位
        updateCacheTradeReportFlag(createTime);

        //新增交易
        addTradeList(createTime);
    }

    private void updateCacheTradeReportFlag(Date createTime) {

        //获取其用户商品集合

        List<Category> categoryList = categoryService.listCategoryByCondition(new CategoryListInput());
        Map<String, User> userMap = getUserMap();
        Map<String, Category> categoryMap = StreamEx.of(categoryList).nonNull().collect(Collectors.toMap(Category::getCode, c -> c, (a, b) -> a));

        //patch由于没有对应用户或商品导致之前的交易单没有处理
        updateCacheTradeReportFlagToTrue(userMap, categoryMap, createTime);
        //大于50W的交易金额patch为无需上报,交易单对应商品或用户关联不到为无需处理
        updateCacheTradeReportFlagToFalse(userMap, categoryMap, createTime);

    }

    private Map<String, User> getUserMap() {
        User u = DTOUtils.newDTO(User.class);
        u.mset(IDTO.AND_CONDITION_EXPR, " third_party_code IS NOT NULL");
        List<User> userList = userService.listByExample(u);
        return StreamEx.of(userList).nonNull().collect(Collectors.toMap(User::getThirdPartyCode, user -> user, (a, b) -> a));
    }

    private void updateCacheTradeReportFlagToFalse(Map<String, User> userMap, Map<String, Category> categoryMap, Date createTime) {
        //大于50W的交易金额patch为无需上报
        hangGuoDataService.updateTradeReportListByBeyondAmount();
        //交易单对应商品或用户关联不到
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.PENDING_HANDLE.getCode());
        List<HangGuoTrade> tradeList = hangGuoDataService.selectTradeReportListByHandleFlag(trade);
        List<HangGuoTrade> collect = StreamEx.of(tradeList).nonNull().filter(t -> !userMap.containsKey(t.getMemberNo())
                || !userMap.containsKey(t.getSupplierNo()) || !categoryMap.containsKey(t.getItemNumber())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            String handleRemark = "交易单对应商品或用户无法关联，标记为暂不处理";
            Map<String, Object> map = new HashMap<>(16);
            map.put("list", collect);
            map.put("handleRemark", handleRemark);
            map.put("handleFlag", DataHandleFlagEnum.UN_NEED_HANDLE.getCode());
            hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
        }
    }


    /**
     * 将无需处理交易数据patch一次标志位
     *
     * @param createTime
     */
    private void updateCacheTradeReportFlagToTrue(Map<String, User> userMap, Map<String, Category> categoryMap, Date createTime) {
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.UN_NEED_HANDLE.getCode());
        List<HangGuoTrade> tradeList = hangGuoDataService.selectTradeReportListByHandleFlag(trade);
        if (CollectionUtils.isEmpty(tradeList)) {
            logger.info("");
            return;
        }

        List<HangGuoTrade> trades = new ArrayList<>();
        StreamEx.of(tradeList).nonNull().forEach(c -> {
            boolean needUpdate = userMap.containsKey(c.getMemberNo()) && userMap.containsKey(c.getSupplierNo()) && categoryMap.containsKey(c.getItemNumber());
            if (needUpdate) {
                trades.add(c);
            }
        });
        if (CollectionUtils.isNotEmpty(trades)) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("list", trades);
            map.put("handleFlag", DataHandleFlagEnum.PENDING_HANDLE.getCode());
            hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
        }
    }

    /**
     * 照片数据
     *
     * @param picList
     * @param createTime
     */
    public void createHangGuoUserPic(List<HangGuoPic> picList, Date createTime) {
        if (CollectionUtils.isEmpty(picList)) {
            logger.info("获取杭果照片数据为空");
            return;
        }
        Map<String, String> picMap = new HashMap<>();
        StreamEx.of(picList).nonNull().forEach(p -> {
            if (StringUtils.isNotBlank(p.getMemberNo())) {
                String picPath = createPicFile(p.getMemberNo(), p.getMemberPic());
                if (null != picPath) {
                    picMap.put(p.getSupplierNo(), picPath);
                }
            } else {
                String picPath = createPicFile(p.getSupplierNo(), p.getSupplierPic());
                if (null != picPath) {
                    picMap.put(p.getSupplierNo(), picPath);
                }
            }
        });
        List<User> updateUserList = new ArrayList<>();
        StreamEx.of(picMap.entrySet()).nonNull().forEach(p -> {
            User user = DTOUtils.newDTO(User.class);
            user.setThirdPartyCode(p.getKey());
            user.setCredentialUrl(p.getValue());
            user.setModified(createTime);
            updateUserList.add(user);
        });
        if (CollectionUtils.isNotEmpty(updateUserList)) {
            hangGuoDataService.batchUpdateUserByThirdCode(updateUserList);
        }
    }

    private String createPicFile(String memberNo, String img64Str) {
        if (img64Str == null) {
            logger.info("createPicFile IS NULL");
            return null;
        }
        String imgPath = defaultConfiguration.getImageDirectory() + ImageCertTypeEnum.USER_PHOTO_HANGGUO.getName();
        imgPath += "/";
        imgPath += DateUtils.format(new Date(), "yyyyMM");
        // 生成jpeg图片
        imgPath += "/" + memberNo + "." + ImageFileTypeEnum.JPEG_TYPE.getName();
        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(img64Str);
            for (int i = 0; i < bytes.length; ++i) {
                // 调整异常数据
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgPath);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("第三方编码：" + memberNo + "保存图片失败，失败原因：" + e.getMessage());
            return null;
        }
        return imgPath;
    }

    /**
     * 会员信息转换
     *
     * @param infoList
     * @param createTime
     * @return
     */
    private List<User> getHangGuoMemberList(List<HangGuoUser> infoList, Date createTime) {
        //新增到经营户正式表
        List<User> userList = new ArrayList<>();
        StreamEx.of(infoList).nonNull().forEach(u -> {
            User sellerUser = getHangGuoMember(u, createTime);
            sellerUser.setCreated(createTime);
            userList.add(sellerUser);
        });
        return userList;
    }

    private User getHangGuoMember(HangGuoUser info, Date createTime) {
        String nullStr = "空";
        Integer version = 0;
        Integer source = 20;
        User sellerUser = DTOUtils.newDTO(User.class);
        sellerUser.setThirdPartyCode(info.getMemberNo());
        sellerUser.setCreated(info.getEnableDate());
        sellerUser.setName(info.getName());
        sellerUser.setCredentialType(info.getCredentialType());
        sellerUser.setCredentialName(info.getCredentialName());
        sellerUser.setCredentialNumber(info.getCredentialNumber());
        Integer vocationType = getVocationType(info.getOperateType());
        sellerUser.setVocationType(vocationType);
        sellerUser.setValidateState(ValidateStateEnum.PASSED.getCode());
        sellerUser.setIdAddr(info.getIdAddr());
        sellerUser.setAddr(info.getOperateAddr());
        sellerUser.setPhone(info.getPhoneNumber());
        sellerUser.setWhereis(info.getWhereis());
        //0非激活1激活
        sellerUser.setState(info.getStatus());
        sellerUser.setCreditLimit(info.getCreditLimit());
        sellerUser.setEffectiveDate(info.getEffectiveDate());
        sellerUser.setRemark(info.getRemarkMemo());
        if (null != info.getEffectiveDate()) {
            //有效时间在当前时间之前则为禁用
            if (info.getEffectiveDate().before(DateUtils.getCurrentDate())) {
                sellerUser.setState(EnabledStateEnum.DISABLED.getCode());
            }
        }

        if (IDTypeEnum.ID_CARD.getName().equals(info.getCredentialName())) {
            sellerUser.setCardNo(info.getCredentialNumber());
        } else {
            sellerUser.setCardNo(nullStr);
        }
        // TODO 默认密码
        sellerUser.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
        sellerUser.setVersion(version);
        sellerUser.setModified(createTime);
        sellerUser.setCreated(createTime);
        sellerUser.setYn(YnEnum.YES.getCode());
        sellerUser.setSource(source);
        return sellerUser;
    }

    /**
     * 将杭果经营性质转换为溯源系统中的经营性质
     *
     * @param operateTypeStr
     * @return
     */
    private Integer getVocationType(String operateTypeStr) {
        if (StringUtils.isBlank(operateTypeStr)) {
            return VocationTypeEnum.PERSONAL.getCode();
        }
        String operateType = operateTypeStr.trim();
        if (HangGuoVocationTypeEnum.INDIVIDUAL_WHOLESALE.getName().equals(operateType)) {
            return VocationTypeEnum.WHOLESALE.getCode();
        } else if (HangGuoVocationTypeEnum.COMMERCIAL_WHOLESALE.getName().equals(operateType)) {
            return VocationTypeEnum.AGRICULTURETRADE.getCode();
        } else if (HangGuoVocationTypeEnum.GROUP.getName().equals(operateType)) {
            return VocationTypeEnum.GROUP.getCode();
        } else if (HangGuoVocationTypeEnum.RETAIL.getName().equals(operateType)) {
            return VocationTypeEnum.PERSONAL.getCode();
        } else {
            return null;
        }
    }

    private List<User> getHangGuoSupplierList(List<HangGuoUser> list, Date createTime) {
        List<User> userList = new ArrayList<>();
        StreamEx.of(list).nonNull().forEach(user -> {
            User sellerUser = getHangGuoSupplier(user, createTime);
            sellerUser.setCreated(createTime);
            userList.add(sellerUser);
        });
        return userList;
    }

    private void updateUserByThirdCode(List<User> updateUserList) {
        if (CollectionUtils.isNotEmpty(updateUserList)) {
            hangGuoDataService.batchUpdateUserByThirdCode(updateUserList);
        }
    }

    private void addUser(List<User> userList) {
        if (CollectionUtils.isNotEmpty(userList)) {
            userService.batchInsert(userList);
        }
    }

    /**
     * 杭果供应商转换为溯源经营户信息
     *
     * @param info
     * @return
     */
    private User getHangGuoSupplier(HangGuoUser info, Date createTime) {
        String nullStr = "空";
        Integer version = 0;
        Integer source = 20;
        User sellerUser = DTOUtils.newDTO(User.class);
        sellerUser.setThirdPartyCode(info.getSupplierNo());
        sellerUser.setName(info.getSupplierName());
        sellerUser.setCredentialType(info.getCredentialType());
        sellerUser.setCredentialName(info.getCredentialName());
        sellerUser.setCredentialNumber(info.getCredentialNumber());
        sellerUser.setSex(info.getSex());
        sellerUser.setLicense(info.getLiscensNo());
        sellerUser.setPhone(info.getMobileNumber());
        sellerUser.setFixedTelephone(info.getPhoneNumber());
        //0非激活1激活
        sellerUser.setState(info.getStatus());
        sellerUser.setAddr(info.getAddr());
        sellerUser.setChargeRate(info.getChargeRate());
        sellerUser.setMangerRate(info.getMangerRate());
        sellerUser.setStorageRate(info.getStorageRate());
        sellerUser.setAssessRate(info.getAssessRate());
        sellerUser.setApprover(info.getApprover());
        sellerUser.setSupplierType(info.getSupplierType());
        sellerUser.setIdAddr(info.getIdAddr());
        sellerUser.setAddr(info.getOperateAddr());
        sellerUser.setEffectiveDate(info.getEffectiveDate());
        sellerUser.setRemark(info.getRemarkMemo());
        if (null != info.getEffectiveDate()) {
            //有效时间在当前时间之前则为禁用
            if (info.getEffectiveDate().before(DateUtils.getCurrentDate())) {
                sellerUser.setState(EnabledStateEnum.DISABLED.getCode());
            }
        }

        if (IDTypeEnum.ID_CARD.getName().equals(info.getCredentialName())) {
            sellerUser.setCardNo(info.getCredentialNumber());
        } else {
            sellerUser.setCardNo(nullStr);
        }
        // TODO 默认密码
        sellerUser.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
        sellerUser.setVersion(version);
        sellerUser.setModified(createTime);
        sellerUser.setCreated(createTime);
        sellerUser.setYn(YnEnum.YES.getCode());
        sellerUser.setValidateState(ValidateStateEnum.PASSED.getCode());
        sellerUser.setSource(source);
        return sellerUser;
    }


    /**
     * 更新杭果商品
     *
     * @param updateHangGuoCommodityList
     * @param createTime
     */
    private void updateCateGoryByThirdCode(List<HangGuoCommodity> updateHangGuoCommodityList, Date createTime) {
        List<Category> categoryList = conversionCommodityList(updateHangGuoCommodityList, createTime);
        //删除杭果需要更新商品
        hangGuoDataService.deleteHangGuoCommodityByThirdCode(categoryList);
        //新增
        hangGuoDataService.bachInsertCommodityList(categoryList);
        //patch
        Category category = new Category();
//        TODO
//        category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
//        category.setCreated(createTime);
        hangGuoDataService.updateHangGuoCommodityParent(category);
    }

    /**
     * 新增商品到正式表
     *
     * @param commodityList
     * @param createTime
     */
    private void addCateGory(List<HangGuoCommodity> commodityList, Date createTime) {

        List<Category> categoryList = conversionCommodityList(commodityList, createTime);

        //先将杭果商品插入到溯源系统
        hangGuoDataService.bachInsertCommodityList(categoryList);
        //patch当天杭果商品的parent_id
        Category category = new Category();
//        TODO
//        category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
//        category.setCreated(createTime);
//        hangGuoDataService.updateHangGuoCommodityParent(category);
    }

    /**
     * 获取商品等级
     *
     * @param category
     * @param commodity
     * @return
     */
    private void setCommodityLevel(Category category, HangGuoCommodity commodity) {
        String goodsCode = commodity.getItemNumber().trim();
        String first = commodity.getFirstCateg().trim();
        String second = commodity.getSecondCateg().trim();
        String categoryCode = commodity.getCategoryNumber().trim();
        //        TODO
        /*
        category.setIsShow(CategoryIsShowEnum.IS_SHOW.getCode());
        //商品编码与大类码一致，商品为第一层级.无parentId
        if (goodsCode.equals(first)) {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_ONE.getCode());
        }
        //商品编码与小类码一致，商品为第二层级
        else if (goodsCode.equals(second)) {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_TWO.getCode());
            category.setParentCode(first);
        }
        //商品编码与品种码一致，商品为第三层级
        else if (goodsCode.equals(categoryCode)) {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_THREE.getCode());
            category.setParentCode(second);
        } else {
            category.setLevel(HangGuoGoodsLevelEnum.GOODS_FIVE.getCode());
            Integer psize = 5;
            String parentGoodsCode = categoryCode;
            if (categoryCode.length() > psize) {
                parentGoodsCode = categoryCode.substring(0, categoryCode.length() - 1);
            }
            category.setParentCode(parentGoodsCode);
            category.setIsShow(CategoryIsShowEnum.NOT_SHOW.getCode());
        }*/
    }

    /**
     * 杭果商品转换为溯源商品
     *
     * @param commodityList
     * @param createTime
     * @return
     */
    private List<Category> conversionCommodityList(List<HangGuoCommodity> commodityList, Date createTime) {
        List<Category> categoryList = new ArrayList();
        //品种集合
        CopyOnWriteArraySet<String> varietySet = new CopyOnWriteArraySet<>();

        //        TODO
        /*
        StreamEx.of(commodityList).nonNull().forEach(c -> {
            Category category = new Category();
            category.setFullName(c.getItemName());
            category.setName(c.getItemName());
            category.setSpecification(c.getItemUnitName());
            category.setCode(c.getItemNumber());
            category.setCreated(createTime);
            category.setModified(createTime);
            category.setMarketId(Long.valueOf(MarketIdEnum.FRUIT_TYPE.getCode()));
            category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
            setCommodityLevel(category, c);

            varietySet.add(c.getCategoryNumber());
            categoryList.add(category);
        });
        //第5层级单独处理
        StreamEx.of(categoryList).nonNull().forEach(category -> {
            Integer categoryLevel = category.getLevel();
            if (categoryLevel.equals(HangGuoGoodsLevelEnum.GOODS_FIVE.getCode())) {
                String goodsCode = category.getCode();
                String parentGoodsCode = goodsCode.substring(0, goodsCode.length() - 1);
                //商品码删除最后一位后与品种码一致时为第四层级
                if (varietySet.contains(parentGoodsCode)) {
                    category.setLevel(HangGuoGoodsLevelEnum.GOODS_FOUR.getCode());
                } else {
                    category.setParentCode(parentGoodsCode);
                }
            }
        });*/
        return categoryList;
    }


    /**
     * 新增交易单(同时扣减对应库存
     *
     * @param createTime
     */
    @Deprecated
    private void addTradeListBackup(Date createTime) {

        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.PENDING_HANDLE.getCode());
        List<HangGuoTrade> tradeList = hangGuoDataService.selectTradeReportListByHandleFlag(trade);
        List<String> userCode = StreamEx.of(tradeList).map(t -> t.getSupplierNo()).collect(Collectors.toList());
        List<String> commodityCode = new ArrayList<>();
        for (HangGuoTrade t : tradeList) {
            if (t != null) {
                userCode.add(t.getMemberNo());
                commodityCode.add(t.getItemNumber());
            }
        }
        //用户，商品
        userCode = StreamEx.of(userCode).nonNull().distinct().collect(Collectors.toList());
        commodityCode = StreamEx.of(commodityCode).nonNull().distinct().collect(Collectors.toList());
        List<User> userList = getUserListByThirdPartyCode(userCode);
        List<Category> categoryList = getCategoryListByThirdCode(commodityCode);
        Map<String, User> userMap = StreamEx.of(userList).nonNull().collect(Collectors.toMap(User::getThirdPartyCode, user -> user, (key1, key2) -> key1));
        Map<String, Category> categoryMap = StreamEx.of(categoryList).nonNull().collect(Collectors.toMap(Category::getCode, category -> category, (key1, key2) -> key1));

        //报备单id
        List<String> billIds = StreamEx.of(tradeList).nonNull().map(t -> t.getRegisterId()).collect(Collectors.toList());

        List<TradeDetail> orderDetailList = hangGuoDataService.getBillTradeDetailListByBillIds(billIds);
        //报备单库存map
        Map<Long, TradeDetail> billMap = StreamEx.of(orderDetailList).nonNull().collect(Collectors.toMap(TradeDetail::getBillId, Function.identity(), (a, b) -> a));
        //新增主单
        List<TradeOrder> addTradeList = getTradeOrderList(tradeList, userMap, createTime);

        tradeOrderService.batchInsert(addTradeList);
        Map<String, TradeOrder> orderMap = StreamEx.of(addTradeList).nonNull().collect(Collectors.toMap(TradeOrder::getTradeNo, Function.identity(), (key1, key2) -> key1));

        //新增request
        List<TradeRequest> addTradeRequestList = getTradeOrderRequestList(tradeList, userMap, categoryMap, orderMap, billMap, createTime);
        tradeRequestService.batchInsert(addTradeRequestList);
        Map<String, TradeRequest> tradeRequestMap = StreamEx.of(addTradeRequestList).nonNull().collect(Collectors.toMap(TradeRequest::getThirdTradeNo, Function.identity(), (a, b) -> a));
        //新增detail
        List<TradeDetail> addDetailList = getTradeOrderDetailRequestList(tradeList, userMap, categoryMap, billMap, tradeRequestMap, createTime);
        tradeDetailService.batchInsert(addDetailList);

        //库存id
        List<Long> stockIdList = StreamEx.of(orderDetailList).nonNull().map(s -> s.getProductStockId()).collect(Collectors.toList());
        List<ProductStock> productStockList = hangGuoDataService.getProductStockListByIds(stockIdList);
        Map<Long, ProductStock> productStockMap = StreamEx.of(productStockList).nonNull().collect(Collectors.toMap(ProductStock::getId, Function.identity(), (a, b) -> a));
        //扣减对应库存
        List<ProductStock> updateStock = beforehandReductionStockWeight(addDetailList, productStockMap);
        productStockService.batchUpdate(updateStock);
    }

    private List<ProductStock> beforehandReductionStockWeight(List<TradeDetail> addDetailList, Map<Long, ProductStock> productStockMap) {
        List<ProductStock> updateStockList = new ArrayList<>();
        Integer weightUnitMagnification = 2;
        StreamEx.of(addDetailList).nonNull().forEach(d -> {
            ProductStock updateStock = new ProductStock();
            ProductStock stock = productStockMap.get(d.getProductStockId());
            BigDecimal weight = BigDecimal.ZERO;
            if (null != stock) {
                //若报备库存单位不是公斤
                if (!WeightUnitEnum.KILO.getCode().equals(stock.getWeightUnit())) {
                    BigDecimal reduction = d.getStockWeight().multiply(new BigDecimal(weightUnitMagnification));
                    weight = stock.getStockWeight().subtract(reduction);
                } else {
                    weight = stock.getStockWeight().subtract(d.getStockWeight());
                }
            }
            updateStock.setId(stock.getId());
            updateStock.setStockWeight(weight);
            updateStockList.add(updateStock);
        });
        return updateStockList;
    }

    /**
     * 新增交易单
     *
     * @param createTime
     */
    private void addTradeList(Date createTime) {

        List<HangGuoTrade> tradeList = getPendingHandleTradeList();
        if (CollectionUtils.isEmpty(tradeList)) {
            logger.info("处理交易单数据为空");
            return;
        }
        List<String> userCode = StreamEx.of(tradeList).map(t -> t.getSupplierNo()).collect(Collectors.toList());
        List<String> commodityCode = new ArrayList<>();
        List<String> billNos = new ArrayList<>();
        for (HangGuoTrade t : tradeList) {
            if (t != null) {
                userCode.add(t.getMemberNo());
                commodityCode.add(t.getItemNumber());
                if (StringUtils.isNotBlank(t.getRegisterNo())) {
                    billNos.add(t.getRegisterNo());
                }
            }
        }
        //用户，商品
        userCode = StreamEx.of(userCode).nonNull().distinct().collect(Collectors.toList());
        commodityCode = StreamEx.of(commodityCode).nonNull().distinct().collect(Collectors.toList());
        billNos = StreamEx.of(billNos).nonNull().distinct().collect(Collectors.toList());
        List<User> userList = getUserListByThirdPartyCode(userCode);
        List<Category> categoryList = getCategoryListByThirdCode(commodityCode);
        Map<String, User> userMap = StreamEx.of(userList).nonNull().collect(Collectors.toMap(User::getThirdPartyCode, user -> user, (key1, key2) -> key1));
        Map<String, Category> categoryMap = StreamEx.of(categoryList).nonNull().collect(Collectors.toMap(Category::getCode, category -> category, (key1, key2) -> key1));

        //报备单库存map
        Map<Long, TradeDetail> billMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(billNos)) {
            billMap = getTradeBillMap(billNos);
        }
        //新增主单
        List<TradeOrder> addTradeList = getTradeOrderList(tradeList, userMap, createTime);
        if (CollectionUtils.isNotEmpty(addTradeList)) {
            tradeOrderService.batchInsert(addTradeList);
        }
        Map<String, TradeOrder> orderMap = StreamEx.of(addTradeList).nonNull().collect(Collectors.toMap(TradeOrder::getTradeNo, Function.identity(), (key1, key2) -> key1));

        //新增request
        List<TradeRequest> addTradeRequestList = getTradeOrderRequestList(tradeList, userMap, categoryMap, orderMap, billMap, createTime);
        if (CollectionUtils.isNotEmpty(addTradeRequestList)) {
            tradeRequestService.batchInsert(addTradeRequestList);
        }
        Map<String, TradeRequest> tradeRequestMap = StreamEx.of(addTradeRequestList).nonNull().collect(Collectors.toMap(TradeRequest::getThirdTradeNo, Function.identity(), (a, b) -> a));
        //新增detail
        List<TradeDetail> addDetailList = getTradeOrderDetailRequestList(tradeList, userMap, categoryMap, billMap, tradeRequestMap, createTime);
        if (CollectionUtils.isNotEmpty(addDetailList)) {
            hangGuoDataService.batchInsertTradeDetail(addDetailList);
        }

        //更新标志位
        updateCacheTradeHandleFlag(DataHandleFlagEnum.PROCESSED.getCode(), tradeList);

    }

    private Map<Long, TradeDetail> getTradeBillMap(List<String> billNos) {
        List<RegisterBill> billList = hangGuoDataService.getRegisterBillByIds(billNos);
        List<String> billIds = StreamEx.of(billList).nonNull().map(b -> b.getCode()).collect(Collectors.toList());
        List<TradeDetail> orderDetailList = hangGuoDataService.getBillTradeDetailListByBillIds(billIds);
        return StreamEx.of(orderDetailList).nonNull().collect(Collectors.toMap(TradeDetail::getBillId, Function.identity(), (a, b) -> a));
    }

    private void updateCacheTradeHandleFlag(Integer handleFlag, List<HangGuoTrade> tradeList) {
        if (CollectionUtils.isNotEmpty(tradeList)) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", tradeList);
            map.put("handleFlag", handleFlag);
            hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
        }
    }

    private List<Category> getCategoryListByThirdCode(List<String> commodityCode) {
        if (CollectionUtils.isEmpty(commodityCode)) {
            return null;
        }
        return hangGuoDataService.getCategoryListByThirdCode(commodityCode);
    }

    private List<User> getUserListByThirdPartyCode(List<String> userCode) {
        if (CollectionUtils.isEmpty(userCode)) {
            return null;
        }
        return hangGuoDataService.getUserListByThirdPartyCode(userCode);
    }

    /**
     * 待处理交易数据
     *
     * @return
     */
    private List<HangGuoTrade> getPendingHandleTradeList() {
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.PENDING_HANDLE.getCode());
        return hangGuoDataService.selectTradeReportListByHandleFlag(trade);
    }

    /**
     * 生成交易单详情
     *
     * @param tradeList
     * @param userMap
     * @param categoryMap
     * @param billMap
     * @param tradeRequestMap
     * @param createTime
     * @return
     */
    private List<TradeDetail> getTradeOrderDetailRequestList(List<HangGuoTrade> tradeList, Map<String, User> userMap, Map<String, Category> categoryMap,
                                                             Map<Long, TradeDetail> billMap, Map<String, TradeRequest> tradeRequestMap, Date createTime) {
        List<TradeDetail> detailList = new ArrayList<>();
        Integer isBatched = 1;
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            Long parentId = null;
            Long checkInId = null;
            Long billId = Long.valueOf(-1);
            if (null != billMap && !billMap.isEmpty()) {
                TradeDetail detail = billMap.get(t.getRegisterId());
                parentId = detail.getId();
                checkInId = detail.getCheckinRecordId();
                billId = detail.getBillId();
            }

            Long buyId = userMap.get(t.getMemberNo()).getId();
            String buyName = userMap.get(t.getMemberNo()).getName();
            Long sellerId = userMap.get(t.getSupplierNo()).getId();
            String sellerName = userMap.get(t.getSupplierNo()).getName();
            String productName = categoryMap.get(t.getItemNumber()).getName();
            BigDecimal weight = new BigDecimal(t.getWeight());
            String nowDate = DateUtils.format(DateUtils.getCurrentDate());
            Long tradeRequestId = tradeRequestMap.get(t.getTradeNo()).getId();

            TradeDetail tradeDetail = new TradeDetail();
            tradeDetail.setCheckinRecordId(checkInId);

            tradeDetail.setBillId(billId);

            tradeDetail.setParentId(parentId);
            tradeDetail.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
            tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
            tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            tradeDetail.setTradeType(TradeTypeEnum.SEPARATE_SALES.getCode());
            tradeDetail.setBuyerId(buyId);
            tradeDetail.setBuyerName(buyName);
            tradeDetail.setSellerId(sellerId);
            tradeDetail.setSellerName(sellerName);
            tradeDetail.setProductName(productName);
            tradeDetail.setStockWeight(weight);
            tradeDetail.setTotalWeight(weight);
            tradeDetail.setWeightUnit(WeightUnitEnum.KILO.getCode());

            tradeDetail.setTradeRequestId(tradeRequestId);

            tradeDetail.setCreated(createTime);
            tradeDetail.setModified(createTime);
            tradeDetail.setIsBatched(isBatched);
            tradeDetail.setBatchNo(nowDate);
            tradeDetail.setParentBatchNo(nowDate);

            detailList.add(tradeDetail);
        });
        return detailList;
    }

    private String getNextCode() {
        return this.codeGenerateService.nextTradeRequestCode();

    }

    /**
     * 生成交易请求
     *
     * @param tradeList
     * @param userMap
     * @param categoryMap
     * @param orderMap
     * @param billMap
     * @param createTime
     * @return
     */
    private List<TradeRequest> getTradeOrderRequestList(List<HangGuoTrade> tradeList, Map<String, User> userMap, Map<String, Category> categoryMap, Map<String, TradeOrder> orderMap, Map<Long, TradeDetail> billMap, Date createTime) {
        List<TradeRequest> requestList = new ArrayList<>();
        Integer reportMaxAmountInt = 500000;
        BigDecimal reportMaxAmount = new BigDecimal(reportMaxAmountInt);
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            Long buyId = userMap.get(t.getMemberNo()).getId();
            String buyName = userMap.get(t.getMemberNo()).getName();
            Long sellerId = userMap.get(t.getSupplierNo()).getId();
            String sellerName = userMap.get(t.getSupplierNo()).getName();
            Long orderId = orderMap.get(t.getTradeNo()).getId();
            Long buyMarketId = userMap.get(t.getMemberNo()).getMarketId();
            Long sellerMarketId = userMap.get(t.getSupplierNo()).getMarketId();
            Long productStockId = null;
            if (null != billMap && !billMap.isEmpty()) {
                productStockId = billMap.get(t.getRegisterId()).getProductStockId();
            }
            String productName = categoryMap.get(t.getItemNumber()).getName();
            TradeRequest request = new TradeRequest();
            request.setBuyerId(buyId);
            request.setBuyerName(buyName);
            request.setSellerId(sellerId);
            request.setSellerName(sellerName);
            request.setTradeWeight(new BigDecimal(t.getWeight()));
            request.setTradeOrderId(orderId);
            request.setProductStockId(productStockId);
            request.setCreated(createTime);
            request.setModified(createTime);
            request.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());
            request.setCode(getNextCode());
            request.setWeightUnit(WeightUnitEnum.KILO.getCode());
            request.setProductName(productName);
            request.setHandleTime(createTime);
            request.setThirdTradeNo(t.getTradeNo());
            request.setBatchNo(t.getBatchNo());
            request.setOriginName(t.getOriginName());
            request.setPositionNo(t.getPositionNo());
            request.setPositionName(t.getPositionName());
            request.setPrice(t.getPrice());
            request.setPackageNumber(t.getPackageNumber());
            request.setNumber(t.getNumber());
            request.setAmount(t.getAmount());
            request.setPosNo(t.getPosNo());
            request.setPayWay(t.getPayWay());
            request.setTotalAmount(t.getTotalAmount());
            request.setOperator(t.getOperator());
            request.setPayer(t.getPayer());
            request.setPayNo(t.getPayNo());
            request.setSourceType(TradeRequestSourceTypeEnum.THIRD_HANGGUO.getCode());
            if (null != t.getAmount() && t.getAmount().compareTo(reportMaxAmount) >= 0) {
                request.setReportFlag(CheckOrderReportFlagEnum.UNTREATED.getCode());
            } else {
                request.setReportFlag(CheckOrderReportFlagEnum.PROCESSED.getCode());
            }
            requestList.add(request);
        });
        return requestList;
    }

    /**
     * 生成交易主单
     *
     * @param tradeList
     * @param userMap
     * @param createTime
     * @return
     */
    private List<TradeOrder> getTradeOrderList(List<HangGuoTrade> tradeList, Map<String, User> userMap, Date createTime) {
        List<TradeOrder> orderList = new ArrayList<>();
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            Long buyId = userMap.get(t.getMemberNo()).getId();
            String buyName = userMap.get(t.getMemberNo()).getName();
            Long buyMarketId = userMap.get(t.getMemberNo()).getMarketId();
            Long sellerId = userMap.get(t.getSupplierNo()).getId();
            String sellerName = userMap.get(t.getSupplierNo()).getName();
            Long sellerMarketId = userMap.get(t.getSupplierNo()).getMarketId();
            TradeOrder order = new TradeOrder();
            order.setBuyerId(buyId);
            order.setBuyerName(buyName);
            order.setSellerId(sellerId);
            order.setSellerName(sellerName);
            order.setOrderStatus(TradeOrderStatusEnum.FINISHED.getCode());
            order.setOrderType(TradeOrderTypeEnum.BUY.getCode());
            order.setCreated(createTime);
            order.setModified(createTime);
            order.setTradeNo(t.getTradeNo());
            orderList.add(order);
        });
        return orderList;
    }

}
