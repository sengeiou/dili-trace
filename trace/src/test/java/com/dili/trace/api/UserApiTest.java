package com.dili.trace.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.client.ClientUpStreamApi;
import com.dili.trace.api.manager.ManagerUserApi;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.service.UpStreamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dili.trace.AutoWiredBaseTest;
import org.springframework.test.annotation.Rollback;

@Rollback(false)
public class UserApiTest extends AutoWiredBaseTest {

	@Autowired
	ManagerUserApi managerUserApi;
	@Autowired
	ClientUpStreamApi upStreamApi;
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
