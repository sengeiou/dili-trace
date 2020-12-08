package com.dili.trace.controller;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.dto.DetectRequestDto;
import com.dili.trace.service.DetectRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * 跳转到DetectRequest页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到DetectRequest页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("createdStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
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
    public @ResponseBody String listPage(DetectRequestDto detectRequestDto) throws Exception {
        EasyuiPageOutput out = this.detectRequestService.listEasyuiPageByExample(detectRequestDto);
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
                        @RequestParam(name = "designatedId", required = true) Long designatedId,
                        @RequestParam(name = "designatedName", required = true) String designatedName) {
        try {
            this.detectRequestService.assignDetector(id, designatedId, designatedName);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }
}