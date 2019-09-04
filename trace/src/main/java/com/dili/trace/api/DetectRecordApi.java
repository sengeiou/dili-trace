package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.config.DefaultConfiguration;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/detect")
@Api(value ="/api/detect", description = "检测任务相关接口")
@InterceptConfiguration(loginRequired=false)
public class DetectRecordApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(DetectRecordApi.class);
    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private DetectRecordService detectRecordService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    /**
     * 保存检查单
     * @param detectRecord
     * @return
     */
    @ApiOperation("上传检测记录")
    @RequestMapping(value = "/saveRecord",method = RequestMethod.POST)
    public BaseOutput<Boolean> saveDetectRecord(DetectRecordParam detectRecord){
        LOGGER.info(defaultConfiguration.getEnTag()+"=sys.en.tag]保存检查单:"+ JSON.toJSONString(detectRecord));
        
        if(!StringUtils.trimToEmpty(defaultConfiguration.getEnTag()).equals(detectRecord.getTag())) {
            LOGGER.error("上传检测任务结果失败:签名出错");
            return BaseOutput.failure("签名出错");
        }
        
        if(StringUtils.isBlank(detectRecord.getRegisterBillCode())){
            LOGGER.error("上传检测任务结果失败无单号");
            return BaseOutput.failure("没有对应的登记单");
        }
        if(StringUtils.isBlank(detectRecord.getDetectOperator())){
            LOGGER.error("上传检测任务结果失败无检测人员");
            return BaseOutput.failure("没有对应的检测人员");
        }
        if(detectRecord.getDetectState()==null){
            LOGGER.error("上传检测任务结果失败无检测状态");
            return BaseOutput.failure("没有对应的检测状态");
        }else if(detectRecord.getDetectState()>2 || detectRecord.getDetectState()<1){
            LOGGER.error("上传检测任务结果失败无,检测状态异常"+detectRecord.getDetectState());
            return BaseOutput.failure("没有对应的检测状态");
        }
        if(detectRecord.getDetectTime()==null){
            LOGGER.error("上传检测任务结果失败无检测时间");
            return BaseOutput.failure("没有对应的检测时间");
        }
        if(StringUtils.isBlank(detectRecord.getPdResult())){
            LOGGER.error("上传检测任务结果失败无检测值");
            return BaseOutput.failure("没有对应的检测值");
        }
        RegisterBill registerBill = registerBillService.findByCode(detectRecord.getRegisterBillCode());
        if(registerBill== null){
            LOGGER.error("上传检测任务结果失败该单号无登记单");
            return BaseOutput.failure("没有对应的登记单");
        }
        if(RegisterBillStateEnum.ALREADY_CHECK.getCode().equals(registerBill.getState())) {
        	LOGGER.error("上传检测任务结果失败,该单号已完成检测");
            return BaseOutput.failure("已完成检测");
        }
        if(!registerBill.getExeMachineNo().equals(detectRecord.getExeMachineNo())){
            LOGGER.error("上传检测任务结果失败，该仪器没有获取该登记单");
            return BaseOutput.failure("该仪器无权操作该单据");
        }
        saveRecordAndUpdateBill(detectRecord,registerBill);
        return BaseOutput.success().setData(true);
    }

    @Transactional(rollbackFor=Exception.class)
    private void saveRecordAndUpdateBill(DetectRecordParam detectRecord,RegisterBill registerBill) {

        if(registerBill.getLatestDetectRecordId()!=null){
            //复检
            ///1.第一次送检 2：复检 状态 1.合格 2.不合格
            detectRecord.setDetectType(2);
            //'默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
            registerBill.setDetectState(detectRecord.getDetectState()+2);
        }else {
            //第一次检测
            detectRecord.setDetectType(1);
            registerBill.setDetectState(detectRecord.getDetectState());
        }
        
        detectRecordService.saveDetectRecord(detectRecord);
        registerBill.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
        registerBill.setLatestDetectRecordId(detectRecord.getId());
        registerBill.setLatestDetectTime(detectRecord.getDetectTime());
        registerBill.setLatestPdResult(detectRecord.getPdResult());
        registerBill.setLatestDetectOperator(detectRecord.getDetectOperator());
        registerBillService.updateSelective(registerBill);
    }


    /**
     * 获取检查任务
     * @param exeMachineNo
     * @return
     */
    @ApiOperation("获取检测任务")
    @RequestMapping(value = "/getDetectTask/{exeMachineNo}/{taskCount}/{tag}",method = RequestMethod.POST)
    public BaseOutput<List<RegisterBill>> getDetectTask(@PathVariable String tag, @PathVariable String exeMachineNo, @PathVariable Integer taskCount){
    	
        if(!StringUtils.trimToEmpty(defaultConfiguration.getEnTag()).equals(tag)) {
            LOGGER.error("上传检测任务结果失败:签名出错");
            return BaseOutput.failure("签名出错");
        }
        
        if(taskCount==null||taskCount<=0) {
        	  return BaseOutput.failure("限制数量出错");
        }
        
        TaskGetParam taskGetParam = new TaskGetParam();
        taskGetParam.setExeMachineNo(exeMachineNo);
        if(taskCount>95){
            taskCount=95;
        }
        taskGetParam.setPageSize(taskCount);
        LOGGER.info(defaultConfiguration.getEnTag()+"=sys.en.tag]获取检查任务:" + JSON.toJSONString(taskGetParam)+tag);
        List<RegisterBill> registerBills=registerBillService.findByExeMachineNo(taskGetParam.getExeMachineNo(), taskGetParam.getPageSize());
        return BaseOutput.success().setData(registerBills);
    }


    @ApiOperation("随机新增10条RegisterBill")
    @RequestMapping(value = "/insertTest", method = RequestMethod.GET)
    public @ResponseBody BaseOutput insertTest() {
        List<RegisterBill> registerBills = getTestRegisterBills();
        LOGGER.info("进行测试登记单数据-----------:" + registerBills.size());
        for (RegisterBill registerBill : registerBills) {
            BaseOutput r = registerBillService.createRegisterBill(registerBill);
            if(!r.isSuccess()){
                return  r;
            }
           registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
            registerBillService.update(registerBill);
        }
        LOGGER.info("进行测试登记单数据----end-------:" + registerBills.size());
        return BaseOutput.success("新增成功").setData(registerBills);
    }
    private List<RegisterBill> getTestRegisterBills(){
        String[] name = {"张三","李四","王五","张亿","Jick","Rose","Tom","Good","蒋介","兰芝"};
        String[] product={"苹果","梨","黄瓜","芹菜","一级蔬菜","萝卜","Fish","火龙果","木瓜","火龙果"};
        String[] city={"成都","北京","哈达","贵阳","兰州","四川成都","云南","香港","杭州","天津"};
        String[] plate={"川A07194","吉J96781","黑MR4039","辽C73037","川B07194","川C07194","川AB7194","川AB7111","川AC7111","川AB71e1"};
        String[] tallyAreaNo={"ta1234","ta1235","ta1236","ta12374","ta1238","ta1239","ta1231","ta1232","ta12355","ta12340"};
        List<RegisterBill> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            RegisterBill registerBill = DTOUtils.newDTO(RegisterBill.class);
            registerBill.setName(name[i]);
            registerBill.setPlate(plate[i]);
            registerBill.setProductName(product[i]);
            registerBill.setProductId(100L + i);
            registerBill.setOriginName(city[i]);
            registerBill.setOriginId(200L+i);
            registerBill.setOperatorName("系统测试");
            registerBill.setWeight(i + 698);
            registerBill.setState(4);
            registerBill.setIdCardNo("51102319890605399"+i);
            registerBill.setAddr(city[i]+"地址"+i);
            if(i%2==0){
                registerBill.setTallyAreaNo(tallyAreaNo[i]);
                registerBill.setRegisterSource(RegisterSourceEnum.TALLY_AREA.getCode());
            }else {
                registerBill.setTradeAccount("100020" + i);
                registerBill.setRegisterSource(RegisterSourceEnum.TRADE_AREA.getCode());
            }
            list.add(registerBill);
        }
        return list;
    }
}
