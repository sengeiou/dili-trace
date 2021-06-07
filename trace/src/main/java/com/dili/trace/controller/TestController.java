package com.dili.trace.controller;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Transient;
import java.sql.Connection;
import java.time.*;
import java.util.Calendar;
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
//        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private ZonedDateTime dateTime = ZonedDateTime.now();

        private ZonedDateTime dateTime2 = ZonedDateTime.now();

        private ZonedDateTime dateTime3 = ZonedDateTime.now();

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        private LocalDateTime dateTime4 = LocalDateTime.now();


        @Transient
        private ZonedDateTime zonedDateTime = ZonedDateTime.now();
        @Transient
        private LocalDateTime localDateTime = LocalDateTime.now();
        @Transient
        private OffsetDateTime offsetDateTime = OffsetDateTime.now();
        @Transient
        @JsonFormat(pattern = "yyyyMMdd HH:mm:ss")
        private ZonedDateTime jsonFormatDateTime = ZonedDateTime.now();

        public ZonedDateTime getZonedDateTime() {
            return zonedDateTime;
        }

        public void setZonedDateTime(ZonedDateTime zonedDateTime) {
            this.zonedDateTime = zonedDateTime;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public OffsetDateTime getOffsetDateTime() {
            return offsetDateTime;
        }

        public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
            this.offsetDateTime = offsetDateTime;
        }

        public ZonedDateTime getJsonFormatDateTime() {
            return jsonFormatDateTime;
        }

        public void setJsonFormatDateTime(ZonedDateTime jsonFormatDateTime) {
            this.jsonFormatDateTime = jsonFormatDateTime;
        }

        public Date getMyDate() {
            return myDate;
        }

        public void setMyDate(Date myDate) {
            this.myDate = myDate;
        }

        public ZonedDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(ZonedDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public ZonedDateTime getDateTime2() {
            return dateTime2;
        }

        public void setDateTime2(ZonedDateTime dateTime2) {
            this.dateTime2 = dateTime2;
        }

        public ZonedDateTime getDateTime3() {
            return dateTime3;
        }

        public void setDateTime3(ZonedDateTime dateTime3) {
            this.dateTime3 = dateTime3;
        }

        public LocalDateTime getDateTime4() {
            return dateTime4;
        }

        public void setDateTime4(LocalDateTime dateTime4) {
            this.dateTime4 = dateTime4;
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
//        rb.getDateTime2().atZone(ZoneId.systemDefault());
        return BaseOutput.successData(rb);
    }

    /**
     * SS
     *
     * @param args
     */
    public static void main(String[] args) {
        Date date = new Date();//local (GMT+8)
        Instant instant = Instant.now();
        System.out.println(instant);//utf
        System.out.println(instant.atOffset(ZoneOffset.of("+8")));//GMT+8
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("Etc/GMT")).toInstant()
                .atOffset(ZoneOffset.of("+8")));

        System.out.println(date.toInstant().atZone(ZoneId.systemDefault()));
        System.out.println(date.toInstant().atZone(ZoneId.of("Etc/GMT")));

        LocalDateTime ldt = LocalDateTime.of(2019, 9, 15, 15, 16, 17);
        ZonedDateTime zbj = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime zny = ldt.atZone(ZoneId.of("America/New_York"));
        System.out.println(zbj);
        System.out.println(zny);

        System.out.println("===================");
        System.out.println(zbj.withFixedOffsetZone());
        System.out.println(zbj.withEarlierOffsetAtOverlap());
        System.out.println(zbj.withLaterOffsetAtOverlap());
        System.out.println("===================");
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        System.out.println(offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()));

        System.out.println(ZoneOffset.systemDefault());

    }
}

