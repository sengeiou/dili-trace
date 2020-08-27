package com.dili.trace.jobs;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.ThirdPartyPushData;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.ThirdPartyReportDataQueryDto;
import com.dili.trace.dto.thirdparty.report.*;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.service.*;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.plugin2.jvm.CircularByteBuffer;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Function;

@Component
public class ThirdPartyPushDataJob implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyPushDataJob.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThirdPartyPushDataService thirdPartyPushDataService;

    @Override
    public void run(String... args) throws Exception {

    }

    // 每五分钟提交一次数据
    @Scheduled(cron = "0 */5 * * * ?")
    public void pushData() {
        Optional<OperatorUser> optUser = Optional.of(new OperatorUser(-1L, "auto"));
        try {
            this.pushCategory(1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void pushCategory(Integer level)
    {
        String tableName = "category";
        String interfaceName = "商品大类新增/修改";
        Category category = new Category();
        category.setLevel(level);
        List<Category> categories = categoryService.list(category);
        List<ThirdPartyPushData> pushDataList = new ArrayList<>();
        List<Category> pushCategories = StreamEx.of(categories).filter(td -> {
            ThirdPartyPushData thirdPartyPushData =
                    thirdPartyPushDataService.getThredPartyPushData(tableName, td.getId());
            if (thirdPartyPushData == null || (thirdPartyPushData != null
                    && thirdPartyPushData.getPushTime().compareTo(td.getModified()) < 0))
            {
                ThirdPartyPushData pushData = new ThirdPartyPushData();
                pushData.setTableName(tableName);
                pushData.setTableId(td.getId());
                pushData.setInterfaceName(interfaceName);
                pushDataList.add(pushData);
                return true;
            }
            return false;
        }).toList();


        this.

                thirdPartyPushDataService.updatePushTime(pushDataList);
    }

}