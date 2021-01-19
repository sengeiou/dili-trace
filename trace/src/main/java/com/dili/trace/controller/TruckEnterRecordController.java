package com.dili.trace.controller;

import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.assets.sdk.dto.CarTypePublicDTO;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.trace.domain.PurchaseIntentionRecord;
import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.dto.query.TruckEnterRecordQueryDto;
import com.dili.trace.rpc.service.CarTypeRpcService;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.TruckEnterRecordService;
import com.dili.trace.service.UapRpcService;
import com.dili.trace.util.MarketUtil;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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
import java.util.List;

/**
 * 司机进门信息
 */
@Controller
@RequestMapping("/truckEnterRecord")
public class TruckEnterRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckEnterRecordController.class);
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


    /**
     * 司机报备新增 OR 修改页面
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        if (null != id) {
            TruckEnterRecord truckEnterRecord = truckEnterRecordService.get(id);
            modelMap.put("truckEnterRecord",truckEnterRecord);
        } else {
            modelMap.put("truckEnterRecord",null);
        }
        return "truckEnterRecord/edit";
    }

    /**
     * 保存数据
     *
     * @param truckEnterRecord
     * @return
     */
    @RequestMapping(value = "/save.action", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput save(@RequestBody TruckEnterRecord truckEnterRecord) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket) {
                return BaseOutput.failure("未登录或登录过期");
            }
            return null == truckEnterRecord.getId() ? truckEnterRecordService.addTruckEnterRecord(truckEnterRecord)
                    : truckEnterRecordService.updateTruckEnterRecord(truckEnterRecord);
        } catch (BusinessException e) {
            LOGGER.info("司机报备业务绑定保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("司机报备业务绑定保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        this.truckEnterRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}
