package com.dili.trace.dto.thirdparty.report;

import java.util.Date;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface ReportDto {
    @JsonIgnore
    default ReportDtoTypeEnum getType(){
        return null;
    };
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    default Date getUpdateTime(){
        return new Date();
    }

    default String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

}