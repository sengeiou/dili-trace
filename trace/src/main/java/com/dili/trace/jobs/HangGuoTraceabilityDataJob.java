package com.dili.trace.jobs;

import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.hangguo.HangGuoCommodity;
import com.dili.trace.domain.hangguo.HangGuoTrade;
import com.dili.trace.domain.hangguo.HangGuoUser;
import com.dili.trace.service.HangGuoDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Override
    public void run(String... args) throws Exception {
        Date endTime = this.registerBillMapper.selectCurrentTime();
        List<HangGuoUser> memberList = this.getMemberList(endTime);
        List<HangGuoCommodity> goodsCategory = getGoodsCategory(endTime);
        List<HangGuoTrade> tradeList = getTradeList(endTime);
        /*if (!CollectionUtils.isEmpty(memberList)) {
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

    }

    /**
     * 商品信息、供应商信息、会员信息每六小时调用一次
     */
    //@Scheduled(cron = "0 0 */6 * * ?")
    public void getData() {
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
        List<HangGuoTrade> tradeList = testTrade();
        return tradeList;
    }

    private List<HangGuoTrade> testTrade() {
        List<HangGuoTrade> tradeList = new ArrayList<>();
        for(int i =1;i<3;i++){
            HangGuoTrade trade = new HangGuoTrade();
            trade.setOrderDate("2020-09-25T05:17:27");
            trade.setSupplierNo("000502");
            trade.setSupplierName("宋敏");
            trade.setBatchNo("2009031655");
            trade.setItemNumber("2010129");
            trade.setItemName("葡萄(巨峰)");
            trade.setUnit("辽宁统");
            trade.setOriginNo("016");
            trade.setOriginName("辽宁");
            trade.setPositionNo("016");
            trade.setPositionName("辽宁");
            trade.setPrice("47");
            trade.setPackageNumber("2");
            trade.setNumber("16");
            trade.setAmount("90");
            trade.setWeight("28");
            trade.setTradeNo("19404990");
            trade.setPosNo("194");
            trade.setPayWay("会员卡");
            trade.setMemberNo("000501");
            trade.setMemberName("赵兰芳0");
            trade.setTotalAmount("90");
            trade.setOperator("孟建丽");
            trade.setPayer("孟建丽");
            trade.setPayNo("82653791");
            tradeList.add(trade);
        }
        return tradeList;
    }

    private List<HangGuoUser> getMemberList(Date endTime) {
        List<HangGuoUser> hangGuoUserList = testUser();
        return hangGuoUserList;
    }


    private List<HangGuoUser> getSupplierList(Date endTime) {
        List<HangGuoUser> hangGuoUserList = testUser();
        return hangGuoUserList;
    }

    private List<HangGuoCommodity> getGoodsCategory(Date endTime) {
        List<HangGuoCommodity> commodityList = testCommodity();
        return commodityList;
    }

    private List<HangGuoCommodity> testCommodity() {
        List<HangGuoCommodity> commodityList = new ArrayList<>();
        HangGuoCommodity commodity = new HangGuoCommodity();
        HangGuoCommodity commodity2 = new HangGuoCommodity();
        commodity.setFirstCateg("2");
        commodity.setSecondCateg("201");
        commodity.setCategoryNumber("201012");
        commodity.setItemNumber("201012");
        commodity.setItemName("杭果苹果");
        commodity.setItemUnitName("陕西同1");
        commodity2.setFirstCateg("2");
        commodity2.setSecondCateg("201");
        commodity2.setCategoryNumber("201012");
        commodity2.setItemNumber("2010129");
        commodity2.setItemName("杭果苹果");
        commodity2.setItemUnitName("陕西同12");

        commodityList.add(commodity);
        commodityList.add(commodity2);
        return commodityList;
    }

    private List<HangGuoUser> testUser() {
        List<HangGuoUser> hangGuoUserList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HangGuoUser user = new HangGuoUser();
            user.setSupplierNo("000501");
            user.setSupplierName("赵兰芳" + i);
            user.setMemberNo("000501");
            user.setName("赵兰芳" + i);
            user.setCredentialType("1");
            user.setCredentialName("身份证");
            user.setCredentialNumber("62262877032148");
            user.setSex("囡");
            user.setLiscensNo("liscensNo");
            user.setMobileNumber("13989891773");
            user.setPhoneNumber("85681038 小");
            user.setStatus(1);
            user.setAddr("addr");
            user.setChargeRate(new BigDecimal(100));
            user.setMangerRate(new BigDecimal(101));
            user.setAssessRate(new BigDecimal(111));
            user.setApprover("apper");
            user.setSupplierType("临时客户");
            user.setIdAddr("甘肃省礼县江口乡茨");
            user.setOperateAddr("operateAddr");
            user.setEffectiveDate(new Date());
            user.setRemarkMemo("备注");
            user.setWhereis("余杭区良渚");
            user.setOperateType("个体批发");
            user.setPhoneNum("13122222222");
            user.setStatus(1);
            user.setCreditLimit("0");
            hangGuoUserList.add(user);
        }
        return hangGuoUserList;
    }

    public static void main(String[] args) {
        //postTest("https://127.0.0.1:8443", "supplierdata");

        /*String url = "http://localhost:8090/ims/service/receive.do?type=0";
        String xmlString = "<?xml version=\"1.0\" encoding=\"gbk\"?>";
        post(url, xmlString);*/
    }

    /*public static boolean post(String url, String xmlString) {
        boolean result = false;
        //创建httpclient工具对象
        HttpClient client = new HttpClient();
        //创建post请求方法
        PostMethod myPost = new PostMethod(url);
        //设置请求超时时间
        client.getHttpConnectionManager().getParams().setConnectionTimeout(300 * 1000);

        try {
            //设置请求头部类型
            myPost.setRequestHeader("Content-Type", "text/html");
            myPost.setRequestEntity(new StringRequestEntity(xmlString, "text/html", "gbk"));
            int statusCode = client.executeMethod(myPost);
            if (statusCode == HttpStatus.SC_OK) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myPost.releaseConnection();
        }
        return result;
    }*/

    /*public static String postTest(String endpoint, String method) {
        String result = null;
        try {

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint);
            // WSDL里面描述的接口名称(要调用的方法)
            call.setOperationName(method);
            // 接口方法的参数名, 参数类型,参数模式  IN(输入), OUT(输出) or INOUT(输入输出)
            call.addParameter("icSystem", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("companyFlag", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("consignOrderNo", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("orderNo", XMLType.XSD_STRING, ParameterMode.IN);
            // 设置被调用方法的返回值类型
            call.setReturnType(XMLType.XSD_STRING);
            //设置方法中参数的值
            Object[] paramValues = new Object[]{"4PL", "NJHY,NJHF", "TC201309172206", ""};
            // 给方法传递参数，并且调用方法
            result = (String) call.invoke(paramValues);
            System.out.println("result is " + result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }*/
}
