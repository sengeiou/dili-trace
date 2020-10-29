package com.dili.trace.jobs;

import com.alibaba.fastjson.JSON;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.ThirdPartySourceData;
import com.dili.trace.domain.hangguo.HangGuoCommodity;
import com.dili.trace.domain.hangguo.HangGuoTrade;
import com.dili.trace.domain.hangguo.HangGuoUser;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.MarketIdEnum;
import com.dili.trace.enums.ThirdSourceTypeEnum;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.service.HangGuoDataService;
import com.dili.trace.service.HangGuoDataUtil;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;*/

/**
 * 对接杭果溯源入口，通过定时任务每6小时调用杭果的接口，交易数据每小时调用一次
 *
 * @author asa.lee
 */
@Component
public class HangGuoTraceabilityDataJob implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(HangGuoTraceabilityDataJob.class);

    @Autowired
    RegisterBillMapper registerBillMapper;
    @Autowired
    HangGuoDataUtil hangGuoDataUtil;
    @Autowired
    HangGuoDataService hangGuoDataService;

    @Value("${thrid.insert.batch.size}")
    private Integer insertBatchSize;

    @Override
    public void run(String... args) throws Exception {
        /*Date endTime = this.registerBillMapper.selectCurrentTime();
        List<HangGuoUser> memberList = this.getMemberList(endTime);
        List<HangGuoCommodity> goodsCategory = getGoodsCategory(endTime);
        List<HangGuoTrade> tradeList = getTradeList(endTime);
        if (!CollectionUtils.isEmpty(memberList)) {
            hangGuoDataUtil.createHangGuoMember(memberList, endTime);
        }*/

        /*if(!CollectionUtils.isEmpty(memberList)){
            hangGuoDataUtil.createHangGuoSupplier(memberList,endTime);
        }*/

        /*if(!CollectionUtils.isEmpty(goodsCategory)){
            hangGuoDataUtil.createCommodity(goodsCategory,endTime);
        }*/

        /*if(!CollectionUtils.isEmpty(tradeList)){
            hangGuoDataUtil.createTrade(tradeList,endTime);
        }*/
        //getTradeData();

    }

    /**
     * 商品信息、供应商信息、会员信息每六小时调用一次
     */
    //@Scheduled(cron = "0 0 */6 * * ?")
    public void getBaseData() {
        try {
            Date endTime = this.registerBillMapper.selectCurrentTime();
            // 商品信息
            List<HangGuoCommodity> categoryList = this.getGoodsCategory(endTime);
            // 供应商信息
            List<HangGuoUser> supplierList = this.getSupplierList(endTime);
            // 会员信息
            List<HangGuoUser> memberList = this.getMemberList(endTime);

            if (!CollectionUtils.isEmpty(categoryList)) {
                hangGuoDataUtil.createCommodity(categoryList, endTime);
            }
            if (!CollectionUtils.isEmpty(supplierList)) {
                hangGuoDataUtil.createHangGuoSupplier(supplierList, endTime);
            }
            if (!CollectionUtils.isEmpty(memberList)) {
                hangGuoDataUtil.createHangGuoMember(memberList, endTime);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 交易数据每小时调用一次
     */
    //@Scheduled(cron = "0 0 */1 * * ?")
    public void getTradeData() {
        try {
            Date endTime = this.registerBillMapper.selectCurrentTime();
            // 商品信息
            List<HangGuoTrade> tradeList = this.getTradeList(endTime);
            if (!CollectionUtils.isEmpty(tradeList)) {
                hangGuoDataUtil.createTrade(tradeList, endTime);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<HangGuoTrade> getTradeList(Date endTime) {
        List<HangGuoTrade> tradeList = new ArrayList<>();
        String tradeStr = getTradeDataStr();

        if (StringUtils.isNotBlank(tradeStr)) {
            tradeList = JSON.parseArray(tradeStr, HangGuoTrade.class);
            logger.info("-----===>获取交易数量总数为：" + tradeList.size());

            // 分批上报
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // 分批数
            Integer part = tradeList.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? tradeList.size() : (i + 1) * batchSize;
                List<HangGuoTrade> partBills = tradeList.subList(i * batchSize, endPos);
                String partStr = JSON.toJSONString(partBills);
                addSource(partStr, ThirdSourceTypeEnum.TRADE.getCode(), ThirdSourceTypeEnum.TRADE.getDesc(), endTime, MarketIdEnum.FRUIT_TYPE.getCode());
            }
            handleTradeParam(tradeList);
        } else {
            addSource(tradeStr, ThirdSourceTypeEnum.TRADE.getCode(), ThirdSourceTypeEnum.TRADE.getDesc(), endTime, MarketIdEnum.FRUIT_TYPE.getCode());
        }
        logger.info("====>end getTradeList time:" + (DateUtils.getCurrentDate().getTime() - endTime.getTime()));
        return tradeList;
    }

    /**
     * 特殊处理参数
     * @param tradeList
     */
    private void handleTradeParam(List<HangGuoTrade> tradeList) {
    }


    private List<HangGuoUser> getMemberList(Date endTime) {
        List<HangGuoUser> hangGuoUserList = new ArrayList<>();
        Date startdate = DateUtils.getCurrentDate();
        String userStr = testUser();

        if (StringUtils.isNotBlank(userStr)) {
            hangGuoUserList = JSON.parseArray(userStr, HangGuoUser.class);
            logger.info("-----===>" + hangGuoUserList.size());

            // 分批插入缓存
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // 分批数
            Integer part = hangGuoUserList.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? hangGuoUserList.size() : (i + 1) * batchSize;
                List<HangGuoUser> partBills = hangGuoUserList.subList(i * batchSize, endPos);
                String partStr = JSON.toJSONString(partBills);
                addSource(partStr, ThirdSourceTypeEnum.MEMBER.getCode(), ThirdSourceTypeEnum.MEMBER.getDesc(), startdate, MarketIdEnum.FRUIT_TYPE.getCode());
            }
            handleUserParam(hangGuoUserList);

        } else {
            addSource(userStr, ThirdSourceTypeEnum.MEMBER.getCode(), ThirdSourceTypeEnum.MEMBER.getDesc(), startdate, MarketIdEnum.FRUIT_TYPE.getCode());
        }
        logger.info("====>end getMemberList time:" + (DateUtils.getCurrentDate().getTime() - startdate.getTime()));
        return hangGuoUserList;
    }

    /**
     * 处理非空项和参数转换
     *
     * @param hangGuoUserList
     */
    private void handleUserParam(List<HangGuoUser> hangGuoUserList) {
        String active = "激活";
        String nullStr = "空";
        StreamEx.of(hangGuoUserList).nonNull().forEach(u -> {
            if (StringUtils.isNotBlank(u.getStatus()) && active.equals(u.getStatus())) {
                u.setStatusCode(EnabledStateEnum.ENABLED.getCode());
            } else {
                u.setStatusCode(EnabledStateEnum.DISABLED.getCode());
            }
            if (StringUtils.isNotBlank(u.getEffectiveDate())) {
                //2010022700:00:00
                u.setEffectiveDateTime(DateUtils.dateStr2Date(u.getEffectiveDate(), "yyyyMMddHH:mm:ss"));
            }
            if (StringUtils.isBlank(u.getPhoneNumber())) {
                u.setPhoneNumber(nullStr);
            }
        });
    }

    /**
     * 新增来源缓存记录
     *
     * @param dataStr
     * @param code
     * @param desc
     * @param startdate
     * @param marketId
     */
    private void addSource(String dataStr, Integer code, String desc, Date startdate, Integer marketId) {
        OperatorUser optUser = new OperatorUser(-1L, "auto");
        ThirdPartySourceData sourceData = new ThirdPartySourceData();
        sourceData.setType(code);
        sourceData.setName(desc);
        sourceData.setData(dataStr);
        sourceData.setCreated(startdate);
        sourceData.setModified(startdate);
        sourceData.setMarketId(marketId == null ? null : Long.valueOf(marketId));
        sourceData.setOperatorId(optUser.getId());
        sourceData.setOperatorName(optUser.getName());
        hangGuoDataService.insertThirdPartySourceData(sourceData);
    }


    private List<HangGuoUser> getSupplierList(Date endTime) {
        List<HangGuoUser> hangGuoUserList = new ArrayList<>();
        Date startdate = DateUtils.getCurrentDate();
        String userStr = testSupplierUser();

        if (StringUtils.isNotBlank(userStr)) {
            hangGuoUserList = JSON.parseArray(userStr, HangGuoUser.class);
            logger.info("-----===>" + hangGuoUserList.size());

            // 分批插入缓存
            Integer batchSize = (insertBatchSize == null || insertBatchSize == 0) ? 1000 : insertBatchSize;
            // 分批数
            Integer part = hangGuoUserList.size() / batchSize;
            for (int i = 0; i <= part; i++) {
                Integer endPos = i == part ? hangGuoUserList.size() : (i + 1) * batchSize;
                List<HangGuoUser> partBills = hangGuoUserList.subList(i * batchSize, endPos);
                String partStr = JSON.toJSONString(partBills);
                addSource(partStr, ThirdSourceTypeEnum.SUPPLIER.getCode(), ThirdSourceTypeEnum.SUPPLIER.getDesc(), startdate, MarketIdEnum.FRUIT_TYPE.getCode());
            }
            handleUserParam(hangGuoUserList);
        } else {
            addSource(userStr, ThirdSourceTypeEnum.SUPPLIER.getCode(), ThirdSourceTypeEnum.SUPPLIER.getDesc(), startdate, MarketIdEnum.FRUIT_TYPE.getCode());
        }
        logger.info("====>end getMemberList time:" + (DateUtils.getCurrentDate().getTime() - startdate.getTime()));
        return hangGuoUserList;
    }

    private String testSupplierUser() {
        String resule = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String notice = restTemplate.getForObject("http://172.18.188.136:8090/mq/interface_test/suppler"
                    , String.class);
            if (StringUtils.isNotBlank(notice)) {
                resule = notice;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resule;
    }

    private List<HangGuoCommodity> getGoodsCategory(Date endTime) {
        List<HangGuoCommodity> commodityList = testCommodity();
        return commodityList;
    }

    private List<HangGuoCommodity> testCommodity() {
        ThirdPartySourceData que = new ThirdPartySourceData();
        que.setName("杭果商品");
        que.setType(1);
        ThirdPartySourceData commodityData = hangGuoDataService.getThirdPartySourceData(que);
        if (null == commodityData) {
            return null;
        }
        String data = commodityData.getData();
        List<HangGuoCommodity> commodityList = JSON.parseArray(data, HangGuoCommodity.class);
        return commodityList;
    }

    private String testUser() {
        String resule = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            //String notice = restTemplate.getForObject("http://172.18.188.136:8090/mq/interface_test/member", String.class);
            String notice = restTemplate.getForObject("http://221.227.109.45:9527/memberdata", String.class);
            if (StringUtils.isNotBlank(notice)) {
                resule = notice;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resule;
    }

    private String getTradeDataStr() {
        String resule = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String notice = restTemplate.getForObject("http://172.18.188.136:8090/mq/interface_test/trade"
                    , String.class);
            if (StringUtils.isNotBlank(notice)) {
                resule = notice;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resule;
    }

    public static void main(String[] args) throws Exception {
        //postTest("https://127.0.0.1:8443", "supplierdata");
        /*String url = "http://localhost:8090/ims/service/receive.do?type=0";
        String xmlString = "<?xml version=\"1.0\" encoding=\"gbk\"?>";
        post(url, xmlString);*/

        /*File file = new File("C:\\Users\\asa.lee\\Desktop\\hangguo\\trade_test.json");
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(fos);
        pw.write(creatTradeJson());
        pw.flush();
        fos.close();*/
    }

    public static String creatTradeJson() {
        List<HangGuoTrade> tradeList = new ArrayList(16);
        for (int i = 1; i < 12000; i++) {
            HangGuoTrade trade = new HangGuoTrade();
            trade.setOrderDate("2020-09-25T05:17:27");
            trade.setSupplierNo("000502");
            trade.setSupplierName("宋敏");
            trade.setBatchNo("2009031655");
            trade.setItemNumber("21103");
            trade.setItemName("葡萄(巨峰)");
            trade.setUnit("辽宁统");
            trade.setOriginNo("016");
            trade.setOriginName("辽宁");
            trade.setPositionNo("016");
            trade.setPositionName("辽宁");
            trade.setPrice(new BigDecimal(5000));
            trade.setPackageNumber(12);
            trade.setNumber(12);
            trade.setAmount(new BigDecimal(600000));
            trade.setWeight("28");
            trade.setTradeNo("19404990"+i);
            trade.setPosNo("194");
            trade.setPayWay("会员卡");
            trade.setMemberNo("000501");
            trade.setMemberName("赵兰芳0");
            trade.setTotalAmount(new BigDecimal(600000));
            trade.setOperator("孟建丽");
            trade.setPayer("孟建丽");
            trade.setPayNo("82653791"+i);
            tradeList.add(trade);
        }
        return JSON.toJSONString(tradeList);
    }

}
