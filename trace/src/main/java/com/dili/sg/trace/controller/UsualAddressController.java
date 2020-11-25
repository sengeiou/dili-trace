package com.dili.sg.trace.controller;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.trace.service.CityService;
import com.dili.trace.service.UserPlateService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.UsualAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/usualAddress")
@Controller
@RequestMapping("/usualAddress")
public class UsualAddressController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsualAddressController.class);

	@Autowired
	UsualAddressService usualAddressService;
	@Autowired
	UserPlateService userPlateService;
	@Resource
	DefaultConfiguration defaultConfiguration;
	@Autowired
	CityService cityService;

	/**
	 * 跳转到UserAddress页面
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到UserAddress页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		return "usualAddress/index";
	}

	/**
	 * 分页查询UsualAddress
	 * @param usualAddress
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询UsualAddress", notes = "分页查询UsualAddress，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "UsualAddress", paramType = "form", value = "UsualAddress的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UsualAddress usualAddress) throws Exception {

		EasyuiPageOutput out = this.usualAddressService.listEasyuiPageByExample(usualAddress, true);
		return out.toString();
	}

	/**
	 * 新增UsualAddress
	 * @param usualAddress
	 * @return
	 */
	@ApiOperation("新增UsualAddress")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "UsualAddress", paramType = "form", value = "UsualAddress的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody UsualAddress usualAddress) {
		try {
			if (usualAddress == null || StringUtils.isBlank(usualAddress.getType())
					|| usualAddress.getAddressId() == null) {
				return BaseOutput.failure("参数错误");
			}
			this.usualAddressService.insertUsualAddress(usualAddress);
			return BaseOutput.success("新增成功").setData(usualAddress.getId());
		} catch (TraceBizException e) {
			LOGGER.error("增加常用地址错误", e);
			return BaseOutput.failure(e.getMessage());
		}catch(org.springframework.dao.DuplicateKeyException dke) {
			return BaseOutput.failure("常用地址已经存在,请不要重复提交");
			
		} catch (Exception e) {
			LOGGER.error("增加常用地址错误", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 修改UsualAddress
	 * @param usualAddress
	 * @return
	 */
	@ApiOperation("修改UsualAddress")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "UsualAddress", paramType = "form", value = "UsualAddress的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(UsualAddress usualAddress) {
		try {
			if (usualAddress == null || StringUtils.isBlank(usualAddress.getType())
					|| usualAddress.getAddressId() == null||usualAddress.getId()==null) {
				return BaseOutput.failure("参数错误");
			}
			
			this.usualAddressService.updateUsualAddress(usualAddress);
			return BaseOutput.success("修改成功");
		} catch (TraceBizException e) {
//			LOGGER.error("修改常用地址错误", e);
			return BaseOutput.failure(e.getMessage());
		}catch(org.springframework.dao.DuplicateKeyException dke) {
			return BaseOutput.failure("常用地址已经存在");
			
		} catch (Exception e) {
			LOGGER.error("修改常用地址错误", e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	/**
	 * 删除UsualAddress
	 * @param id
	 * @return
	 */
	@ApiOperation("删除UsualAddress")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", paramType = "form", value = "UsualAddress的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		this.usualAddressService.deleteUsualAddress(id);
		return BaseOutput.success("删除成功");
	}

	/**
	 * 查询UsualAddress
	 * @param input
	 * @return
	 */
	@ApiOperation("查询UsualAddress")
	@RequestMapping(value = "/listUsualAddress.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput listUsualAddress(UsualAddress input) {
		UsualAddressTypeEnum addressType=	UsualAddressTypeEnum.getUsualAddressType(input.getType());
		List<UsualAddress>list=this.usualAddressService.findUsualAddressByType(addressType);
		return BaseOutput.success().setData(list);
	}


}