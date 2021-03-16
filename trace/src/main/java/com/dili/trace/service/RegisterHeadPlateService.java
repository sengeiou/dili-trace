package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterHeadPlate;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 台账车牌
 */
@Service
public class RegisterHeadPlateService extends BaseServiceImpl<RegisterHeadPlate, Long> {
    /**
     * 删除并新增车牌信息
     *
     * @param registerHeadId
     * @param plateList
     * @return
     */
    public int deleteAndInsertPlateList(Long registerHeadId, List<String> plateList) {

        if (registerHeadId == null || plateList == null || plateList.isEmpty()) {
            return 0;
        }
        RegisterHeadPlate q = new RegisterHeadPlate();
        q.setRegisterHeadId(registerHeadId);
        this.deleteByExample(q);
        List<RegisterHeadPlate> headPlateList = StreamEx.of(plateList).filter(StringUtils::isNotBlank).map(plate -> {
            RegisterHeadPlate headPlate = new RegisterHeadPlate();
            headPlate.setPlate(plate);
            headPlate.setRegisterHeadId(registerHeadId);
            return headPlate;
        }).toList();
        if (headPlateList.isEmpty()) {
            return 0;
        }
        return this.batchInsert(headPlateList);
    }

    /**
     * 根据台账id查询相关车牌信息
     *
     * @param registerHeadId
     * @return
     */
    public List<RegisterHeadPlate> findHeadPlateByHeadId(Long registerHeadId) {

        if (registerHeadId == null) {
            return Lists.newArrayList();
        }
        RegisterHeadPlate q = new RegisterHeadPlate();
        q.setRegisterHeadId(registerHeadId);
        return this.listByExample(q);
    }
}
