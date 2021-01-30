package com.dili.trace.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.TallyAreaNoMapper;
import com.dili.trace.domain.RUserTallyArea;
import com.dili.trace.domain.TallyAreaNo;
import com.dili.trace.util.ChineseStringUtil;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

@Service
public class TallyAreaNoService extends BaseServiceImpl<TallyAreaNo, Long> {//implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TallyAreaNoService.class);
    private Map<String, List<String>> areaAndNumMap = new HashMap<>();

    @Autowired
    TallyAreaNoMapper tallyAreaNoMapper;

    public TallyAreaNoService() {
        areaAndNumMap.put("A1", Lists.newArrayList("-6-", "-7-", "7街", "-8-", "-9-", "-XQ-", "虾区"));
        areaAndNumMap.put("A2", Lists.newArrayList("-3-", "3街", "-5-", "5街", "-18-", "东大街"));
        areaAndNumMap.put("B1", Lists.newArrayList("-11-", "-12-", "-13-", "HY"));
        areaAndNumMap.put("B2", Lists.newArrayList("-10-", "BX", "SZ"));

    }

    @Autowired
    UserService userService;
    @Autowired
    RUserTallyAreaService ruserTallyAreaService;

    // @Override
    // public void run(String... args) throws Exception {
    //     User uq = DTOUtils.newDTO(User.class);
    //     uq.setPage(1);
    //     uq.setRows(200);
    //     while (true) {
    //         List<User> userList = StreamEx.of(this.userService.listPageByExample(uq).getDatas()).map(userItem -> {

    //             String parsedTallyAreaNos = StringUtils
    //                     .trimToNull(this.parseAndConvertTallyAreaNos(userItem.getTallyAreaNos()));
    //             if (!Objects.equals(parsedTallyAreaNos, StringUtils.trimToNull(userItem.getTallyAreaNos()))) {
    //                 User u = DTOUtils.newDTO(User.class);
    //                 u.setId(userItem.getId());
    //                 u.setTallyAreaNos(parsedTallyAreaNos);
    //                 this.userService.updateSelective(u);
    //                 return this.userService.get(u.getId());
    //             }
    //             return userItem;

    //         }).toList();
    //         if (userList.isEmpty()) {
    //             return;
    //         }
    //         try {
    //             List<RUserTallyArea> list = StreamEx.of(userList).nonNull().mapToEntry(u -> u, u -> u.getTallyAreaNos())
    //                     .filterValues(StringUtils::isNotBlank).mapValues(String::trim)
    //                     .mapValues(ChineseStringUtil::cToe).mapValues(ChineseStringUtil::full2Half)
    //                     .mapValues(tallyAreaNos -> StreamEx.of(tallyAreaNos.split(",")).filter(StringUtils::isNotBlank)
    //                             .toList())
    //                     .flatMapKeyValue((u, t) -> {
    //                         return StreamEx.of(t).map(tallyAreaNo -> {
    //                             TallyAreaNo tallyAreaItem = this.parseAndSave(tallyAreaNo);
    //                             RUserTallyArea ruserTallyArea = this.ruserTallyAreaService
    //                                     .saveOrUpdate(tallyAreaItem.getId(), u.getId());
    //                             return ruserTallyArea;
    //                         });
    //                     }).toList();
    //             logger.info("list.size={}", list.size());
    //         } catch (Exception e) {
    //             logger.error(e.getMessage(), e);
    //         }

    //         uq.setPage(uq.getPage() + 1);
    //     }
    // }

    public String parseAndConvertTallyAreaNos(String tallyAreaNos) {
        return StreamEx.of(this.parseAndConvert(tallyAreaNos)).joining(",");

    }

    private List<String> parseAndConvert(String tallyAreaNos) {
        return StreamEx.ofNullable(tallyAreaNos).map(StringUtils::trimToNull).nonNull().flatArray(str -> str.split(","))
                .map(StringUtils::trimToNull).map(str -> {
                    return StringUtils.remove(str, " ");
                }).nonNull().distinct().toList();

    }

    public List<TallyAreaNo> saveOrUpdateTallyAreaNo(Long userId, String tallyAreaNos) {
        List<String> tallyAreaNoList = this.parseAndConvert(tallyAreaNos);
        return this.saveOrUpdateTallyAreaNo(userId, tallyAreaNoList);

    }

    public List<TallyAreaNo> saveOrUpdateTallyAreaNo(Long userId, List<String> tallyAreaNoList) {
        if (userId == null) {
            return Lists.newArrayList();
        }
        RUserTallyArea deleteDomain = new RUserTallyArea();
        deleteDomain.setUserId(userId);
        this.ruserTallyAreaService.deleteByExample(deleteDomain);
        if (tallyAreaNoList.isEmpty()) {
            return Lists.newArrayList();
        }
        this.tallyAreaNoMapper.cleanUselessTallyAreaNo();

        List<TallyAreaNo> list = StreamEx.of(tallyAreaNoList).map(number -> {
            TallyAreaNo tallyAreaNoItem = this.parseAndSave(number);
            RUserTallyArea rUserTallyArea = new RUserTallyArea();
            rUserTallyArea.setUserId(userId);
            rUserTallyArea.setTallyAreaNoId(tallyAreaNoItem.getId());
            rUserTallyArea.setCreated(new Date());
            rUserTallyArea.setModified(new Date());
            this.ruserTallyAreaService.insertSelective(rUserTallyArea);
            return tallyAreaNoItem;

        }).toList();

        return list;

    }

    private TallyAreaNo parseAndSave(String number) {
        String area = EntryStream.of(areaAndNumMap).filterValues(list -> {
            return StreamEx.of(list).anyMatch(flag -> {
                return number.contains(flag);
            });
        }).map(e -> e.getKey()).findFirst().orElse("Others");
        TallyAreaNo query = new TallyAreaNo();
        query.setNumber(number);

        TallyAreaNo item = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            TallyAreaNo obj = new TallyAreaNo();
            obj.setNumber(number);
            obj.setArea(area);
            this.insertSelective(obj);
            return obj;
        });
        return item;
    }

}