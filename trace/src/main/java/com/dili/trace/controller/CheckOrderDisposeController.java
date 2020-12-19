package com.dili.trace.controller;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.CheckOrderDispose;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.dto.CheckOrderDisposeDto;
import com.dili.trace.enums.CheckDisposeTypeEnum;
import com.dili.trace.enums.ImageCertBillTypeEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.dili.trace.service.CheckOrderDisposeService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.util.MarketUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/11/3
 */
@Api("/checkOrderDispose")
@Controller
@RequestMapping("/checkOrderDispose")
public class CheckOrderDisposeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckOrderDisposeController.class);
    @Autowired
    private CheckOrderDisposeService checkOrderDisposeService;

    @Autowired
    private ImageCertService imageCertService;

    /**
     * 跳转到不合格处置登记页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到不合格处置登记页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("disposeDateStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("disposeDateEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        return "checkOrderDispose/index";
    }

    /**
     * 跳转到edit页面
     * @param modelMap
     * @param id
     * @return
     */
    @ApiOperation("跳转到edit页面")
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        CheckOrderDisposeDto checkOrderDispose = new CheckOrderDisposeDto();
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("disposeDateString", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        modelMap.put("checkOrderDispose",checkOrderDispose);
        if (id != null) {
            CheckOrderDispose checkOrderDisposeBase = checkOrderDisposeService.get(id);
            BeanUtils.copyProperties(checkOrderDisposeBase,checkOrderDispose);
            List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(id, ImageCertBillTypeEnum.DISPOSE_TYPE.getCode());
            if(CollectionUtils.isNotEmpty(imageCerts) && imageCerts.size()==1){
                checkOrderDispose.setUrl(imageCerts.get(0).getUid());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String disposeDateString = dateFormat.format(checkOrderDispose.getDisposeDate());
            modelMap.put("disposeDateString",disposeDateString);
            modelMap.put("checkOrderDispose", checkOrderDispose);
        }
        modelMap.put("disposeTypeMap", Stream.of(CheckDisposeTypeEnum.values())
                .collect(Collectors.toMap(CheckDisposeTypeEnum::getCode, CheckDisposeTypeEnum::getName)));
        return "checkOrderDispose/edit";
    }

    /**
     * 分页查询不合格检测单
     * @param checkOrderDispose
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询不合格检测单", notes = "分页查询不合格检测单，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CheckOrderDispose", paramType = "form", value = "CheckOrderDispose的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(CheckOrderDisposeDto checkOrderDispose) throws Exception {
        checkOrderDispose.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput out = this.checkOrderDisposeService.selectForEasyuiPage(checkOrderDispose, true);
        return out.toString();
    }

    /**
     * 新增不合格处置单
     * @param checkOrderDispose
     * @return
     */
    @ApiOperation("新增不合格处置单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CheckOrderDispose", paramType = "form", value = "CheckOrderDispose的form信息", required = true, dataType = "string") })
    @RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<Long> insert(@RequestBody CheckOrderDisposeDto checkOrderDispose) {
        try {
            checkOrderDispose.setMarketId(MarketUtil.returnMarket());
            checkOrderDisposeService.insertSelective(checkOrderDispose);
            if(checkOrderDispose.getUrl() != null) {
                ImageCert imageCert = new ImageCert();
                imageCert.setBillId(checkOrderDispose.getId());
                imageCert.setUid(checkOrderDispose.getUrl());
                imageCert.setCertType(ImageCertTypeEnum.DETECT_REPORT.getCode());
                imageCert.setBillType(ImageCertBillTypeEnum.DISPOSE_TYPE.getCode());
                imageCertService.insertSelective(imageCert);
            }
            return BaseOutput.success("新增不合格处置单成功").setData(checkOrderDispose.getId());
        } catch (TraceBizException e) {
            LOGGER.error("新增不合格处置单失败", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("新增不合格处置单失败", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 修改不合格处置单
     * @param checkOrderDispose
     * @return
     */
    @ApiOperation("修改不合格处置单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CheckOrderDispose", paramType = "form", value = "CheckOrderDispose的form信息", required = true, dataType = "string") })
    @RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody BaseOutput update(@RequestBody CheckOrderDisposeDto checkOrderDispose) {
        try {
            checkOrderDisposeService.updateSelective(checkOrderDispose);
            if(checkOrderDispose.getUrl()!=null) {
                List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(checkOrderDispose.getId(), ImageCertBillTypeEnum.DISPOSE_TYPE.getCode());
                if (CollectionUtils.isNotEmpty(imageCerts) && imageCerts.size() == 1) {
                    imageCerts.get(0).setUid(checkOrderDispose.getUrl());
                    imageCertService.updateSelective(imageCerts.get(0));
                }
            }
            return BaseOutput.success("修改检测单成功");
        } catch (TraceBizException e) {
            LOGGER.error("修改检测单失败", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改检测单失败", e);
            return BaseOutput.failure(e.getMessage());
        }

    }
}
