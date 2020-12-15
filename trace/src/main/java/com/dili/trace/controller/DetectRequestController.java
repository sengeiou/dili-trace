package com.dili.trace.controller;

import com.dili.common.annotation.DetectRequestMessageEvent;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.dto.DetectRequestWithBillDto;
import com.dili.trace.service.DetectRequestService;
import com.dili.trace.service.UserRpcService;
import com.dili.trace.util.MarketUtil;
import com.dili.uap.sdk.domain.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 检测请求业务类
 */
@Api("/detectRequest")
@Controller
@RequestMapping("/detectRequest")
public class DetectRequestController {
    private static final Logger logger = LoggerFactory.getLogger(DetectRequestController.class);

    @Autowired
    DetectRequestService detectRequestService;

    @Autowired
    UserRpcService userRpcService;

    /**
     * 跳转到DetectRequest页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到DetectRequest页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        return "detectRequest/index";
    }

    /**
     * 分页查询DetectRequest
     *
     * @param detectRequestDto 查询条件
     * @return DetectRequest 列表
     * @throws Exception
     */
    @ApiOperation(value = "分页查询DetectRequest", notes = "分页查询DetectRequest，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DetectRequest", paramType = "form", value = "DetectRequest的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@RequestBody DetectRequestWithBillDto detectRequestDto) throws Exception {
        // detectRequestDto.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput out = this.detectRequestService.listBasePageByExample(detectRequestDto);
        return out.toString();
    }

    /**
     * 指派检测员页面
     *
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/assign.html", method = RequestMethod.GET)
    public String assign(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        modelMap.put("detectRequest", this.detectRequestService.get(id));
        return "detectRequest/assign";
    }

    /**
     * 指派检测员
     * @param id 检测请求ID
     * @param designatedId 检测员ID
     * @param designatedName 检测员姓名
     * @return 指派结果
     */
    @RequestMapping(value = "/doAssignDetector.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doAssign(@RequestParam(name = "id", required = true) Long id,
                        @RequestParam(name = "designatedId", required = false) Long designatedId,
                        @RequestParam(name = "designatedName", required = false) String designatedName,
                        @RequestParam(name = "detectTime", required = false) Date detectTime) {
        try {
            this.detectRequestService.assignDetector(id, designatedId, designatedName, detectTime);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 撤销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doUndo.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doUndo(@RequestParam(name = "id", required = true) Long id) {
        try {
            this.detectRequestService.undoDetectRequest(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 根据名字查询检测员信息
     *
     * @param name
     * @return
     */
    @RequestMapping("/detector")
    @ResponseBody
    public Map<String, ?> listByName(String name) {
        Long marketId = MarketUtil.returnMarket();
        List<User> detectorUsers = userRpcService.findDetectDepartmentUsers(name, marketId).orElse(Lists.newArrayList());
        List<Map<String, Object>> list = Lists.newArrayList();
        for (User c : detectorUsers) {
            Map<String, Object> obj = Maps.newHashMap();
            obj.put("id", c.getId());
            obj.put("data", name);
            obj.put("value", c.getUserName());
            list.add(obj);
        }

        Map map = Maps.newHashMap();
        map.put("suggestions", list);
        return map;
    }

    /**
     * 查询当前controller可用事件
     *
     * @param billIdList
     * @return
     */
    @RequestMapping("/queryEvents.action")
    @ResponseBody
    public List<String> queryEvents(@RequestBody List<Long> billIdList) {
        List<DetectRequestMessageEvent> list= Lists.newArrayList();
        if (billIdList == null || billIdList.size()==0) {
            return StreamEx.of(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }
        // 只要有记录，可以导出和查看详情
        list.add(DetectRequestMessageEvent.export);
        list.add(DetectRequestMessageEvent.detail);
        if(billIdList.size()==1){
            return StreamEx.of(this.detectRequestService.queryEvents(billIdList.get(0))).append(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }else{
            return StreamEx.of(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }
    }
}