package com.dili.trace.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.alibaba.excel.EasyExcel;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.VocationTypeEnum;
import com.dili.trace.excel.ExcelUserData;
import com.dili.trace.excel.ExcelUserDataListener;
import com.dili.trace.service.CategoryService;
import com.dili.trace.util.ChineseStringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

@Controller
@RequestMapping("/userUpload")
public class UserUploadController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation("跳转到updateUser页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String updateUser(ModelMap modelMap, Long id) {
        return "userUpload/index";
    }

    @RequestMapping(value = "/checkData.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput checkData(@RequestBody ExcelUserData input) {
        try {
            ExcelUserData data = this.feedData(input);
            return BaseOutput.success().setData(data);
        } catch (Exception e) {
            return BaseOutput.failure("Excel解析出错");
        }
    }

    // @RequestMapping(value = "/parseExcel.action", method = RequestMethod.POST)
    // @ResponseBody
    // public BaseOutput parseExcel(@RequestParam(name = "excel", required = true)
    // MultipartFile excel) {
    // try {
    // InputStream is = excel.getInputStream();
    // List<ExcelUserData> data = this.parse(is);
    // return BaseOutput.success().setData(data);
    // } catch (IOException e) {
    // return BaseOutput.failure("Excel解析出错");
    // }
    // }

    // private List<ExcelUserData> parse(InputStream is) {
    //     ExcelUserDataListener userExcelDataListener = new ExcelUserDataListener();
    //     EasyExcel.read(is, ExcelUserData.class, userExcelDataListener).sheet().doRead();
    //     List<ExcelUserData> data = userExcelDataListener.getExcelUserDatas();
    //     return StreamEx.of(data).map(eu -> {
    //         return this.feedData(eu);
    //     }).toList();
    // }

    private ExcelUserData feedData(ExcelUserData eu) {

        this.findByName(eu.getCategoryName()).ifPresent(category -> {
            eu.setCategoryId(category.getId());
        });
        this.findPreserveTypeByName(eu.getPreserveTypeName()).ifPresent(pt -> {
            eu.setPreserveType(pt.getCode());
        });
        this.findVocationTypeByName(eu.getVocationTypeName()).ifPresent(vt -> {
            eu.setVocationType(vt.getCode());
        });
        String tallyAreaNo=StringUtils.trimToEmpty(eu.getTallyAreaNo());
        tallyAreaNo=ChineseStringUtil.cToe(tallyAreaNo);
        tallyAreaNo=ChineseStringUtil.full2Half(tallyAreaNo);

        eu.setTallyAreaNo(tallyAreaNo);
        return eu;

    }

    private Optional<PreserveTypeEnum> findPreserveTypeByName(String preserveTypeName) {
        if (StringUtils.isBlank(preserveTypeName)) {
            return Optional.empty();
        }
        return StreamEx.of(PreserveTypeEnum.values()).filterBy(PreserveTypeEnum::getName, preserveTypeName.trim())
                .findFirst();
    }

    private Optional<VocationTypeEnum> findVocationTypeByName(String vocationTypeName) {
        if (StringUtils.isBlank(vocationTypeName)) {
            return Optional.empty();
        }
        return StreamEx.of(VocationTypeEnum.values()).filterBy(VocationTypeEnum::getName, vocationTypeName.trim())
                .findFirst();
    }

    private Optional<Category> findByName(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return Optional.empty();
        }
        return StreamEx.of(this.categoryService.listCategoryByCondition(categoryName.trim())).findFirst();
    }
}