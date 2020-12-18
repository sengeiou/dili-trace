package com.dili.trace.controller;

import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.assets.sdk.dto.CarTypePublicDTO;
import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.dto.query.TruckEnterRecordQueryDto;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.TruckEnterRecordService;
import com.dili.trace.service.UapRpcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 司机进门信息
 */
@Controller
@RequestMapping("/truckEnterRecord")
public class TruckEnterRecordController {
    @Autowired
    TruckEnterRecordService truckEnterRecordService;
    @Autowired
    AssetsRpcService assetsRpcService;
    @Autowired
    UapRpcService uapRpcService;

    /**
     * 跳转到TruckEnterRecord页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到TruckEnterRecord页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        List<CarTypeDTO> carTypeList = this.assetsRpcService.listCarType(new CarTypePublicDTO(), uapRpcService.getCurrentFirm().get().getId());
        modelMap.put("carTypeList", carTypeList);
        return "truckEnterRecord/index";
    }

    /**
     * 分页查询TruckEnterRecord
     *
     * @param queryInput
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询TruckEnterRecord", notes = "分页查询TruckEnterRecord，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "TruckEnterRecord", paramType = "form", value = "TruckEnterRecord的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@RequestBody TruckEnterRecordQueryDto queryInput) throws Exception {
        return this.truckEnterRecordService.listEasyuiPageByExample(queryInput, true).toString();

    }
}
