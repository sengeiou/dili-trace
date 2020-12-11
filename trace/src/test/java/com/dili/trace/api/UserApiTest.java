package com.dili.trace.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.manager.ManagerUserApi;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.enums.VocationTypeEnum;
import com.dili.trace.service.UpStreamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.User;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@Rollback(false)
public class UserApiTest extends AutoWiredBaseTest {

	@Autowired
	ManagerUserApi managerUserApi;
	@Autowired
	UpStreamApi upStreamApi;
	@Autowired
	UpStreamService upStreamService;


	@Test
	public void addUpstream(){
		UpStreamDto upStream = new UpStreamDto();
		upStream.setUpstreamType(10);
		upStream.setTelphone("13822340987");
		upStream.setName("个体户一");
		upStream.setUpORdown(20);
		Long [] ids = {29l};
		upStreamService.addUpstream(upStream,new OperatorUser(29l,"田波光"),true);
	}

	@Test
	public void modifyUpstream(){
		UpStreamDto upStream = new UpStreamDto();
		upStream.setId(19l);
		upStream.setName("个体户甲-改");
		upStreamApi.doModifyUpStream(upStream);
	}

	@Test
	public void listUpstream(){
		UpStreamDto upStream = new UpStreamDto();
		upStream.setSourceUserId(18l);
		upStream.setUpORdown(20);
		BaseOutput<BasePage<UpStream>> pageUpstream = upStreamApi.listPagedUpStream(upStream);
		System.out.println(pageUpstream.getCode());
	}


}
