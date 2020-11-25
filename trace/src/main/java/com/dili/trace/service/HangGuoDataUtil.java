package com.dili.trace.service;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.util.MD5Util;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.hangguo.HangGuoCommodity;
import com.dili.trace.domain.hangguo.HangGuoPic;
import com.dili.trace.domain.hangguo.HangGuoTrade;
import com.dili.trace.domain.hangguo.HangGuoUser;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.glossary.hanguo.HangGuoGoodsLevelEnum;
import com.dili.trace.glossary.hanguo.HangGuoVocationTypeEnum;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class HangGuoDataUtil {
    private static final Logger logger = LoggerFactory.getLogger(HangGuoDataUtil.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtService userExtService;
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

    @Value("${thrid.insert.batch.size}")
    private Integer insertBatchSize;
    private static final String TRADE_REQUEST_CODE_TYPE = "TRADE_REQUEST_CODE";
    //上报金额上限
    private Integer reportMaxAmountInt = 300000;

    /**
     * 会员信息
     *
     * @param infoList
     */
    public void createHangGuoMember(List<HangGuoUser> infoList, Date createTime) {
        if (CollectionUtils.isEmpty(infoList)) {
            logger.info("===========>获取杭果会员数据为空");
            return;
        }
        Long startTime = DateUtils.getCurrentDate().getTime();
        try {
            List<String> codeList = StreamEx.of(infoList).nonNull().map(c -> c.getMemberNo()).collect(Collectors.toList());
            Long mapTime = DateUtils.getCurrentDate().getTime();
            logger.info("===========>生成经营户编码map用时：" + (mapTime - startTime));
            //已存在用户list
            List<User> updateList = getUserListByThirdPartyCode(codeList);
            Long queETime = DateUtils.getCurrentDate().getTime();
            logger.info("===========>生成查询已存在经营户编码用时：" + (queETime - mapTime));
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
            Long pTime = DateUtils.getCurrentDate().getTime();
            logger.info("===========>生成经营户数据用时：" + (pTime - queETime));
            if (CollectionUtils.isNotEmpty(userList)) {
                // 分批上报
                Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
                // 分批数
                Integer part = userList.size() / batchSize;
                for (int i = 0; i <= part; i++) {
                    Integer endPos = i == part ? userList.size() : (i + 1) * batchSize;
                    List<User> partBills = userList.subList(i * batchSize, endPos);
                    addUser(partBills);
                    addUserExt(partBills);
                }
            }
            Long cTime = DateUtils.getCurrentDate().getTime();
            logger.info("===========>插入经营户数据用时：" + (cTime - pTime));
            //更新经营户正式表
//            List<User> updateUserList = getHangGuoMemberList(updateHangGuoUserList, createTime);
//            if (CollectionUtils.isNotEmpty(updateUserList)) {
//                updateUserByThirdCode(updateUserList);
//            }
            logger.info("===========>处理经营户数据总用时：" + (cTime - startTime));
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

            if (CollectionUtils.isNotEmpty(userList)) {
                // 分批上报
                Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
                // 分批数
                Integer part = userList.size() / batchSize;
                for (int i = 0; i <= part; i++) {
                    Integer endPos = i == part ? userList.size() : (i + 1) * batchSize;
                    List<User> partBills = userList.subList(i * batchSize, endPos);
                    addUser(partBills);
                    addUserExt(partBills);
                }
            }

            //更新经营户正式表 【拉取数据时，数据已经存在则忽略】
//            List<User> updateUserList = getHangGuoSupplierList(updateHangGuoUserList, createTime);
//            updateUserByThirdCode(updateUserList);
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
        StreamEx.of(commodityList).nonNull().forEach(c -> {
            c.setFirstCateg(c.getFirstCateg().trim());
            c.setSecondCateg(c.getSecondCateg().trim());
            c.setItemName(c.getItemName().trim());
            c.setItemNumber(c.getItemNumber().trim());
            c.setItemUnitName(c.getItemUnitName() == null ? null : c.getItemUnitName().trim());
        });
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
        Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
        // 分批数
        Integer part = tradeList.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? tradeList.size() : (i + 1) * batchSize;
            List<HangGuoTrade> partBills = tradeList.subList(i * batchSize, endPos);

            StreamEx.of(partBills).nonNull().forEach(p -> {
                p.setRegisterNo(p.getRegisterNo() == null ? null : p.getRegisterNo().trim());
            });
            hangGuoDataService.batchInsertCacheTradeList(partBills);
        }
        //patch标志位
        updateCacheTradeReportFlag(createTime);
        //新增交易
        addTradeList(createTime);
        logger.info("============>>> 处理交易数据用时：" + (DateUtils.getCurrentDate().getTime() - createTime.getTime()));
    }

    /**
     *
     * @param createTime
     */
    private void updateCacheTradeReportFlag(Date createTime) {

        //获取其用户商品集合
        Category category = new Category();
        category.setMarketId(Long.valueOf(MarketIdEnum.FRUIT_TYPE.getCode()));
        List<Category> categoryList = categoryService.list(category);
        Map<String, User> userMap = getUserMap();
        Map<String, Category> categoryMap = StreamEx.of(categoryList).nonNull().collect(Collectors.toMap(Category::getCode, c -> c, (a, b) -> a));

        //patch由于没有对应用户或商品导致之前的交易单没有处理
        updateCacheTradeReportFlagToTrue(userMap, categoryMap, createTime);
        //大于50W的交易金额patch为无需上报,交易单对应商品或用户关联不到为无需处理
        updateCacheTradeReportFlagToFalse(userMap, categoryMap, createTime);

    }

    /**
     *
     * @return
     */
    private Map<String, User> getUserMap() {
        User u = DTOUtils.newDTO(User.class);
        u.setMarketId(Long.valueOf(MarketIdEnum.FRUIT_TYPE.getCode()));
        u.setYn(YnEnum.YES.getCode());
        List<User> userList = userService.listByExample(u);
        return StreamEx.of(userList).nonNull().collect(Collectors.toMap(us -> us.getThirdPartyCode(), user -> user, (a, b) -> a));
    }

    /**
     *
     * @param userMap
     * @param categoryMap
     * @param createTime
     */
    private void updateCacheTradeReportFlagToFalse(Map<String, User> userMap, Map<String, Category> categoryMap, Date createTime) {
        //大于50W的交易金额patch为无需上报
        hangGuoDataService.updateTradeReportListByBeyondAmount(reportMaxAmountInt);
        //交易单对应商品或用户关联不到
        HangGuoTrade trade = new HangGuoTrade();
        trade.setHandleFlag(DataHandleFlagEnum.PENDING_HANDLE.getCode());
        List<HangGuoTrade> tradeList = hangGuoDataService.selectTradeReportListByHandleFlag(trade);
        List<HangGuoTrade> collect = StreamEx.of(tradeList).nonNull().filter(t -> !userMap.containsKey(t.getMemberNo().trim())
                || !userMap.containsKey(t.getSupplierNo().trim()) || !categoryMap.containsKey(t.getItemNumber().trim())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            String handleRemark = "交易单对应商品或用户无法关联，标记为暂不处理";
            //分批处理
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // 分批数
            Integer part = collect.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? collect.size() : (i + 1) * batchSize;
                List<HangGuoTrade> partBills = collect.subList(i * batchSize, endPos);

                Map<String, Object> map = new HashMap<>(16);
                map.put("list", partBills);
                map.put("handleRemark", handleRemark);
                map.put("handleFlag", DataHandleFlagEnum.UN_NEED_HANDLE.getCode());
                hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
            }


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
            boolean needUpdate = userMap.containsKey(c.getMemberNo().trim()) && userMap.containsKey(c.getSupplierNo().trim()) && categoryMap.containsKey(c.getItemNumber().trim());
            if (needUpdate) {
                trades.add(c);
            }
        });


        if (CollectionUtils.isNotEmpty(trades)) {
            //分批处理
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // 分批数
            Integer part = trades.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? trades.size() : (i + 1) * batchSize;
                List<HangGuoTrade> partBills = trades.subList(i * batchSize, endPos);

                Map<String, Object> map = new HashMap<>(16);
                map.put("list", partBills);
                map.put("handleFlag", DataHandleFlagEnum.PENDING_HANDLE.getCode());
                hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
            }
        }
    }

    /**
     * 照片数据
     *
     * @param picList
     * @param createTime
     */
    public void createHangGuoUserPicList(List<HangGuoPic> picList, Date createTime) {
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
//            user.setCredentialUrl(p.getValue());
            user.setCardNoFrontUrl(p.getValue());
            user.setCardNoBackUrl(p.getValue());
            user.setModified(createTime);
            updateUserList.add(user);
        });
        if (CollectionUtils.isNotEmpty(updateUserList)) {
            hangGuoDataService.batchUpdateUserByThirdCode(updateUserList);
        }
    }

    /**
     *
     * @param code
     * @param type
     * @param picContent
     * @return
     */
    public String createHangGuoUserPic(String code, String type, String picContent) {
        String picPath = null;
        if (StringUtils.isBlank(picContent)) {
            logger.info("获取杭果照片数据为空");
            return picPath;
        }
        String fileName = type;
        if (StringUtils.isNotBlank(code)) {
            picPath = createPicFile(fileName + code, picContent);
        } else {
            picPath = createPicFile(fileName + UUID.randomUUID().toString(), picContent);
        }
        return picPath;
    }

    /**
     *
     * @param memberNo
     * @param img64Str
     * @return
     */
    private String createPicFile(String memberNo, String img64Str) {
        if (StringUtils.isBlank(img64Str)) {
            logger.info("createPicFile IS NULL");
            return null;
        }
        img64Str = img64Str.trim();
        String imgPath = defaultConfiguration.getImageDirectory() + ImageCertTypeEnum.USER_PHOTO_HANGGUO.getName();
        imgPath += "/";
        imgPath += DateUtils.format(new Date(), "yyyyMM");
        File filepatch = new File(imgPath);
        if (!filepatch.exists()) {
            filepatch.mkdirs();
        }
        // 生成jpeg图片
        imgPath += "/" + memberNo.trim() + "." + ImageFileTypeEnum.JPEG_TYPE.getName();
        try {
            // Base64解码
            logger.info("=====>>pic length:" + img64Str.length());
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
            if (EnabledStateEnum.ENABLED.getCode().equals(sellerUser.getState())) {
                userList.add(sellerUser);
            }
        });
        return userList;
    }

    /**
     *
     * @param info
     * @param createTime
     * @return
     */
    private User getHangGuoMember(HangGuoUser info, Date createTime) {
        String nullStr = "''";
        Integer version = 0;
        Integer source = 20;
        User sellerUser = DTOUtils.newDTO(User.class);
        sellerUser.setThirdPartyCode(info.getMemberNo() == null ? null : info.getMemberNo().trim());
        sellerUser.setCreated(info.getEnableDate());
        sellerUser.setName(info.getName() == null ? null : info.getName().trim());
        Integer vocationType = getVocationType(info.getOperateType());
        sellerUser.setVocationType(vocationType);
        sellerUser.setValidateState(ValidateStateEnum.PASSED.getCode());
        sellerUser.setAddr(info.getOperateAddr());
        sellerUser.setPhone(info.getPhoneNumber() == null ? nullStr : info.getPhoneNumber().trim());
        //0非激活1激活
        sellerUser.setState(info.getStatusCode());
        if (null != info.getEffectiveDateTime()) {
            //有效时间在当前时间之前则为禁用
            if (info.getEffectiveDateTime().before(DateUtils.getCurrentDate())) {
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
        sellerUser.setMarketId(Long.valueOf(MarketIdEnum.FRUIT_TYPE.getCode()));
        sellerUser.setMarketName(MarketIdEnum.FRUIT_TYPE.getName());
        sellerUser.setIsActive(UserActiveEnum.DOWN.getCode());
        // 杭果全部为【个人】
        sellerUser.setUserType(UserTypeEnum.USER.getCode());

        UserExt userExt = new UserExt();
        userExt.setCredentialType(info.getCredentialType() == null ? null : info.getCredentialType().trim());
        userExt.setCredentialName(info.getCredentialName());
        userExt.setCredentialNumber(info.getCredentialNumber() == null ? null : info.getCredentialNumber().trim());
        userExt.setIdAddr(info.getIdAddr());
        userExt.setWhereis(info.getWhereis());
        userExt.setCreditLimit(info.getCreditLimit());
        userExt.setEffectiveDate(info.getEffectiveDateTime());
        userExt.setRemark(info.getRemarkMemo());
        sellerUser.setUserExt(userExt);

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

    /**
     *
     * @param list
     * @param createTime
     * @return
     */
    private List<User> getHangGuoSupplierList(List<HangGuoUser> list, Date createTime) {
        List<User> userList = new ArrayList<>();
        StreamEx.of(list).nonNull().forEach(user -> {
            User sellerUser = getHangGuoSupplier(user, createTime);
            sellerUser.setCreated(createTime);
            if (EnabledStateEnum.ENABLED.getCode().equals(sellerUser.getState())) {
                userList.add(sellerUser);
            }
        });
        return userList;
    }

    /**
     *
     * @param updateUserList
     */
    private void updateUserByThirdCode(List<User> updateUserList) {
        if (CollectionUtils.isNotEmpty(updateUserList)) {
            hangGuoDataService.batchUpdateUserByThirdCode(updateUserList);
        }
    }

    /**
     *
     * @param userList
     */
    private void addUser(List<User> userList) {
        if (CollectionUtils.isNotEmpty(userList)) {
            userService.batchInsert(userList);
        }
    }

    /**
     *
     * @param userList
     */
    private void addUserExt(List<User> userList) {
        if (CollectionUtils.isNotEmpty(userList)) {
            List userExts = StreamEx.of(userList).map(u -> {
                UserExt userExt = u.getUserExt();
                userExt.setUserId(u.getId());
                return userExt;
            }).toList();
            userExtService.batchInsert(userExts);
        }
    }

    /**
     * 杭果供应商转换为溯源经营户信息
     *
     * @param info
     * @return
     */
    private User getHangGuoSupplier(HangGuoUser info, Date createTime) {
        String nullStr = "''";
        Integer version = 0;
        Integer source = 20;
        User sellerUser = DTOUtils.newDTO(User.class);
        sellerUser.setThirdPartyCode(info.getSupplierNo() == null ? null : info.getSupplierNo().trim());
        sellerUser.setName(info.getSupplierName() == null ? null : info.getSupplierName().trim());
        sellerUser.setLicense(info.getLiscensNo());
        sellerUser.setPhone(info.getMobileNumber() == null ? null : info.getMobileNumber().trim());
        if (null != info.getEffectiveDateTime()) {
            //有效时间在当前时间之前则为禁用
            if (info.getEffectiveDateTime().before(DateUtils.getCurrentDate())) {
                sellerUser.setState(EnabledStateEnum.DISABLED.getCode());
            }
        }

        if (IDTypeEnum.ID_CARD.getName().equals(info.getCredentialName().trim())) {
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
        sellerUser.setMarketId(Long.valueOf(MarketIdEnum.FRUIT_TYPE.getCode()));
        sellerUser.setMarketName(MarketIdEnum.FRUIT_TYPE.getName());
        sellerUser.setIsActive(UserActiveEnum.DOWN.getCode());
        sellerUser.setAddr(info.getOperateAddr());
        //0非激活1激活
        sellerUser.setState(info.getStatusCode());
        // 杭果全部个人
        sellerUser.setUserType(UserTypeEnum.USER.getCode());

        // 维护 UserExt
        UserExt userExt = new UserExt();
        userExt.setCredentialType(info.getCredentialType());
        userExt.setCredentialName(info.getCredentialName());
        userExt.setCredentialNumber(info.getCredentialNumber() == null ? null : info.getCredentialNumber().trim());
        userExt.setSex(info.getSex());
        userExt.setFixedTelephone(info.getPhoneNumber());
        userExt.setChargeRate(info.getChargeRate());
        userExt.setMangerRate(info.getMangerRate());
        userExt.setStorageRate(info.getStorageRate());
        userExt.setAssessRate(info.getAssessRate());
        userExt.setApprover(info.getApprover());
        userExt.setSupplierType(info.getSupplierType() == null ? nullStr : info.getSupplierType());
        userExt.setIdAddr(info.getIdAddr());
        userExt.setEffectiveDate(info.getEffectiveDateTime());
        userExt.setRemark(info.getRemarkMemo());

        sellerUser.setUserExt(userExt);


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
        //更新商品信息
        hangGuoDataService.batchUpdateCategoryByThirdCode(categoryList);
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
        category.setType(CategoryTypeEnum.SUPPLEMENT.getCode());
        category.setCreated(createTime);
        category.setMarketId(MarketIdEnum.FRUIT_TYPE.getCode().longValue());
        hangGuoDataService.updateHangGuoCommodityParent(category);

        hangGuoCategoryLevelFaultTolerant();
    }

    /**
     * 杭果商品容错处理,由于其上级品种编码与预定规则不符时,作一次特殊处理patch
     */
    private void hangGuoCategoryLevelFaultTolerant() {
        Category category = new Category();
        category.setMarketId(MarketIdEnum.FRUIT_TYPE.getCode().longValue());
        List<Category> categoryFaultList = hangGuoDataService.getCategoryFaultList(category);
        StreamEx.of(categoryFaultList).nonNull().forEach(c -> {
            if (StringUtils.isNotBlank(c.getParentCode())) {
                String parentCode = c.getParentCode();
                if (StringUtils.isNotBlank(parentCode)) {
                    recursionCategoty(c, c.getParentCode(), 3);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(categoryFaultList)) {
            logger.info("商品断层处理,处理数据量:" + categoryFaultList.size());
            categoryService.batchUpdate(categoryFaultList);
        }
    }

    /**
     * 递归处理
     *
     * @param c
     * @param count
     */
    private void recursionCategoty(Category c, String parentCode, int count) {
        if (count <= 0) {
            logger.info("递归结束");
            return;
        }
        logger.info("递归处理商品父节点,parentCode:" + parentCode + " |count:" + count);
        parentCode = turnSubStr(parentCode);
        Category parentCategory = hangGuoDataService.getCategoryByThirdCode(parentCode);
        if (null != parentCategory) {
            c.setParentCode(parentCategory.getCode());
            c.setParentId(parentCategory.getId());
        } else {
            count--;
            recursionCategoty(c, parentCode, count);
        }
    }

    /**
     *
     * @param parentCode
     * @return
     */
    private String turnSubStr(String parentCode) {
        if (StringUtils.isNotBlank(parentCode)) {
            return parentCode.substring(0, parentCode.length() - 1);
        }
        return null;
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
        }
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
        StreamEx.of(commodityList).nonNull().forEach(c -> {
            Category category = new Category();
            category.setFullName(c.getItemName() == null ? null : c.getItemName().trim());
            category.setName(c.getItemName() == null ? null : c.getItemName().trim());
            category.setSpecification(c.getItemUnitName() == null ? null : c.getItemUnitName().trim());
            category.setCode(c.getItemNumber() == null ? null : c.getItemNumber().trim());
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
                String goodsCode = category.getCode().trim();
                String parentGoodsCode = goodsCode.substring(0, goodsCode.length() - 1);
                //商品码删除最后一位后与品种码一致时为第四层级
                if (varietySet.contains(parentGoodsCode)) {
                    category.setLevel(HangGuoGoodsLevelEnum.GOODS_FOUR.getCode());
                } else {
                    category.setParentCode(parentGoodsCode);
                }
            }
        });
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

    /**
     *
     * @param addDetailList
     * @param productStockMap
     * @return
     */
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
        //插入交易缓存表
        Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
        // 分批数
        Integer part = tradeList.size() / batchSize;
        for (int i = 0; i <= part; i++) {
            Integer endPos = i == part ? tradeList.size() : (i + 1) * batchSize;
            List<HangGuoTrade> partBills = tradeList.subList(i * batchSize, endPos);
            doPartTrade(partBills, createTime);
        }
    }

    /**
     * 新增交易信息到正式表
     *
     * @param tradeList
     * @param createTime
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void doPartTrade(List<HangGuoTrade> tradeList, Date createTime) {
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
        if (CollectionUtils.isEmpty(userList) || CollectionUtils.isEmpty(categoryList)) {
            logger.info("交易数据没有对应的经营户或者商品,无法关联,不创建正式交易单");
            return;
        }
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

    /**
     *
     * @param billNos
     * @return
     */
    private Map<Long, TradeDetail> getTradeBillMap(List<String> billNos) {
        List<RegisterBill> billList = hangGuoDataService.getRegisterBillByIds(billNos);
        List<String> billIds = StreamEx.of(billList).nonNull().map(b -> b.getCode()).collect(Collectors.toList());
        List<TradeDetail> orderDetailList = hangGuoDataService.getBillTradeDetailListByBillIds(billIds);
        return StreamEx.of(orderDetailList).nonNull().collect(Collectors.toMap(TradeDetail::getBillId, Function.identity(), (a, b) -> a));
    }

    /**
     *
     * @param handleFlag
     * @param tradeList
     */
    private void updateCacheTradeHandleFlag(Integer handleFlag, List<HangGuoTrade> tradeList) {
        if (CollectionUtils.isNotEmpty(tradeList)) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", tradeList);
            map.put("handleFlag", handleFlag);
            hangGuoDataService.batchUpdateCacheTradeHandleFlag(map);
        }
    }

    /**
     *
     * @param commodityCode
     * @return
     */
    private List<Category> getCategoryListByThirdCode(List<String> commodityCode) {
        if (CollectionUtils.isEmpty(commodityCode)) {
            return null;
        }
        return hangGuoDataService.getCategoryListByThirdCode(commodityCode);
    }

    /**
     *
     * @param userCode
     * @return
     */
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
                if (null != detail) {
                    parentId = detail.getId();
                    checkInId = detail.getCheckinRecordId();
                    billId = detail.getBillId();
                }
            }

            Long buyId = userMap.get(t.getMemberNo().trim()).getId();
            String buyName = userMap.get(t.getMemberNo().trim()).getName();
            Long sellerId = userMap.get(t.getSupplierNo().trim()).getId();
            String sellerName = userMap.get(t.getSupplierNo().trim()).getName();
            String productName = categoryMap.get(t.getItemNumber().trim()).getName();
            BigDecimal weight = new BigDecimal(t.getWeight());
            String nowDate = DateUtils.format(DateUtils.getCurrentDate());
            Long tradeRequestId = tradeRequestMap.get(t.getTradeNo().trim()).getId();

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
        BigDecimal reportMaxAmount = new BigDecimal(reportMaxAmountInt);
        //createTime
        LocalDateTime dateTime = Instant.ofEpochMilli(createTime.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        String preKey = "prefix";
        String seqKey = "seq";
        Map<String, String> map = generNextCode(tradeList.size(), dateTime);
        String codePrefix = map.get(preKey);
        String codeSeq = map.get(seqKey);
        AtomicInteger index = new AtomicInteger(0);
        StreamEx.of(tradeList).nonNull().forEach(t -> {
            int currentIndex = index.getAndIncrement();
            String code = codePrefix;
            Integer codeSeqInt = Integer.valueOf(codeSeq) + currentIndex;
            code = code.concat(String.valueOf(codeSeqInt));
            logger.info("===>生成编号为：" + code);
            Long buyId = userMap.get(t.getMemberNo().trim()).getId();
            String buyName = userMap.get(t.getMemberNo().trim()).getName();
            Long sellerId = userMap.get(t.getSupplierNo().trim()).getId();
            String sellerName = userMap.get(t.getSupplierNo().trim()).getName();
            Long orderId = orderMap.get(t.getTradeNo().trim()).getId();
            Long buyMarketId = userMap.get(t.getMemberNo().trim()).getMarketId();
            Long sellerMarketId = userMap.get(t.getSupplierNo().trim()).getMarketId();
            Long productStockId = null;
            if (null != billMap && !billMap.isEmpty() && StringUtils.isNotBlank(t.getRegisterId())) {
                if (null != billMap.get(t.getRegisterId())) {
                    productStockId = billMap.get(t.getRegisterId()).getProductStockId();
                }
            }
            String productName = categoryMap.get(t.getItemNumber().trim()).getName();
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
            request.setCode(code);
            requestList.add(request);
        });
        return requestList;
    }

    /**
     *
     * @param addSize
     * @param now
     * @return
     */
    //@Transactional(propagation = Propagation.REQUIRED)
    private Map<String, String> generNextCode(int addSize, LocalDateTime now) {
        int tradeRequestSize = 5;
        String preKey = "prefix";
        String seqKey = "seq";
        CodeGenerate codeGenerate = new CodeGenerate();
        codeGenerate.setType(TRADE_REQUEST_CODE_TYPE);
        List<CodeGenerate> codeGenerateList = codeGenerateService.listByExample(codeGenerate);
        if (CollectionUtils.isNotEmpty(codeGenerateList)) {
            codeGenerate = codeGenerateList.get(0);
        }
        if (codeGenerate == null) {
            throw new TraceBizException("生成编号错误");
        }
        String nextSegment = now.format(DateTimeFormatter.ofPattern(codeGenerate.getPattern()));
        if (!nextSegment.equals(codeGenerate.getSegment())) {
            codeGenerate.setSeq(1L);
            codeGenerate.setSegment(nextSegment);
            codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            codeGenerate.setSeq(codeGenerate.getSeq() + addSize);
            codeGenerate.setModified(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        }
        codeGenerateService.updateSelective(codeGenerate);
        /*return StringUtils.trimToEmpty(codeGenerate.getPrefix()).concat(nextSegment)
                .concat(StringUtils.leftPad(String.valueOf(codeGenerate.getSeq()), tradeRequestSize, "0"));*/
        Map<String, String> map = new HashMap<>(16);
        map.put(preKey, StringUtils.trimToEmpty(codeGenerate.getPrefix()).concat(nextSegment));
        map.put(seqKey, String.valueOf(codeGenerate.getSeq()));
        return map;
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
            Long buyId = userMap.get(t.getMemberNo().trim()).getId();
            String buyName = userMap.get(t.getMemberNo().trim()).getName();
            Long buyMarketId = userMap.get(t.getMemberNo().trim()).getMarketId();
            Long sellerId = userMap.get(t.getSupplierNo().trim()).getId();
            String sellerName = userMap.get(t.getSupplierNo().trim()).getName();
            Long sellerMarketId = userMap.get(t.getSupplierNo().trim()).getMarketId();
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
