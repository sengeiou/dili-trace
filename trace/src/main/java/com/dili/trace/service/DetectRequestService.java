package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.DetectRequestMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.IdNameDto;
import com.dili.trace.enums.DetectRequestStatusEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 检测请求service
 */
@Service
public class DetectRequestService extends BaseServiceImpl<DetectRequest, Long> {

    @Autowired
    BillService billService;
    @Autowired
    DetectRequestMapper detectRequestMapper;

    /**
     * 根据报备单创建检测请求数据
     *
     * @param billId
     * @return
     */
    public DetectRequest createByBillId(@NotNull Long billId, DetectTypeEnum detectTypeEnum, @NotNull IdNameDto creatorDto, @NotNull Optional<IdNameDto> designatedDto) {
        RegisterBill billItem = this.billService.get(billId);
        if (billItem == null) {
            throw new TraceBizException("报备单不存在");
        }


        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setDetectRequestStatus(DetectRequestStatusEnum.NEW.getCode());
        detectRequest.setDetectType(detectTypeEnum.getCode());

        this.findLatestDetectRequest(billId).ifPresent(detectRequestItem -> {
            if (!DetectRequestStatusEnum.FINISHED.equalsToCode(detectRequestItem.getDetectRequestStatus())) {
                throw new TraceBizException("已经有进行中的检测任务");
            }
/*            if(DetectTypeEnum.INITIAL_CHECK.equalsToCode(detectRequestItem.getDetectType())){
                detectRequest.setDetectType(DetectTypeEnum.RECHECK.getCode());
            }*/

        });


        detectRequest.setBillId(billItem.getBillId());
        detectRequest.setCreated(new Date());
        detectRequest.setModified(new Date());

        designatedDto.ifPresent(idname -> {
            detectRequest.setDesignatedId(idname.getId());
            detectRequest.setDesignatedName(idname.getName());
        });
        detectRequest.setCreatorId(creatorDto.getId());
        detectRequest.setCreatorName(creatorDto.getName());
        this.insert(detectRequest);

        RegisterBill registerBill = new RegisterBill();
        registerBill.setId(billId);
        registerBill.setDetectRequestId(detectRequest.getId());
        this.billService.updateSelective(registerBill);
        return detectRequest;
    }


    /**
     * 查询已经关联的检测任务
     *
     * @param billId
     * @return
     */
    private Optional<DetectRequest> findLatestDetectRequest(Long billId) {
        if (billId == null) {
            throw new TraceBizException("参数错误");
        }

        DetectRequest example = new DetectRequest();
        example.setSort("id");
        example.setOrder("desc");
        example.setPage(1);
        example.setRows(1);
        return StreamEx.of(this.detectRequestMapper.selectByExample(example)).findFirst();
    }

    /**
     * 根据id集合查询
     *
     * @param detectRequestIdList
     * @return
     */
    public Map<Long, DetectRequest> findDetectRequestByIdList(List<Long> detectRequestIdList) {
        if (detectRequestIdList.isEmpty()) {
            return Maps.newHashMap();
        }

        Example example = new Example(DetectRequest.class);
        example.and().andIn("id", detectRequestIdList);
        return StreamEx.of(this.detectRequestMapper.selectByExample(example)).toMap(DetectRequest::getId, Function.identity());
    }
}
