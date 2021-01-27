package com.dili.trace.service;


import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.DetectRequestQueryDto;
import com.dili.trace.api.output.SampleSourceListOutputDto;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.dto.DetectRequestOutDto;
import com.dili.trace.enums.DetectStatusEnum;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.function.Function;


@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient
@Rollback
public class DetectRequestServiceTest extends AutoWiredBaseTest {

    @Autowired
    DetectRequestService detectRequestService;



    @Test
    public void listPageByUserCategory() {

        DetectRequestQueryDto detectRequestDto = new DetectRequestQueryDto();
        List<Integer> detectStatusList = StreamEx.of(DetectStatusEnum.values()).map(DetectStatusEnum::getCode).toList();
        detectRequestDto.setIsDeleted(YesOrNoEnum.NO.getCode());
        detectRequestDto.setDetectStatusList(detectStatusList);
        detectRequestDto.setSort("created");
        detectRequestDto.setOrder("desc");
        BasePage<DetectRequestOutDto> basePage = detectRequestService.listPageByUserCategory(detectRequestDto);
    }

    @Test
    public void listPagedSampleSourceDetect(){
        DetectRequestQueryDto query=new DetectRequestQueryDto();
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
        if(StringUtils.isBlank(query.getSort())){
            query.setSort("created");
            query.setOrder("desc");
        }
        BasePage<SampleSourceListOutputDto> basePage = this.detectRequestService.listPagedSampleSourceDetect(query);
    }
}
