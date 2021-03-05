package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.domain.RegisterTallyAreaNo;
import com.dili.trace.enums.BillTypeEnum;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

/**
 * 到货摊位号
 */
@Service
public class RegisterTallyAreaNoService extends TraceBaseService<RegisterTallyAreaNo, Long> {
    /**
     * 插入绑定的到货摊位号
     *
     * @param tallyAreaNoList
     * @param billId
     * @param billTypeEnum
     * @return
     */
    public int insertTallyAreaNoList(List<String> tallyAreaNoList, Long billId, BillTypeEnum billTypeEnum) {
        if (billId == null || billTypeEnum == null) {
            throw new TraceBizException("参数错误");
        }
        List<RegisterTallyAreaNo> noList = StreamEx.ofNullable(tallyAreaNoList).flatCollection(Function.identity())
                .filter(StringUtils::isNotBlank)
                .map(noStr -> {
                    RegisterTallyAreaNo areaNo = new RegisterTallyAreaNo();
                    areaNo.setBillType(billTypeEnum.getCode());
                    areaNo.setTallyareaNo(noStr.trim());
                    areaNo.setCreated(LocalDateTime.now());
                    areaNo.setBillId(billId);
                    return areaNo;
                })
                .toList();
        if (noList.isEmpty()) {
            return 0;
        }
        return this.batchInsert(noList);
    }
}
