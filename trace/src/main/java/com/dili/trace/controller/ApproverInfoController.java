package com.dili.trace.controller;

import com.dili.trace.util.MarketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.ApproverInfo;
import com.dili.trace.dto.ApproverInfoQueryDto;
import com.dili.trace.service.ApproverInfoService;
import com.dili.trace.service.Base64SignatureService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/approverInfo")
@Controller
@RequestMapping("/approverInfo")
public class ApproverInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApproverInfoController.class);

    @Autowired
    ApproverInfoService approverInfoService;
    @Autowired
    Base64SignatureService base64SignatureService;

    /**
     * 跳转到ApproverInfo页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到ApproverInfo列表页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "approverInfo/index";
    }

    /**
     * 跳转到ApproverInfo页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到ApproverInfo修改页面")
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        if (id != null) {
            ApproverInfo item = this.approverInfoService.get(id);
            String base64Signature = this.base64SignatureService.findBase64SignatureByApproverInfoId(item.getId());
            item.setSignBase64(base64Signature);
            modelMap.put("item", item);
        }
        return "approverInfo/edit";
    }

    /**
     * 分页查询ApproverInfo
     *
     * @param approverInfo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询ApproverInfo", notes = "分页查询ApproverInfo，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ApproverInfo", paramType = "form", value = "ApproverInfo的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(ApproverInfoQueryDto approverInfo) throws Exception {
        EasyuiPageOutput out = this.approverInfoService.listEasyuiPageByExample(approverInfo, true);
        return out.toString();
    }

    /**
     * 查询ApproverInfo签名
     *
     * @param input
     * @return
     */
    @ApiOperation("查询ApproverInfo签名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "ApproverInfo的主键", required = true, dataType = "long")})
    @RequestMapping(value = "/findBase64Sign.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput findBase64Sign(ApproverInfo input) {
        String base64Image = this.base64SignatureService.findBase64SignatureByApproverInfoId(input.getId());
        return BaseOutput.success().setData(base64Image);
    }


    /**
     * 新增ApproverInfo
     *
     * @param approverInfo
     * @return
     */
    @ApiOperation("新增ApproverInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ApproverInfo", paramType = "form", value = "ApproverInfo的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Long> insert(@RequestBody ApproverInfo approverInfo) {
        try {
            approverInfo.setUserId(0L);
            approverInfo.setMarketId(MarketUtil.returnMarket());
            this.approverInfoService.insertApproverInfo(approverInfo);
            return BaseOutput.success("新增成功").setData(approverInfo.getId());
        } catch (TraceBizException e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 修改ApproverInfo
     *
     * @param approverInfo
     * @return
     */
    @ApiOperation("修改ApproverInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ApproverInfo", paramType = "form", value = "ApproverInfo的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(ApproverInfo approverInfo) {
        try {
            this.approverInfoService.updateApproverInfo(approverInfo);
            return BaseOutput.success("修改成功").setData(approverInfo.getId());
        } catch (TraceBizException e) {
            LOGGER.error("修改用户", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用户", e);
            return BaseOutput.failure(e.getMessage());
        }

    }

    /**
     * 删除ApproverInfo
     *
     * @param id
     * @return
     */
    @ApiOperation("删除ApproverInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "ApproverInfo的主键", required = true, dataType = "long")})
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        this.approverInfoService.delete(id);
        return BaseOutput.success("修改成功");
    }

    /**
     * 跳转到ApproverInfo页面
     *
     * @param modelMap
     * @param id
     * @return
     */
    @ApiOperation("跳转到ApproverInfo页面")
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        ApproverInfo item = this.approverInfoService.get(id);
        String base64Signature = this.base64SignatureService.findBase64SignatureByApproverInfoId(item.getId());

        item.setSignBase64(base64Signature);
        modelMap.put("item", item);

        return "approverInfo/view";
    }
}