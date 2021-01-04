package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.config.DefaultConfiguration;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.DetectTaskApiOutputDto;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectTaskService;
import com.dili.trace.service.SgRegisterBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/detect")
@Api(value = "/api/detect", description = "检测任务相关接口")
//@InterceptConfiguration(loginRequired = false)
@AppAccess(role = Role.NONE,url = "",subRoles = {})
public class DetectRecordApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(DetectRecordApi.class);

    @Autowired
    private DefaultConfiguration defaultConfiguration;
    @Autowired
    DetectTaskService detectTaskService;

    /**
     * 保存检查单
     *
     * @param detectRecord
     * @return
     */
    @ApiOperation("上传检测记录")
    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    public BaseOutput<Boolean> saveDetectRecord(@RequestBody DetectRecordParam detectRecord) {

        LOGGER.info(defaultConfiguration.getEnTag() + "=sys.en.tag]保存检查单:" + JSON.toJSONString(detectRecord));
        if (!StringUtils.trimToEmpty(defaultConfiguration.getEnTag()).equals(detectRecord.getTag())) {
            LOGGER.error("上传检测任务结果失败:签名出错");
            return BaseOutput.failure("签名出错");
        }

        if (StringUtils.isBlank(detectRecord.getRegisterBillCode())) {
            LOGGER.error("上传检测任务结果失败无编号");
            return BaseOutput.failure("没有对应的登记单");
        }
        if (StringUtils.isBlank(detectRecord.getDetectOperator())) {
            LOGGER.error("上传检测任务结果失败无检测人员");
            return BaseOutput.failure("没有对应的检测人员");
        }
        if (detectRecord.getDetectState() == null) {
            LOGGER.error("上传检测任务结果失败无检测状态");
            return BaseOutput.failure("没有对应的检测状态");
        } else if (detectRecord.getDetectState() > 2 || detectRecord.getDetectState() < 1) {
            LOGGER.error("上传检测任务结果失败无,检测状态异常" + detectRecord.getDetectState());
            return BaseOutput.failure("没有对应的检测状态");
        }
        if (detectRecord.getDetectTime() == null) {
            LOGGER.error("上传检测任务结果失败无检测时间");
            return BaseOutput.failure("没有对应的检测时间");
        }
        if (StringUtils.isBlank(detectRecord.getPdResult())) {
            LOGGER.error("上传检测任务结果失败无检测值");
            return BaseOutput.failure("没有对应的检测值");
        }

        return this.detectTaskService.updateDetectTask(detectRecord);
    }

    /**
     * 获取检查任务
     *
     * @param exeMachineNo
     * @return
     */
    @ApiOperation("获取检测任务")
    @RequestMapping(value = "/getDetectTask/{exeMachineNo}/{taskCount}/{tag}", method = RequestMethod.POST)
    public BaseOutput<List<DetectTaskApiOutputDto>> getDetectTask(@PathVariable String tag, @PathVariable String exeMachineNo,
                                                                  @PathVariable Integer taskCount) {

        if (!StringUtils.trimToEmpty(defaultConfiguration.getEnTag()).equals(tag)) {
            LOGGER.error("上传检测任务结果失败:签名出错");
            return BaseOutput.failure("签名出错");
        }

        if (taskCount == null || taskCount <= 0) {
            return BaseOutput.failure("限制数量出错");
        }

        TaskGetParam taskGetParam = new TaskGetParam();
        taskGetParam.setExeMachineNo(exeMachineNo);
        taskGetParam.setPageSize(taskCount > 95 ? 95 : taskCount);
        taskGetParam.setMarketId(8L);//SG marektId
        LOGGER.info("获取检查任务:[sys.en.tag={},input-tag={},input-data={}]", defaultConfiguration.getEnTag(), tag, JSON.toJSONString(taskGetParam));
        List<DetectTaskApiOutputDto> deteckTaskOutputList = this.detectTaskService.findByExeMachineNo(taskGetParam);
        return BaseOutput.success().setData(deteckTaskOutputList);
    }




}
