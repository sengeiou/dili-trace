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
        if (!CollectionUtils.isEmpty(memberList)) {
            hangGuoDataUtil.createHangGuoMember(memberList, endTime);
        }
    }

    /**
     * 商品信息、供应商信息、会员信息每六小时调用一次
     */
    //@Scheduled(cron = "0 0 */6 * * ?")
    public void pushData() {
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
    public void pushTradeData() {
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
        List<HangGuoTrade> tradeList= new ArrayList<>();
        return tradeList;
    }

    private List<HangGuoUser> getMemberList(Date endTime) {
        List<HangGuoUser> hangGuoUserList = new ArrayList<>();
        for(int i=0;i<4;i++){
            HangGuoUser user = new HangGuoUser();
            user.setSupplierNo("000501");
            user.setSupplierName("赵兰芳"+i);
            user.setMemberNo("000501");
            user.setName("赵兰芳"+i);
            user.setCredentialType("1");
            user.setCredentialName("身份证");
            user.setCredentialNumber("62262877032148");
            user.setSex("囡");
            user.setLiscensNo("liscensNo");
            user.setMobileNumber("13989891773");
            user.setPhoneNumber("85681038 小");
            user.setStatus(1);
            user.setAddr("addr");
            user.setChargeRate("100");
            user.setMangerRate("101");
            user.setAssessRate("102");
            user.setApprover("apper");
            user.setSupplierType("临时客户");
            user.setIdAddr("甘肃省礼县江口乡茨");
            user.setOperateAddr("operateAddr");
            user.setEffectiveDate(new Date());
            user.setRemarkMemo("备注");
            hangGuoUserList.add(user);
        }
        return hangGuoUserList;
    }

    private List<HangGuoUser> getSupplierList(Date endTime) {
        return null;
    }

    private List<HangGuoCommodity> getGoodsCategory(Date endTime) {
        return null;
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
