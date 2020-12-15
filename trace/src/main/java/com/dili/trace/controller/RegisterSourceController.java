package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.RegisterSourceDto;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.TradeTypeService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 类型接口
 */
@Api("/registerSource")
@Controller
@RequestMapping("/registerSource")
public class RegisterSourceController {
    @Autowired
    TradeTypeService tradeTypeService;

    /**
     * 查询数据
     *
     * @param queryInput
     * @return
     */
    @RequestMapping("/querySource.action")
    @ResponseBody
    public BaseOutput<List<RegisterSourceDto>> querySource(@RequestBody RegisterSourceDto queryInput) {
        Integer registerSource = queryInput.getRegisterSource();
        String query = queryInput.getQuery();
        if (RegisterSourceEnum.TRADE_AREA.equalsToCode(registerSource)) {
            List<RegisterSourceDto> list = StreamEx.of(this.tradeTypeService.findAll()).map(tt -> {
                RegisterSourceDto dto = new RegisterSourceDto();
                dto.setSourceId(tt.getTypeId());
                dto.setSourceName(tt.getTypeName());
                dto.setRegisterSource(registerSource);
                return dto;
            }).toList();
            return BaseOutput.successData(list);
        } else if (RegisterSourceEnum.TALLY_AREA.equalsToCode(registerSource)) {
            RegisterSourceDto dto = new RegisterSourceDto();
            dto.setSourceId(query);
            dto.setSourceName(query);
            dto.setRegisterSource(registerSource);
            return BaseOutput.successData(Lists.newArrayList(dto));
        }

        return BaseOutput.success();

    }
}
