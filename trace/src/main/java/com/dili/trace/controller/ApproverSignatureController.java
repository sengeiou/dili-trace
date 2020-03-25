package com.dili.trace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.ApproverSignature;
import com.dili.trace.service.ApproverSignatureService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/approverSignature")
@Controller
@RequestMapping("/approverSignature")
public class ApproverSignatureController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApproverSignatureController.class);

	@Autowired
	ApproverSignatureService approverSignatureService;

	@ApiOperation("跳转到ApproverSignature页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		
		return "approverSignature/index";
	}

	@ApiOperation(value = "分页查询ApproverSignature", notes = "分页查询ApproverSignature，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ApproverSignature", paramType = "form", value = "ApproverSignature的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(ApproverSignature approverSignature) throws Exception {
		EasyuiPageOutput out=this.approverSignatureService.listEasyuiPageByExample(approverSignature,true);
		return out.toString();
	}

	@ApiOperation("新增ApproverSignature")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ApproverSignature", paramType = "form", value = "ApproverSignature的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody ApproverSignature approverSignature) {
		try {
			approverSignature.setUserId(0L);
			approverSignature.setSignBase64("aaa");
			this.approverSignatureService.insertSelective(approverSignature);
			return BaseOutput.success("新增成功").setData(approverSignature.getId());
		} catch (BusinessException e) {
			LOGGER.error("register", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("register", e);
			return BaseOutput.failure();
		}
	}

	@ApiOperation("修改ApproverSignature")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ApproverSignature", paramType = "form", value = "ApproverSignature的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(ApproverSignature approverSignature) {
		try {
			return BaseOutput.success("修改成功");
		} catch (BusinessException e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	@ApiOperation("删除ApproverSignature")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", paramType = "form", value = "ApproverSignature的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		 this.approverSignatureService.delete(id);
		 return BaseOutput.success("修改成功");
	}

	
}