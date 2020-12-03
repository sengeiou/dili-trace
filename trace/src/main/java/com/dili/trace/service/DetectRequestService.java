package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.DetectRequestMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.IdNameDto;
import com.dili.trace.enums.DetectResultEnum;
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
     * 查询检测请求
     *
     * @param billId
     * @return
     */
    public Optional<DetectRequest> findDetectRequestByBillId(Long billId) {
        if (billId == null) {
            return Optional.empty();
        }

        RegisterBill registerBill=this.billService.get(billId);
        if(registerBill==null){
            return Optional.empty();
        }
        Long detectRequestId=registerBill.getDetectRequestId();
        return Optional.ofNullable(this.get(detectRequestId));

    }

    /**
     * 创建检测请求
     *
     * @param billId
     * @param creatorDto
     * @return
     */
    public Long createOtherPassedRequest(@NotNull Long billId, @NotNull IdNameDto creatorDto) {

        DetectRequest item = this.createByBillId(billId, DetectTypeEnum.NEW, creatorDto, Optional.empty());

        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setId(item.getId());
        detectRequest.setDetectType(DetectTypeEnum.OTHERS.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.OTHERS.getCode());
        detectRequest.setDetectResult(DetectResultEnum.PASSED.getCode());

        this.updateSelective(detectRequest);
        return item.getId();
    }

    /**
     * 更新检测结果
     *
     * @param detectRequestId
     * @param resultEnum
     * @return
     */
    public Long updateRequest(Long detectRequestId, DetectResultEnum resultEnum) {
        DetectRequest item = this.get(detectRequestId);
        if (item == null) {
            throw new TraceBizException("数据不存在");
        }
        if (DetectResultEnum.NONE == resultEnum) {
            throw new TraceBizException("检测值错误");
        }
        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setId(detectRequestId);
        detectRequest.setDetectResult(resultEnum.getCode());
        this.updateSelective(detectRequest);
        return detectRequestId;
    }



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
        detectRequest.setDetectType(detectTypeEnum.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.NONE.getCode());
        detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());



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
