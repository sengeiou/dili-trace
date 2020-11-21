package com.dili.trace.controller;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.Category;
import com.dili.trace.enums.CategoryIsShowEnum;
import com.dili.trace.enums.CategoryTypeEnum;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.service.CategoryService;
import com.dili.trace.util.MarketUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/10/27
 */
@Controller
@RequestMapping("/goodsManagement")
public class GoodsManagementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsManagementController.class);
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("跳转到goodsManagement页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "goodsManagement/index";
    }

    @ApiOperation("跳转到edit页面")
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id, Long currentNodeId) {
        modelMap.put("goods", new Category());
        if (id != null) {

            //TODO
//            Category goods = this.categoryService.get(id);
            Category goods = null;
            modelMap.put("goods", goods);
        }
        modelMap.put("currentNodeId", currentNodeId);
        modelMap.put("goodsTypeMap", Stream.of(CategoryTypeEnum.values())
                .collect(Collectors.toMap(CategoryTypeEnum::getCode, CategoryTypeEnum::getName)));
        modelMap.put("goodsStateMap", Stream.of(EnabledStateEnum.values())
                .collect(Collectors.toMap(EnabledStateEnum::getCode, EnabledStateEnum::getName)));
        modelMap.put("goodsShowMap", Stream.of(CategoryIsShowEnum.values())
                .collect(Collectors.toMap(CategoryIsShowEnum::getCode, CategoryIsShowEnum::getName)));
        return "goodsManagement/edit";
    }

    /**
     * 商品菜单树加载
     *
     * @return
     */
    @RequestMapping(value = "/listGoods.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listGoods() throws Exception {
        Category category = new Category();
        //TODO
/*        category.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput easyuiPageOutput = categoryService.listEasyuiPage(category, true);*/
        EasyuiPageOutput easyuiPageOutput =new EasyuiPageOutput();
        /*List<Category> rows = easyuiPageOutput.getRows();
        rows.removeIf(e -> e.getLevel()!=1 && e.getParentId()==null);
        easyuiPageOutput.setRows(rows);
        easyuiPageOutput.setTotal(rows.size());*/
        return easyuiPageOutput.toString();
    }

    /**
     * 点击商品菜单树节点加载数据到表格
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listByTreeNode(@RequestParam Long id, @RequestParam Boolean leaf, Category category) throws Exception {
        //TODO
 /*       category.setName(null);
        category.setCode(null);
        category.setSpecification(null);
        category.setIsShow(null);
        category.setState(null);
        category.setType(null);
        if (leaf) {
            category.setId(id);
            return this.categoryService.listEasyuiPage(category, true).toString();
        }
        EasyuiPageOutput easyuiPageOutputBase = this.categoryService.listEasyuiPage(category, true);
        category.setId(null);
        category.setParentId(id);
        category.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput easyuiPageOutput = this.categoryService.listEasyuiPage(category, true);
        List rows = easyuiPageOutput.getRows();
        List rowsBase = easyuiPageOutputBase.getRows();
        rows.forEach(row -> {
            rowsBase.add(row);
        });
        easyuiPageOutputBase.setRows(rowsBase);
        easyuiPageOutputBase.setTotal(1 + easyuiPageOutput.getRows().size());
        return easyuiPageOutputBase.toString();*/
        return "";
    }

    /**
     * 按条件查询菜单列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/listByQueryParams.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listByQueryParams(Category category) throws Exception {
        if ("".equals(category.getName())) {
            category.setName(null);
        }
        if ("".equals(category.getCode())) {
            category.setCode(null);
        }
        //TODO
/*        if ("".equals(category.getSpecification())) {
            category.setSpecification(null);
        }
        category.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput easyuiPageOutput = this.categoryService.selectForEasyuiPage(category, true);
        return easyuiPageOutput.toString();*/
        return "";
    }


    @ApiOperation("新增商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Goods", paramType = "form", value = "Goods的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Long> insert(@RequestBody Category category, String currentNodeId) {
        //TODO
       /* try {
            Category categoryById = categoryService.get(Long.parseLong(currentNodeId));
            if (Objects.nonNull(categoryById)) {
                category.setParentId(categoryById.getId());
                category.setLevel(categoryById.getLevel() + 1);
                category.setFullName(categoryById.getFullName() + "," + category.getName());
                category.setParentCode(categoryById.getCode());
            }
            category.setMarketId(MarketUtil.returnMarket());
            categoryService.insertSelective(category);
            return BaseOutput.success("新增商品成功").setData(category.getId());
        } catch (TraceBusinessException e) {
            LOGGER.error("新增商品失败", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("新增商品失败", e);
            return BaseOutput.failure();
        }*/
        return BaseOutput.failure();
    }

    @ApiOperation("修改商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Goods", paramType = "form", value = "Goods的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(@RequestBody Category category) {
        //TODO
        /*try {
            categoryService.updateSelective(category);
            return BaseOutput.success("修改成功");
        } catch (TraceBusinessException e) {
            LOGGER.error("修改用户失败", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用户失败", e);
            return BaseOutput.failure(e.getMessage());
        }*/
        return BaseOutput.failure();

    }

    /**
     * @param id
     * @param enable 是否启用
     * @return
     */
    @RequestMapping(value = "/doEnable.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doEnable(Long id, Boolean enable) {
        //TODO
        /*try {
            categoryService.updateEnable(id, enable);
            return BaseOutput.success("修改商品状态成功");
        } catch (TraceBusinessException e) {
            LOGGER.error("修改商品状态", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用商品状态", e);
            return BaseOutput.failure();
        }*/
        return BaseOutput.failure();

    }
}
