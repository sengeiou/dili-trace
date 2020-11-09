package com.dili.trace.controller;


import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.*;
import com.dili.trace.dto.CheckExcelDto;
import com.dili.trace.dto.CheckOrderDto;
import com.dili.trace.enums.CheckResultTypeEnum;
import com.dili.trace.enums.CheckTypeEnum;
import com.dili.trace.enums.ImageCertBillTypeEnum;
import com.dili.trace.excel.ExcelListener;
import com.dili.trace.service.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/10/30
 */
@Api("/checkBill")
@Controller
@RequestMapping("/checkBill")
public class CheckBillController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckBillController.class);

    @Autowired
    private CheckBillService checkBillService;

    @Autowired
    private ImageCertService imageCertService;

    @Autowired
    private CheckOrderDataService checkOrderDataService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;


    @ApiOperation("跳转到检测登记页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("checkTimeStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("checkTimeEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        return "checkBill/index";
    }

    @ApiOperation("跳转到edit页面")
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        CheckOrderDto checkOrder = new CheckOrderDto();
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("checkTimeString", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        modelMap.put("checkOrder", checkOrder);
        if (id != null) {
            CheckOrder checkOrderBase = checkBillService.get(id);
            BeanUtils.copyProperties(checkOrderBase, checkOrder);
            CheckOrderData checkOrderData = new CheckOrderData();
            checkOrderData.setCheckId(id);
            List<CheckOrderData> checkOrderDatas = checkOrderDataService.listByExample(checkOrderData);
            if (CollectionUtils.isNotEmpty(checkOrderDatas) && checkOrderDatas.size() == 1) {
                checkOrder.setNormalValue(checkOrderDatas.get(0).getNormalValue());
                checkOrder.setValue(checkOrderDatas.get(0).getValue());
                checkOrder.setProject(checkOrderDatas.get(0).getProject());
            }
            List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(id, ImageCertBillTypeEnum.INSPECTION_TYPE.getCode());
            if (CollectionUtils.isNotEmpty(imageCerts) && imageCerts.size() == 1) {
                checkOrder.setUrl(imageCerts.get(0).getUrl());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String checkTimeString = dateFormat.format(checkOrder.getCheckTime());
            modelMap.put("checkTimeString", checkTimeString);
            modelMap.put("checkOrder", checkOrder);
        }
        modelMap.put("checkTypeMap", Stream.of(CheckTypeEnum.values())
                .collect(Collectors.toMap(CheckTypeEnum::getCode, CheckTypeEnum::getName)));
        modelMap.put("checkResultMap", Stream.of(CheckResultTypeEnum.values())
                .collect(Collectors.toMap(CheckResultTypeEnum::getCode, CheckResultTypeEnum::getName)));
        return "checkBill/edit";
    }


    @ApiOperation(value = "分页查询检测单", notes = "分页查询检测单，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CheckOrder", paramType = "form", value = "CheckOrder的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(CheckOrderDto checkOrder) throws Exception {
        checkOrder.setMarketId(MarketUtil.returnMarket());
        //checkOrder.setMarketId(1L);
        EasyuiPageOutput out = this.checkBillService.selectForEasyuiPage(checkOrder, true);
        return out.toString();
    }

    @ApiOperation("新增检测单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CheckOrder", paramType = "form", value = "CheckOrder的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<Long> insert(@RequestBody CheckOrderDto checkOrder) {
        try {
            checkOrder.setMarketId(MarketUtil.returnMarket());
            checkBillService.insertSelective(checkOrder);

            checkBillService.insertOtherTable(checkOrder, checkOrder.getId());
            return BaseOutput.success("新增检测单成功").setData(checkOrder.getId());
        } catch (TraceBusinessException e) {
            LOGGER.error("新增检测单失败", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("新增检测单失败", e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation("修改检测单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CheckOrder", paramType = "form", value = "CheckOrder的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody CheckOrderDto checkOrder) {
        try {
            checkBillService.updateSelective(checkOrder);
            checkBillService.updateOtherTable(checkOrder);
            return BaseOutput.success("修改检测单成功");
        } catch (TraceBusinessException e) {
            LOGGER.error("修改检测单失败", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改检测单失败", e);
            return BaseOutput.failure(e.getMessage());
        }

    }

    /**
     * 导入Excel文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput excelImport(MultipartFile file) throws Exception {
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        ExcelTypeEnum excelType = null;
        if ("xlsx".equals(fileType)) {
            excelType = ExcelTypeEnum.XLSX;
        } else if ("xls".equals(fileType)) {
            excelType = ExcelTypeEnum.XLS;
        }
        InputStream inputStream = file.getInputStream();
        //实例化实现了AnalysisEventListener接口的类
        ExcelListener listener = new ExcelListener();
        //传入参数
        ExcelReader excelReader = new ExcelReader(inputStream, excelType, null, listener);
        //读取信息
        excelReader.read(new Sheet(1, 1, CheckExcelDto.class));
        //获取数据
        List<Object> list = listener.getDatas();
        if(CollectionUtils.isNotEmpty(list)) {
            //转换数据类型,并插入到数据库
            try {
                for (int i = 0; i < list.size(); i++) {
                    CheckExcelDto checkExcelDto = (CheckExcelDto) list.get(i);
                    if (CheckTypeEnum.QUALITATIVE.getName().equals(checkExcelDto.getCheckType())) {
                        checkExcelDto.setCheckType(String.valueOf(CheckTypeEnum.QUALITATIVE.getCode()));
                    } else if (CheckTypeEnum.QUANTITATIVE.getName().equals(checkExcelDto.getCheckType())) {
                        checkExcelDto.setCheckType(String.valueOf(CheckTypeEnum.QUANTITATIVE.getCode()));
                    }
                    if (CheckResultTypeEnum.PASS.getName().equals(checkExcelDto.getCheckResult())) {
                        checkExcelDto.setCheckResult(String.valueOf(CheckResultTypeEnum.PASS.getCode()));
                    } else if (CheckResultTypeEnum.NO_PASS.getName().equals(checkExcelDto.getCheckResult())) {
                        checkExcelDto.setCheckResult(String.valueOf(CheckResultTypeEnum.NO_PASS.getCode()));
                    }
                    User userFromCard = this.getUserFromCard(checkExcelDto.getIdCard());
                    CheckOrder checkOrder = new CheckOrder();
                    BeanUtils.copyProperties(checkExcelDto,checkOrder);
                    checkOrder.setMarketId(MarketUtil.returnMarket());
                    checkOrder.setUserId(String.valueOf(userFromCard.getId()));
                    checkOrder.setTallyAreaNos(userFromCard.getTallyAreaNos());
                    checkBillService.insertSelective(checkOrder);
                    CheckOrderData checkOrderData = new CheckOrderData();
                    checkOrderData.setProject(checkExcelDto.getProject());
                    checkOrderData.setNormalValue(checkExcelDto.getNormalValue());
                    checkOrderData.setValue(checkExcelDto.getValue());
                    checkOrderData.setCheckId(checkOrder.getId());
                    checkOrderDataService.insertSelective(checkOrderData);
                }
                return BaseOutput.success("导入检测单成功");
            } catch (TraceBusinessException e) {
                LOGGER.error("导入检测单失败", e);
                return BaseOutput.failure(e.getMessage());
            } catch (Exception e) {
                LOGGER.error("导入检测单失败", e);
                return BaseOutput.failure(e.getMessage());
            }
        }else {
            return BaseOutput.failure("导入检测单失败");
        }
    }


    @RequestMapping(value = "/getUserName.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getUserNameByCard(@RequestParam String idCard) {
        try {
            User userFromCard = this.getUserFromCard(idCard);
            if(Objects.nonNull(userFromCard)){
                return BaseOutput.successData(userFromCard);
            }
            return BaseOutput.failure("查询用户名失败");
        }catch (TraceBusinessException e){
            LOGGER.error("查询用户名失败", e);
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e) {
            LOGGER.error("查询用户名失败", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/getGoodsName.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getGoodsNameByCode(@RequestParam Long goodsCode,ModelMap modelMap) {
        try {
            Category category = categoryService.get(goodsCode);
            category.setMarketId(MarketUtil.returnMarket());
            //category.setMarketId(1L);
            if(Objects.nonNull(category)){
                return BaseOutput.successData(category.getName());
            }
            return BaseOutput.success("查询产品名称失败");
        }catch (TraceBusinessException e){
            LOGGER.error("查询产品名称失败", e);
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e) {
            LOGGER.error("查询产品名称失败", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    private User getUserFromCard(String idCard){
        User user = DTOUtils.newDTO(User.class);
        user.setCardNo(idCard);
        user.setMarketId(MarketUtil.returnMarket());
        //user.setMarketId(1L);
        List<User> users = userService.listByExample(user);
        if(CollectionUtils.isNotEmpty(users) && users.size() ==1) {
            return users.get(0);
        }
        return user;
    }


}
