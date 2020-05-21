package com.dili.trace.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.BusinessException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.common.util.MD5Util;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.domain.UserQrItem;
import com.dili.trace.domain.UserQrItemDetail;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.QrItemStatusEnum;
import com.dili.trace.glossary.QrItemTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserQrItemDetailService;
import com.dili.trace.service.UserQrItemService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.MaskUserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
	BaseInfoRpcService baseInfoRpcService;
	@Autowired
	UsualAddressService usualAddressService;

	@ApiOperation("跳转到User页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		// Date now = new Date();
		// modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		// modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		LocalDateTime now = LocalDateTime.now();
		modelMap.put("createdStart", now.withYear(2019).withMonth(1).withDayOfMonth(1)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
		modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));

		// modelMap.put("cities", this.queryCitys());
		modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));

		return "user/index";
	}

	@ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UserListDto user) throws Exception {
		EasyuiPageOutput out = this.userService.listEasyuiPageByExample(user);
		return out.toString();
	}

	@ApiOperation("新增User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Long> insert(@RequestBody User user) {
		try {
			user.setPassword(MD5Util.md5(defaultConfiguration.getPassword()));
			user.setState(EnabledStateEnum.ENABLED.getCode());
			userService.register(user, false);
			return BaseOutput.success("新增成功").setData(user.getId());
		} catch (BusinessException e) {
			LOGGER.error("register", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("register", e);
			return BaseOutput.failure();
		}
	}

	@ApiOperation("修改User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(User user) {
		try {
			userService.updateUser(user);
			return BaseOutput.success("修改成功");
		} catch (BusinessException e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("修改用户", e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	@ApiOperation("删除User")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		return userService.deleteUser(id);
	}

	/**
	 * 业户条件查询
	 * @param userListDto
	 * @return
	 */
	@RequestMapping(value = "/listByCondition.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput listByCondition(UserListDto userListDto) {
		return BaseOutput.success().setData(userService.listByExample(userListDto));
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
		} catch (BusinessException e) {
			LOGGER.error("修改用户状态", e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("修改用户状态", e);
			return BaseOutput.failure();
		}

	}

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

	@RequestMapping(value = "/findPlates.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput findPlates(Long userId) {
		try {
			List<String> plateList = this.userPlateService.findUserPlateByUserId(userId).stream()
					.map(UserPlate::getPlate).collect(Collectors.toList());
			return BaseOutput.success().setData(plateList);

		} catch (Exception e) {
			LOGGER.error("查询失败", e);
			return BaseOutput.failure();
		}

	}

	@RequestMapping(value = "/findPlatesByTallyAreaNo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput findPlatesByTallyAreaNo(String tallyAreaNo) {
		try {
			List<UserPlate> list = this.userPlateService.findUserPlateByTallyAreaNo(tallyAreaNo);
			// List<String> plateList =
			// this.userPlateService.findUserPlateByTallyAreaNo(tallyAreaNo).stream().map(UserPlate::getPlate)
			// .collect(Collectors.toList());
			// plateList.add("SSS");
			// UserPlate item=DTOUtils.newDTO(UserPlate.class);
			// item.setPlate("ABC");
			// item.setId(1L);
			// list.add(item);
			return BaseOutput.success().setData(list);

		} catch (Exception e) {
			LOGGER.error("查询失败", e);
			return BaseOutput.failure();
		}

	}

	@ApiOperation("跳转到User页面")
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable Long id) {
		User userItem = this.userService.get(id);
		String userPlateStr = this.userPlateService.findUserPlateByUserId(id).stream().map(UserPlate::getPlate)
				.collect(Collectors.joining(","));
		if (userItem != null) {
			userItem.setAddr(MaskUserInfo.maskAddr(userItem.getAddr()));
			userItem.setCardNo(MaskUserInfo.maskIdNo(userItem.getCardNo()));
			userItem.setPhone(MaskUserInfo.maskPhone(userItem.getPhone()));
		}

		modelMap.put("userItem", userItem);
		modelMap.put("userPlates", userPlateStr);

		return "user/view";
	}

	// private List<City> queryCitys() {
	// List<String> prirityCityNames = Arrays.asList("北京市", "哈尔滨市", "牡丹江市", "佳木斯市",
	// "鹤岗市", "绥化市", "内蒙古自治区", "呼和浩特市",
	// "包头市", "呼伦贝尔市", "天津市", "沈阳市", "大连市", "河北省", "苏州市", "烟台市", "合肥市", "长春市",
	// "四平市", "上海市");
	//
	// List<City> cityList = new ArrayList<>();
	// for (String name : prirityCityNames) {
	// CityListInput query = new CityListInput();
	// query.setKeyword(name);
	// List<City> list = this.baseInfoRpcService.listCityByCondition(name);
	// City city = list.stream().filter(item ->
	// item.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	// if (city != null) {
	// cityList.add(city);
	// }
	//
	// }
	// return cityList;
	//
	// }
	@Autowired
	UserQrItemService userQrItemService;
	@Autowired
	UserQrItemDetailService userQrItemDetailService;
	@ApiOperation("跳转到qrstatus页面")
	@RequestMapping(value = "/qrstatus.html", method = RequestMethod.GET)
	public String qrstatus(ModelMap modelMap, Long id) {
		List<UserQrItem> userQrItemlist = Collections.emptyList();
		if (id != null) {
			UserQrItem userQrIztem = new UserQrItem();
			userQrIztem.setUserId(id);
			userQrItemlist = this.userQrItemService.listByExample(userQrIztem);
		}

		List<UserQrItemDetail>userQrItemDetailList= this.userQrItemDetailService.findByUserQrItemIdList(userQrItemlist.stream().map(UserQrItem::getId).collect(Collectors.toList()));
		Map<Long,String>itemIdDetailListMap=userQrItemDetailList.stream().collect(Collectors.groupingBy(UserQrItemDetail::getUserQrItemId,Collectors.mapping(UserQrItemDetail::getObjectId,Collectors.joining(","))));
		modelMap.put("userQrItemlist", userQrItemlist);
		modelMap.put("itemIdDetailListMap", itemIdDetailListMap);
		
		Map<Integer,String>qrItemTypeMap=Stream.of(QrItemTypeEnum.values()).collect(Collectors.toMap(QrItemTypeEnum::getCode, QrItemTypeEnum::getDesc));
		modelMap.put("qrItemTypeMap", qrItemTypeMap);

		Map<Integer,String>qrItemStatusMap=Stream.of(QrItemStatusEnum.values()).collect(Collectors.toMap(QrItemStatusEnum::getCode, QrItemStatusEnum::getDesc));
		modelMap.put("qrItemStatusMap", qrItemStatusMap);
		
		modelMap.put("userId", id);

		return "user/qrstatus";
	}
	@ApiOperation("跳转到qrstatus页面")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit(ModelMap modelMap, Long id) {
		modelMap.put("item", DTOUtils.newDTO(User.class));
		if (id != null) {
			User user = this.userService.get(id);
			modelMap.put("item", user);
		}
		modelMap.put("userTypeMap", Stream.of(UserTypeEnum.values()).collect(Collectors.toMap(UserTypeEnum::getCode, UserTypeEnum::getDesc)));

		String userPlateStr = this.userPlateService.findUserPlateByUserId(id).stream().map(UserPlate::getPlate)
				.collect(Collectors.joining(","));

		modelMap.put("userPlates", userPlateStr);

		modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));
		return "user/edit";
	}

	@RequestMapping(value = "/queryUser.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput queryUser(User input) {
		try {
			input.setId(132L);
			List<User> list = this.userService.listByExample(input);
			return BaseOutput.success().setData(list);

		} catch (Exception e) {
			LOGGER.error("查询失败", e);
			return BaseOutput.failure();
		}

	}
}