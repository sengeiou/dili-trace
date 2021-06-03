package com.dili.trace.controller;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 测试接口
 */
@RestController
@RequestMapping(value = "/testController")
@AppAccess(role = Role.NONE)
public class TestController {

    static class DemoData {
        private Date myDate = new Date();
        @JsonFormat(pattern = "yyyyMMdd HH:mm:ss")
//        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime dateTime = LocalDateTime.now();

        private LocalDateTime dateTime2 = LocalDateTime.now();

        public Date getMyDate() {
            return myDate;
        }

        public void setMyDate(Date myDate) {
            this.myDate = myDate;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public LocalDateTime getDateTime2() {
            return dateTime2;
        }

        public void setDateTime2(LocalDateTime dateTime2) {
            this.dateTime2 = dateTime2;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }

    /**
     * 测试用
     *
     * @return
     */
    @RequestMapping(value = "/test.action", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput test() {
        DemoData rb = new DemoData();
        return BaseOutput.successData(rb);
    }
}
