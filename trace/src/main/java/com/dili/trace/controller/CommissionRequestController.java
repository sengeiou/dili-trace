package com.dili.trace.controller;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.dto.DetectRequestWithBillDto;
import com.dili.trace.service.DetectRequestService;
import com.dili.trace.service.UserRpcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 场外委托检测管理
 */
@Api("/commissionDetectRequest")
@Controller
@RequestMapping("/commissionDetectRequest")
public class CommissionRequestController {
    private static final Logger logger = LoggerFactory.getLogger(CommissionRequestController.class);

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
    @ApiOperation("跳转到commissionDetectRequest页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        return "commissionDetectRequest/index";
    }

    /**
     * 分页查询DetectRequest
     *
     * @param detectRequestDto 查询条件
     * @return DetectRequest 列表
     * @throws Exception
     */
    @ApiOperation(value = "分页查询commissionDetectRequest", notes = "分页查询commissionDetectRequest，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commissionDetectRequest", paramType = "form", value = "commissionDetectRequest的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@RequestBody DetectRequestWithBillDto detectRequestDto) throws Exception {
        // detectRequestDto.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput out = this.detectRequestService.listBasePageByExample(detectRequestDto);
        return out.toString();
    }

    /**
     * 接单页面
     *
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/confirm.html", method = RequestMethod.GET)
    public String assign(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        modelMap.put("detectRequest", this.detectRequestService.get(id));
        return "commissionDetectRequest/confirm";
    }
}