package com.dili.trace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.service.Base64SignatureService;
import com.dili.trace.service.TradeDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/tradeDetail")
@Controller
@RequestMapping("/tradeDetail")
public class TradeDetailController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeDetailController.class);

	@Autowired
	TradeDetailService tradeDetailService;
	@Autowired
	Base64SignatureService base64SignatureService;

	/**
	 *跳转到index页面
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到index页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {

		return "tradeDetail/index";
	}

	/**
	 * 分页查询
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询ApproverInfo，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ApproverInfo", paramType = "form", value = "ApproverInfo的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody EasyuiPageOutput listPage(TradeDetail input) throws Exception {
		EasyuiPageOutput out = this.tradeDetailService.listEasyuiPageByExample(input, true);
		return out;
	}

	/**
	 * 新增
	 * @param input
	 * @return
	 */
	@ApiOperation("新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ApproverInfo", paramType = "form", value = "ApproverInfo的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(TradeDetail input) {
		try {
			this.tradeDetailService.insertSelective(input);
			return BaseOutput.success("新增成功").setData(input.getId());
		} catch (TraceBizException e) {
			LOGGER.error("register", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("register", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 修改
	 * @param input
	 * @return
	 */
	@ApiOperation("修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ApproverInfo", paramType = "form", value = "ApproverInfo的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(TradeDetail input) {
		try {
			this.tradeDetailService.updateSelective(input);
			return BaseOutput.success("修改成功").setData(input.getId());
		} catch (TraceBizException e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ApiOperation("删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", paramType = "form", value = "ApproverInfo的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		this.tradeDetailService.delete(id);
		return BaseOutput.success("修改成功");
	}

	/**
	 * 详情
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@ApiOperation("详情")
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable Long id) {
		TradeDetail item = this.tradeDetailService.get(id);
		modelMap.put("item", item);
		return "tradeDetail/view";
	}
}