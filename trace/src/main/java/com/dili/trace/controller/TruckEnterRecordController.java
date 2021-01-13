package com.dili.trace.controller;

import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.assets.sdk.dto.CarTypePublicDTO;
import com.dili.trace.domain.PurchaseIntentionRecord;
import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.dto.query.TruckEnterRecordQueryDto;
import com.dili.trace.rpc.service.CarTypeRpcService;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.TruckEnterRecordService;
import com.dili.trace.service.UapRpcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
    CarTypeRpcService carTypeRpcService;
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
        List<CarTypeDTO> carTypeList = this.carTypeRpcService.listCarType();
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
        queryInput.setMarketId(this.uapRpcService.getCurrentFirm().get().getId());
        return this.truckEnterRecordService.listEasyuiPageByExample(queryInput, true).toString();

    }

    /**
     * 司机报备查看页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @RequestParam(required = true, name = "id") Long id) {
        TruckEnterRecord truckEnterRecord = truckEnterRecordService.get(id);
        modelMap.put("truckEnterRecord",truckEnterRecord);
        return "truckEnterRecord/view";
    }
}
