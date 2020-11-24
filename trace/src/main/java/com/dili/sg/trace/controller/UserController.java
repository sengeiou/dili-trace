package com.dili.sg.trace.controller;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.util.MD5Util;
import com.dili.sg.trace.glossary.UserTypeEnum;
import com.dili.sg.trace.util.MaskUserInfo;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.BeanMapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/user")
@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	@Autowired
	UserPlateService userPlateService;
	@Resource
	DefaultConfiguration defaultConfiguration;
	@Autowired
	UsualAddressService usualAddressService;

	/**
	 * 跳转到User页面
	 * @param modelMap
	 * @return
	 */
	@ApiOperation("跳转到User页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		LocalDateTime now=LocalDateTime.now();
		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
		modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
		
		modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));
		
		return "user/index";
	}

	/**
	 * 分页查询User
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UserListDto user) throws Exception {
		EasyuiPageOutput out=this.userService.listEasyuiPageByExample(BeanMapUtil.trimBean(user));
		return out.toString();
	}

	/**
	 * 新增User
	 * @param user
	 * @return
	 */
	@ApiOperation("新增User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody User user) {
		try {
			UserTypeEnum userTypeEnum= UserTypeEnum.fromCode(user.getUserType()).orElse(null);
			if(userTypeEnum==null) {
				return BaseOutput.failure("用户类型错误");
			}
			userService.register(user,userTypeEnum,defaultConfiguration.getPassword());
			return BaseOutput.success("新增成功").setData(user.getId());
		} catch (TraceBizException e) {
			LOGGER.error("register", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("register", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 修改User
	 * @param user
	 * @return
	 */
	@ApiOperation("修改User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(@RequestBody User user) {
		try {
			userService.updateUser(user);
			return BaseOutput.success("修改成功");
		} catch (TraceBizException e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	/**
	 * 删除User
	 * @param id
	 * @return
	 */
	@ApiOperation("删除User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		return userService.deleteUser(id);
	}

	/**
	 *
	 * @param id
	 * @param enable 是否启用
	 * @return
	 */
	@RequestMapping(value = "/doEnable.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput doEnable(Long id, Boolean enable) {
		try {
			userService.updateEnable(id, enable);
			return BaseOutput.success("修改用户状态成功");
		} catch (TraceBizException e) {
			LOGGER.error("修改用户状态", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("修改用户状态", e);
			return BaseOutput.failure();
		}

	}

	/**
	 * 找回密码【接口已通】
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "找回密码【接口已通】", notes = "找回密码")
	@RequestMapping(value = "/resetPassword.action", method = RequestMethod.POST)
	@ResponseBody
	public BaseOutput<Boolean> resetPassword(Long id) {
		User user = DTOUtils.newDTO(User.class);
		user.setId(id);
		user.setPassword(MD5Util.md5(defaultConfiguration.getPassword()));
		userService.updateSelective(user);
		return BaseOutput.success().setData(true);
	}

	/**
	 * 查找用户车牌
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/findPlates.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput findPlates(Long userId) {
		try {
			List<String> plateList = this.userPlateService.findUserPlateByUserId(userId).stream().map(UserPlate::getPlate)
					.collect(Collectors.toList());
			return BaseOutput.success().setData(plateList);

		} catch (Exception e) {
			LOGGER.error("查询失败", e);
			return BaseOutput.failure();
		}

	}

	/**
	 * 通过理货区号查询车牌
	 * @param tallyAreaNo
	 * @return
	 */
	@RequestMapping(value = "/findPlatesByTallyAreaNo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput findPlatesByTallyAreaNo(String tallyAreaNo) {
		try {
			List<UserPlate>list=this.userPlateService.findUserPlateByTallyAreaNo(tallyAreaNo);
			return BaseOutput.success().setData(list);

		} catch (Exception e) {
			LOGGER.error("查询失败", e);
			return BaseOutput.failure();
		}

	}

	/**
	 * 通过理货区号查询数据
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/queryByTallyAreaNo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput queryByTallyAreaNo(String query) {
		try {
			List<DTO>data=this.userService.queryByTallyAreaNo(query);
			return BaseOutput.success().setData(data);

		} catch (Exception e) {
			LOGGER.error("查询失败", e);
			return BaseOutput.failure();
		}

	}

	/**
	 * 跳转到User页面
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@ApiOperation("跳转到User页面")
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap,@PathVariable Long id) {
		User userItem=this.userService.get(id);
		String userPlateStr=this.userPlateService.findUserPlateByUserId(id).stream().map(UserPlate::getPlate).collect(Collectors.joining(","));
		if(userItem!=null) {
			userItem.setAddr(MaskUserInfo.maskAddr(userItem.getAddr()));
			userItem.setCardNo(MaskUserInfo.maskIdNo(userItem.getCardNo()));
			userItem.setPhone(MaskUserInfo.maskPhone(userItem.getPhone()));
		}
		
		modelMap.put("userItem", userItem);
		modelMap.put("userPlates", userPlateStr);
		
		return "user/view";
	}
}