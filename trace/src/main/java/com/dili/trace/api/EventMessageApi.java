package com.dili.trace.api;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息api
 */
@Api(value = "/api/message", description = "有关于消息相关的接口")
@RestController
@RequestMapping(value = "/api/message")
@AppAccess(role = Role.ANY)
public class EventMessageApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMessageApi.class);

    /**
     * 已读/未读
     *
     * @param eventMessage
     * @return
     */
    @ApiOperation(value = "已读/未读", notes = "已读/未读")
    @RequestMapping(value = "/read.api", method = RequestMethod.POST)
    public BaseOutput signRead(@RequestBody Object eventMessage) {
        return BaseOutput.success();

    }


    /**
     * 查询消息列表
     *
     * @param eventMessage
     * @return
     */
    @Deprecated
    @ApiOperation(value = "查询消息列表", notes = "消息列表")
    @RequestMapping(value = "/pageMssage.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<Object>> pageMessage(@RequestBody Object eventMessage) {

        return BaseOutput.success();
    }

    /**
     * 查询消息列表（新）
     *
     * @param eventMessage
     * @return
     */
    @ApiOperation(value = "查询消息列表（新）", notes = "消息列表")
    @RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
//    @InterceptConfiguration
    public BaseOutput<BasePage<Object>> listPage(@RequestBody Object eventMessage) {

        return BaseOutput.success();
    }


    /**
     * 已读全部
     *
     * @param eventMessage
     * @return
     */
    @ApiOperation(value = "已读全部", notes = "已读全部")
    @RequestMapping(value = "/doReadAll.api", method = RequestMethod.POST)
    public BaseOutput doReadAll(@RequestBody Object eventMessage) {

        return BaseOutput.success();
    }
}
